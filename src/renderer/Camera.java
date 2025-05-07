package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.isZero;

/**
 * Camera class represents a virtual camera in 3D space.
 */
public class Camera implements Cloneable {
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
    private int nX = 0;
    /**
     * Number of pixels in the Y direction.
     */
    private int nY = 0;

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
     * Inner static Builder class for building Camera instances.
     */
    public static class Builder {
        private final Camera camera = new Camera();

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

            return camera.clone();
        }
    }
}
