package geometries;

import lighting.LightSource;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        /**
         * The geometry that was intersected.
         */
        public final Geometry geometry;
        /**
         * The intersection point.
         */
        public final Point point;
        /**
         * The material of the geometry.
         */
        public final Material material;
        /**
         * The direction of the ray that caused the intersection.
         */
        public Vector directionRay;
        /**
         * The normal vector in front the geometry in intersection point.
         */
        public Vector normal;
        /**
         * scale of the direction of the ray and the normal vector
         */
        public Double scaleDN;
        /**
         *the light source that illuminates the scenet
         */
        public LightSource lightSource;
        /**
         * the light source that is directed to intersection point.
         */
        public Vector directionLightSource;
        /**
         * scale of the direction of the light source and the normal vector
         */
        public Double scaleDL;



        /**
         * Constructs an intersection from a geometry and a point.
         * @param geometry the intersected geometry
         * @param point the intersection point
         */
        public Intersection(Geometry geometry, Point point, Material material) {
            if (geometry == null || point == null) {
                throw new IllegalArgumentException("Geometry and point cannot be null");
            }
            this.material = material;
            this.geometry = geometry;
            this.point = point;
        }

        /**
         * Constructs an intersection from a geometry and a point.
         * @param geometry the intersected geometry
         * @param point the intersection point
         */
        public Intersection(Geometry geometry, Point point) {
            if (geometry == null)
                throw new IllegalArgumentException("Geometry must not be null");
            this.geometry = geometry;
            this.point = point;
            this.material = geometry.getMaterial();
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
