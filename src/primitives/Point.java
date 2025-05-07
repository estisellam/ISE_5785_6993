package primitives;

/**
 * Represents a point in 3D space.
 * <p>
 * This class provides methods for performing operations such as
 * calculating distances, adding vectors, and subtracting points.
 * </p>
 * <p>
 * The {@code Point} class is immutable and thread-safe.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     Point p1 = new Point(1, 2, 3);
 *     Point p2 = new Point(4, 5, 6);
 *     Vector v = p1.subtract(p2);
 *     double distance = p1.distance(p2);
 * </pre>
 *
 * @author esti vaknin
 * @version 1.0
 * @since 2023
 */
public class Point {

    /**
     * A constant representing the origin point (0, 0, 0).
     */
    public static final Point ZERO = new Point(0, 0, 0);
    /**
     * The coordinates of the point in 3D space.
     */
    protected final Double3 coordinates;

    /**
     * Constructs a new {@code Point} with the specified x, y, and z coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    public Point(double x, double y, double z) {
        coordinates = new Double3(x, y, z);
    }

    /**
     * Constructs a new {@code Point} from a {@link Double3} object.
     *
     * @param x the {@code Double3} object representing the coordinates
     */
    public Point(Double3 x) {
        coordinates = x;
    }

    /**
     * Returns the x-coordinate of the point.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return coordinates.d1();
    }

    /**
     * Returns the y-coordinate of the point.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return coordinates.d2();
    }

    /**
     * Returns the z-coordinate of the point.
     *
     * @return the z-coordinate
     */
    public double getZ() {
        return coordinates.d3();
    }

    /**
     * Returns the coordinates of the point as a {@link Double3} object.
     *
     * @return the coordinates of the point
     */
    public Double3 getCoordinate() {
        return coordinates;
    }

    /**
     * Subtracts the specified point from this point and returns the resulting vector.
     *
     * @param a the point to subtract
     * @return the resulting vector
     */
    public Vector subtract(Point a) {
        Double3 d = this.coordinates.subtract(a.coordinates);
        return new Vector(d.d1(), d.d2(), d.d3());
    }

    /**
     * Adds the specified vector to this point and returns the resulting point.
     *
     * @param a the vector to add
     * @return the resulting point
     */
    public Point add(Vector a) {
        Double3 d = this.coordinates.add(a.coordinates);
        return new Point(d.d1(), d.d2(), d.d3());
    }

    /**
     * Calculates the squared distance between this point and the specified point.
     *
     * @param a the other point
     * @return the squared distance
     */
    public double distanceSquared(Point a) {
        double x = this.coordinates.d1() - a.coordinates.d1();
        double y = this.coordinates.d2() - a.coordinates.d2();
        double z = this.coordinates.d3() - a.coordinates.d3();
        return x * x + y * y + z * z;
    }

    /**
     * Calculates the distance between this point and the specified point.
     *
     * @param a the other point
     * @return the distance
     */
    public double distance(Point a) {
        return Math.sqrt(this.distanceSquared(a));
    }

    /**
     * Compares this point to the specified object for equality.
     *
     * @param obj the object to compare
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point other)) return false;
        return this.coordinates.equals(other.coordinates);
    }


    /**
     * Returns the hash code value for this point.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    /**
     * Returns a string representation of this point.
     *
     * @return a string representation of the point
     */
    @Override
    public String toString() {
        return "Point{" + "coordinate=" + coordinates + '}';
    }
}