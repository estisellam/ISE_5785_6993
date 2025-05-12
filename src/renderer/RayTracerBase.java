package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract base class for ray tracing.
 */
public abstract class RayTracerBase {
    protected final Scene scene;

    /**
     * Constructor that initializes the scene.
     * @param scene the scene to trace rays in
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Abstract method to trace a ray and compute the color.
     * @param ray the ray to trace
     * @return the color at the intersection point
     */
    public abstract Color traceRay(Ray ray);
}
