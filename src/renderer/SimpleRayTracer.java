package renderer;

import geometries.Intersectable;
import primitives.*;
import scene.Scene;
import java.util.List;

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

        return calcColor(closest);
    }


    /**
     * Computes the color at the intersection point.
     * Currently returns only the emission color of the geometry.
     *
     * @param intersection the closest intersection (geometry + point)
     * @return the color at the point
     */
    private Color calcColor(Intersectable.Intersection intersection) {
        return scene.ambientLight.getIntensity().scale(intersection.geometry.getMaterial().KA)
                .add(intersection.geometry.getEmission());
    }

}
