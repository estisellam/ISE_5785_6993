package geometries;

/**
 * abstract class to represent radial geometry
 */
public abstract class RadialGeometry extends Geometry
{
   /**
    * radius of the radial geometry
    */
   protected double radius;

   /**
    * constructor with a radius
    * @param a
    */
   public RadialGeometry(double a)
   {
      radius = a;
   }

   /**
    * func get for radius
    * @return
    */
   public double getRadius()
   {
      return radius;
   }
}
