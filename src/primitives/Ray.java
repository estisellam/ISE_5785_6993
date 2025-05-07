package primitives;

import static primitives.Util.isZero;

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
}