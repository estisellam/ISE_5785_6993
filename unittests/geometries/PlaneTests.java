package geometries;

import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Plane} class.
 */
class PlaneTests {

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testConstructorWithThreePoints() {
        //=============TC01: Create a plane from three non-collinear points =============//
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);
        assertNotNull(plane, "Plane should be created when points are not on the same line.");

        //=============TC02: The points are collinear (should throw an error) =============//
        Point p4 = new Point(2, 2, 0);
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p4), "Error expected when points are on the same line.");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(Point)}.
     */
    @Test
    void testNormalVector() {
        //=============TC01: The normal should be perpendicular to the plane =============//
        Point p1 = new Point(1, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0, 0, 1);
        Plane plane = new Plane(p1, p2, p3);
        Vector normal = plane.getNormal(p1);
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        assertEquals(0, normal.dotProduct(v1), "Normal must be perpendicular to one vector");
        assertEquals(0, normal.dotProduct(v2), "Normal must be perpendicular to another vector");
        assertEquals(1, normal.length(), "Normal must have length 1");
    }

    /**
     * Test method for edge cases in {@link geometries.Plane#Plane(Point, Point, Point)}.
     */
    @Test
    void testCasesForThreePoints() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(1, 2, 3);
        Point p3 = new Point(4, 5, 6);

        //=============TC01: Two points are identical =============//
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p3), "Error: two points are identical.");

        //=============TC02: All points are identical =============//
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1), "Error: all points are identical.");
    }

    /**
     * Test method for {@link geometries.Plane#Plane(Point, Vector)}.
     */
    @Test
    void testConstructorWithPointAndVector() {
        //=============TC01: Create a plane with a valid point and normal vector =============//
        Point p1 = new Point(0, 0, 0);
        Vector normal = new Vector(1, 0, 0);
        Plane plane = new Plane(p1, normal);
        assertNotNull(plane, "Plane should be created with a valid point and normal vector.");
    }
}