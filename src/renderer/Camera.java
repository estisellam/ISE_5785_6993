package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.*;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Camera class represents a virtual camera in 3D space.
 */
public class Camera implements Cloneable {

    /**
     * pro2
     * Adaptive feature variables for rendering the image
     * with adaptive sampling, the camera can adjust how many rays to shoot
     * based on the complexity of the scene in each pixel.
     **/
    private boolean isAdaptiveEnabled = false;
    /**
     * pro2
     * the depth of adaptive sampling.
     * this amount used to determine how many times to split a pixel
     */
    private int adaptiveDepth = 4;


    /*  Anti-Aliasing (AA) feature variables
     If true, the camera will use anti-aliasing and generate multiple rays per pixel*/
    private boolean isAAEnabled = false;

    /* How many rays to generate per pixel for anti-aliasing
     For example: 81 means 9x9 grid of rays inside each pixel*/
    private int aaRays = 81;

    // This variable decides if we want to use jitter (random small changes) -stage 8 -bonus
    private boolean useJitter = false;

    /**
     * pro2
     * Enable or disable adaptive sampling.
     *
     * @param enable true to enable adaptive sampling, false to disable it
     * @return the camera itself (for chaining)
     */
    public Camera enableAdaptive(boolean enable) {
        this.isAdaptiveEnabled = enable;
        return this;
    }

    /**
     * pro2
     * set function for adaptive depth.
     *
     * @param depth the depth of adaptive sampling
     * @return the camera itself (for chaining)
     */
    public Camera setAdaptiveDepth(int depth) {
        this.adaptiveDepth = depth;
        return this;
    }

    /**
     * Turn on jitter mode.
     * Jitter adds small random offsets to rays to make the image smoother.
     * Without it, rays go through fixed points. With jitter, they move a bit randomly.
     * This helps to reduce sharp edges and make the image more natural.
     *
     * @return the camera (so we can chain commands) -stage 8 -bonus
     */
    public Camera enableJitter() {
        this.useJitter = true; // now the camera will use jitter
        return this; // return the camera so we can continue building it
    }

    /**
     * Camera location in 3D space.
     */
    private Point location;
    /**
     * Direction vector from the camera to the target point.
     */
    private Vector to;
    /**
     * Up vector representing the camera's vertical direction.
     */
    private Vector up;
    /**
     * Right vector representing the camera's horizontal direction.
     */
    private Vector right;
    /**
     * Distance from the camera to the view plane.
     */
    private double VpDistance = 0;
    /**
     * Width of the view plane.
     */
    private double VpWidth = 0;
    /**
     * Height of the view plane.
     */
    private double VpHeight = 0;
    /**
     * Number of pixels in the X direction.
     */
    private int nX = 1;
    /**
     * Number of pixels in the Y direction.
     */
    private int nY = 1;

    /**
     * ImageWriter instance for rendering the image.
     */
    private ImageWriter imageWriter;
    /**
     * RayTracerBase instance for ray tracing.
     */
    private RayTracerBase rayTracer;


    /**
     * Default constructor for Camera.
     */
    public Camera() {
    }

