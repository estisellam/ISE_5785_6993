package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Plane} class.
 */
class PlaneTests {
    private static final double DELTA = 0.000001;

    /**
     * Test constructor with three points.
     */
    @Test
    void testConstructorWithThreePoints() {
        // ============ EP: Three non-collinear points ============ //
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        assertDoesNotThrow(() -> new Plane(p1, p2, p3), "Plane should be created with valid non-collinear points.");

        // ============ Error case: Collinear points ============ //
        Point collinear = new Point(2, 0, 0); // lies on the line between p1 and p2
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, collinear),
                "Should throw an exception when points are collinear.");
    }

    /**
     * Test edge cases for constructor with three points.
     */
    @Test
    void testConstructorEdgeCases() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(1, 2, 3); // same as p1
        Point p3 = new Point(4, 5, 6);

        // ============ BVA: Two identical points ============ //
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p3),
                "Should throw an exception when two points are identical.");

        // ============ BVA: All points are identical ============ //
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1),
                "Should throw an exception when all points are identical.");
    }

    /**
     * Test constructor with point and normal vector.
     */
    @Test
    void testConstructorWithPointAndVector() {
        // ============ EP: Valid point and normal vector ============ //
        Point p = new Point(0, 0, 0);
        Vector normal = new Vector(1, 0, 0);
        assertDoesNotThrow(() -> new Plane(p, normal),
                "Plane should be created with a valid point and normal vector.");
    }

    /**
     * Test getNormal method for correctness.
     */
    @Test
    void testGetNormal() {
        // ============ EP: Normal vector is perpendicular to plane vectors ============ //
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(1, 0, 0);
        Point p3 = new Point(0, 1, 0);
        Plane plane = new Plane(p1, p2, p3);

        // Expect the normal to be orthogonal to both vectors formed by the plane points
        Vector normal = plane.getNormal(p1);
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);

        assertEquals(0, normal.dotProduct(v1), DELTA, "Normal should be perpendicular to first edge.");
        assertEquals(0, normal.dotProduct(v2), DELTA, "Normal should be perpendicular to second edge.");
        assertEquals(1, normal.length(), DELTA, "Normal should be a unit vector.");
    }
    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Plane plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray from outside, not parallel, hits the plane (1 point)
        Ray ray1 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Point> result1 = plane.findIntersections(ray1);
        assertNotNull(result1, "Ray should hit the plane");
        assertEquals(1, result1.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, 1)), result1, "Wrong intersection point");

        // TC02: Ray from outside, not parallel, misses the plane (0 points)
        Ray ray2 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray2), "Ray should miss the plane");

        // =============== Boundary Values Tests ==================

        // **** Group 1: Ray is parallel to plane
        // TC11: Ray is outside the plane (0 points)
        Ray ray11 = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray11), "Ray parallel and outside");

        // TC12: Ray is inside the plane (0 points)
        Ray ray12 = new Ray(new Point(0.5, 0.5, 1), new Vector(1, 0, 0));
        assertNull(plane.findIntersections(ray12), "Ray parallel and in plane");

        // **** Group 2: Ray is orthogonal to plane
        // TC21: Ray starts before the plane (1 point)
        Ray ray21 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        List<Point> result21 = plane.findIntersections(ray21);
        assertNotNull(result21, "Ray orthogonal before plane");
        assertEquals(1, result21.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0, 0, 1)), result21, "Wrong intersection point");

        // TC22: Ray starts in the plane (0 points)
        Ray ray22 = new Ray(new Point(0, 0, 1), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray22), "Ray orthogonal in plane");

        // TC23: Ray starts after the plane (0 points)
        Ray ray23 = new Ray(new Point(0, 0, 2), new Vector(0, 0, 1));
        assertNull(plane.findIntersections(ray23), "Ray orthogonal after plane");

        // **** Group 3: Ray not parallel and not orthogonal, starts in plane (0 points)
        Ray ray3 = new Ray(new Point(0.5, 0.5, 1), new Vector(0, 1, 1));
        assertNull(plane.findIntersections(ray3), "Ray from in plane at angle");

        // **** Group 4: Ray starts at plane's base point (0 points)
        Ray ray4 = new Ray(new Point(0, 0, 1), new Vector(1, 1, 1));
        assertNull(plane.findIntersections(ray4), "Ray from base point at angle");
    }
}
