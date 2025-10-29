package org.example;

public class Scene {

    ShaderProgramCache shaderProgramCache;
    Polygon object;

    public Scene(ShaderProgramCache shaderProgramCache) {
        this.shaderProgramCache = shaderProgramCache;
        setupScene();
    }

    private void setupScene() {
        object = new Polygon();
        object.setShader(shaderProgramCache.getShader("default"));
    }

    public void update(float deltaTime) {
        object.render();
    }

}
