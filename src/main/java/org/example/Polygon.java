package org.example;

public class Polygon extends SceneObject {
    public Polygon() {
        super();
        verticesFloat = Triangle.getVerticesFloat();
        indices = Triangle.getIndices();
        bindGeometry();
    }

    @Override
    public void render() {
        super.render();
    }
}
