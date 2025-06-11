package primitives;

import java.util.List;

import static primitives.Util.isZero;
import geometries.Intersectable.Intersection;

/**
 * Represents a ray in 3D space, defined by an origin point and a direction vector.
 * Provides methods to retrieve the ray's properties and calculate points along the ray.
 * <p>
 * A ray is a half-line that starts at a specific point (origin) and extends infinitely
 * in a specific direction.
 * <p>
 * This class is immutable and thread-safe.
 *
 * @author esti
 */
public class Ray {

    /**
     * The origin point of the ray.
     */
    private final Point head;

    /**
     * The direction vector of the ray.
     */
    private final Vector direction;

    /**
     * Constructs a ray with a given origin point and direction vector.
     *
     * @param a The origin point of the ray.
     * @param b The direction vector of the ray.
     * @throws IllegalArgumentException If the direction vector is null or not normalized.
     */
    public Ray(Point a, Vector b) {
        head = a;
        direction = b.normalize();
    }

    /**
     * Returns a point on the ray at a given distance from the origin.
     *
     * @param t The distance from the origin. A positive value indicates a point
     *          in the direction of the ray, while a negative value indicates a
     *          point in the opposite direction.
     * @return The point on the ray at the given distance.
     */
    public Point getPoint(double t) {
        return isZero(t) ? head : head.add(direction.scale(t));
    }

    /**
     * Returns the origin point of the ray.
     *
     * @return The origin point of the ray.
     */
    public Point getHead() {
        return head;
    }

    /**
     * Returns the direction vector of the ray.
     *
     * @return The direction vector of the ray.
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Compares this ray to another object for equality.
     *
     * @param object The object to compare.
     * @return {@code true} if the object is a ray with the same origin and direction,
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        return (object instanceof Ray other) && head.equals(other.head) && direction.equals(other.direction);
    }


    @Override
    public int hashCode() {
        return head.hashCode() + direction.hashCode();
    }


    @Override
    public String toString() {
        return "primitives.Ray{" + "head=" + head + ", direction=" + direction + '}';
    }

    /**
     * returns the closest point to the ray's head from a list of intersections.
     * @param intersections the list of intersections to search
     * @return the closest point to the ray's head, or null if the list is empty or null
     */
    public Intersection findClosestIntersection(List<Intersection> intersections) {
        if (intersections == null || intersections.isEmpty()) {
            return null;
        }

        Intersection closest = null;
        double minDistance = Double.POSITIVE_INFINITY;

        for (Intersection i : intersections) {
            double distance = i.point.distance(this.head);
            if (distance < minDistance) {
                minDistance = distance;
                closest = i;
            }
        }

        return closest;
    }

    public Point findClosestPoint(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return null;
        }

        Point closest = null;
        double minDistanceSquared = Double.MAX_VALUE;

        for (Point p : points) {
            double dist = this.head.distanceSquared(p); // assuming 'this' is a Ray and head is the origin
            if (dist < minDistanceSquared) {
                minDistanceSquared = dist;
                closest = p;
            }
        }

        return closest;
    }

    // Small constant to avoid self-intersection
    private static final double DELTA = 0.01;

    /**
     * Creates a new ray with the head slightly moved along the normal to avoid self-intersection.
     *
     * @param head    the original ray start point
     * @param dir     the direction of the ray
     * @param normal  the normal vector at the point
     * @return a new Ray with a biased origin
     */
    public static Ray createBiasedRay(Point head, Vector dir, Vector normal) {
        double dot = normal.dotProduct(dir);
        Vector delta = dir.normalize().scale(DELTA);  //always move in the direction of the ray
        return new Ray(head.add(delta), dir);
    }




}
