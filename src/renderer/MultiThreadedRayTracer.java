package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

import java.util.List;

/**
 * pro2
 * Multi-threaded ray tracer that uses a simple ray tracer as its base.
 * This class is designed to handle ray tracing in a multi-threaded environment.
 * It extends the RayTracerBase class and utilizes the SimpleRayTracer for actual ray tracing logic.
 */
public class MultiThreadedRayTracer extends RayTracerBase {

    /**
     * The base ray tracer used for actual ray tracing operations.
     * This is an instance of SimpleRayTracer which performs the core ray tracing logic.
     */
    private final SimpleRayTracer baseTracer;

    /**
     * Constructor for MultiThreadedRayTracer.
     *
     * @param scene the scene to be rendered by this ray tracer.
     */
    public MultiThreadedRayTracer(Scene scene) {
        super(scene);
        baseTracer = new SimpleRayTracer(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        return baseTracer.traceRay(ray);
    }

    @Override
    public Color traceRay(List<Ray> rays) {
        return baseTracer.traceRay(rays);
    }
}