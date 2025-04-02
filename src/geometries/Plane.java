package geometries;
import primitives.*;

/**
 * class to represent a plane
 */
public class Plane extends Geometry
{
   /**
    * point on the plane
    */
   protected final Point point;
   /**
    * normal to the plane
    */
   protected final Vector normal;

   /**
    * constructor with 3 points for a plane
    * @param x
    * @param y
    * @param z
    * @throws IllegalArgumentException if points are collinear or the normal vector length is zero
    */
   public Plane(Point x, Point y, Point z) throws IllegalArgumentException
   {
      point = x;
      Vector a = y.subtract(x);
      Vector b = z.subtract(x);

      // Check if points are collinear
      if (a.crossProduct(b).length() == 0) {
         throw new IllegalArgumentException("The points are collinear and do not define a valid plane.");
      }

      normal = a.crossProduct(b).normalize();
   }

   /**
    * constructor with point and vector for a plane
    * @param x
    * @param a
    */
   public Plane(Point x, Vector a)
   {
      point = x;
      normal = a.normalize();
   }

   /**
    * func get for normal
    * @param p
    * @return
    */
   @Override
   public Vector getNormal(Point p)
   {
      return normal;
   }
}
