package renderer;

import org.junit.jupiter.api.Test;
import primitives.*;
import lighting.*;
import geometries.*;
import scene.Scene;

import static primitives.Util.isZero;

class createMagicalSunsetUnderwaterScene {


    private final Scene scene = new Scene("Magical Sunset Underwater Scene");
    private final Camera.Builder camera = Camera.getBuilder()
            .setLocation(new Point(150, -100, 60))
            .setDirection(new Point(0, 0, 0), new Vector(0, 0, 1)) // up מוגדר ל-Z – בטוח
            .setVpDistance(1000)
            .setVpSize(200, 200)
            .setRayTracer(scene, RayTracerType.SIMPLE);

    /**
     * Creates a magical underwater scene with a sunset atmosphere.
     */
    private void buildUnderwaterScene() {

        // === Water Surface & Ripples ===
        // Create a transparent water plane and add small ripple spheres for realism
        scene.geometries.add(
                new Plane(new Point(0, 0, 0), new Vector(0, 0, 1))
                        .setEmission(new Color(100, 180, 230))
                        .setMaterial(new Material()
                                .setKT(0.6)//transparency
                                .setKR(0.1)//reflection
                                .setKS(0.2)//specular
                                .setShininess(130)//shininess
                                .setRefractiveIndex(1.333))// water refractive index
        );

        Material rippleMat = new Material().setKT(0.7).setKS(0.3).setShininess(100);
        Color rippleColor = new Color(120, 200, 250);
        scene.geometries.add(
                new Sphere(new Point(20, 10, 0.2), 0.5)
                        .setEmission(rippleColor)
                        .setMaterial(rippleMat),
                new Sphere(new Point(-15, -5, 0.15), 0.5)
                        .setEmission(rippleColor)
                        .setMaterial(rippleMat),
                new Sphere(new Point(5, -20, 0.25), 0.5)
                        .setEmission(rippleColor)
                        .setMaterial(rippleMat)
        );

        // === Lighting from Above ===
        // Add a soft blue spotlight from above to simulate sun shining through water
        scene.lights.add(
                new SpotLight(new Color(60, 120, 200), new Point(0, 0, 100), new Vector(0, 0, -1))
                        .setKl(0.0003)
                        .setKq(0.0001)
                        .setNarrowBeam(40)
        );

        // === Island Base ===
        // Create a wide sandy island base using a cylinder
        scene.geometries.add(
                new Cylinder(
                        new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)),
                        6, 4
                )
                        .setEmission(new Color(194, 154, 107))
                        .setMaterial(new Material()
                                .setKD(0.9)
                                .setKS(0.1)
                                .setShininess(20))
        );

        // === Castle Tower ===
        // Add a concrete-gray cylindrical tower for the central structure
        scene.geometries.add(
                new Cylinder(
                        new Ray(new Point(0, 0, 4), new Vector(0, 0, 1)),
                        2, 3
                )
                        .setEmission(new Color(160, 160, 160))
                        .setMaterial(new Material()
                                .setKD(0.6)
                                .setKS(0.3)
                                .setShininess(50))
        );

        // === Castle Roof ===
        // Add a red spherical roof atop the tower for a decorative finish
        scene.geometries.add(
                new Sphere(new Point(0, 0, 7), 2)
                        .setEmission(new Color(255, 0, 0))
                        .setMaterial(new Material()
                                .setKD(0.5)
                                .setKS(0.2)
                                .setShininess(50))
        );

        // === Castle Door Frame & Door ===
        // Add the main rectangular door and its frame for entrance detail
        scene.geometries.add(
                new Cylinder(
                        new Ray(new Point(0, -2.02, 5), new Vector(0, 1, 0)),
                        0.7, 3
                )
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material()
                                .setKD(0.8)
                                .setKS(0.3)
                                .setShininess(30))
        );
        scene.geometries.add(
                new Cylinder(
                        new Ray(new Point(0.9, -2.02, 5), new Vector(0, 1, 0)),
                        0.01, 1.85
                )
                        .setEmission(new Color(0, 0, 0))
                        .setMaterial(new Material()
                                .setKD(0.6)
                                .setKS(0.2)
                                .setShininess(15))
        );
        // === Vegetation: Flower Stem & Petals ===
        // Add a green stem and pink blossom to the flower on the island
        scene.geometries.add(
                new Cylinder(
                        new Ray(new Point(2, 2, 4), new Vector(0, 0, 1)),
                        0.12, 1.5
                )
                        .setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material()
                                .setKD(0.6)
                                .setKS(0.3)
                                .setShininess(20))
        );
        scene.geometries.add(
                new Sphere(new Point(2, 2, 5.5), 0.25)
                        .setEmission(new Color(255, 192, 203))
                        .setMaterial(new Material()
                                .setKD(0.6)
                                .setKS(0.4)
                                .setShininess(100))
        );
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);
            double dx = Math.cos(angle) * 0.5;
            double dy = Math.sin(angle) * 0.5;
            scene.geometries.add(
                    new Sphere(new Point(2 + dx, 2 + dy, 5.5), 0.2)
                            .setEmission(new Color(255, 105, 135))
                            .setMaterial(new Material()
                                    .setKD(0.7)
                                    .setKS(0.3)
                                    .setShininess(80))
            );
        }

        // === Vegetation: Tree & Leaves ===
        // Add a brown tree trunk and green leafy canopy

        // Add a tall cylinder for the trunk
        scene.geometries.add(
                new Cylinder(
                        new Ray(new Point(-4, -1, 4), new Vector(0, 0, 1)),
                        0.4, 5
                )
                        .setEmission(new Color(120, 72, 30))
                        .setMaterial(new Material()
                                .setKD(0.8)
                                .setKS(0.2)
                                .setShininess(10))
        );
        // Add a spherical canopy for the leaves
        scene.geometries.add(
                new Sphere(new Point(-4, -1, 11.2), 2.2)
                        .setEmission(new Color(34, 139, 34))
                        .setMaterial(new Material()
                                .setKD(0.7)
                                .setKS(0.2)
                                .setShininess(5))
        );
        // Add smaller spheres for additional leaves
        scene.geometries.add(
                new Sphere(new Point(-4, -1, 13.2), 1.7)
                        .setEmission(new Color(50, 160, 50))
                        .setMaterial(new Material()
                                .setKD(0.6)
                                .setKS(0.2)
                                .setShininess(5))
        );
        // Add a few more smaller spheres as leaves
        scene.geometries.add(
                new Sphere(new Point(-4, -1, 14.9), 1.2)
                        .setEmission(new Color(70, 200, 70))
                        .setMaterial(new Material()
                                .setKD(0.5)
                                .setKS(0.1)
                                .setShininess(5))
        );

        // === Vegetation: Decorative Fence ===
