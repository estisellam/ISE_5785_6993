package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

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
    @Test
    void testFindIntersections() {
        Cylinder cylinder = new Cylinder(
                new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),
                2, // radius
                5  // height
        );

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray intersects both side and one base
        Ray r1 = new Ray(new Point(3, 0, 2), new Vector(-1, 0, 0));
        assertEquals(2, cylinder.findIntersections(r1).size(), "Should intersect side and one base");

        // TC02: Ray starts inside cylinder and goes out through side
        Ray r2 = new Ray(new Point(1, 0, 1), new Vector(1, 0, 0));
        assertEquals(1, cylinder.findIntersections(r2).size(), "Ray inside should intersect once");

        // TC03: Ray misses cylinder completely
        Ray r3 = new Ray(new Point(5, 5, 5), new Vector(1, 0, 0));
        assertNull(cylinder.findIntersections(r3), "Ray misses cylinder");

        // ============ Boundary Values Tests ==================

        // TC04: Ray intersects exactly at top base center
        Ray r4 = new Ray(new Point(0, 0, 6), new Vector(0, 0, -1));
        assertEquals(1, cylinder.findIntersections(r4).size(), "Should intersect top base center");

        // TC05: Ray tangent to cylinder side (no intersection)
        Ray r5 = new Ray(new Point(2, 0, -1), new Vector(0, 0, 1));
        assertNull(cylinder.findIntersections(r5), "Tangent ray should not intersect");

        // TC06: Ray along axis, enters bottom and exits top
        Ray r6 = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertEquals(2, cylinder.findIntersections(r6).size(), "Ray through axis should intersect both bases");

        // TC07: Ray hits only bottom base from below
        Ray r7 = new Ray(new Point(0, 0, -1), new Vector(0, 0, 1));
        assertEquals(2, cylinder.findIntersections(r7).size(), "Should hit both bases");
    }




}
