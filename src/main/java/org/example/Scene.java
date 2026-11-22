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
        camera.setPosition(1513268, -334932, 10);

//        Polygon testWay = new Polygon(shaderProgramCache, "1577105508");
//        testWay.setPosition(0, 0, 0);

        OSMWayRenderer.WayGeometry geometry = new OSMWayRenderer().createGeometry(new Way("1577105508").wayVectors(), true);
        Polygon polygon = new Polygon(shaderProgramCache, geometry.vertices, geometry.indices);
        objects.add(polygon);
        //objects.add(new Polygon(shaderProgramCache, Triangle.getVerticesFloat(), Triangle.getIndices()));
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
