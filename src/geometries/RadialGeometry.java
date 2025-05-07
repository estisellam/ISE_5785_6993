package geometries;

/**
 * Represents an abstract radial geometry in 3D space.
 * Provides a base for geometries with a defined radius.
 */
public abstract class RadialGeometry extends Geometry {

    /**
     * The radius of the radial geometry.
     */
    protected double radius;

    /**
     * Constructs a radial geometry with the specified radius.
     *
     * @param a The radius of the radial geometry.
     * @throws IllegalArgumentException If the radius is less than or equal to zero.
     */
    public RadialGeometry(double a) {
        if (a <= 0) {
            throw new IllegalArgumentException("Error: radius should be greater than zero.");
        }
        radius = a;
    }

    /**
     * Returns the radius of the radial geometry.
     *
     * @return The radius of the radial geometry.
     */
    public double getRadius() {
        return radius;
    }
}