package org.example;

public class Polygon extends SceneObject {
    public Polygon() {
        super();
        verticesFloat = new float[]{
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f,  0.5f, 0.0f
        };

        indices = new int[] {
                0, 1, 2,
        };
        bindGeometry();
    }

    @Override
    public void render() {

        super.render();
    }
}
