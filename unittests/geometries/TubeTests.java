package geometries;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for {@link geometries.Tube} class.
 */
class TubeTests
{
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Tube#Tube(double, Ray)}.
     */
    @Test
    void testConstructor() {
        //=============TC01: Valid tube creation =============//
        Point center = new Point(0, 0, 0);
        Vector direction = new Vector(0, 0, 1);
        double radius = 5;
        Ray ray = new Ray(center, direction);
        Tube tube = new Tube(radius, ray);
        assertNotNull(tube, "Tube should be created successfully.");
    }

    /**
     * Test method for edge cases in {@link geometries.Tube#Tube(double, Ray)}.
     */
    @Test
    void testEdgeCases() {
        Point center = new Point(0, 0, 0);
        Vector direction = new Vector(0, 0, 1);
        double radius = 0;
        Ray ray = new Ray(center, direction);

        //=============TC01: Tube with zero radius =============//
        assertThrows(IllegalArgumentException.class, () -> new Tube(radius, ray), "Error: radius should be greater than zero.");
    }

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partition Tests ==============

        Point axisPoint = new Point(0, 0, 0);  // Point on the axis of the tube
        Vector axisDirection = new Vector(0, 0, 1);  // Direction of the tube's axis
        double radius = 1;
        Ray ray = new Ray(axisPoint, axisDirection);
        Tube tube = new Tube(radius, ray);

        // TC01: Point on the surface of the tube at a right angle to the axis
        Point pointAtRightAngle = new Point(1, 0, 0); // Should be on the surface
        assertDoesNotThrow(() -> tube.getNormal(pointAtRightAngle),
                "No exception expected for getNormal method at right angle");
        Vector resultAtRightAngle = tube.getNormal(pointAtRightAngle);
        assertEquals(1, resultAtRightAngle.length(), DELTA, "Tube normal is not a unit vector at right angle point");
        assertEquals(0d, resultAtRightAngle.dotProduct(axisDirection), DELTA,
                "Tube normal should be orthogonal to the axis direction at right angle point");

