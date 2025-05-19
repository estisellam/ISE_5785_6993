package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * Represents a triangle in 3D space, defined by three vertices.
 * Extends the {@link Polygon} class.
 * Provides methods to retrieve the triangle's properties.
 * <p>
 * A triangle is a special case of a polygon with exactly three vertices.
 * It is always convex and lies in a single plane.
 *
 * @author esti
 */
public class Triangle extends Polygon {

    /**
     * Constructs a triangle using three points (vertices).
     *
     * @param x The first vertex of the triangle.
     * @param y The second vertex of the triangle.
     * @param z The third vertex of the triangle.
     * @throws IllegalArgumentException If the vertices are collinear or invalid for a triangle.
     */
    public Triangle(Point x, Point y, Point z) {
        super(x, y, z);
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null) return null;

        Point p0 = ray.getHead();              // origin of ray
        Vector v = ray.getDirection();         // direction of ray
        Point p = planeIntersections.get(0);   // intersection point with the plane

        Point a = vertices.get(0);             // vertex A
        Point b = vertices.get(1);             // vertex B
        Point c = vertices.get(2);             // vertex C

        Vector v1 = a.subtract(p0);
        Vector v2 = b.subtract(p0);
        Vector v3 = c.subtract(p0);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = alignZero(v.dotProduct(n1));
        double s2 = alignZero(v.dotProduct(n2));
        double s3 = alignZero(v.dotProduct(n3));

        if ((s1 > 0 && s2 > 0 && s3 > 0) || (s1 < 0 && s2 < 0 && s3 < 0))

            return List.of(new Intersection(this,p)); // Point is inside the triangle

        return null; // Point is outside the triangle
    }
}