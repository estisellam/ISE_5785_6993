package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * unit test for point class
 */
class PointTests {

    /**
     * test method for {@link primitives.Point#add(Vector)}
     */
    @Test
    void testAdd()
    {
        //=============TC01:Test add point to vector =============//
        Point p1= new Point(1, 2, 3);
        Vector v1= new Vector(1, 2, 3);
        Point p2=p1.add(v1);
        Point p3=new Point(2,4,6);
        assertEquals(p2, p3,"Add() wrong result point");
    }

    /**
     * test method for {@link primitives.Point#subtract(Point)}
     */
    @Test
    void testSubtract()
    {
        //=============TC01:Test subtract point from point in positive result =============//
        Point p1= new Point(1, 2, 3);
        Point p2= new Point(4, 5, 6);
        Vector v1=p2.subtract(p1);
        Vector v2=new Vector(3, 3, 3);
        assertEquals(v1, v2,"Subtract() wrong result vector");

        //=============TC02:Test subtract point from point in negative result =============//
        Vector v3=p1.subtract(p2);
        Vector v4=new Vector(-3, -3, -3);
        assertEquals(v3, v4,"Subtract() wrong result vector");


    }

    /**
     * test method for {@link primitives.Point#distanceSquared(Point)}
     */

    @Test
    void testDistanceSquared()
    {
        //=============TC01:Test distance squared between two points =============//
        Point p1= new Point(1, 2, 3);
        Point p2= new Point(4, 5, 6);
        double d1=p1.distanceSquared(p2);
        double d2=27;
        assertEquals(d1, d2,"Distance squared() wrong result");

        //=============TC02:Test distance squared between two points =============//
        Point p3= new Point(0, 0, 0);
        Point p4= new Point(0, 0, 0);
        double d3=p3.distanceSquared(p4);
        double d4=0;
        assertEquals(d3, d4,"Distance squared() wrong result");
    }

    /**
     * test method for {@link primitives.Point#distance(Point)}
     */

    @Test
    void testDistance()
    {
        //=============TC01:Test distance between two points =============//
        Point p1= new Point(1, 2, 3);
        Point p2= new Point(4, 5, 6);
        double d1=p1.distance(p2);
        double d2=Math.sqrt(27);
        assertEquals(d1, d2,"Distance() wrong result");

        //=============TC02:Test distance between two points =============//
        Point p3= new Point(0, 0, 0);
        Point p4= new Point(0, 0, 0);
        double d3=p3.distance(p4);
        double d4=0;
        assertEquals(d3, d4,"Distance() wrong result");

    }
}