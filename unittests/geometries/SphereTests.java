package geometries;

import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Sphere} class.
 */
class SphereTests {

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
     * Test method for edge cases in {@link geometries.Sphere#Sphere(primitives.Point, double)}.
     */
    @Test
    void testEdgeCases() {
        Point center = new Point(0, 0, 0);
        double radius = 0;

        //=============TC01: Sphere with zero radius =============//
        assertThrows(IllegalArgumentException.class, () -> new Sphere(center, radius), "Error: radius should be greater than zero.");
    }

}