    /**
     * Static method to get a new builder for Camera.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Copy constructor for Camera.
     *
     * @return
     */
    @Override
    public Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // This shouldn't happen since we implement Cloneable
        }
    }

    /**
     * Constructs a ray from the camera through the center of the given pixel.
     *
     * @param nX number of columns (pixels in width)
     * @param nY number of rows (pixels in height)
     * @param j  pixel column index
     * @param i  pixel row index
     * @return the constructed ray through pixel (i,j)
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point Pc = location.add(to.scale(VpDistance));

        double Ry = VpHeight / nY;
        double Rx = VpWidth / nX;

        double xj = (j - (nX - 1) / 2.0) * Rx;
        double yi = -(i - (nY - 1) / 2.0) * Ry;

        Point Pij = Pc;
        if (!isZero(xj)) {
            Pij = Pij.add(right.scale(xj));
        }
        if (!isZero(yi)) {
            Pij = Pij.add(up.scale(yi));
        }

        Vector dir = Pij.subtract(location);
        return new Ray(location, dir);
    }

    /**
     * pro2
     * Renders the image by casting rays through each pixel of the view plane.
     * This method uses the ray tracer to trace rays and color the pixels accordingly.
     * It supports both single-threaded and multi-threaded ray tracing.
     *
     * @return the camera instance itself for chaining
     */
    public Camera renderImage() {
        // Check if the ray tracer is set, if not throw an exception
        if (rayTracer instanceof MultiThreadedRayTracer) {
            // Use parallel processing for ray casting if MultiThreadedRayTracer is used
            java.util.stream.IntStream.range(0, nY).parallel().forEach(i -> {
                for (int j = 0; j < nX; j++) {
                    castRay(i, j);
                }
            });
        }
        // If not using MultiThreadedRayTracer, use sequential processing
        else {
            for (int i = 0; i < nY; i++) {
                for (int j = 0; j < nX; j++) {
                    castRay(i, j);
                }
            }
        }
        return this;
    }


    /**
     * Prints the grid on the image.
     *
     * @param interval interval between grid lines
     * @param color    color of the grid lines
     * @return the camera instance
     */
    public Camera printGrid(int interval, Color color) {
        for (int i = 0; i < nY; i++) {
            for (int j = 0; j < nX; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the rendered image to a file with the given name
     * and returns the camera instance.
     *
     * @param name the base name of the output image file (without extension)
     * @return this camera instance
     */
    public Camera writeToImage(String name) {
        imageWriter.writeToImage(name);
        return this;
    }

    /**
     * Casts a ray through the given pixel and colors it using the ray tracer.
     *
     * @param i row index (Y-axis)
     * @param j column index (X-axis)
     */
    private void castRay(int i, int j) {
        Color color;

        if (isAdaptiveEnabled) {
            Point center = location.add(to.scale(VpDistance))
                    .add(right.scale((j - (nX - 1) / 2.0) * (VpWidth / nX)))
                    .add(up.scale(-(i - (nY - 1) / 2.0) * (VpHeight / nY)));

            color = adaptiveCastRay(center, VpWidth / nX, VpHeight / nY, adaptiveDepth);
        } else if (isAAEnabled) {
            List<Ray> rays = constructAAbeamThroughPixel(nX, nY, j, i);
            color = rayTracer.traceRay(rays);
        } else {
            Ray ray = constructRay(nX, nY, j, i);
            color = rayTracer.traceRay(ray);
        }

        imageWriter.writePixel(j, i, color);
    }


    /**
     * Inner static Builder class for building Camera instances.
     */
    public static class Builder {
        /**
         * Camera instance to be built.
         */
        private final Camera camera = new Camera();

        /**
         * Sets the image writer for the camera.
         *
         * @param location location of the image
         * @return the image writer
         */
        public Builder setLocation(Point location) {
            if (location == null)
                throw new IllegalArgumentException("Location cannot be null");
            camera.location = location;
            return this;
        }

        /**
         * Sets the direction of the camera towards a target point.
         *
         * @param target target point
         * @return the direction
         */

        public Builder setDirection(Point target) {
            if (target == null)
                throw new IllegalArgumentException("Target point cannot be null");

            Vector to = target.subtract(camera.location);
            if (isZero(to.getX()) && !isZero(to.getY()) && isZero(to.getZ()))
                throw new IllegalArgumentException("To vector cannot point directly up along Y axis");

            camera.to = to.normalize();
            camera.up = Vector.AXIS_Y;
            return this;
        }

        /**
         * Sets the direction of the camera towards a target point with a specified up vector.
         *
         * @param target target point
         * @param up     up vector
         * @return the direction
         */
        public Builder setDirection(Point target, Vector up) {
            if (target == null)
                throw new IllegalArgumentException("Target point cannot be null");
            if (up == null)
                throw new IllegalArgumentException("Up vector cannot be null");

            Vector to = target.subtract(camera.location).normalize();
            camera.right = to.crossProduct(up).normalize();
            camera.up = camera.right.crossProduct(to).normalize();
            camera.to = to;

            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  width must be positive
         * @param height height must be positive
         * @return the size
         */

        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0)
                throw new IllegalArgumentException("Width and height must be positive");
            camera.VpWidth = width;
            camera.VpHeight = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance distance must be positive
         * @return the distance
         */

        public Builder setVpDistance(double distance) {
            if (distance <= 0)
                throw new IllegalArgumentException("View plane distance must be positive");
            camera.VpDistance = distance;
            return this;
        }

        /**
         * Sets the resolution of the view plane.
         *
         * @param nX number of pixels in the X direction
         * @param nY number of pixels in the Y direction
         * @return return the resolution
         */

        public Builder setResolution(int nX, int nY) {
            if (nX <= 0 || nY <= 0)
                throw new IllegalArgumentException("Resolution must be positive integers");
            camera.nX = nX;
            camera.nY = nY;
            camera.imageWriter = new ImageWriter(nX, nY);
            return this;
        }


        /**
         * Builds the camera instance.
         *
         * @return the constructed Camera
         */

        public Camera build() {
            if (camera.location == null)
                throw new MissingResourceException("Missing location", "Camera", "location");
            if (camera.to == null)
                throw new MissingResourceException("Missing to vector", "Camera", "to");
            if (camera.up == null)
                throw new MissingResourceException("Missing up vector", "Camera", "up");

            try {
                camera.right = camera.to.crossProduct(camera.up).normalize();
                camera.up = camera.right.crossProduct(camera.to).normalize();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("To and Up vectors must not be parallel");
            }

            if (camera.rayTracer == null) {//default if not defined
                camera.rayTracer = new SimpleRayTracer(new scene.Scene("default"));
            }

            return camera.clone();
        }

        /**
         * Sets the image writer for the camera.
         *
         * @param scene scene to be rendered
         * @param type  type of ray tracer
         * @return the image writer
         */
        public Builder setRayTracer(Scene scene, RayTracerType type) {
            if (type == RayTracerType.SIMPLE) {
                camera.rayTracer = new SimpleRayTracer(scene);
            } else if (type == RayTracerType.MULTI_THREADED) {//check if multi-threaded pro2
                camera.rayTracer = new MultiThreadedRayTracer(scene);
            }
            return this;
        }

    }

    /**
     * Enable or disable the Anti-Aliasing effect.
     *
     * @param enable true to turn on AA, false to turn it off
     * @return the camera itself (for chaining)
     */
    public Camera enableAA(boolean enable) {
        this.isAAEnabled = enable;
        return this;
    }

    /**
     * Set how many rays to use per pixel for anti-aliasing.
     *
     * @param count number of rays (must be a perfect square like 9, 25, 81, etc.)
     * @return the camera itself (for chaining)
     */
    public Camera setAARays(int count) {
        this.aaRays = count;
        return this;
    }

    /**
     * pro2
     * Constructs a beam of rays through a pixel for anti-aliasing.
     *
     * @param nX number of pixels in the X direction
     * @param nY number of pixels in the Y direction
     * @param j  column index of the pixel (X-axis)
     * @param i  row index of the pixel (Y-axis)
     * @return list of rays that go through the pixel (i,j)
     */
    public List<Ray> constructAAbeamThroughPixel(int nX, int nY, int j, int i) {
        List<Ray> rays = new ArrayList<>();

        // how many rays per row and per column (like small grid inside pixel)
        int gridSize = (int) Math.sqrt(aaRays);

        // pixel size on the screen
        double rX = VpWidth / nX;
        double rY = VpHeight / nY;

        // center of the pixel
        Point pc = location.add(to.scale(VpDistance))  // go forward
                .add(right.scale((j - (nX - 1) / 2.0) * rX)) // move right or left
                .add(up.scale(-1 * (i - (nY - 1) / 2.0) * rY)); // move up or down

        // size of one tiny square inside pixel
        double subRx = rX / gridSize;
        double subRy = rY / gridSize;

        Random rand = new Random(); // for random numbers

        // go over each small square (sub-pixel)
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {

                // where to shoot the ray inside the pixel
                double xOffset = (col + 0.5) * subRx - rX / 2;
                double yOffset = (row + 0.5) * subRy - rY / 2;

                // if jitter is ON – add random shift
                if (useJitter) {
                    double jitterX = (rand.nextDouble() - 0.5) * subRx; // random shift in x
                    double jitterY = (rand.nextDouble() - 0.5) * subRy; // random shift in y
                    xOffset += jitterX;
                    yOffset += jitterY;
                }

                // final point we shoot to
                Point pij = pc.add(right.scale(xOffset)).add(up.scale(-yOffset));

                // make a ray from camera to this point
                Vector dir = pij.subtract(location);
                if (!isZero(alignZero(dir.lengthSquared()))) {
                    rays.add(new Ray(location, dir));
                }
            }
        }

        return rays;
    }

    /**
     * pro2
     * Generates a jittered point around the base point.
     * we use this to create a small random offset for anti-aliasing.
     * to make the image smoother, we shoot rays to slightly different points around the pixel center.
     *
     * @param base        the point to jitter from (usually the center of the pixel)
     * @param pixelWidth  the width of the pixel
     * @param pixelHeight the height of the pixel
     * @return point with jitter applied
     */
    private Point jitteredPoint(Point base, double pixelWidth, double pixelHeight) {
        double dx = 0, dy = 0;
        if (useJitter) {
            Random rand = new Random();
            dx = (rand.nextDouble() - 0.5) * pixelWidth;
            dy = (rand.nextDouble() - 0.5) * pixelHeight;
        }
        return base.add(right.scale(dx)).add(up.scale(dy));
    }

    /**
     * pro2
     * Traces a ray from the camera location to a jittered point
     *
     * @param base        the base point to jitter from (usually the center of the pixel)
     * @param pixelWidth  the width of the pixel
     * @param pixelHeight the height of the pixel
     * @return the color of the traced ray
     */
    private Color traceJitteredRay(Point base, double pixelWidth, double pixelHeight) {
        Point p = jitteredPoint(base, pixelWidth, pixelHeight);
        return rayTracer.traceRay(new Ray(location, p.subtract(location)));
    }


    /**
     * pro2
     * Checks if two colors are similar enough to be considered the same.
     * This is used to avoid unnecessary calculations in adaptive ray casting.
     *
     * @param c1 first color
     * @param c2 second color
     * @return true if colors are similar, false otherwise
     */
    private boolean areColorsSimilar(Color c1, Color c2) {
        double threshold = 10.0; // אפשר לשנות לפי איכות מבוקשת
        return Math.abs(c1.getRed() - c2.getRed()) < threshold &&
                Math.abs(c1.getGreen() - c2.getGreen()) < threshold &&
                Math.abs(c1.getBlue() - c2.getBlue()) < threshold;
    }

    /**
     * pro2
     * method to calculate the average color from a list of colors.
     *
     * @param colors list of colors to average
     * @return the average color
     */
    private Color averageColor(List<Color> colors) {
        Color sum = Color.BLACK;
        for (Color c : colors) {
            sum = sum.add(c);
        }
        return sum.reduce(colors.size());
    }

    /**
     * pro2
     * Main function that starts adaptive sampling with a new cache
     *
     * @param center      the center point of the pixel
     * @param pixelWidth  width of the pixel
     * @param pixelHeight height of the pixel
     * @param depth       current recursion depth
     * @return the averaged color for the pixel
     */
    private Color adaptiveCastRay(Point center, double pixelWidth, double pixelHeight, int depth) {
        Map<Point, Color> cache = new HashMap<>();
        return adaptiveCastRay(center, pixelWidth, pixelHeight, depth, cache);
    }

    /**
     * pro2
     * Recursive function that does adaptive super sampling with caching
     * Adaptive ray casting method that recursively traces rays through a pixel
     * until a certain depth is reached or the pixel size is small enough.
     * This version uses a cache to avoid recalculating colors for the same point.
     *
     * @param center
     * @param pixelWidth
     * @param pixelHeight
     * @param depth
     * @param cache
     * @return
     */
    private Color adaptiveCastRay(Point center, double pixelWidth, double pixelHeight, int depth, Map<Point, Color> cache) {
        // Check if this point was already calculated
        if (cache.containsKey(center)) {
            return cache.get(center);
        }

        // If max depth is reached, send one ray with jitter
        if (depth == 0) {
            Point samplePoint = jitteredPoint(center, pixelWidth, pixelHeight);
            Vector dir = samplePoint.subtract(location);
            Color color = rayTracer.traceRay(new Ray(location, dir));
            cache.put(center, color);
            return color;
        }

        // Divide the pixel to 4 corners
        double halfWidth = pixelWidth / 2;
        double halfHeight = pixelHeight / 2;

        Point topLeft = center.add(right.scale(-halfWidth)).add(up.scale(halfHeight));
        Point topRight = center.add(right.scale(halfWidth)).add(up.scale(halfHeight));
        Point bottomLeft = center.add(right.scale(-halfWidth)).add(up.scale(-halfHeight));
        Point bottomRight = center.add(right.scale(halfWidth)).add(up.scale(-halfHeight));

        // Send rays to 4 corners and get the colors
        Color c1 = traceCachedRay(topLeft, halfWidth, halfHeight, cache);
        Color c2 = traceCachedRay(topRight, halfWidth, halfHeight, cache);
        Color c3 = traceCachedRay(bottomLeft, halfWidth, halfHeight, cache);
        Color c4 = traceCachedRay(bottomRight, halfWidth, halfHeight, cache);

        // If all colors are similar, return their average
        if (areColorsSimilar(c1, c2) && areColorsSimilar(c1, c3) && areColorsSimilar(c1, c4)) {
            Color avg = averageColor(List.of(c1, c2, c3, c4));
            cache.put(center, avg);
            return avg;
        }

        // If colors are different, divide the pixel again
        Color sub1 = adaptiveCastRay(topLeft, halfWidth, halfHeight, depth - 1, cache);
        Color sub2 = adaptiveCastRay(topRight, halfWidth, halfHeight, depth - 1, cache);
        Color sub3 = adaptiveCastRay(bottomLeft, halfWidth, halfHeight, depth - 1, cache);
        Color sub4 = adaptiveCastRay(bottomRight, halfWidth, halfHeight, depth - 1, cache);

        Color avg = averageColor(List.of(sub1, sub2, sub3, sub4));
        cache.put(center, avg);
        return avg;
    }

    // This function checks cache before sending a ray
    private Color traceCachedRay(Point point, double pixelWidth, double pixelHeight, Map<Point, Color> cache) {
        if (cache.containsKey(point))
            return cache.get(point);
        Color color = traceJitteredRay(point, pixelWidth, pixelHeight);
        cache.put(point, color);
        return color;
    }
}