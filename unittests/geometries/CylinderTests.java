package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Cylinder} class.
 */
class CylinderTests
{
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Cylinder#Cylinder(primitives.Ray, double, double)}.
     */
    @Test
    void testConstructor()
    {
        // TC01: Valid cylinder creation with axis ray, radius, and height
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        double radius = 2;
        double height = 5;
        Cylinder cylinder = new Cylinder(axisRay, radius, height);
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal()
    {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Normal of a point on the side surface of the cylinder
        Point axisPoint = new Point(0, 0, 0);
        Vector axisDirection = new Vector(0, 0, 1);
        double radius = 1;
        double height = 5;
        Cylinder cylinder = new Cylinder(new Ray(axisPoint, axisDirection), radius, height);

        Point sidePoint = new Point(1, 0, 2);
        Vector result = cylinder.getNormal(sidePoint);
        assertEquals(1, result.length(), DELTA, "Cylinder normal is not a unit vector");
        assertEquals(0d, result.dotProduct(axisDirection), DELTA, "Cylinder normal should be orthogonal to the axis");

        // =============== Boundary Values Tests ==================

        // TC02: Normal of a point at the center of the bottom base
        Point bottomCenter = new Point(0, 0, 0);
        result = cylinder.getNormal(bottomCenter);
        assertEquals(axisDirection.scale(-1), result, "Normal should point opposite to axis direction at bottom base center");

        // TC03: Normal of a point at the center of the top base
        Point topCenter = new Point(0, 0, height);
        result = cylinder.getNormal(topCenter);
        assertEquals(axisDirection, result, "Normal should point in axis direction at top base center");

        // TC04: Normal of a point on the edge between bottom base and side
        Point edgeBottom = new Point(1, 0, 0);
        result = cylinder.getNormal(edgeBottom);
        assertEquals(axisDirection.scale(-1), result, "Normal at edge with bottom base should match bottom normal");

        // TC05: Normal of a point on the edge between top base and side
        Point edgeTop = new Point(1, 0, height);
        result = cylinder.getNormal(edgeTop);
        assertEquals(axisDirection, result, "Normal at edge with top base should match top normal");

        // TC06: Point very close to the axis, checking numerical stability
        Point closeToAxis = new Point(0.000001, 0, 2);
        result = cylinder.getNormal(closeToAxis);
        assertEquals(1, result.length(), DELTA, "Normal should be unit vector near the axis");
        assertEquals(0d, result.dotProduct(axisDirection), DELTA, "Normal should be orthogonal to the axis near the axis");
    }

    /**
     * Test method for {@link geometries.Cylinder#findIntersections(primitives.Ray)}.
     */
    /**
     * Test method for {@link geometries.Cylinder#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // Creating cylinder once for all cases
        Cylinder cylinder = new Cylinder(
                new Ray(new Point(1, 2, 3), new Vector(1, 1, 1).normalize()),
                1,
                5
        );

        // ===== Equivalence Partition Tests =====

        // TC01: Ray goes through the cylinder (2 points on side)
        assertEquals(2, cylinder.findIntersections(
                new Ray(new Point(0, 3, 3), new Vector(2, -1, 1))
        ).size(), "TC01: Ray crosses side");

        // TC02: Ray starts inside the cylinder and exits through side (1 point)
        assertEquals(1, cylinder.findIntersections(
                new Ray(new Point(1.5, 2.5, 3.5), new Vector(1, 0, 0))
        ).size(), "TC02: Ray starts inside and hits side");

        // TC03: Ray starts outside and directed away (0 points)
        assertNull(cylinder.findIntersections(
                new Ray(new Point(10, 10, 10), new Vector(1, 1, 1))
        ), "TC03: Ray outside and away");

        // TC04: Ray starts before and hits bottom base (1 point)
        assertEquals(1, cylinder.findIntersections(
                new Ray(new Point(0, 1, 2), new Vector(1, 1, 1))
        ).size(), "TC04: Hits bottom base");

        // TC05: Ray starts before and hits top base (1 point)
        assertEquals(1, cylinder.findIntersections(
                new Ray(new Point(5, 5, 5), new Vector(-1, -1, -1))
        ).size(), "TC05: Hits top base");

        // TC06: Ray goes through both bases (2 points)
        assertEquals(2, cylinder.findIntersections(
                new Ray(new Point(-1, 0, 1), new Vector(1, 1, 1))
        ).size(), "TC06: Through both bases");

        // ===== Boundary Value Tests =====

        // TC07: Ray tangent to side surface (0 points)
        assertNull(cylinder.findIntersections(
                new Ray(new Point(2, 3, 4), new Vector(1, 0, 0))
        ), "TC07: Tangent to side");

        // TC08: Ray starts on surface and goes inward (1 point)
        assertEquals(1, cylinder.findIntersections(
                new Ray(new Point(2, 2, 4), new Vector(-1, 0, 0))
        ).size(), "TC08: From surface inward");

        // TC09: Ray starts on surface and goes outward (0 points)
        assertNull(cylinder.findIntersections(
                new Ray(new Point(2, 2, 4), new Vector(1, 0, 0))
        ), "TC09: From surface outward");

        // TC10: Ray starts slightly before bottom base (1 point)
        assertEquals(1, cylinder.findIntersections(
                new Ray(new Point(1, 2, 2.9), new Vector(1, 1, 1))
        ).size(), "TC10: Near bottom center inward");

        // TC11: Ray starts at center of top base (0 points)
        assertNull(cylinder.findIntersections(
                new Ray(new Point(1 + 5 / Math.sqrt(3.0),
                        2 + 5 / Math.sqrt(3.0),
                        3 + 5 / Math.sqrt(3.0)),
                        new Vector(1, 1, 1))
        ), "TC11: Center top base out");

        // TC12: Ray on edge between side and top base (0 or 1 point depending on implementation)
        var result12 = cylinder.findIntersections(
                new Ray(new Point(2, 3, 5), new Vector(-1, -1, -1))
        );
        assertTrue(result12 == null || result12.size() <= 1, "TC12: Edge of top");

        // TC13: Ray exactly along axis (2 points)
        assertEquals(2, cylinder.findIntersections(
                new Ray(new Point(0, 1, 2), new Vector(1, 1, 1))
        ).size(), "TC13: Along axis");

        // TC14: Ray parallel to axis but outside radius (0 points)
        assertNull(cylinder.findIntersections(
                new Ray(new Point(4, 4, 4), new Vector(1, 1, 1))
        ), "TC14: Parallel outside");

        // TC15: Ray parallel to axis and inside radius (2 points)
        assertEquals(2, cylinder.findIntersections(
                new Ray(new Point(1.5, 2.5, 3.5), new Vector(1, 1, 1))
        ).size(), "TC15: Parallel inside");

        // TC16: Ray hits edge between base and side exactly (1 point)
        assertEquals(1, cylinder.findIntersections(
                new Ray(new Point(2, 3, 2), new Vector(-1, -1, 1))
        ).size(), "TC16: Hits edge bottom");
    }

}




