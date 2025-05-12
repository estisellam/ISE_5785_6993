package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * Sence class represents a scene in the ray tracing process.
 * It contains the name of the scene.
 */
public class Scene {
    /**
     * Name of the scene.
     */
    final public String name;
    /**
     * Background color of the scene. this is public because it is used in the simple ray tracer class
     */
    public Color background= Color.BLACK;
    /**
     * Geometries in the scene. this is public because it is used in the simple ray tracer class
     */
    public Geometries geometries= new Geometries();
    /**
     * Ambient light in the scene. this is public because it is used in the simple ray tracer class
     */
    public AmbientLight ambientLight=AmbientLight.NONE;
    /**
     * Constructor for Sence class.
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
    }
    /**
     * Set the background color of the scene.
     * @param c the background color
     * @return this scene object (for chaining)
     */
    public Scene setBackground(Color c) {
        background = c;
        return this;
    }

    /**
     * set the ambinentLight of the scene
     * @param a
     * @return
     */
    public Scene setAmbientLight(AmbientLight a) {
        ambientLight=a;
        return this;
    }

    /**
     * set the geometries of the scene
     * @param g the geometries of the scene
     * @return this scene object (for chaining)
     */
    public Scene setGeometries(Geometries g) {
        geometries = g;
        return this;
    }
}
