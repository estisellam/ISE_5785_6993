package geometries;
import primitives.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

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
        // TC01: Normal of a simple sphere
        Point center = new Point(0, 0, 0);
        double radius = 1;
        Sphere sphere = new Sphere(center, radius);

        // Ensure no exception is thrown when calling getNormal
        assertDoesNotThrow(() -> sphere.getNormal(center), "No exception expected for getNormal method on Sphere");

        // Getting the normal vector and validating its properties
        Vector result = sphere.getNormal(center);

        // Normal should be a unit vector
        assertEquals(1, result.length(), DELTA, "Sphere normal is not a unit vector");

        // The normal vector should point outward, which means its dot product with a zero vector should be 0
        assertEquals(0d, result.dotProduct(center.subtract(center)), DELTA, "Sphere normal should point outward");
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
        Ray ray3 = new Ray(new Point(1, 0.5, 0), new Vector(1, 0, 0));
        List<Point> result3 = sphere.findIntersections(ray3);
        assertNotNull(result3, "Ray starts inside the sphere");
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(List.of(gp2), result3, "Wrong intersection point");

        // TC04: Ray starts after the sphere (0 points)
        Ray ray4 = new Ray(new Point(2, 0, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray4), "Ray starts after sphere");

        // =============== Boundary Values Tests ==================
        // **** Group 1: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        Ray ray11 = new Ray(new Point(0.0651530771650466, 0.355051025721682, 0), v310);
        List<Point> result11 = sphere.findIntersections(ray11);
        assertNotNull(result11, "Ray from surface inward");
        assertEquals(1, result11.size(), "Wrong number of points");

        // TC12: Ray starts at sphere and goes outside (0 points)
        Ray ray12 = new Ray(new Point(0.0651530771650466, 0.355051025721682, 0), v110);
        assertNull(sphere.findIntersections(ray12), "Ray from surface outward");

        // **** Group 2: Ray's line goes through the center
        // TC21: Ray starts before the sphere (2 points)
        Ray ray21 = new Ray(p01, new Vector(2, 0, 0)); // center is at (1,0,0)
        List<Point> result21 = sphere.findIntersections(ray21);
        assertNotNull(result21, "Ray through center before sphere");
        assertEquals(2, result21.size(), "Wrong number of points");

        // TC22: Ray starts at sphere and goes inside (1 point)
        Ray ray22 = new Ray(new Point(0, 0, 0), new Vector(1, 0, 0));
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

        // **** Group 3: Ray's line is tangent to the sphere (all tests 0 points)
        // TC31: Ray starts before the tangent point
        Ray ray31 = new Ray(new Point(0, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray31), "Ray before tangent point");

        // TC32: Ray starts at the tangent point
        Ray ray32 = new Ray(new Point(1, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray32), "Ray at tangent point");

        // TC33: Ray starts after the tangent point
        Ray ray33 = new Ray(new Point(2, -1, 0), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray33), "Ray after tangent point");

        // **** Group 4: Special cases
        // TC41: Ray's line is outside sphere, ray is orthogonal to ray start to sphere's center line
        Ray ray41 = new Ray(new Point(0, 0, 2), new Vector(1, 0, 0));
        assertNull(sphere.findIntersections(ray41), "Ray orthogonal and outside");

        // TC42: Ray starts inside, ray is orthogonal to ray start to sphere's center line
        Ray ray42 = new Ray(new Point(1, 0, 0.5), new Vector(0, 1, 0));
        List<Point> result42 = sphere.findIntersections(ray42);
        assertNotNull(result42, "Orthogonal ray from inside");
        assertEquals(1, result42.size(), "Wrong number of points");


    }
}