// Add a circular fence with small cones and nails for detail, plus light per post
        int fenceCount = 16;
        double radius = 4.5;
        double zBase = 4;
        double postHeight = 1.2;

        for (int i = 0; i < fenceCount; i++) {
            double angle = Math.toRadians(-90 + i * (180.0 / (fenceCount - 1)));
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;

            // fence post
            scene.geometries.add(
                    new Cylinder(
                            new Ray(new Point(x, y, zBase), new Vector(0, 0, 1)),
                            0.15, postHeight
                    )
                            .setMaterial(new Material().setKD(0.8).setKS(0.2).setShininess(10))
            );

            // cone top decoration
            scene.geometries.add(
                    new Sphere(new Point(x, y, zBase + postHeight + 0.25), 0.15)
                            .setEmission(new Color(255, 255, 200))
                            .setMaterial(new Material().setKD(0.8).setKS(0.3).setShininess(20))
            );

            // decorative nail
            scene.geometries.add(
                    new Sphere(new Point(x, y, zBase + postHeight + 0.1), 0.05)
                            .setEmission(new Color(90, 90, 90))
                            .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(30))
            );
// Add a light source on top of each post to make them look like lanterns
            scene.lights.add(
                    new PointLight(new Color(100, 100, 100), new Point(x, y, zBase + postHeight + 0.25))
                            .setKl(0.002)
                            .setKq(0.00003)
            );
        }

        // === Fish School ===
        // Add multiple fish at different depths to bring life and color underwater
        Vector to = new Vector(-170, 100, -62).normalize();
        Point cam = new Point(150, -100, 60);

        addFish(scene, cam.add(to.scale(150)).add(new Vector(5, -3, -2.5)), 0.8, 0.3, new Color(255, 100, 100));
        addFish(scene, cam.add(to.scale(170)).add(new Vector(-6, 2, -4)), 0.7, 0.25, new Color(100, 255, 200));
        addFish(scene, cam.add(to.scale(190)).add(new Vector(-5, -4, -6)), 1.0, 0.4, new Color(0, 180, 60));
        addFish(scene, cam.add(to.scale(160)).add(new Vector(-4, -1, -1)), 0.85, 0.32, new Color(150, 255, 150));
        addFish(scene, cam.add(to.scale(180)).add(new Vector(2, -2, -8)), 0.95, 0.38, new Color(255, 255, 100));

        // === Extra Island ===
        // Add a small secondary island near the fish path for extra terrain
        scene.geometries.add(
                new Cylinder(
                        new Ray(new Point(10, 10, 10), new Vector(0, 0, 1)),
                        7, 2
                )
                        .setEmission(new Color(194, 154, 107))
                        .setMaterial(new Material()
                                .setKD(0.5)
                                .setKS(0.1)
                                .setShininess(20))
        );
        // === Pyramid & Glass Box ===
        // Add a transparent glass pyramid and a dark box underneath for visual intrigue
        Point baseCenter = new Point(10, 10, 13);
        double size1 = 2;
        double height = 3;

        Point p1 = baseCenter.add(new Vector(-size1, -size1, 0));
        Point p2 = baseCenter.add(new Vector(size1, -size1, 0));
        Point p3 = baseCenter.add(new Vector(size1, size1, 0));
        Point p4 = baseCenter.add(new Vector(-size1, size1, 0));
        Point apex = baseCenter.add(new Vector(0, 0, height));

        Material glassMaterial = new Material()
                .setKD(0.2)
                .setKS(0.5)
                .setShininess(100)
                .setKT(0.8);
        Color glassColor = new Color(0, 50, 70);

        scene.geometries.add(
                new Polygon(p1, p2, p3, p4)
                        .setEmission(glassColor)
                        .setMaterial(glassMaterial)
        );
        scene.geometries.add(
                new Triangle(p1, p2, apex)
                        .setEmission(glassColor)
                        .setMaterial(glassMaterial),
                new Triangle(p2, p3, apex)
                        .setEmission(glassColor)
                        .setMaterial(glassMaterial),
                new Triangle(p3, p4, apex)
                        .setEmission(glassColor)
                        .setMaterial(glassMaterial),
                new Triangle(p4, p1, apex)
                        .setEmission(glassColor)
                        .setMaterial(glassMaterial)
        );

        // === Wooden Box under Pyramid ===
        // Place a dark-colored wooden box under the pyramid as a base
        double baseThickness = 1.0;
        Point baseBottomCenter = baseCenter.add(new Vector(0, 0, -baseThickness));

        Point b1 = baseBottomCenter.add(new Vector(-size1 - 0.5, -size1 - 0.5, 0));
        Point b2 = baseBottomCenter.add(new Vector(size1 + 0.5, -size1 - 0.5, 0));
        Point b3 = baseBottomCenter.add(new Vector(size1 + 0.5, size1 + 0.5, 0));
        Point b4 = baseBottomCenter.add(new Vector(-size1 - 0.5, size1 + 0.5, 0));
        Point t1 = b1.add(new Vector(0, 0, baseThickness));
        Point t2 = b2.add(new Vector(0, 0, baseThickness));
        Point t3 = b3.add(new Vector(0, 0, baseThickness));
        Point t4 = b4.add(new Vector(0, 0, baseThickness));

        Material baseMaterial = new Material()
                .setKD(0.7)
                .setKS(1)
                .setShininess(50);
        Color baseColor = new Color(0, 0, 0);

        scene.geometries.add(
                new Polygon(b1, b2, b3, b4).setEmission(baseColor).setMaterial(baseMaterial),
                new Polygon(t1, t2, t3, t4).setEmission(baseColor).setMaterial(baseMaterial),
                new Polygon(b1, b2, t2, t1).setEmission(baseColor).setMaterial(baseMaterial),
                new Polygon(b2, b3, t3, t2).setEmission(baseColor).setMaterial(baseMaterial),
                new Polygon(b3, b4, t4, t3).setEmission(baseColor).setMaterial(baseMaterial),
                new Polygon(b4, b1, t1, t4).setEmission(baseColor).setMaterial(baseMaterial)
        );

        // === Flowers Next to Pyramid ===
        // Add three small flowers around the pyramid for decoration
        Point[] flowerBases = {
                new Point(14.5, 11, 12),
                new Point(11, 14.5, 12),
                new Point(8, 11, 12)
        };
        for (Point base : flowerBases) {
            scene.geometries.add(
                    new Cylinder(new Ray(base, new Vector(0, 0, 1)), 0.12, 1.5)
                            .setEmission(new Color(34, 139, 34))
                            .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(20))
            );
            scene.geometries.add(
                    new Sphere(base.add(new Vector(0, 0, 1.5)), 0.25)
                            .setEmission(new Color(255, 192, 203))
                            .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(100))
            );
            for (int i = 0; i < 6; i++) {
                double angle = Math.toRadians(i * 60);
                double dx = Math.cos(angle) * 0.5;
                double dy = Math.sin(angle) * 0.5;
                scene.geometries.add(
                        new Sphere(base.add(new Vector(dx, dy, 1.5)), 0.2)
                                .setEmission(new Color(100, 0, 120))
                                .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(80))
                );
            }
            Color cloudColor = new Color(220, 120, 200);
            Material cloudMaterial = new Material()
                    .setKD(0.25)
                    .setKS(0.1)
                    .setKT(0.4)
                    .setShininess(15);
            double z = 16;

            scene.geometries.add(
                    new Sphere(new Point(-12, 0, z), 3).setEmission(cloudColor).setMaterial(cloudMaterial),
                    new Sphere(new Point(-9, 1.5, z), 2.5).setEmission(cloudColor).setMaterial(cloudMaterial),
                    new Sphere(new Point(-15, -1.5, z), 2.5).setEmission(cloudColor).setMaterial(cloudMaterial),
                    new Sphere(new Point(-7, 2.5, z - 0.5), 1.8).setEmission(cloudColor).setMaterial(cloudMaterial),
                    new Sphere(new Point(-17, -2.5, z - 0.5), 1.8).setEmission(cloudColor).setMaterial(cloudMaterial),
                    new Sphere(new Point(-11, 1.5, z + 2.7), 1).setEmission(cloudColor).setMaterial(cloudMaterial),
                    new Sphere(new Point(-13, -1.5, z + 2.7), 1).setEmission(cloudColor).setMaterial(cloudMaterial),
                    new Sphere(new Point(-12, 0, z - 2.5), 0.6).setEmission(cloudColor).setMaterial(cloudMaterial)
            );

            Color cloudColor1 = new Color(220, 120, 200);
            Material cloudMaterial1 = new Material()
                    .setKD(0.4)
                    .setKS(0.1)
                    .setKT(0.4)
                    .setShininess(15);
            double z1 = 22;

            scene.geometries.add(
                    new Sphere(new Point(16, 1, z1), 3).setEmission(cloudColor1).setMaterial(cloudMaterial1),
                    new Sphere(new Point(19.5, 2.5, z1), 2.5).setEmission(cloudColor1).setMaterial(cloudMaterial1),
                    new Sphere(new Point(12, -1.5, z1), 2.5).setEmission(cloudColor1).setMaterial(cloudMaterial1),
                    new Sphere(new Point(21.5, 1.5, z1), 1.8).setEmission(cloudColor1).setMaterial(cloudMaterial1),
                    new Sphere(new Point(10, 0.5, z1), 1.8).setEmission(cloudColor1).setMaterial(cloudMaterial1),
                    new Sphere(new Point(17, 2, z1 + 2.7), 1).setEmission(cloudColor1).setMaterial(cloudMaterial1),
                    new Sphere(new Point(15, 0, z1 + 2.7), 1).setEmission(cloudColor1).setMaterial(cloudMaterial1),
                    new Sphere(new Point(16, 1, z1 - 2.5), 0.6).setEmission(cloudColor1).setMaterial(cloudMaterial1)
            );

            Color cloudColor2 = new Color(180, 140, 255);
            Material cloudMaterial2 = new Material()
                    .setKD(0.4)
                    .setKS(0.1)
                    .setKT(0.4)
                    .setShininess(15);
            double z2 = 18;

            scene.geometries.add(
                    new Sphere(new Point(-10, -8, z2), 3).setEmission(cloudColor2).setMaterial(cloudMaterial2),
                    new Sphere(new Point(-6.5, -6.5, z2), 2.5).setEmission(cloudColor2).setMaterial(cloudMaterial2),
                    new Sphere(new Point(-13.5, -9.5, z2), 2.5).setEmission(cloudColor2).setMaterial(cloudMaterial2),
                    new Sphere(new Point(-10, -5.5, z2 - 0.5), 1.8).setEmission(cloudColor2).setMaterial(cloudMaterial2),
                    new Sphere(new Point(-15, -10.5, z2 - 0.5), 1.8).setEmission(cloudColor2).setMaterial(cloudMaterial2),
                    new Sphere(new Point(-16, -7, z2 + 2.7), 1).setEmission(cloudColor2).setMaterial(cloudMaterial2),
                    new Sphere(new Point(-14, -9, z2 + 2.7), 1).setEmission(cloudColor2).setMaterial(cloudMaterial2),
                    new Sphere(new Point(-15, -8, z2 - 2.5), 0.6).setEmission(cloudColor2).setMaterial(cloudMaterial2)
            );
        }
        // === Additional Island & Steps ===
        // Add a second sandy island and stone steps to connect it, creating depth
        Color islandColor = new Color(210, 180, 140);
        Material islandMaterial = new Material()
                .setKD(0.5)
                .setKS(0.1)
                .setShininess(15);

        // Main island cylinder
        scene.geometries.add(
                new Cylinder(new Ray(new Point(5, -2, 0), new Vector(0, 0, 1)), 5, 4)
                        .setEmission(islandColor)
                        .setMaterial(islandMaterial)
        );
        scene.geometries.add(
                new Cylinder(new Ray(new Point(8.5, -2, 0), new Vector(0, 0, 1)), 3.5, 4)
                        .setEmission(islandColor)
                        .setMaterial(islandMaterial)
        );

        // Shallow platform at top of island
        scene.geometries.add(
                new Cylinder(new Ray(new Point(8.5, -2, 4.01), new Vector(0, 0, 1)), 1.5, 0.1)
                        .setEmission(new Color(0, 100, 150))
                        .setMaterial(new Material().setKD(0.3).setKS(0.5).setKT(0.5).setShininess(100))
        );

        // Short trunk and leaves on the island
        scene.geometries.add(
                new Cylinder(new Ray(new Point(6.5, -2.8, 4), new Vector(0, 0, 1)), 0.2, 2)
                        .setEmission(new Color(100, 70, 40))
                        .setMaterial(new Material().setKD(0.7).setKS(0.2).setShininess(10))
        );
        scene.geometries.add(
                new Sphere(new Point(6.5, -2.8, 6), 0.8)
                        .setEmission(new Color(60, 170, 60))
                        .setMaterial(new Material().setKD(0.6).setKS(0.3).setShininess(20))
        );

        // Stone steps leading down to water level
        Color stepColor = new Color(60, 60, 90);
        Material stepMaterial = new Material().setKD(0.6).setKS(0.3).setShininess(15);
        double stepWidth = 2.0, stepDepth = 1.5, stepHeight = 0.3;
        Point baseStep = new Point(11.5, -2, 4);

        for (int i = 0; i < 20; i++) {
            double z = 4 - i * stepHeight;
            double y = -2 - i * (stepDepth * 0.7);

            scene.geometries.add(
                    new Polygon(
                            new Point(baseStep.getX() - stepWidth / 2, y, z),
                            new Point(baseStep.getX() + stepWidth / 2, y, z),
                            new Point(baseStep.getX() + stepWidth / 2, y - stepDepth, z - stepHeight),
                            new Point(baseStep.getX() - stepWidth / 2, y - stepDepth, z - stepHeight)
                    ).setEmission(stepColor)
                            .setMaterial(stepMaterial)
            );
        }


        // === Lighting Setup ===
