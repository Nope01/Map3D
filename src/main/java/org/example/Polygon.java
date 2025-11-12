package org.example;

import org.example.database.Way;
import org.example.shapes.SimplePolygon;
import org.example.shapes.Triangle;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BACK;

public class Polygon extends SceneObject {
    public Polygon(String wayID) {
        super();
//        verticesFloat = SimplePolygon.getVerticesFloat();
//        List<Vector3f> verticesVec = VectorUtils.floatToVector3f(SimplePolygon.getVerticesFloat());
//        Map<Vector3f, Integer> map = VectorUtils.verticesToMap(verticesVec);
//        indices = EarClipping.getTrianglesFromVertices(verticesVec, map);

        Way way = new Way(wayID);
        List<Vector3f> verticesVec = way.wayVectors();
        verticesFloat = VectorUtils.vector3fToFloat(verticesVec);
        Map<Vector3f, Integer> map = VectorUtils.verticesToMap(verticesVec);
        indices = EarClipping.getTrianglesFromVertices(verticesVec, map);

        System.out.println(this);
        bindGeometry();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render() {
//        glFrontFace(GL_CW);
//        glEnable(GL_CULL_FACE);
//        glCullFace(GL_BACK);
        super.render();
    }
}
