package org.example;

import org.example.shapes.SimplePolygon;
import org.example.shapes.Triangle;
import org.joml.Vector3f;

import java.util.List;

public class Polygon extends SceneObject {
    public Polygon() {
        super();
        verticesFloat = SimplePolygon.getVerticesFloat();
        List<Vector3f> verticesVec = VectorUtils.floatToVector3f(SimplePolygon.getVerticesFloat());
        System.out.println(verticesVec.size());
        indices = EarClipping.getTrianglesFromVertices(verticesVec);
        System.out.println(indices[1]);
        bindGeometry();
    }

    @Override
    public void render() {
        super.render();
    }
}
