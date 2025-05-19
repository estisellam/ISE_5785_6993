package primitives;

/**
 * Material class represents the material of an object in a 3D scene.
 */
public class Material {
    /**
     * Diffusion coefficient
     */
    public Double3 KA = Double3.ONE;
    /**
     * Reflection coefficient
     */
    public Double3 KD = Double3.ZERO;
    /**
     *
     */
    public Double3 KS = Double3.ZERO;
    /**
     * Shininess coefficient
     */
    public int Nsh=0;

    /**
     * setter for the reflection coefficient with a Double3 value
     * @param Kd point in 3D space
     * @return the material
     */
    public Material setKD(Double3 Kd)
    {
        this.KD = Kd;
        return this;
    }

    /**
     * setter for the reflection coefficient with a double value
     * @param Kd the reflection coefficient in double
     * @return the material
     */
    public Material setKD(double Kd)
    {
        this.KD = new Double3(Kd);
        return this;
    }
    /**
     * setter for the reflection coefficient with a Double3 value
     * @param Ks point in 3D space
     * @return the material
     */
    public Material setKS(Double3 Ks)
    {
        this.KS = Ks;
        return this;
    }
    /**
     * setter for the reflection coefficient with a double value
     * @param Ks the reflection coefficient in double
     * @return the material
     */
    public Material setKS(double Ks)
    {
        this.KS = new Double3(Ks);
        return this;
    }
    /**
     * setter for the shininess coefficient
     * @param Nsh the shininess coefficient
     * @return the material
     */
    public Material setShininess(int Nsh)
    {
        this.Nsh = Nsh;
        return this;
    }


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