// Add several light sources to create natural underwater atmosphere

// Sunset spotlight – warm light from the top-right corner, simulates sunlight from outside the water
        scene.lights.add(
                new SpotLight(new Color(120, 100, 90), new Point(100, -100, 150), new Vector(-1, 1, -1))
                        //how much the light fades over distance
                        .setKl(0.001)
                        .setKq(0.0001)

        );

// Ambient point light – general soft light to make sure objects are not too dark
        scene.lights.add(
                new PointLight(new Color(150, 150, 150), new Point(-2, 3, -0.5))
                        .setKl(0.0006)
                        .setKq(0.0002)
        );

// Underwater spotlight from below – adds blue light going up from deep water
        scene.lights.add(
                new SpotLight(new Color(80, 120, 255), new Point(0, 0, -10), new Vector(0, 0, 1))
                        .setKl(0.0003)
                        .setKq(0.00005)
                        .setNarrowBeam(20)
        );

// Side underwater light – blue light coming from the side, adds contrast and detail
        scene.lights.add(
                new SpotLight(new Color(40, 80, 160), new Point(20, -20, -15), new Vector(-0.5, 1, 1).normalize())
                        .setKl(0.0004)
                        .setKq(0.00005)
                        .setNarrowBeam(25)
        );

// Deep underwater light – dark blue light going up, gives the scene a deep ocean feel
        scene.lights.add(
                new SpotLight(new Color(20, 60, 130), new Point(0, 0, -10), new Vector(0, 0, 1))
                        .setKl(0.0004)
                        .setKq(0.0001)
                        .setNarrowBeam(25)
        );
        //light from the top, simulates sunlight from outside the water
        scene.lights.add(
                new SpotLight(new Color(60, 110, 200), new Point(0, 0, 10), new Vector(0, 0, -1))
                        .setKl(0.0004)
                        .setKq(0.00008)
                        .setNarrowBeam(60) // sharp
        );
        scene.lights.add(
                new DirectionalLight(
                        new Color(40, 40, 50),        // warm soft light
                        new Vector(-0.2, -1, -0.3).normalize() // light coming diagonally from above-left
                )
        );
