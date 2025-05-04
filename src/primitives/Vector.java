package primitives;

public class Vector extends Point {
    /**
     * X axis unit vector
     */
    public static final Vector AXIS_X = new Vector(1, 0, 0);
    /**
     * Y axis unit vector
     */
    public static final Vector AXIS_Y = new Vector(0, 1, 0);
    /**
     * Z axis unit vector
     */
    public static final Vector AXIS_Z = new Vector(0, 0, 1);
    /**
     * X axis opposite unit vector
     */
    public static final Vector MINUS_X = new Vector(-1, 0, 0);
    /**
     * Y axis opposite unit vector
     */
    public static final Vector MINUS_Y = new Vector(0, -1, 0);
    /**
     * Z axis opposite unit vector
     */
    public static final Vector MINUS_Z = new Vector(0, 0, -1);

    /**
     * Vector constructor by 3 coordinate values
     *
     * @param coordinate
     */
    public Vector(Double3 coordinate) {
        super(coordinate);
        if (coordinate.equals(Double3.ZERO)) {
            throw new IllegalArgumentException(" zero vector not allowed");
        }
    }

    /**
     * vector constuctor by 3 points
     *
     * @param x
     * @param y
     * @param z
     */

    public Vector(double x, double y, double z) {
       this(new Double3(x, y, z));
    }

    /**
     * add function between two vectors
     *
     * @param vector
     * @return
     */
    @Override
    public Vector add(Vector vector) {
        return new Vector(coordinates.add(vector.coordinates));

    }

    /**
     * This function returns a new vector that is the result of multiplying the current vector by a scalar
     *
     * @param scalar the scalar to multiply the vector by
     * @return A new Vector object.
     */
    public Vector scale(double scalar) {
        return new Vector(coordinates.scale(scalar));

    }

    /**
     * This function calculates the dot product of two vectors.
     *
     * @param vector
     * @return
     */
    public double dotProduct(Vector vector) {
        return coordinates.d1() * vector.coordinates.d1() + coordinates.d2() * vector.coordinates.d2() + coordinates.d3() * vector.coordinates.d3();
    }

    /**
     * This function calculates the cross product of two vectors.
     *
     * @param vector
     * @return
     */
    public Vector crossProduct(Vector vector) {
        double x = coordinates.d2() * vector.coordinates.d3() - coordinates.d3() * vector.coordinates.d2();
        double y = coordinates.d3() * vector.coordinates.d1() - coordinates.d1() * vector.coordinates.d3();
        double z = coordinates.d1() * vector.coordinates.d2() - coordinates.d2() * vector.coordinates.d1();
        return new Vector(new Double3(x, y, z));
    }

    public double lengthSquared() {
        double u1 = coordinates.d1();
        double u2 = coordinates.d2();
        double u3 = coordinates.d3();
        return u1 * u1 + u2 * u2 + u3 * u3;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * func for normalize vector
     *
     * @return
     */
    public Vector normalize() {
        double length = length();
        if (length == 0) {
            throw new IllegalStateException("Cannot normalize a zero-length vector");
        }
        return scale(1 / length);
    }

    /**
     * This function returns a string representation of the vector.
     *
     * @return
     */

    @Override
    public String toString() {
        return "Vector " + super.toString();
    }

    /**
     * This function checks if two vectors are equal.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof Vector && super.equals(obj);
        }
    }

    /**
     * this function returns the hash code of the vector
     *
     * @return
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
