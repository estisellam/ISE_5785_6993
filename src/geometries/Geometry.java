package geometries;
import primitives.*;

import java.util.List;

/**
 * abstract class to represent a geometry
 */
public abstract class Geometry implements Intersectable
{
   /**
    * abstract function to get normal
    * @param p point to get the normal at
    * @return the normal vector at the point
    */
   public abstract Vector getNormal(Point p);

   public abstract List<Point> findIntersections(Ray ray);
}
