package geometries;
import primitives.Vector;
import primitives.*;

import java.util.Collections;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


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
    * @param center point of the sphere
    * @param radius radius of the sphere
    */
   public Sphere(Point center, double radius)
   {
      super(radius);
      this.center = center;
   }

   /**
    * func get for center
    * @return center
    */
   public Point getCenter()
   {
      return center;
   }

   /**
    * func get for normal
    * @param point point on the sphere
    * @return normal vector
    */
   @Override
   public Vector getNormal(Point point)
   {
      return point.subtract(center).normalize();
   }

   /**
    * toString function
    * @return string representation of the sphere
    */
   @Override
   public String toString()
   {
      return "Sphere:" + "center:" + center + "radius:" + radius;
   }


   @Override
   public List<Point> findIntersections(Ray ray) {
      Point p0 = ray.getHead();
      Vector v = ray.getDirection();

      Vector u;
      try {
         u = center.subtract(p0);
      } catch (IllegalArgumentException e) {
         // The ray starts at the center of the sphere
         return List.of(center.add(v.scale(radius)));
      }

      double tm = alignZero(u.dotProduct(v));
      double dSquared = alignZero(u.lengthSquared() - tm * tm);
      double rSquared = radius * radius;

      if (dSquared >= rSquared) {
         return null; // No intersection, the ray doesn't intersect the sphere
      }

      double thSquared = alignZero(rSquared - dSquared);
      if (isZero(thSquared)) {
         return null; // Exactly one intersection, ray is tangent to the sphere
      }

      double th = Math.sqrt(thSquared);
      double t1 = alignZero(tm - th);
      double t2 = alignZero(tm + th);

      // Avoid returning point at head (t == 0 or negative)
      boolean t1Valid = t1 > 0 && !isZero(t1);
      boolean t2Valid = t2 > 0 && !isZero(t2);

      if (t1Valid && t2Valid) {
         return List.of(ray.getPoint(t1), ray.getPoint(t2));
      }

      if (t1Valid) {
         return List.of(ray.getPoint(t1));
      }

      if (t2Valid) {
         return List.of(ray.getPoint(t2));
      }

      return null; // No valid intersection
   }


}



