package com.notearth.mesh;

public class Mesh {

    protected final float[] vertexData; // v1: x, y, z, u, v, nx, ny, nz, v2: x
    protected final int[] indices;

    public Mesh(float[] vertexData, int[] indices) {
        this.vertexData = vertexData;
        this.indices = indices;
    }

    public void addToVertexData(int index, float value) {
        vertexData[index] += value;
    }

    public float[] getVertexData() {
        return vertexData;
    }

    public int[] getIndices() {
        return indices;
    }
}
