package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * Represents an abstract geometry in 3D space.
 * Provides methods to calculate the normal vector at a given point
 * and to find intersections with a given ray.
 */
public abstract class Geometry implements Intersectable {

    /**
     * Returns the normal vector to the geometry at a given point.
     *
     * @param p The point at which the normal vector is calculated.
     * @return The normal vector to the geometry at the given point.
     */
    public abstract Vector getNormal(Point p);

    /**
     * Finds intersection points of a ray with the geometry.
     *
     * @param ray The ray to intersect with the geometry.
     * @return A list of intersection points, or null if no intersections exist.
     */
    public abstract List<Point> findIntersections(Ray ray);
}