        // TC02: Point on the surface of the tube at a general position
        Point generalPoint = new Point(1, 0, 5); // Same distance from axis, but higher along z-axis
        assertDoesNotThrow(() -> tube.getNormal(generalPoint),
                "No exception expected for getNormal at a general surface point");
        Vector normalGeneral = tube.getNormal(generalPoint);
        assertEquals(1, normalGeneral.length(), DELTA, "Tube normal is not a unit vector at general point");
        assertEquals(0d, normalGeneral.dotProduct(axisDirection), DELTA,
                "Tube normal should be orthogonal to the axis direction at general point");
    }


    /**
     * Test method for {@link geometries.Tube#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Tube tube = new Tube(1d, new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)));
        Point a = new Point(-2, 0, 1);
        Point b = new Point(0.5, 0, 1);
        Vector x = new Vector(1, 0, 0);
        Vector y = new Vector(1, 1, 0);
        Vector z = new Vector(0, 0, 1);
        Vector w = new Vector(0, 1, 0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray outside and does not intersect (0 points)
        assertNull(tube.findIntersections(new Ray(a, new Vector(-1, 0, 0))), "Ray completely outside and away from tube");

        // TC02: Ray crosses the tube (2 points)
        Ray ray2 = new Ray(new Point(-2, 0.5, 1), x);
        List<Point> result2 = tube.findIntersections(ray2);
        assertNotNull(result2, "Ray should intersect the tube");
        assertEquals(2, result2.size(), "Expected 2 intersection points");

        // TC03: Ray starts inside the tube (1 point)
        Ray ray3 = new Ray(b, new Vector(1, 0, 0));
        List<Point> result3 = tube.findIntersections(ray3);
        assertNotNull(result3, "Ray inside the tube");
        assertEquals(1, result3.size(), "Expected 1 intersection point");

        // TC04: Ray starts after the tube (0 points)
        Ray ray4 = new Ray(new Point(2, 0, 1), new Vector(1, 0, 0));
        assertNull(tube.findIntersections(ray4), "Ray starts outside and after tube");

        // =============== Boundary Value Tests ==================

        // TC11: Ray tangent to the tube (0 points)
        Ray ray11 = new Ray(new Point(-1, 1, 1), x);
        assertNull(tube.findIntersections(ray11), "Ray tangent to tube");

        // TC12: Ray starts exactly on surface and goes inside (1 point)
        Ray ray12 = new Ray(new Point(1, 0, 1), new Vector(-1, 0, 0));
        List<Point> result12 = tube.findIntersections(ray12);
        assertNotNull(result12, "Ray from surface inward");
        assertEquals(1, result12.size(), "Expected 1 intersection point");

        // TC13: Ray starts exactly on surface and goes outward (0 points)
        Ray ray13 = new Ray(new Point(1, 0, 1), new Vector(1, 0, 0));
        assertNull(tube.findIntersections(ray13), "Ray from surface outward");

        // TC14: Ray is orthogonal to the axis and intersects (2 points)
        Ray ray14 = new Ray(new Point(-2, 0.5, 0), x);
        List<Point> result14 = tube.findIntersections(ray14);
        assertNotNull(result14, "Orthogonal ray");
        assertEquals(2, result14.size(), "Expected 2 points");

        // TC15: Ray parallel to the axis (0 points)
        Ray ray15 = new Ray(new Point(2, 0, 0), z);
        assertNull(tube.findIntersections(ray15), "Ray parallel and outside");

        // TC16: Ray inside tube parallel to axis (0 points, never intersects wall)
        Ray ray16 = new Ray(new Point(0.5, 0, 0), z);
        assertNull(tube.findIntersections(ray16), "Ray inside and parallel");

        // TC17: Ray inside tube but diagonal to axis (1 point)
        Ray ray17 = new Ray(new Point(0.5, 0, 1), y);
        List<Point> result17 = tube.findIntersections(ray17);
        assertNotNull(result17, "Ray inside diagonal");
        assertEquals(1, result17.size(), "Expected 1 intersection point");

        // TC18: Ray originates on the axis (2 points)
        Ray ray18 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
        List<Point> result18 = tube.findIntersections(ray18);
        assertNotNull(result18, "Ray from axis outward");
        assertEquals(2, result18.size(), "Expected 2 intersection points");

        // TC19: Ray orthogonal to axis and intersects at z ≠ 0 (2 points)
        Ray ray19 = new Ray(new Point(-2, 0, 5), x);
        List<Point> result19 = tube.findIntersections(ray19);
        assertNotNull(result19, "Orthogonal intersection at z != 0");
        assertEquals(2, result19.size(), "Expected 2 points");

        // TC20: Ray orthogonal, starts inside (1 point)
        Ray ray20 = new Ray(new Point(0.5, 0, 3), x);
        List<Point> result20 = tube.findIntersections(ray20);
        assertNotNull(result20, "Orthogonal from inside");
        assertEquals(1, result20.size(), "Expected 1 point");
        // ============ Additional Equivalence Partitions Tests ==============

        // TC21: Ray passes through the tube at angle (2 points)
        Ray ray21 = new Ray(new Point(-2, -2, 1), y);
        List<Point> result21 = tube.findIntersections(ray21);
        assertNotNull(result21, "Diagonal ray across tube");
        assertEquals(2, result21.size(), "Expected 2 intersection points");

        // TC22: Ray inside and away from axis (1 point)
        Ray ray22 = new Ray(new Point(0.9, 0, 0), new Vector(1, 1, 0));
        List<Point> result22 = tube.findIntersections(ray22);
        assertNotNull(result22, "Skewed from inside");
        assertEquals(1, result22.size(), "Expected 1 intersection point");

        // TC23: Ray hits exactly the side of the tube (1 point)
        Ray ray23 = new Ray(new Point(-1, 1, 1), new Vector(1, 0, 0));
        List<Point> result23 = tube.findIntersections(ray23);
        assertNotNull(result23, "Hit exactly on side");
        assertEquals(1, result23.size(), "Expected 1 intersection");

        // TC24: Ray intersects but only touches internally (1 point)
        Ray ray24 = new Ray(new Point(0.999999, 0, -1), z);
        List<Point> result24 = tube.findIntersections(ray24);
        assertNull(result24, "Almost on edge but fully inside, no wall hit");

        // TC25: Ray crosses diagonally through axis (2 points)
        Ray ray25 = new Ray(new Point(-2, 0, 0), new Vector(2, 0, 2));
        List<Point> result25 = tube.findIntersections(ray25);
        assertNotNull(result25, "Diagonal through center");
        assertEquals(2, result25.size(), "Expected 2 intersection points");

        // TC26: Ray grazes the tube very close but does not touch (0 points)
        Ray ray26 = new Ray(new Point(-2, 1.0001, 0), x);
        assertNull(tube.findIntersections(ray26), "Ray barely misses tube");

        // TC27: Ray enters and exits with shallow angle (2 points)
        Ray ray27 = new Ray(new Point(-5, 0.9, 0), new Vector(5, 0, 1));
        List<Point> result27 = tube.findIntersections(ray27);
        assertNotNull(result27, "Shallow angle");
        assertEquals(2, result27.size(), "Expected 2");

        // TC28: Ray inside, going away from wall (0 points)
        Ray ray28 = new Ray(new Point(0.5, 0, 0), new Vector(0.1, 0, 0.1));
        assertNull(tube.findIntersections(ray28), "Inside, away from wall");

        // TC29: Ray intersects near base (2 points)
        Ray ray29 = new Ray(new Point(-2, 0.99, 0.1), new Vector(2, 0, 0));
        List<Point> result29 = tube.findIntersections(ray29);
        assertNotNull(result29, "Near base intersection");
        assertEquals(2, result29.size(), "Expected 2");

        // TC30: Ray intersects near top (2 points)
        Ray ray30 = new Ray(new Point(-2, 0.99, 5), new Vector(2, 0, 0));
        List<Point> result30 = tube.findIntersections(ray30);
        assertNotNull(result30, "Near top intersection");
        assertEquals(2, result30.size(), "Expected 2");
        
        // TC31: Ray exactly on surface all along (0 points)
        Ray ray31 = new Ray(new Point(1, 0, -2), z);
        assertNull(tube.findIntersections(ray31), "Ray on surface");

        // TC32: Ray starts at edge and diagonal outward (0 points)
        Ray ray32 = new Ray(new Point(1, 0, 0), new Vector(1, 1, 1));
        assertNull(tube.findIntersections(ray32), "Outward from edge");

        // TC33: Ray just misses by 0.0001 (0 points)
        Ray ray33 = new Ray(new Point(-2, 1.00001, 0), x);
        assertNull(tube.findIntersections(ray33), "Tiny miss");

        // TC34: Ray grazes tube along entire side, but outside (0 points)
        Ray ray34 = new Ray(new Point(-2, 1, 0), x);
        assertNull(tube.findIntersections(ray34), "Grazing along edge");

        // TC35: Ray enters from inside at steep angle (1 point)
        Ray ray35 = new Ray(new Point(0.5, 0.5, 1), new Vector(2, 2, 1));
        List<Point> result35 = tube.findIntersections(ray35);
        assertNotNull(result35, "Steep angle from inside");
        assertEquals(1, result35.size(), "Expected 1 point");

        // TC36: Ray from outside, intersects at shallow angle (2 points)
        Ray ray36 = new Ray(new Point(-3, 0.2, 0), new Vector(2, 0.1, 0));
        List<Point> result36 = tube.findIntersections(ray36);
        assertNotNull(result36, "Shallow hit from side");
        assertEquals(2, result36.size(), "Expected 2 points");

        // TC37: Ray from outside, hits top edge (1 point)
        Ray ray37 = new Ray(new Point(-2, 0, 10), new Vector(1, 0, -10));
        List<Point> result37 = tube.findIntersections(ray37);
        assertNotNull(result37, "Hit from above");
        assertEquals(1, result37.size(), "Expected 1 point");

        // TC38: Ray hits from inside, tangent to exit point (1 point)
        Ray ray38 = new Ray(new Point(0.5, 0, 0), new Vector(1, 1, 0));
        List<Point> result38 = tube.findIntersections(ray38);
        assertNotNull(result38, "Internal tangent");
        assertEquals(1, result38.size(), "Expected 1 point");

        // TC39: Ray starts on axis, goes diagonal (2 points)
        Ray ray39 = new Ray(new Point(0, 0, 0), new Vector(1, 1, 1));
        List<Point> result39 = tube.findIntersections(ray39);
        assertNotNull(result39, "From axis diagonal");
        assertEquals(2, result39.size(), "Expected 2 points");

        // TC40: Ray starts from surface, diagonal along tube wall (0 points)
        Ray ray40 = new Ray(new Point(1, 0, 0), new Vector(0, 1, 1));
        assertNull(tube.findIntersections(ray40), "Along surface diagonally");

        // TC41: Ray parallel to the axis and starts exactly on it (0 points)
        Ray ray41 = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        assertNull(tube.findIntersections(ray41), "Ray parallel and starts on axis");

        // TC42: Ray passes through a single point but results in two identical intersection points (2 points)
        Ray ray42 = new Ray(new Point(0, 1, 1), new Vector(0, 0, 1));
        List<Point> result42 = tube.findIntersections(ray42);
        assertNotNull(result42, "Ray passes through but gives two identical points");
        assertEquals(2, result42.size(), "Expected 2 identical points");

        // TC43: Ray starts far away and does not intersect (0 points)
        Ray ray43 = new Ray(new Point(10, 0, 0), new Vector(1, 0, 0));
        assertNull(tube.findIntersections(ray43), "Ray far away and does not intersect");



    }
}
