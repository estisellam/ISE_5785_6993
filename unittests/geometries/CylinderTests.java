package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link geometries.Cylinder} class.
 */
class CylinderTests {

    /**
     * Test method for {@link geometries.Cylinder#Cylinder(primitives.Ray, double, double)}.
     */
    @Test
    void testConstructor()
    {
        //=============TC01: Valid cylinder creation =============//
        Ray axisRay = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        double radius = 2;
        double height = 5;
        Cylinder cylinder = new Cylinder(axisRay, radius, height);
        assertNotNull(cylinder, "Cylinder should be created successfully.");
    }


}
