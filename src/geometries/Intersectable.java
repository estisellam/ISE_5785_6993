package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Represents an object that can be intersected by a ray.
 * Provides a method to find intersection points with a given ray.
 */
public interface Intersectable {

    /**
     * Finds intersection points of a ray with the object.
     *
     * @param ray The ray to intersect with the object.
     * @return A list of intersection points, or null if no intersections exist.
     */
    List<Point> findIntersections(Ray ray);
}