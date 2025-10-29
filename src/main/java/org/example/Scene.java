package org.example;

public class Scene {

    ShaderProgramCache shaderProgramCache;
    long window;
    Camera camera;
    Polygon object;

    public Scene(int width, int height, ShaderProgramCache shaderProgramCache, long window) {
        this.shaderProgramCache = shaderProgramCache;
        this.window = window;
        setupScene(width, height);
    }

    private void setupScene(int width, int height) {
        camera = new Camera(width, height, window);
        camera.setPosition(0, 0, 1);

        object = new Polygon();
        object.setShader(shaderProgramCache.getShader("default"));
    }

    public void update(float deltaTime) {
        camera.update();
    }

    public void render() {
        object.render();
    }

    public Camera getCamera() {
        return camera;
    }

}
