package org.example;

import org.example.database.DbAccess;

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
        camera.setPosition(74165, 437275, 100);

        List<String> allWayIDs = DbAccess.getAllWayIDs();
        for (int i = 0; i < allWayIDs.size(); i++) {
            objects.add(new Polygon(allWayIDs.get(i)));
            objects.get(i).setShader(shaderProgramCache.getShader("default"));

        }

    }

    public void update(float deltaTime) {
        camera.update();
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
