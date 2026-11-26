package org.example;

import org.example.database.Way;
import org.example.OSMWayRenderer.WayType;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Polygon extends SceneObject {

    public WayType type;


    public Polygon(ShaderProgramCache shaderProgramCache, Way way) {
        super(shaderProgramCache);
        List<Vector3f> nodes = way.wayVectors();
        boolean closed = isClosedWay(nodes);

        OSMWayRenderer.WayGeometry geometry =
                new OSMWayRenderer().createGeometry(nodes, closed);

        this.verticesFloat = geometry.vertices;
        this.indices = geometry.indices;
        this.type = geometry.type;

        configureDrawMode();

        bindGeometry();
    }
    public Polygon(ShaderProgramCache shaderProgramCache, String wayID) {
        this(shaderProgramCache, new Way(wayID));
    }

    public Polygon(
            ShaderProgramCache shaderProgramCache,
            float[] verticesFloat,
            int[] indices
    ) {
        this(
                shaderProgramCache,
                verticesFloat,
                indices,
                WayType.POLYGON
        );
    }


    public Polygon(
            ShaderProgramCache shaderProgramCache,
            float[] verticesFloat,
            int[] indices,
            WayType type
    ) {
        super(shaderProgramCache);

        this.verticesFloat = verticesFloat;
        this.indices = indices;
        this.type = type;

        configureDrawMode();

        bindGeometry();
    }


    private void configureDrawMode() {

        switch (type) {

            case POLYGON:
                setDrawMode(GL_TRIANGLES);
                break;

            case LINE:
                setDrawMode(GL_LINES);
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported geometry type: " + type
                );
        }
    }


    private boolean isClosedWay(List<Vector3f> nodes) {

        if (nodes == null || nodes.size() < 4) {
            return false;
        }

        Vector3f first = nodes.getFirst();
        Vector3f last = nodes.getLast();

        return first.x == last.x && first.y == last.y;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render() {
        super.render();
    }
}