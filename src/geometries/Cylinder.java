package geometries;

import primitives.Point;
import primitives.Vector;

import primitives.Ray;

/**
 * class to represent a cylinder
 */
public class Cylinder extends Tube {
   private final double height;

   /**
    * constructor with ray, radius and height
    * @param axisRay
    * @param radius
    * @param height
    */
   public Cylinder(Ray axisRay, double radius, double height)
   {
      super(axisRay._p0,axisRay._dir, radius);
      this.height = height;
   }


   /**
    * func to get normal
    * @param point
    * @return
    */
   @Override
   public Vector getNormal(Point point) {
      return null;
   }

}