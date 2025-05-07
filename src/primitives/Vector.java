package primitives;

import static primitives.Util.isZero;

/**
 * Represents a 3D vector in space.
 * Provides methods for vector arithmetic, normalization, and other vector operations.
 * <p>
 * This class is immutable and thread-safe.
 *
 * @author esti
 */
public class Vector extends Point {

    /**
     * Unit vector along the X-axis.
     */
    public static final Vector AXIS_X = new Vector(1, 0, 0);

    /**
     * Unit vector along the Y-axis.
     */
    public static final Vector AXIS_Y = new Vector(0, 1, 0);

    /**
     * Unit vector along the Z-axis.
     */
    public static final Vector AXIS_Z = new Vector(0, 0, 1);

    /**
     * Opposite unit vector along the X-axis.
     */
    public static final Vector MINUS_X = new Vector(-1, 0, 0);

    /**
     * Opposite unit vector along the Y-axis.
     */
    public static final Vector MINUS_Y = new Vector(0, -1, 0);

    /**
     * Opposite unit vector along the Z-axis.
     */
    public static final Vector MINUS_Z = new Vector(0, 0, -1);

    /**
     * Constructs a vector using a `Double3` object.
     *
     * @param coordinate The 3D coordinates of the vector.
     * @throws IllegalArgumentException If the vector is a zero vector.
     */
    public Vector(Double3 coordinate) {
        super(coordinate);
        if (isZero(coordinate.d1()) && isZero(coordinate.d2()) && isZero(coordinate.d3())) {
            throw new IllegalArgumentException("Zero vector is not allowed");
        }
    }

    /**
     * Constructs a vector using three coordinate values.
     *
     * @param x The X-coordinate of the vector.
     * @param y The Y-coordinate of the vector.
     * @param z The Z-coordinate of the vector.
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Adds another vector to this vector.
     *
     * @param vector The vector to add.
     * @return A new vector representing the sum of the two vectors.
     */
    @Override
    public Vector add(Vector vector) {
        return new Vector(coordinates.add(vector.coordinates));
    }

    /**
     * Scales this vector by a scalar value.
     *
     * @param scalar The scalar value to multiply the vector by.
     * @return A new vector representing the scaled vector.
     */
    public Vector scale(double scalar) {
        return new Vector(coordinates.scale(scalar));
    }

    /**
     * Calculates the dot product of this vector and another vector.
     *
     * @param vector The other vector.
     * @return The dot product of the two vectors.
     */
    public double dotProduct(Vector vector) {
        return coordinates.d1() * vector.coordinates.d1() +
                coordinates.d2() * vector.coordinates.d2() +
                coordinates.d3() * vector.coordinates.d3();
    }

    /**
     * Calculates the cross product of this vector and another vector.
     *
     * @param vector The other vector.
     * @return A new vector representing the cross product.
     */
    public Vector crossProduct(Vector vector) {
        double x = coordinates.d2() * vector.coordinates.d3() - coordinates.d3() * vector.coordinates.d2();
        double y = coordinates.d3() * vector.coordinates.d1() - coordinates.d1() * vector.coordinates.d3();
        double z = coordinates.d1() * vector.coordinates.d2() - coordinates.d2() * vector.coordinates.d1();
        return new Vector(new Double3(x, y, z));
    }

    /**
     * Calculates the squared length of this vector.
     *
     * @return The squared length of the vector.
     */
    public double lengthSquared() {
        double u1 = coordinates.d1();
        double u2 = coordinates.d2();
        double u3 = coordinates.d3();
        return u1 * u1 + u2 * u2 + u3 * u3;
    }

    /**
     * Calculates the length (magnitude) of this vector.
     *
     * @return The length of the vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Normalizes this vector to a unit vector.
     *
     * @return A new vector representing the normalized vector.
     * @throws IllegalStateException If the vector has zero length.
     */
    public Vector normalize() {
        double length = length();
        if (length == 0) {
            throw new IllegalStateException("Cannot normalize a zero-length vector");
        }
        return scale(1 / length);
    }


    @Override
    public String toString() {
        return "Vector " + super.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof Vector && super.equals(obj);
        }
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }
}