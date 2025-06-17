package renderer;

/**
 * Ray tracer types
 */
public enum RayTracerType {
    /**
     * Simple (basic) ray tracer
     */
    SIMPLE,
    /**
     * Ray tracer using regular grid
     */
    GRID,
    /**
     * Ray tracer using adaptive grid - for more efficient memory usage
     */
    MULTI_THREADED
}
