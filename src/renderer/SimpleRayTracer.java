package renderer;

import geometries.Intersectable;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Simple implementation of ray tracer.
 */
public class SimpleRayTracer extends RayTracerBase {

    /**
     * Constructor for SimpleRayTracer.
     *
     * @param scene the scene to be rendered
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Ray head offset size for shadow ray for moving the ray origin to avoid self-intersection
     */
    private static final double DELTA = 0.1;


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

            localEffects = localEffects.add(calcColorLocalEffects(intersection));
        }

        Color emission = intersection.geometry.getEmission();
        return ambient.add(localEffects).add(emission);
    }

    /**
     * Preprocesses the intersection data.
     *
     * @param intersection the intersection data
     * @param rayDirection the direction of the ray
     * @return true if the intersection is valid, false otherwise
     */

    public boolean preprocessIntersection(Intersectable.Intersection intersection, Vector rayDirection) {
        Vector normal = intersection.geometry.getNormal(intersection.point);
        double nv = normal.dotProduct(rayDirection);
        intersection.normal = normal;
        intersection.directionRay = rayDirection;
        intersection.scaleDN = nv;

        // Check if the ray is paralleled  to  the geometry
        return !isZero(nv);
    }

    /**
     * Sets the light source for the intersection.
     *
     * @param intersection the intersection data
     * @param lightSource  the light source
     * @return true if the light source is valid, false otherwise
     */
    public boolean setLightSource(Intersectable.Intersection intersection, LightSource lightSource) {
        Vector l;
        try {
            l = lightSource.getL(intersection.point);
            if (isZero(l.lengthSquared())) return false;
        } catch (IllegalArgumentException e) {
            return false;
        }

        double nl = alignZero(intersection.normal.dotProduct(l));
        double nv = alignZero(intersection.normal.dotProduct(intersection.directionRay));

        intersection.lightSource = lightSource;
        intersection.directionLightSource = l;
        intersection.scaleDL = nl;
        intersection.scaleDN = nv;

        return nl * nv > 0;
    }


    /**
     * Calculates the color at the intersection point based on local effects.
     *
     * @param intersection the intersection data
     * @return the color at the intersection point
     */
    private Color calcColorLocalEffects(Intersectable.Intersection intersection) {
        Double3 kd = intersection.material.KD;
        Double3 ks = intersection.material.KS;
        int nShininess = intersection.material.Nsh;

        double nl = alignZero(intersection.normal.dotProduct(intersection.directionLightSource));
        double nv = alignZero(intersection.normal.dotProduct(intersection.directionRay));
        if (nl * nv <= 0)
            return Color.BLACK;

        // === Shadow check ===
        if (!unshaded(intersection)) {
            return Color.BLACK;
        }

        // === Diffuse ===
        Color diffusive = intersection.lightSource
                .getIntensity(intersection.point)
                .scale(kd.scale(nl < 0 ? -nl : nl));

        // === Specular ===
        Vector r = intersection.directionLightSource.subtract(intersection.normal.scale(2 * nl)).normalize();
        double minusVR = -1.0 * intersection.directionRay.dotProduct(r);
        if (minusVR <= 0)
            return diffusive; // no specular component

        Color specular = intersection.lightSource.getIntensity(intersection.point)
                .scale(ks.scale(Math.pow(minusVR, nShininess)));

        return diffusive.add(specular);
    }

    /**
     * Calculates the specular component of the color at the intersection point.
     *
     * @param intersection the intersection data
     * @return the specular component of the color
     */
    Double3 calcSpecular(Intersectable.Intersection intersection) {
        Vector l = intersection.directionLightSource;
        Vector n = intersection.normal;

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
     *
     * @param intersection the intersection data
     * @return the diffuse component of the color
     */
    Double3 calcDiffuse(Intersectable.Intersection intersection) {
        double nl = intersection.scaleDL;
        if (nl <= 0) return Double3.ZERO;

        return intersection.material.KD.scale(nl);
    }

    /**
     * Checks if the point is unshaded (no object blocks the light).
     *
     * @param intersection the intersection point on the geometry
     * @return true if the light source is not blocked, false if shadowed
     */
    private boolean unshaded(Intersectable.Intersection intersection) {
        Vector l = intersection.directionLightSource;

        if (l == null || isZero(l.lengthSquared())) {
            return true; // no direction so assume not shaded
        }

        Ray shadowRay = new Ray(intersection.point, l.scale(-1), intersection.normal);

        List<Intersectable.Intersection> intersections =
                scene.geometries.calculateIntersections(shadowRay);

        if (intersections == null)
            return true;

        double lightDistance = intersection.lightSource.getDistance(intersection.point);

        for (Intersectable.Intersection i : intersections) {
            if (intersection.point.distance(i.point) < lightDistance)
                return false;
        }

        return true;
    }

}
