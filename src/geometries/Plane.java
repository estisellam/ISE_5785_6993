package geometries;
import primitives.*;

import java.util.Collections;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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
    * @param x point on the plane
    * @param y point on the plane
    * @param z point on the plane
    * @throws IllegalArgumentException if points are collinear or the normal vector length is zero
    */
   public Plane(Point x, Point y, Point z) throws IllegalArgumentException
   {
      point = x;
      Vector a = y.subtract(x);
      Vector b = z.subtract(x);

      // Check if points are collinear
      if (a.crossProduct(b).length() == 0)
      {
         throw new IllegalArgumentException("The points are collinear and do not define a valid plane.");
      }

      normal = a.crossProduct(b).normalize();
   }

   /**
    * constructor with point and vector for a plane
    * @param x point on the plane
    * @param a point on the plane
    */
   public Plane(Point x, Vector a)
   {
      point = x;
      normal = a.normalize();
   }

   /**
    * func get for point
    * @return point
    */
   public Point getPoint()
   {
        return point;
   }

   /**
    * func get for normal
    * @param p point on the plane
    * @return the normal vector of the plane
    */
   @Override
   public Vector getNormal(Point p)
   {
      return normal;
   }
   @Override
    public String toString() {
        return "Plane{" +
                  "point:" + point +
                  "normal:" + normal +
                  '}';
    }
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Plane plane)) return false;
        if (!super.equals(object)) return false;
        return java.util.Objects.equals(point, plane.point) && java.util.Objects.equals(normal, plane.normal);
    }
    @Override
    public int hashCode()
    {
        return point.hashCode() + normal.hashCode();
    }


   @Override
   public List<Point> findIntersections(Ray ray) {
       Vector x = ray.getDirection();
       Point a = ray.getHead();

       double nv = normal.dotProduct(x);

       // If the ray is parallel to the plane, no intersection
       if (isZero(nv)) {
           return null;
       }

       // If the ray starts exactly at the plane point, no intersection
       if (a.equals(point)) {
           return null;
       }

       // Calculate t using the formula for ray-plane intersection
       double t = normal.dotProduct(point.subtract(a)) / nv;

       // If t is less than or equal to 0, the intersection is behind the ray or at its start
       if (alignZero(t) <= 0) return null;

       // Get the intersection point using getPoint
       Point intersection = ray.getPoint(t);

       // Return the list with the single intersection point
       return List.of(intersection);
   }





}
