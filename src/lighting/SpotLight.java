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
     * Narrow beam angle in degrees
     */
    private int narrowBeam = 1;

    /**
     * Constructor for SpotLight.
     *
     * @param d         the direction of the light
     * @param intensity the intensity (color) of the light
     * @param position  the position of the light source

     */
    public SpotLight( Color intensity, Point position,Vector d) {
        super(intensity, position);
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
        double dirFactor = Math.max(0, direction.dotProduct(l));
        if (dirFactor == 0) {
            return Color.BLACK;
        }

        double focusFactor = Math.pow(dirFactor, narrowBeam);
        return super.getIntensity(p).scale(focusFactor);
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

    /**
     * Sets the constant attenuation factor.
     * @param kC the constant attenuation factor
     * @return this SpotLight instance
     */
    public SpotLight setKC(double kC) {
        super.setKc(kC);
        return this;
    }
    /**
     * Sets the linear attenuation factor.
     * @param kL the linear attenuation factor
     * @return this SpotLight instance
     */
    public SpotLight setKL(double kL) {
        super.setKl(kL);
        return this;
    }
    /**
     * Sets the quadratic attenuation factor.
     * @param kQ the quadratic attenuation factor
     * @return this SpotLight instance
     */
    public SpotLight setKQ(double kQ) {
        super.setKq(kQ);
        return this;
    }

    /**
     * Sets the narrow beam angle.
     * @param narrowBeam the narrow beam angle in degrees
     * @return this SpotLight instance
     */
    @Override
    public SpotLight setNarrowBeam(int narrowBeam) {
        this.narrowBeam = narrowBeam;
        return this;
    }

}