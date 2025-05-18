package lighting;

import primitives.Color;

/**
 * Ambient light class represents the light that is scattered in the
 */
public class AmbientLight extends Light {
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK);

    /**
     * Constructor for ambient light.
     * @param Ia the color of the ambient light
     */
    public AmbientLight(Color Ia) {
        super(Ia);
    }


}
