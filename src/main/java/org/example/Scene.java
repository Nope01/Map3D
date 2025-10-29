package org.example;

public class Scene {

    Polygon object;

    public Scene() {
        setupScene();
    }

    private void setupScene() {
        object = new Polygon();
    }

    public void update(float deltaTime) {
        object.render();
    }

}
