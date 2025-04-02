package geometries;

import primitives.Point;
import primitives.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Tube} class.
 */
class TubeTests {

    /**
     * Test method for {@link geometries.Tube#Tube(primitives.Point, primitives.Vector, double)}.
     */
    @Test
    void testConstructor() {
        //=============TC01: Valid tube creation =============//
        Point center = new Point(0, 0, 0);
        Vector direction = new Vector(0, 0, 1);
        double radius = 5;
        Tube tube = new Tube(center, direction, radius);
        assertNotNull(tube, "Tube should be created successfully.");
    }

    /**
     * Test method for edge cases in {@link geometries.Tube#Tube(primitives.Point, primitives.Vector, double)}.
     */
    @Test
    void testEdgeCases() {
        Point center = new Point(0, 0, 0);
        Vector direction = new Vector(0, 0, 1);
        double radius = 0;

        //=============TC01: Tube with zero radius =============//
        assertThrows(IllegalArgumentException.class, () -> new Tube(center, direction, radius), "Error: radius should be greater than zero.");
    }

}
