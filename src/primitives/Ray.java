package primitives;

/**
 *  class represents ray
 */
public class Ray
{
   /**
    * point of origin
    */
   public final Point _p0;
   /**
    * direction of the ray
    */
   public final Vector _dir;

   /**
    * constructor with point and vector
    * @param p0
    * @param dir
    */
   public Ray(Point p0, Vector dir) {
      _p0 = p0;
      _dir = dir.normalize();
   }

   /**
    * ovveride equals func
    * @param object
    * @return
    */
   public boolean equals(Object object) {
      if (!(object instanceof Ray)) return false;
      if (!super.equals(object)) return false;
      Ray ray = (Ray) object;
      return java.util.Objects.equals(_p0, ray._p0) && java.util.Objects.equals(_dir, ray._dir);
   }

   /**
    * ovveride hashcode func
    * @return
    */

   public int hashCode() {
      return java.util.Objects.hash(super.hashCode(), _p0, _dir);
   }

   /**
    * ovveride tostring func
    * @return
    */
   @Override
   public String toString() {
      return "primitives.Ray{" +
              "_p0=" + _p0 +
              ", _dir=" + _dir +
              '}';
   }

}
