package renderer;

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
        List<Point> intersections = scene.geometries.findIntersections(ray);

        if (intersections == null || intersections.isEmpty()) {
            return scene.background;
        }

        Point closest = ray.findClosestPoint(intersections);

        return calcColor(closest);
    }



    private Color calcColor(Point point) {
        return scene.ambientLight.getIntensity();
    }

}
