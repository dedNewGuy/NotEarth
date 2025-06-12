package com.notearth.mesh;

import com.jogamp.opengl.math.Vec3f;


public class Plane {

    private final Vec3f position;
    private final float width;
    private final float height;
    private final int segment;

    private final Mesh mesh;

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
                float ny = 0.0f;
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

        calculateVertexNormals(vertexData, indices);

        return new Mesh(vertexData, indices);
    }

    public void calculateVertexNormals(float[] vertexData, int[] indices) {
        int idx;
        for (int i = 0; i < indices.length; i += 3) {

            int v1Index = indices[i];
            int v2Index = indices[i + 1];
            int v3Index = indices[i + 2];

            idx = v1Index * 8;
            Vec3f a = new Vec3f(vertexData[idx], vertexData[idx + 1], vertexData[idx + 2]);

            idx = v2Index * 8;
            Vec3f b = new Vec3f(vertexData[idx], vertexData[idx + 1], vertexData[idx + 2]);

            idx = v3Index * 8;
            Vec3f c = new Vec3f(vertexData[idx], vertexData[idx + 1], vertexData[idx + 2]);

            Vec3f faceNormals = computeFaceNormals(a.copy(), b.copy(), c.copy());

            idx = v1Index * 8;
            vertexData[idx + 5] += faceNormals.x();
            vertexData[idx + 6] += faceNormals.y();
            vertexData[idx + 7] += faceNormals.z();

            idx = v2Index * 8;
            vertexData[idx + 5] += faceNormals.x();
            vertexData[idx + 6] += faceNormals.y();
            vertexData[idx + 7] += faceNormals.z();

            idx = v3Index * 8;
            vertexData[idx + 5] += faceNormals.x();
            vertexData[idx + 6] += faceNormals.y();
            vertexData[idx + 7] += faceNormals.z();
        }

        for (int i = 0; i < vertexData.length; i += 8) {

            Vec3f vertexNormal = new Vec3f(vertexData[i + 5], vertexData[i + 6], vertexData[i + 7]);
            vertexNormal.normalize();


            vertexData[i + 5] = vertexNormal.x();
            vertexData[i + 6] = vertexNormal.y();
            vertexData[i + 7] = vertexNormal.z();
        }
    }

    private Vec3f computeFaceNormals(Vec3f a, Vec3f b, Vec3f c) {
        return b.sub(a).cross(c.sub(a));
    }

    public Vec3f getPosition() {
        return position;
    }

    public int getSegment() {
        return segment;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
