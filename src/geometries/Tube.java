package geometries;
import primitives.*;
import java.util.List;
/**
 * class to represent a tube
 */
public class Tube extends RadialGeometry
{
   /**
    * ray of the main axis of the tube
    */
   protected final Ray mainAxis;

   /**
    * constructor
    * @param x - radius
    * @param y - ray
    */
   public Tube(double x, Ray y)
   {
      super(x);
        if (y == null)
             throw new IllegalArgumentException("Error: ray cannot be null.");
        mainAxis =y;
   }

   /**
    * get normal to the tube
    * @param x
    * @return
    */
   @Override
   public Vector getNormal(Point x) {
      double a = (x.getX() - mainAxis.getHead().getX()) * mainAxis.getDirection().getX() +
                 (x.getY() - mainAxis.getHead().getY()) * mainAxis.getDirection().getY() +
                 (x.getZ() - mainAxis.getHead().getZ()) * mainAxis.getDirection().getZ();
      Point projected = mainAxis.getPoint(a);
      Vector normal = x.subtract(projected);
      return normal.normalize();
   }


   @Override
   public List<Point> findIntersections(Ray ray)
   {
      return null;
   }


}
