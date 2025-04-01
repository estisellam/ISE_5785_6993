package geometries;
import primitives.*;

/**
 * abstract class to represent a geometry
 */
public abstract class Geometry
{
   /**
    * abstract function to get normal
    * @param p
    * @return
    */
   public abstract Vector getNormal(Point p);
}
