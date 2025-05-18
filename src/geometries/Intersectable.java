package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * Abstract class representing geometric objects that can be intersected by rays.
 */
public abstract class Intersectable {

    /**
     * Finds intersection points of a ray with the geometry.
     * Returns only the points (without geometry reference).
     *
     * @param ray The ray to intersect with.
     * @return List of intersection points, or null if none.
     */
    public final List<Point> findIntersections(Ray ray) {
        var intersections = calculateIntersections(ray);
        return intersections == null ? null
                : intersections.stream().map(i -> i.point).toList();
    }

    /**
     * Calculates detailed intersections (including geometry).
     *
     * @param ray the ray
     * @return list of intersections or null
     */
    public final List<Intersection> calculateIntersections(Ray ray) {
        return calculateIntersectionsHelper(ray);
    }

    /**
     * Abstract helper method to be implemented by concrete geometries.
     *
     * @param ray the ray
     * @return list of intersections, or null
     */
    protected abstract List<Intersection> calculateIntersectionsHelper(Ray ray);
    /**
     * Passive data structure representing a geometry and its intersection point.
     */
    public static class Intersection {
        public final Geometry geometry;
        public final Point point;

        /**
         * Constructs an intersection from a geometry and a point.
         * @param geometry the intersected geometry
         * @param point the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Intersection that)) return false;
            return geometry == that.geometry && point.equals(that.point);
        }

        @Override
        public String toString() {
            return "Intersection{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
}
