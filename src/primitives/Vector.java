package primitives;

public class Vector extends Point
{

   public Vector(Double3 coordinate)
   {
      super(coordinate);
      if(coordinate.equals(Double3.ZERO))
      {
         throw new IllegalArgumentException(" zero vector not allowed");
      }
   }

   public Vector(double x, double y, double z)
   {
      super(x, y, z);
      if(coordinate.equals(Double3.ZERO)){
         throw new IllegalArgumentException("zero vector not allowed");
      }
   }



   public Vector add(Vector vector)
   {
      return new Vector(coordinate.add(vector.coordinate));

   }

   /**
    * This function returns a new vector that is the result of multiplying the current vector by a scalar
    * @param scalar the scalar to multiply the vector by
    * @return A new Vector object.
    */
   public Vector scale(double scalar)
   {
      return new Vector(coordinate.scale(scalar));

   }

   public double dotProduct(Vector vector)
   {
      return coordinate.d1() * vector.coordinate.d1() + coordinate.d2() * vector.coordinate.d2() + coordinate.d3() * vector.coordinate.d3();
   }

   public Vector crossProduct(Vector vector)
   {
      double x = coordinate.d2() * vector.coordinate.d3() - coordinate.d3() * vector.coordinate.d2();
      double y = coordinate.d3() * vector.coordinate.d1() - coordinate.d1() * vector.coordinate.d3();
      double z = coordinate.d1() * vector.coordinate.d2() - coordinate.d2() * vector.coordinate.d1();
      return new Vector(new Double3(x, y, z));
   }

   public double lengthSquared()
   {
      double u1 =coordinate.d1();
      double u2 = coordinate.d2();
      double u3 = coordinate.d3();
      return u1 * u1 + u2 * u2 + u3 * u3;
   }

   public double length()
   {
      return Math.sqrt(lengthSquared());
   }

   public Vector normalize()
   {
      double length = length();
      return new Vector(coordinate.reduce(length));
   }

   @Override
   public String toString()
   {
      return "Vector{} " + coordinate;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      else
      {
         return obj instanceof Vector && super.equals(obj);
      }
   }


   @Override
   public int hashCode()
   {
      return super.hashCode();
   }
}
