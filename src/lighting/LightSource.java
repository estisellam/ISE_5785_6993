package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface representing a light source in a 3D scene.
 * It provides methods to get the intensity and direction of the light at a given point.
 */
public interface LightSource {
    /**
     * Get the intensity of the light at a given point.
     * @param p the point in space
     * @return the color intensity of the light at the point
     */
    public Color getIntensity(Point p);

    /**
     * Get the direction of the light at a given point.
     * @param p the point in space
     * @return the direction vector of the light at the point
     */
    public Vector getL(Point p);
    /**
     * Returns the distance from the light source to a given point.
     *
     * @param point the point in space
     * @return the distance to the light source
     */
    double getDistance(Point point);

}
