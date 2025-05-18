package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a collection of geometries that can be intersected by a ray.
 * Provides methods to add geometries and find intersection points with a given ray.
 */
public  class Geometries extends Intersectable {

    /**
     * A list of geometries in the collection.
     */
    private final List<Intersectable> geometries = new LinkedList<>();

    /**
     * Creates an empty collection of geometries.
     */
    public Geometries() {
        // No initialization required as the list is already initialized.
    }

    /**
     * Creates a collection of geometries with the given geometries.
     *
     * @param geometries One or more geometries to add to the collection.
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries One or more geometries to add.
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }

    @Override
        protected List<Intersection> calculateIntersectionsHelper(Ray ray) {
        List<Intersection> intersections = null;
        for (Intersectable geo : geometries) {
            List<Intersection> geometryIntersections = geo.calculateIntersections(ray);
            if (geometryIntersections != null && !geometryIntersections.isEmpty()) {
                if (intersections == null) {
                    intersections = new LinkedList<>(geometryIntersections);
                } else {
                    intersections.addAll(geometryIntersections);
                }
            }
        }
        return intersections;
    }
}