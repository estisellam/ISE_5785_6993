package primitives;

/**
 * Material class represents the material of an object in a 3D scene.
 */
public class Material {
    /**
     * Diffusion coefficient
     */
    public Double3 KA= Double3.ONE;

    /**
     * setter for the diffusion coefficient with a Double3 value
     * @param ka point in 3D space
     * @return the material
     */
    public Material setKA(Double3 ka) {
        this.KA = ka;
        return this;
    }
    /**
     * setter for the diffusion coefficient with a double value
     * @param ka the diffusion coefficient in double
     * @return the material
     */
    public Material setKA(double ka) {
        this.KA = new Double3(ka);
        return this;
    }
}
