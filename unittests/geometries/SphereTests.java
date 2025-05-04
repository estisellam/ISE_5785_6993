package geometries;
import org.junit.jupiter.api.Test;
import primitives.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Sphere} class.
 */
class SphereTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Sphere#Sphere(primitives.Point, double)}.
     */
    @Test
    void testConstructor() {
        //=============TC01: Valid sphere creation =============//
        Point center = new Point(0, 0, 0);
        double radius = 5;
        Sphere sphere = new Sphere(center, radius);
        assertNotNull(sphere, "Sphere should be created successfully.");
    }

    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partition Tests ==============

        // TC01: Normal on a point on the surface of the tube
        Point axisPoint = new Point(0, 0, 0);             // Point on the axis of the tube
        Vector axisDirection = new Vector(0, 0, 1);       // Direction of the tube's axis
        double radius = 1;
        Ray axisRay = new Ray(axisPoint, axisDirection);  // Tube's axis
        Tube tube = new Tube(radius, axisRay);

        // Choose a point on the surface of the tube (distance from axis is exactly the radius)
        Point surfacePoint = new Point(1, 0, 5); // 1 unit in x-direction from axis, z=5

        assertDoesNotThrow(() -> tube.getNormal(surfacePoint), "No exception expected for getNormal on surface point");
        Vector normal = tube.getNormal(surfacePoint);

        // Check that the normal is a unit vector
        assertEquals(1, normal.length(), DELTA, "Normal vector should be normalized");

        // Check orthogonality to the axis direction
        assertEquals(0, normal.dotProduct(axisDirection), DELTA, "Normal should be orthogonal to axis direction");

        // TC02: Another surface point
        Point surfacePoint2 = new Point(0, 1, -2); // y=1 from axis, z=-2
        assertDoesNotThrow(() -> tube.getNormal(surfacePoint2), "No exception expected for getNormal at another surface point");
        Vector normal2 = tube.getNormal(surfacePoint2);
        assertEquals(1, normal2.length(), DELTA, "Normal vector should be normalized");
        assertEquals(0, normal2.dotProduct(axisDirection), DELTA, "Normal should be orthogonal to axis direction");
    }



    /** a point used in some tests */
    private final Point p001 = new Point(0, 0, 1);
    /** A point used in some tests */
    private final Point p100 = new Point(1, 0, 0);
    /** A vector used in some tests */
    private final Vector v001 = new Vector(0, 0, 1);
    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);
        final Point gp3 =new Point(1.4114378277661477, 0.9114378277661477, 0.0);
        final var exp = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310));
        assertNotNull(result1, "Can't be empty list");
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(exp, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        Ray ray3 = new Ray(new Point(1, 0.5, 0), new Vector(1, 1, 0).normalize());
        List<Point> result3 = sphere.findIntersections(ray3);
        assertNotNull(result3, "Ray starts inside the sphere");
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(List.of(gp3), result3, "Wrong intersection point");

        // TC04: Ray starts after the sphere (0 points)
        Ray ray4 = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray4), "Ray starts after sphere");

        // =============== Boundary Values Tests ==================
        // Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        Ray ray11 = new Ray(gp1, v310);
        List<Point> result11 = sphere.findIntersections(ray11);
        assertNotNull(result11, "Ray from surface inward");
        assertEquals(1, result11.size(), "Wrong number of points");

        // TC12: Ray starts at sphere and goes outside (0 points)
        Ray ray12 = new Ray(gp1, gp1.subtract(p100));
        assertNull(sphere.findIntersections(ray12), "Ray from surface outward");

        // Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        Ray ray21 = new Ray(p01, new Vector(2, 0, 0));
        List<Point> result21 = sphere.findIntersections(ray21);
        assertNotNull(result21, "Ray through center before sphere");
        assertEquals(2, result21.size(), "Wrong number of points");

        // TC22: Ray starts at surface and goes inside (1 point)
        Point surfacePoint22 = new Point(0, 0, 0); // נעדכן מיד
        surfacePoint22 = new Point(p100.getX() - 1, p100.getY(), p100.getZ()); // בקצה השמאלי של הכדור
        Ray ray22 = new Ray(surfacePoint22, new Vector(1, 0, 0));
        List<Point> result22 = sphere.findIntersections(ray22);
        assertNotNull(result22, "Ray from surface to center");
        assertEquals(1, result22.size(), "Wrong number of points");

        // TC23: Ray starts inside (1 point)
        Ray ray23 = new Ray(new Point(1, 0, 0.5), new Vector(0, 0, 1));
        List<Point> result23 = sphere.findIntersections(ray23);
        assertNotNull(result23, "Ray from inside to surface");
        assertEquals(1, result23.size(), "Wrong number of points");

        // TC24: Ray starts at the center (1 point)
        Ray ray24 = new Ray(p100, new Vector(0, 1, 0));
        List<Point> result24 = sphere.findIntersections(ray24);
        assertNotNull(result24, "Ray from center");
        assertEquals(1, result24.size(), "Wrong number of points");

        // TC25: Ray starts at sphere and goes outside (0 points)
        Ray ray25 = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray25), "Ray from surface outward through center line");

        // TC26: Ray starts after sphere (0 points)
        Ray ray26 = new Ray(new Point(3, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray26), "Ray after sphere through center line");

        // Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        Ray ray31 = new Ray(new Point(0, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray31), "Ray before tangent point");

        Ray ray32 = new Ray(new Point(1, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray32), "Ray at tangent point");

        Ray ray33 = new Ray(new Point(2, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray33), "Ray after tangent point");

        // Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        Point orthogonalPointOutside = new Point(p100.getX() - 1, p100.getY(), p100.getZ() + 2); // ב-x זהה לשפת הכדור, z רחוק
        Ray ray41 = new Ray(orthogonalPointOutside, new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray41), "Ray orthogonal and outside");

        // TC42: Ray starts inside, ray is orthogonal to ray start to sphere's center line
        Ray ray42 = new Ray(new Point(1, 0, 0.5), new Vector(0, 1, 0));
        List<Point> result42 = sphere.findIntersections(ray42);
        assertNotNull(result42, "Orthogonal ray from inside");
        assertEquals(1, result42.size(), "Wrong number of points");
    }

}
