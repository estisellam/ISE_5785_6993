package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Class representing a spotlight in a 3D scene.
 * A SpotLight is a PointLight with a direction.
 */
public class SpotLight extends PointLight {
    /**
     * Vector representing the direction of the spotlight (normalized).
     */
    private final Vector direction;

    /**
     * Constructor for SpotLight.
     *
     * @param d         the direction of the light
     * @param intensity the intensity (color) of the light
     * @param position  the position of the light source
     * @param kC        constant attenuation factor
     * @param kL        linear attenuation factor
     * @param kQ        quadratic attenuation factor
     */
    public SpotLight(Vector d, Color intensity, Point position, double kC, double kL, double kQ) {
        super(position, intensity, kC, kL, kQ);
        this.direction = d.normalize();
    }

    /**
     * Calculates the intensity at point p considering the spotlight direction.
     *
     * @param p the point in space
     * @return the color intensity of the light at point p
     */
    @Override
    public Color getIntensity(Point p) {
        Vector l = getL(p);
        double factor = Math.max(0, direction.dotProduct(l));
        if (factor == 0) {
            return Color.BLACK;
        }
        return super.getIntensity(p).scale(factor);
    }

    /**
     * Returns a normalized direction vector from the light to point p.
     *
     * @param p the point in space
     * @return normalized vector from light to point
     */
    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }
}