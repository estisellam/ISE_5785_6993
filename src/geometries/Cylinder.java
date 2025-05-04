package geometries;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import java.util.LinkedList;
import java.util.List;

/**
 * class to represent a cylinder
 */
public class Cylinder extends Tube
{
   private final double height;

   /**
    * constructor with ray, radius and height
    *
    * @param axisRay
    * @param radius
    * @param height
    */
      public Cylinder(Ray axisRay, double radius, double height)
   {
      super(radius, axisRay);
      this.height = height;
   }

   /**
    * getter func for hight
    * @return
    */

   public double getHeight()
   {
      return height;
   }

   /**
    * func to get normal
    *
    */
   @Override
   public Vector getNormal(Point point) {
      Point p0 = mainAxis.getHead(); // base center
      Vector dir = mainAxis.getDirection();

      double t;
      try {
         Vector v = point.subtract(p0);
         t = alignZero(v.dotProduct(dir));
      } catch (IllegalArgumentException e) {
         return dir.scale(-1); // point is at base center
      }

      if (isZero(t)) // bottom base
         return dir.scale(-1);
      if (isZero(t - height)) // top base
         return dir;

      // side
      Point o = mainAxis.getPoint(t);
      return point.subtract(o).normalize();
   }


   @Override
   public List<Point> findIntersections(Ray ray) {
      Point x = ray.getHead();
      Vector a = ray.getDirection();

      Point y = mainAxis.getHead();
      Vector b = mainAxis.getDirection();

      Vector c = x.subtract(y);

      double ab = a.dotProduct(b);
      double cb = c.dotProduct(b);

      Vector d = isZero(ab) ? null : b.scale(ab);
      Vector e = d == null ? a : a.subtract(d);

      Vector f = isZero(cb) ? null : b.scale(cb);
      Vector g = f == null ? c : c.subtract(f);

      double A = alignZero(e.lengthSquared());
      double B = alignZero(2 * e.dotProduct(g));
      double C = alignZero(g.lengthSquared() - radius * radius);
      double D = alignZero(B * B - 4 * A * C);

      List<Point> result = null;

      if (!isZero(A) && D >= 0) {
         double sD = Math.sqrt(D);
         double t1 = alignZero((-B + sD) / (2 * A));
         double t2 = alignZero((-B - sD) / (2 * A));

         if (t1 > 0) {
            Point z1 = ray.getPoint(t1);
            double h1 = alignZero(z1.subtract(y).dotProduct(b));
            if (h1 > 0 && h1 < height) {
               result = List.of(z1);
            }
         }

         if (t2 > 0) {
            Point z2 = ray.getPoint(t2);
            double h2 = alignZero(z2.subtract(y).dotProduct(b));
            if (h2 > 0 && h2 < height) {
               if (result == null) {
                  result = List.of(z2);
               } else {
                  double d1 = result.get(0).distance(x);
                  double d2 = z2.distance(x);
                  if (d1 < d2) {
                     result = List.of(result.get(0), z2);
                  } else {
                     result = List.of(z2, result.get(0));
                  }
               }
            }
         }
      }

      double nv = b.dotProduct(a);
      if (!isZero(nv)) {
         double t = alignZero(y.subtract(x).dotProduct(b) / nv);
         if (t > 0) {
            Point z = ray.getPoint(t);
            if (alignZero(z.subtract(y).lengthSquared() - radius * radius) < 0) {
               if (result == null) {
                  result = List.of(z);
               } else {
                  double d1 = result.get(0).distance(x);
                  double d2 = z.distance(x);
                  if (d1 < d2) {
                     result = List.of(result.get(0), z);
                  } else {
                     result = List.of(z, result.get(0));
                  }
               }
            }
         }

         Point y2 = y.add(b.scale(height));
         t = alignZero(y2.subtract(x).dotProduct(b) / nv);
         if (t > 0) {
            Point z = ray.getPoint(t);
            if (alignZero(z.subtract(y2).lengthSquared() - radius * radius) < 0) {
               if (result == null) {
                  result = List.of(z);
               } else {
                  double d1 = result.get(0).distance(x);
                  double d2 = z.distance(x);
                  if (d1 < d2) {
                     result = List.of(result.get(0), z);
                  } else {
                     result = List.of(z, result.get(0));
                  }
               }
            }
         }
      }

      return result;
   }

}





