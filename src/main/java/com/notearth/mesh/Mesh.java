package com.notearth.mesh;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Mesh {

    protected final float[] vertexData;
    protected final int[] indices;

    public Mesh(float[] vertexData, int[] indices) {
        this.vertexData = vertexData;
        this.indices = indices;
    }

}
