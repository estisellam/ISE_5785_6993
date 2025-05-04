package primitives;

import static primitives.Util.isZero;

/**
 *  class represents ray
 */
public class Ray
{
   /**
    * point of origin
    */
   private final Point head;
   /**
    * direction of the ray
    */
   private final Vector direction;

   /**
    * constructor with point and vector
    * @param a
    * @param b
    */
   public Ray(Point a, Vector b) {
      head = a;
      direction = b.normalize();
   }

   /**
    The `getPoint(double a)` method returns a point on a line starting at `origin` and going in the direction of `direction`
    . The point is calculated based on the value of `a`, which controls how far along the line the point is. If there is a problem
    (like `null` or an error), it returns the `origin` point instead.
    * @return
    */
   public Point getPoint(double a)
   {
      if(isZero(a)) return head;
      //P=P0+V*t
         return head.add(direction.scale(a));
   }
   /**
    * getter for the head
    * @return
    */

   public Point getHead()
   {
      return head;
   }

   /**
    * getter for the direction
    * @return
    */
    public Vector getDirection()
    {
        return direction;
    }

   /**
    * ovveride equals func
    * @param object
    * @return
    */
   public boolean equals(Object object)
   {
      if (this == object) return true;
      return (object instanceof Ray other) && head.equals(other.head) && direction.equals(other.direction);
   }

   /**
    * ovveride hashcode func
    * @return
    */
   public int hashCode()
   {
      return head.hashCode() + direction.hashCode();
   }
   /**
    * ovveride tostring func
    * @return
    */
   @Override
   public String toString()
   {
      return "primitives.Ray{" + "head=" + head + "direction=" + direction + '}';
   }

}
