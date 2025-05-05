package renderer;
import geometries.*;
import primitives.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for ray intersections from camera with geometries.
 */
public class CameraIntersectionsIntegrationTests {

    /**
     * Helper method to count total number of intersections
     * from camera rays with a given geometry.
     */
    private int countIntersections(Camera cam, Intersectable geo, int nX, int nY) {
        int count = 0;
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                Ray ray = cam.constructRay(nX, nY, j, i);
                List<Point> intersections = geo.findIntersections(ray);
                if (intersections != null) {
                    count += intersections.size();
                }
            }
        }
        return count;
    }

    /**
     * Create a standard camera used in all tests
     */
    private Camera createStandardCamera() {
        return Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
    }

    /**
     * Test method for sphere intersections with camera rays.
     */
    @Test
    void testSphereIntersections() {
        Camera cam = createStandardCamera();
        Sphere sphere = new Sphere(new Point(0, 0, -3), 1);
        assertEquals(2, countIntersections(cam, sphere, 3, 3), "Wrong number of sphere intersections");
    }

    /**
     * Test method for sphere intersections with camera rays.
     */
    @Test
    void testSphereBigIntersections() {
        Camera cam = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0.5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        Sphere sphere = new Sphere(new Point(0, 0, -2.5), 2.5);
        assertEquals(18, countIntersections(cam, sphere, 3, 3), "Wrong number of sphere intersections");
    }

    /**
     * Test method for plane intersections with camera rays.
     */
    @Test
    void testPlaneIntersections() {
        Camera cam = createStandardCamera();
        Plane plane = new Plane(new Point(0, 0, -3), new Vector(0, 0, 1));
        assertEquals(9, countIntersections(cam, plane, 3, 3), "Wrong number of plane intersections");
    }

    /**
     * Test method for triangle intersections with camera rays.
     */
    @Test
    void testTriangleIntersections() {
        Camera cam = createStandardCamera();
        Triangle triangle = new Triangle(
                new Point(0, 1, -2),
                new Point(1, -1, -2),
                new Point(-1, -1, -2));
        assertEquals(1, countIntersections(cam, triangle, 3, 3), "Wrong number of triangle intersections");
    }

    /**
     * Test method for camera rotation around a target point.
     * This test checks if the camera can still intersect - bonus
     */
    @Test
    void testCameraRotationAroundTarget() {
        Point target = new Point(0, 0, -3);

        Camera cam1 = Camera.getBuilder()
                .setLocation(new Point(0, 0, 0))
                .setDirection(target, Vector.AXIS_Y)
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        Camera cam2 = Camera.getBuilder()
                .setLocation(new Point(1, 1, 0))
                .setDirection(target, Vector.AXIS_Y)
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();

        Sphere sphere = new Sphere(new Point(0, 0, -2.5), 2.5);

        int count1 = countIntersections(cam1, sphere, 3, 3);
        int count2 = countIntersections(cam2, sphere, 3, 3);

        assertTrue(count2 > 0, "Rotated camera should still intersect the sphere");

    }

}
