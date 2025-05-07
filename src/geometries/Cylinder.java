package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a cylinder in 3D space, defined by a central axis, radius, and height.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a cylinder with a given axis ray, radius, and height.
     *
     * @param axisRay The central axis of the cylinder.
     * @param radius  The radius of the cylinder.
     * @param height  The height of the cylinder.
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(radius, axisRay);
        this.height = height;
    }

    /**
     * Returns the height of the cylinder.
     *
     * @return The height of the cylinder.
     */
    public double getHeight() {
        return height;
    }

    @Override
    public Vector getNormal(Point point) {
        Point p0 = mainAxis.getHead();
        Vector dir = mainAxis.getDirection();

        double t;
        try {
            Vector v = point.subtract(p0);
            t = alignZero(v.dotProduct(dir));
        } catch (IllegalArgumentException e) {
            return dir.scale(-1);
        }

        if (isZero(t))
            return dir.scale(-1);
        if (isZero(t - height))
            return dir;

        Point o = mainAxis.getPoint(t);
        return point.subtract(o).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p = ray.getHead();
        Vector v = ray.getDirection();
        Point p0 = mainAxis.getHead();
        Vector va = mainAxis.getDirection();

        List<Point> result = new LinkedList<>();

        List<Point> tubePoints = super.findIntersections(ray);
        if (tubePoints != null) {
            for (Point pt : tubePoints) {
                double t = alignZero(pt.subtract(p0).dotProduct(va));
                if (alignZero(t) >= 0 && alignZero(t - height) <= 0) {
                    result.add(pt);
                }
            }
        }

        double nv = va.dotProduct(v);
        if (!isZero(nv)) {
            Vector u = p0.subtract(p);
            double t1 = alignZero(va.dotProduct(u) / nv);
            if (alignZero(t1) >= 0) {
                Point q1 = ray.getPoint(t1);
                try {
                    Vector vec = q1.subtract(p0);
                    if (alignZero(vec.lengthSquared() - radius * radius) <= 0) {
                        result.add(q1);
                    }
                } catch (IllegalArgumentException e) {
                    result.add(q1);
                }
            }

            Point top = p0.add(va.scale(height));
            Vector uTop = top.subtract(p);
            double t2 = alignZero(va.dotProduct(uTop) / nv);
            if (t2 > 0) {
                Point q2 = ray.getPoint(t2);
                try {
                    Vector vec = q2.subtract(top);
                    if (alignZero(vec.lengthSquared() - radius * radius) <= 0) {
                        if (result.stream().noneMatch(pnt -> pnt.equals(q2))) {
                            result.add(q2);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    result.add(q2);
                }
            }
        }

        if (result.isEmpty()) {
            return null;
        }

        result.sort(Comparator.comparingDouble(pnt -> pnt.distance(p)));
        return result;
    }
}