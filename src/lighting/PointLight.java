package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * Class representing a point light in a 3D scene.
 * The light has a position in space and its intensity decreases with distance.
 */
public class PointLight extends Light implements LightSource {

    /**
     * The position of the point light in space.
     */
    protected final Point position;

    /**
     * Attenuation factors: constant (kC), linear (kL), and quadratic (kQ)
     */
    private double kC = 0;
    private double kL = 0;
    private double kQ = 0;

    /**
     * Constructor for PointLight.
     *
     * @param position  the position of the light
     * @param intensity the intensity (color) of the light
     */
    public PointLight( Color intensity ,Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Calculates the intensity of the light at point p, taking attenuation into account.
     *
     * @param p the point in space
     * @return the attenuated color intensity
     */
    @Override
    public Color getIntensity(Point p) {
        double d = position.distance(p);
        double attenuation = kC + kL * d + kQ * d * d;

        if (isZero(attenuation)) {
            return Color.BLACK;
        }

        return intensity.scale(1 / attenuation);
    }

    /**
     * Returns a normalized vector from the light position to the given point.
     *
     * @param p the point in space
     * @return normalized direction vector from light to point
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }

    /**
     * set the constant attenuation factor
     *
     * @param kC constant attenuation factor
     * @return this PointLight object for method chaining
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * set the linear attenuation factor
     *
     * @param kL linear attenuation factor
     * @return this PointLight object for method chaining
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }
    /**
     * set the quadratic attenuation factor
     *
     * @param kQ quadratic attenuation factor
     * @return this PointLight object for method chaining
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /**
     * Sets the narrow beam angle for the light.
     * @param narrowBeam the narrow beam angle
     * @return this PointLight object for method chaining
     */
    public PointLight setNarrowBeam(int narrowBeam) {
        return this;
    }
}