// === Hidden Underwater Energy Tube ===
// A subtle glowing pipe deep beneath the island

        Tube hiddenTube = new Tube(
                0.3,
                new Ray(
                        new Point(-10, -10, -7), // start point under the sea
                        new Vector(1, 0.3, 0)    // slightly diagonal in X-Y plane
                )
        );

// Material: low diffuse, high specular, some transparency to make it glow from inside
        hiddenTube.setEmission(new Color(20, 40, 200))  //blue
                .setMaterial(new Material().setKD(0.05).setKS(0.7).setKT(0).setShininess(200));

// Add to scene
        scene.geometries.add(hiddenTube);

//Light coming out of the tube
        scene.lights.add(
                new SpotLight(new Color(30, 60, 220), new Point(-10, -10, -7), new Vector(0.5, 0.3, 1))
                        .setKl(0.0006)
                        .setKq(0.0003)
                        .setNarrowBeam(10)

        );


    }

    /**
     * Add a fish to the scene at the specified center point with given dimensions and color.
     *
     * @param scene      the scene to add the fish to
     * @param center     the center point of the fish
     * @param bodyLength the length of the fish body
     * @param bodyRadius the radius of the fish body
     * @param bodyColor  the color of the fish body
     */
    private void addFish(Scene scene, Point center, double bodyLength, double bodyRadius, Color bodyColor) {
        // === Fish Body ===
        // Create the main body sphere for the fish
        scene.geometries.add(
                new Sphere(center, bodyRadius)
                        .setEmission(bodyColor)
                        .setMaterial(new Material().setKD(0).setKS(0.3).setShininess(20))
        );

        // === Front Head ===
        // Add a slightly smaller sphere at front to shape the fish's head
        scene.geometries.add(
                new Sphere(center.add(new Vector(0.5, 0, 0)), bodyRadius * 0.85)
                        .setEmission(bodyColor.reduce(1))
                        .setMaterial(new Material().setKD(0).setKS(0.3).setShininess(20))
        );

        // === Tail ===
        // Add a triangle for the tail for directional shape and movement hint
        scene.geometries.add(
                new Triangle(
                        center.add(new Vector(-bodyLength / 2, 0, 0)),
                        center.add(new Vector(-bodyLength / 2 - 0.4, 0.3, 0.2)),
                        center.add(new Vector(-bodyLength / 2 - 0.4, -0.3, -0.2))
                )
                        .setEmission(bodyColor.reduce(1))
                        .setMaterial(new Material().setKD(0).setKS(0.2).setShininess(20))
        );

        // === Fins & Eye ===
        // Add top, bottom, side fins and an eye for realism
        scene.geometries.add(
                new Triangle(
                        center.add(new Vector(0, 0, bodyRadius)),
                        center.add(new Vector(-0.2, 0, bodyRadius + 0.4)),
                        center.add(new Vector(0.2, 0, bodyRadius + 0.4))
                ).setEmission(bodyColor.reduce(1))
                        .setMaterial(new Material().setKD(0).setKS(0.2).setShininess(20)),

                new Triangle(
                        center.add(new Vector(0, 0, -bodyRadius)),
                        center.add(new Vector(-0.2, 0, -bodyRadius - 0.3)),
                        center.add(new Vector(0.2, 0, -bodyRadius - 0.3))
                ).setEmission(bodyColor.reduce(1))
                        .setMaterial(new Material().setKD(0).setKS(0.2).setShininess(20)),

                new Triangle(
                        center.add(new Vector(0.1, bodyRadius, 0)),
                        center.add(new Vector(0.4, bodyRadius + 0.3, 0.2)),
                        center.add(new Vector(-0.2, bodyRadius + 0.3, -0.2))
                ).setEmission(bodyColor.reduce(1))
                        .setMaterial(new Material().setKD(0).setKS(0.2).setShininess(20)),

                new Sphere(center.add(new Vector(0.25, -bodyRadius - 0.08, 0.1)), 0.06)
                        .setEmission(new Color(10, 10, 10))
                        .setMaterial(new Material().setKD(0).setKS(0.3).setShininess(20))
        );
    }

    // Case 1: AA without Adaptive, no multithreading
    // Checks image quality and time
    @Test
    void test_NoAdaptive_NoMT() {
        buildUnderwaterScene();

        camera.setResolution(600, 600)
                .setRayTracer(scene, RayTracerType.SIMPLE);

        Camera cam = camera.build()
                .enableAdaptive(false)
                .enableAA(true)
                .setAARays(81)
                .enableJitter();

        long start = System.nanoTime();
        cam.renderImage().writeToImage("underwater_NoAdaptive_NoMT");
        long end = System.nanoTime();

        System.out.println("case 1- NoAdaptive + NoMT: " + (end - start) / 1e9 + " seconds");
    }

    // Case 2: AA without Adaptive, with multithreading
    // Checks image quality and time
    @Test
    void test_NoAdaptive_MT() {
        buildUnderwaterScene();

        camera.setResolution(600, 600)
                .setRayTracer(scene, RayTracerType.MULTI_THREADED);

        Camera cam = camera.build()
                .enableAdaptive(false)
                .enableAA(true)
                .setAARays(81)
                .enableJitter();

        long start = System.nanoTime();
        cam.renderImage().writeToImage("underwater_NoAdaptive_MT");
        long end = System.nanoTime();

        System.out.println("case 2- NoAdaptive + MT: " + (end - start) / 1e9 + " seconds");
    }

