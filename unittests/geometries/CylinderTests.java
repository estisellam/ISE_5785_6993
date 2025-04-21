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

    @Test
    void testGetNormal_EquivalencePartitions()
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
    }

    @Test
    void testGetNormal_BoundaryValues()
    {
        // =============== Boundary Values Tests ==================

        Point axisPoint = new Point(0, 0, 0);
        Vector axisDirection = new Vector(0, 0, 1);
        double radius = 1;
        double height = 5;
        Cylinder cylinder = new Cylinder(new Ray(axisPoint, axisDirection), radius, height);

        // TC02: Normal of a point at the center of the bottom base
        Point bottomCenter = new Point(0, 0, 0);
        Vector result = cylinder.getNormal(bottomCenter);
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
    }

    @Test
    void testGetNormal_EdgeCase()
    {
        // TC06: Point very close to the axis, checking numerical stability
        Point axisPoint = new Point(0, 0, 0);
        Vector axisDirection = new Vector(0, 0, 1);
        double radius = 2;
        double height = 5;
        Cylinder cylinder = new Cylinder(new Ray(axisPoint, axisDirection), radius, height);

        Point closeToAxis = new Point(0.000001, 0, 2);
        Vector result = cylinder.getNormal(closeToAxis);
        assertEquals(1, result.length(), DELTA, "Normal should be unit vector near the axis");
        assertEquals(0d, result.dotProduct(axisDirection), DELTA, "Normal should be orthogonal to the axis near the axis");
    }

}
