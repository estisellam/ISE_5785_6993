package primitives;

/**
 * class to represent a point
 */
public class Point
{
   /**
    * coordinate of the point in 3d
    */
   protected final Double3 coordinate;
   /**
    * point at origin
    */
   public final static Point ZERO = new Point(0, 0, 0);

   /**
    * constructor with 3 points
    * @param x
    * @param y
    * @param z
    */
   public Point(double x, double y, double z)
   {
      coordinate = new Double3(x, y, z);
   }

   /**
    * constructor with a point in 3d
    * @param x
    */
   public Point(Double3 x)
   {
      coordinate = x;
   }

   /**
    * vector between two points
    * @param a
    * @return
    */
   public Vector subtract(Point a)
   {
      if(this==a)
      {

      }
      Double3 d=this.coordinate.subtract(a.coordinate);
      Vector v = new Vector(d);
      return v;
   }
   /**
    * add a vector to point
    */
   public Point add(Vector a)
   {
      Double3 d=this.coordinate.add(a.coordinate);
      Point p = new Point(d);
      return p;
   }

   /**
    * distance squared between two points
    * @param a
    * @return
    */
   public double distanceSquared(Point a)
   {
      double x= this.coordinate.d1()-a.coordinate.d1();
      double y= this.coordinate.d2()-a.coordinate.d2();
      double z= this.coordinate.d3()-a.coordinate.d3();
      return x*x+y*y+z*z;
   }

   /**
    * distance between two points
    * @param a
    * @return
    */
   public double distance(Point a)
   {
      return Math.sqrt(this.distanceSquared(a));
   }

   /**
    * ovveride function to compare two points
    * @param obj
    * @return
    */
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      Point point = (Point) obj;
      return coordinate.equals(point.coordinate);
   }

   /**
    * ovveride function to print point
    * @return
    */

   @Override
   public String toString()
   {
      return "Point{" + "coordinate=" + coordinate + '}';
   }

}
