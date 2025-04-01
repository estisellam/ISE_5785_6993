package geometries;
import primitives.*;

/**
 * class to represent a tube
 */
public class Tube extends RadialGeometry
{
   /**
    * center of the tube
    */
   protected Point center;
   /**
    * direction of the tube
    */
   protected Vector direction;

   /**
    * constructor with 3 points for a tube
    * @param x
    * @param a
    * @param y
    */
   public Tube(Point x, Vector a, double y)
   {
      super(y);
      center = x;
      direction = a.normalize();
      radius = y;
   }

   /**
    * get normal to the tube
    * @param x
    * @return
    */
   @Override
   public Vector getNormal(Point x)
   {
      return null;

   }
}
