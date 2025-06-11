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
     * Maximum number of color calculations for reflections and refractions
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     * Minimum color value for calculations
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     * Initial reflection coefficient
     */
    private static final Double3 INITIAL_K = Double3.ONE;

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
        Intersectable.Intersection closest = findClosestIntersection(ray);

        if (closest == null) {
            return scene.background;
        }

        return calcColor(closest, ray);
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

        Double3 ktr = transparency(intersection, intersection.directionLightSource, intersection.lightSource);
        if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;


        // === Diffuse ===
        Color diffusive = intersection.lightSource
                .getIntensity(intersection.point)
                .scale(kd.scale(nl < 0 ? -nl : nl))
                .scale(ktr);  // add transparency factor - ktr - Diffuse
        // === Specular ===
        Vector r = intersection.directionLightSource.subtract(intersection.normal.scale(2 * nl)).normalize();
        double minusVR = -1.0 * intersection.directionRay.dotProduct(r);
        if (minusVR <= 0)
            return diffusive; // no specular component

        Color specular = intersection.lightSource.getIntensity(intersection.point)
                .scale(ks.scale(Math.pow(minusVR, nShininess)))
                .scale(ktr);

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

        Ray shadowRay = Ray.createBiasedRay(intersection.point, l.scale(-1), intersection.normal);

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

    /**
     * Calculates the color at the intersection point.
     * the recursive method that calculates the color at the intersection point call calcColor(Intersectable.Intersection intersection, Ray ray);
     *
     * @param intersection the intersection data
     * @param ray          the ray that intersects with the geometry
     * @return the color at the intersection point
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray) {
        return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K);
    }

    /**
     * Calculates the color at the intersection point with recursion for reflections and refractions. called from calcColor(Intersectable.Intersection intersection, Ray ray);
     *
     * @param intersection the intersection data
     * @param ray          the ray that intersects with the geometry
     * @param level        the current recursion level
     * @param k            the reflection/refraction coefficient
     * @return the color at the intersection point
     */
    private Color calcColor(Intersectable.Intersection intersection, Ray ray, int level, Double3 k) {
        if (!preprocessIntersection(intersection, ray.getDirection())) {
            return Color.BLACK;
        }

        Color color = scene.ambientLight.getIntensity().scale(intersection.material.KA)
                .add(intersection.geometry.getEmission());

        for (LightSource light : scene.lights) {
            if (!setLightSource(intersection, light)) continue;
            color = color.add(calcColorLocalEffects(intersection));
        }

        if (level == 1 || k.lowerThan(MIN_CALC_COLOR_K)) {
            return color;
        }
//add global effects like reflection and refraction
        return color.add(calcGlobalEffects(intersection, ray, level, k));
    }


    /**
     * Calculates global effects like reflection and refraction (recursive).
     *
     * @param intersection the intersection data
     * @param ray          the incoming ray
     * @param level        recursion depth remaining
     * @param k            the cumulative reflection/refraction coefficient
     * @return the color contribution from global effects
     */
    private Color calcGlobalEffects(Intersectable.Intersection intersection, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;

        Vector n = intersection.normal;
        Point point = intersection.point;
        Vector v = ray.getDirection();

        // Reflection (KR)
        Double3 kR = intersection.material.KR;
        Double3 kkr = k.product(kR);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) {
            Vector r = v.subtract(n.scale(2 * v.dotProduct(n))); // reflect direction
            Ray reflectedRay = Ray.createBiasedRay(point, r, n);

            List<Intersectable.Intersection> reflectedIntersections = scene.geometries.calculateIntersections(reflectedRay);
            if (reflectedIntersections != null) {
                reflectedIntersections.removeIf(i -> i.geometry == intersection.geometry); // סינון הקריטי
                Intersectable.Intersection reflectedIntersection = reflectedRay.findClosestIntersection(reflectedIntersections);
                if (reflectedIntersection != null) {
                    color = color.add(calcColor(reflectedIntersection, reflectedRay, level - 1, kkr).scale(kR));
                }
            }
        }

        // Refraction (KT)
        Double3 kT = intersection.material.KT;
        Double3 kkt = k.product(kT);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {
            Ray refractedRay = Ray.createBiasedRay(point, v, n);  // same direction, bias with normal

            List<Intersectable.Intersection> refractedIntersections = scene.geometries.calculateIntersections(refractedRay);
            if (refractedIntersections != null) {
                refractedIntersections.removeIf(i -> i.geometry == intersection.geometry); // סינון הקריטי
                Intersectable.Intersection refractedIntersection = refractedRay.findClosestIntersection(refractedIntersections);
                if (refractedIntersection != null) {
                    color = color.add(calcColor(refractedIntersection, refractedRay, level - 1, kkt).scale(kT));
                }
            }
        }

        return color;
    }


    /**
     * Finds the closest intersection point to the ray's origin.
     *
     * @param ray the ray to check
     * @return the closest intersection, or null if no intersections found
     */
    private Intersectable.Intersection findClosestIntersection(Ray ray) {
        List<Intersectable.Intersection> intersections = scene.geometries.calculateIntersections(ray);
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }
        return ray.findClosestIntersection(intersections);
    }

    /**
     * Calculates the transparency at the intersection point based on the light source and the direction.
     *
     * @param intersection the intersection data
     * @param l            the direction to the light source
     * @param light        the light source
     * @return the transparency factor at the intersection point
     */

    private Double3 transparency(Intersectable.Intersection intersection, Vector l, LightSource light) {
        Ray shadowRay = Ray.createBiasedRay(intersection.point, l.scale(-1), intersection.normal);
        List<Intersectable.Intersection> intersections = scene.geometries.calculateIntersections(shadowRay);

        if (intersections == null) return Double3.ONE;

        double lightDistance = light.getDistance(intersection.point);
        Double3 ktr = Double3.ONE;

        for (Intersectable.Intersection i : intersections) {
            if (intersection.point.distance(i.point) < lightDistance) {
                ktr = ktr.product(i.geometry.getMaterial().KT);
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
            }
        }

        return ktr;
    }

    /**
     * Calculate the average color from multiple rays (for Anti-Aliasing or DOF).
     *
     * @param rays list of rays to trace
     * @return the averaged color
     */
    public Color traceRay(List<Ray> rays) {
        Color totalColor = Color.BLACK; // start with black

        for (Ray ray : rays) {
            totalColor = totalColor.add(traceRay(ray)); // add color of each ray
        }

        // average = total / number of rays
        return totalColor.reduce(rays.size());
    }
}
