package org.example;

import org.example.database.DbAccess;
import org.example.database.Way;
import org.example.shapes.Triangle;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    ShaderProgramCache shaderProgramCache;
    long window;
    Camera camera;
    List<Polygon> objects = new ArrayList<>();

    public Scene(int width, int height, ShaderProgramCache shaderProgramCache, long window) {
        this.shaderProgramCache = shaderProgramCache;
        this.window = window;
        setupScene(width, height);
    }

    private void setupScene(int width, int height) {
        camera = new Camera(width, height, window);
        //7 digits for x (lon), 6 digits for y (lat)
        camera.setPosition(1511588, -338141, 100);

        List<Way> ways = DbAccess.getAllWaysAroundAPoint(
                -33.814215364215464,
                151.16991217659032,
                500,
                "highway"
        );
        for (Way way : ways) {
            System.out.println(way.toString());
            Polygon polygon = new Polygon(shaderProgramCache, way);
            objects.add(polygon);
        }

        
        System.out.println("all objects added");

//        OSMWayRenderer.WayGeometry geometry = new OSMWayRenderer().createGeometry(new Way("1577105508").wayVectors(), true);
//        Polygon polygon = new Polygon(shaderProgramCache, geometry.vertices, geometry.indices);
//        objects.add(polygon);
    }

    public void update(float deltaTime) {
        camera.update(deltaTime);
        for (Polygon polygon : objects) {
            polygon.update();
        }
    }

    public void render() {
        for (Polygon polygon : objects) {
            polygon.render();
        }
    }

    public Camera getCamera() {
        return camera;
    } 

}
