package geometries;
import primitives.Vector;
import primitives.Point;



/**
 * class to represent a sphere
 */
public class Sphere extends RadialGeometry
{
   /**
    * center of the sphere
    */
   private final Point center;

   /**
    * constructor with point, radius and sphere
    * @param center
    * @param radius
    */
   public Sphere(Point center, double radius)
   {
      super(radius);
      this.center = center;
   }

   /**
    * func get for normal
    * @param point
    * @return
    */
   @Override
   public Vector getNormal(Point point)
   {
      return null;
   }

   /**
    * ovveride tostring func
    * @return
    */
   @Override
   public String toString() {
      return "Sphere{" +
              "_center=" + center +
              ", _radius=" + radius +
              '}';
   }
}