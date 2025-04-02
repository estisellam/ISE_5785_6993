package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Polygon} class
 */
class PolygonTests {

    /**
     * Delta value for accuracy when comparing double values
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}
     */
    @Test
    void testConstructor() {
        //============= TC01: Correct quadrilateral with vertices in correct order =============//
        assertDoesNotThrow(() -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        //============= TC02: Wrong order of vertices =============//
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(0, 1, 0),
                        new Point(1, 0, 0),
                        new Point(-1, 1, 1)),
                "Constructed a polygon with wrong order of vertices");

        //============= TC03: Points are not on the same plane =============//
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 2, 2)),
                "Constructed a polygon with vertices that are not in the same plane");

        //============= TC04: Concave quadrilateral =============//
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)),
                "Constructed a concave polygon");

        //============= Boundary Values Tests =============//

        //============= TC10: A vertex is on a side of the quadrilateral =============//
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with a vertex on a side");

        //============= TC11: Last point is the same as the first point =============//
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 0, 1)),
                "Constructed a polygon with duplicate vertices");

        //============= TC12: Two points are the same =============//
        assertThrows(IllegalArgumentException.class, () -> new Polygon(
                        new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(0, 1, 0)),
                "Constructed a polygon with duplicate points");
    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point)}
     */
    @Test
    void testGetNormal() {
        //============= TC01: Simple test using a quadrilateral =============//
        Point[] pts = {
                new Point(0, 0, 1),
                new Point(1, 0, 0),
                new Point(0, 1, 0),
                new Point(-1, 1, 1)
        };
        Polygon pol = new Polygon(pts);

        // Ensure no exceptions occur
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "getNormal() threw an exception");

        // Calculate the normal vector
        Vector result = pol.getNormal(new Point(0, 0, 1));

        // Check if the result is a unit vector
        assertEquals(1, result.length(), DELTA, "Polygon normal is not a unit vector");

        // Check if the result is perpendicular to all edges
        for (int i = 0; i < 3; ++i) {
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon normal is not perpendicular to one of the edges");
        }
    }
}