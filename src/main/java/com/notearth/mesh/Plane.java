package com.notearth.mesh;

import com.jogamp.opengl.math.Vec3f;

import java.util.Arrays;

public class Plane {

    private Vec3f position;
    private float width, height;
    private int segment;

    private Mesh mesh;

    public Plane(Vec3f position, float width, float height, int segment) {
        this.position = position.copy();
        this.width = width;
        this.height = height;
        this.segment = segment;
        mesh = buildPlane();
    }

    private Mesh buildPlane() {
        int nVertices = segment + 1;
        int vertexDataLength = 8 * nVertices * nVertices;
        int indicesLength = 6 * (segment * segment);
        float[] vertexData = new float[vertexDataLength];
        int[] indices = new int[indicesLength];

        float offsetX = (float)(width / segment);
        float offsetZ = (float)(height / segment); // Z since x-Axis and z-Axis is the plane space

        float textureOffset = (float)1;

        // Construct vertex data
        int idx = 0;
        for (int row = 0; row < nVertices; ++row) {
            for (int col = 0; col < nVertices; ++col) {
                int vertexDataIndex = idx * 8;
                float x = position.x() + (offsetX * col);
                float y = position.y();
                float z = position.z() + (offsetZ * row);

                float u = 0 + (textureOffset * col);
                float v = 1 - (textureOffset * row);

                float nx = 0.0f;
                float ny = 1.0f;
                float nz = 0.0f;

                vertexData[vertexDataIndex] = x;
                vertexData[vertexDataIndex + 1] = y;
                vertexData[vertexDataIndex + 2] = z;

                vertexData[vertexDataIndex + 3] = u;
                vertexData[vertexDataIndex + 4] = v;

                vertexData[vertexDataIndex + 5] = nx;
                vertexData[vertexDataIndex + 6] = ny;
                vertexData[vertexDataIndex + 7] = nz;

                idx += 1;
            }
        }

        idx = 0;
        for (int row = 0; row < segment; ++row) {
            for (int col = 0; col < segment; ++col) {
                int topLeft = row * nVertices + col;
                int topRight = topLeft + 1;
                int bottomLeft = (row + 1) * nVertices + col;
                int bottomRight = bottomLeft + 1;

                int indicesIndex = idx;

                indices[indicesIndex] = topRight;
                indices[indicesIndex + 1] = topLeft;
                indices[indicesIndex + 2] = bottomLeft;

                indices[indicesIndex + 3] = bottomLeft;
                indices[indicesIndex + 4] = bottomRight;
                indices[indicesIndex + 5] = topRight;

                idx += 6;
            }
        }

        return new Mesh(vertexData, indices);
    }

    public Vec3f getPosition() {
        return position;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
