package geometries;
import primitives.Vector;
import primitives.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Triangle} class.
 */
class TriangleTests {

    /**
     * Test method for {@link geometries.Triangle#Triangle(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testConstructor() {
        //=============TC01: Valid triangle creation =============//
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Triangle triangle = new Triangle(p1, p2, p3);
        assertNotNull(triangle, "Triangle not created successfully.");
    }

    /**
     * Test method for edge cases in {@link geometries.Triangle#Triangle(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testCasesForThreePoints() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(1, 2, 3);
        Point p3 = new Point(4, 5, 6);

        //=============TC01: Two points are identical =============//
        assertThrows(IllegalArgumentException.class, () -> new Triangle(p1, p2, p3), "Error: two points are identical.");

        //=============TC02: All points are identical =============//
        assertThrows(IllegalArgumentException.class, () -> new Triangle(p1, p1, p1), "Error: all points are identical.");
    }


}
