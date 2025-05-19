package renderer;

import geometries.Intersectable;
import lighting.LightSource;
import primitives.*;
import scene.Scene;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Simple implementation of ray tracer.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor for SimpleRayTracer.
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<Intersectable.Intersection> intersections = scene.geometries.calculateIntersections(ray);

        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }

        Intersectable.Intersection closest = ray.findClosestIntersection(intersections);

        return calcColor(closest, ray);
    }

    /**
     * Computes the color at the intersection point.
     * Currently returns only the emission color of the geometry.
     *
     * @param intersection the closest intersection (geometry + point)
     * @return the color at the point
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        Color ambient = scene.ambientLight.getIntensity().scale(intersection.material.KA);

        Color localEffects = Color.BLACK;

        for (LightSource light : scene.lights) {
            if (!setLightSource(intersection, light)) continue;

            Color lightIntensity = light.getIntensity(intersection.point);
            Double3 diffuse = calcDiffuse(intersection);
            Double3 specular = calcSpecular(intersection);

            localEffects = localEffects.add(lightIntensity.scale(diffuse.add(specular)));
        }

        return ambient.add(localEffects);
    }

    /**
     * Preprocesses the intersection data.
     * @param intersection the intersection data
     * @param rayDirection the direction of the ray
     * @return true if the intersection is valid, false otherwise
     */

    public boolean preprocessIntersection(Intersectable.Intersection intersection, Vector rayDirection) {
        Vector normal = intersection.geometry.getNormal(intersection.point);
        Vector v = rayDirection;
        double nv = normal.dotProduct(v);

        intersection.Normal = normal;
        intersection.directionRay = v;
        intersection.ScaleDN = nv;

        return !isZero(nv);
    }

    /**
     * Sets the light source for the intersection.
     * @param intersection the intersection data
     * @param lightSource  the light source
     * @return true if the light source is valid, false otherwise
     */
    public boolean setLightSource(Intersectable.Intersection intersection, LightSource lightSource) {
        Vector l = lightSource.getL(intersection.point);
        double nl = intersection.Normal.dotProduct(l);

        intersection.lightSource = lightSource;
        intersection.DirectionLightSource = l;
        intersection.ScaleDL = nl;

        return !isZero(nl) && !isZero(intersection.ScaleDN);
    }

    /**
     * Calculates the color at the intersection point based on local effects.
     * @param intersection the intersection data
     * @return the color at the intersection point
     */
    Color calcColorLocalEffects(Intersectable.Intersection intersection) {
        Double3 diffuse = calcDiffuse(intersection);
        Double3 specular = calcSpecular(intersection);

        return intersection.lightSource.getIntensity(intersection.point).scale(diffuse.add(specular));
    }

    /**
     * Calculates the specular component of the color at the intersection point.
     * @param intersection the intersection data
     * @return the specular component of the color
     */
    Double3 calcSpecular(Intersectable.Intersection intersection) {
        Vector l = intersection.DirectionLightSource;
        Vector n = intersection.Normal;

        // r = l - 2*(l·n)*n
        double ln = l.dotProduct(n);
        Vector r = l.subtract(n.scale(2 * ln));

        // v = -direction of ray
        Vector v = intersection.directionRay.scale(-1);

        double vr = v.dotProduct(r);
        if (vr <= 0) return Double3.ZERO;

        Material mat = intersection.material;
        return mat.KS.scale(Math.pow(vr, mat.Nsh));
    }

    /**
     * Calculates the diffuse component of the color at the intersection point.
     * @param intersection the intersection data
     * @return the diffuse component of the color
     */
    Double3 calcDiffuse(Intersectable.Intersection intersection) {
        double nl = intersection.ScaleDL;
        if (nl <= 0) return Double3.ZERO;

        return intersection.material.KD.scale(nl);
    }
}
