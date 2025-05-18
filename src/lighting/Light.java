package lighting;

import primitives.Color;

/**
 * Light class represents a light source in a 3D scene.
 * It contains the intensity of the light.
 */
abstract class Light {
    /**
     * power of the light source
     */
    final protected Color intensity;

    /**
     * constructor
     *
     * @param i - the power of the light
     */
    protected Light(Color i) {
        intensity = i;
    }

    /**
     * getter for the intensity
     *
     * @return the intensity of the light
     */
    public Color getIntensity() {
        return intensity;
    }

}