// Case 3: Adaptive AA, no multithreading
// Checks image quality and time

    @Test
    void test_Adaptive_NoMT() {
        buildUnderwaterScene();

        camera.setResolution(600, 600)
                .setRayTracer(scene, RayTracerType.SIMPLE);

        Camera cam = camera.build()
                .enableAdaptive(true)
                .setAdaptiveDepth(4)
                .enableJitter();

        long start = System.nanoTime();
        cam.renderImage().writeToImage("underwater_Adaptive_NoMT");
        long end = System.nanoTime();

        System.out.println("case 3- Adaptive + NoMT: " + (end - start) / 1e9 + " seconds");
    }

    // Case 4: Adaptive AA, with multithreading
    // Checks image quality and time
    @Test
    void test_Adaptive_MT() {
        buildUnderwaterScene();

        camera.setResolution(600, 600)
                .setRayTracer(scene, RayTracerType.MULTI_THREADED);

        Camera cam = camera.build()
                .enableAdaptive(true)
                .setAdaptiveDepth(4)
                .enableJitter();

        long start = System.nanoTime();
        cam.renderImage().writeToImage("underwater_Adaptive_MT");
        long end = System.nanoTime();

        System.out.println("case 4-Adaptive + MT: " + (end - start) / 1e9 + " seconds");
    }
}

