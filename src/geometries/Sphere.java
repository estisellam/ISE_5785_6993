package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a sphere in 3D space.
 * Provides methods to retrieve the sphere's properties and find intersections with a ray.
 * Extends the {@link RadialGeometry} class.
 *
 * @author esti
 */
public class Sphere extends RadialGeometry {

    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a sphere with the specified center and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     * @throws IllegalArgumentException If the radius is less than or equal to zero.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the center point of the sphere.
     *
     * @return The center point of the sphere.
     */
    public Point getCenter() {
        return center;
    }


    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }


    @Override
    public String toString() {
        return "Sphere: center: " + center + ", radius: " + radius;
    }


    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        Vector u;
        try {
            u = center.subtract(p0);
        } catch (IllegalArgumentException e) {
            // The ray starts at the center of the sphere
            return List.of(new Intersection(this,center.add(v.scale(radius))));
        }

        double tm = alignZero(u.dotProduct(v));
        double dSquared = alignZero(u.lengthSquared() - tm * tm);
        double rSquared = radius * radius;

        if (alignZero(dSquared- rSquared)>=0) {
            return null; // No intersection, the ray doesn't intersect the sphere
        }



        double th = Math.sqrt(rSquared - dSquared);

        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        // Avoid returning point at head (t == 0 or negative)
        boolean t1Valid = t1 > 0;
        boolean t2Valid = t2 > 0;

        if (t1Valid && t2Valid) {
            return List.of(new Intersection(this,ray.getPoint(t1)),new Intersection(this,ray.getPoint(t2)) );
        }

        if (t1Valid) {
            return List.of(new Intersection(this,ray.getPoint(t1)));
        }

        if (t2Valid) {
            return List.of(new Intersection(this, ray.getPoint(t2)));
        }

        return null; // No valid intersection
    }



}