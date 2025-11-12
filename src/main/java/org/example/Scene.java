package org.example;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    ShaderProgramCache shaderProgramCache;
    long window;
    Camera camera;
    Polygon object;
    List<Polygon> objects = new ArrayList<>();

    public Scene(int width, int height, ShaderProgramCache shaderProgramCache, long window) {
        this.shaderProgramCache = shaderProgramCache;
        this.window = window;
        setupScene(width, height);
    }

    private void setupScene(int width, int height) {
        camera = new Camera(width, height, window);
        camera.setPosition(7416, 43727, 10);

        object = new Polygon("4224978");
        object.setShader(shaderProgramCache.getShader("default"));
    }

    public void update(float deltaTime) {
        camera.update();
        object.update();
    }

    public void render() {
        object.render();
    }

    public Camera getCamera() {
        return camera;
    }

}
