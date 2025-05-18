package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
*class representing directional light in a 3D scene
 *  */
public class DirectionalLight extends Light implements LightSource{
    /**
     * vector representing the direction of the light
     */
    private final Vector direction;
    /**
     * constructor
     * @param intensity the intensity of the light
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }

}
