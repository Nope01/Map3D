package org.example;

import org.example.database.Way;
import org.example.OSMWayRenderer.WayType;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Polygon extends SceneObject {

    public WayType type;

    /**
     * Creates a Polygon from an OSM way ID.
     * Determines whether the way is closed automatically.
     */
    public Polygon(ShaderProgramCache shaderProgramCache, String wayID) {
        super(shaderProgramCache);

        Way way = new Way(wayID);
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

    /**
     * Creates geometry manually, defaulting to a filled polygon.
     */
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

    /**
     * Creates geometry manually with an explicit rendering type.
     */
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

    /**
     * Maps the OSM geometry type to an OpenGL draw mode.
     */
    private void configureDrawMode() {

        switch (type) {

            case POLYGON:
                setDrawMode(GL_TRIANGLES);
                System.out.println("Type is polygon");
                break;

            case LINE:
                setDrawMode(GL_LINES);
                System.out.println("Type is line");
                break;

            default:
                throw new IllegalArgumentException(
                        "Unsupported geometry type: " + type
                );
        }
    }

    /**
     * Determines whether an OSM way is closed.
     *
     * OSM ways normally repeat their first node at the end.
     */
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