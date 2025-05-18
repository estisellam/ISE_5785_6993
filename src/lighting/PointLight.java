package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

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
    private  double kC=0;
    private  double kL=0;
    private  double kQ=0;

    /**
     * Constructor for PointLight.
     *
     * @param position  the position of the light
     * @param intensity the intensity (color) of the light
     * @param kC        constant attenuation factor
     * @param kL        linear attenuation factor
     * @param kQ        quadratic attenuation factor
     */
    public PointLight(Point position, Color intensity, double kC, double kL, double kQ) {
        super(intensity);
        this.position = position;
        this.kC = kC;
        this.kL = kL;
        this.kQ = kQ;
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
        return intensity.reduce((int) attenuation);
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
}