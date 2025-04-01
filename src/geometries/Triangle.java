package geometries;
import primitives.Point;

/**
 * class to represent a triangle
 */
public class Triangle extends Polygon
{
    /**
     * constructor with 3 points for a triangle
     * @param x
     * @param y
     * @param z
     */
    public Triangle(Point x, Point y, Point z)
    {
        super(x, y, z);
    }
}