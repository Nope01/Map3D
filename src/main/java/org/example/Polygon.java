package org.example;

import org.example.shapes.SimplePolygon;
import org.example.shapes.Triangle;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

import static org.example.VectorUtils.verticesToMap;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BACK;

public class Polygon extends SceneObject {
    public Polygon() {
        super();
        verticesFloat = SimplePolygon.getVerticesFloat();
        List<Vector3f> verticesVec = VectorUtils.floatToVector3f(SimplePolygon.getVerticesFloat());
        Map<Vector3f, Integer> map = VectorUtils.verticesToMap(verticesVec);
        indices = EarClipping.getTrianglesFromVertices(verticesVec, map);
        for (int i = 0; i < indices.length; i++) {
            System.out.print(indices[i++]);
            System.out.print(indices[i++]);
            System.out.print(indices[i++]);
            System.out.println("");
        }
        bindGeometry();
    }

    @Override
    public void update() {
        super.update();
        //rotation.add(0.0f, 0.9f, 0.0f);
    }

    @Override
    public void render() {
//        glFrontFace(GL_CW);
//        glEnable(GL_CULL_FACE);
//        glCullFace(GL_BACK);
        super.render();
    }
}
