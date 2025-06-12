package com.notearth.mesh;

public class Mesh {

    protected final float[] vertexData;
    protected final int[] indices;

    public Mesh(float[] vertexData, int[] indices) {
        this.vertexData = vertexData;
        this.indices = indices;
    }

}
