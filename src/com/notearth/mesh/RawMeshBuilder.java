package com.notearth.mesh;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class RawMeshBuilder {

    private final float[] vertexData;
    private final int[] indices;

    public RawMeshBuilder(float[] vertexData, int[] indices) {
        this.vertexData = vertexData;
        this.indices = indices;
    }

    public void render(GL2 gl) {
        gl.glBegin(GL.GL_TRIANGLES);
        for (int index : indices) {
            int idx = index * 3;
            float x = vertexData[idx];
            float y = vertexData[idx + 1];
            float z = vertexData[idx + 2];

            gl.glVertex3f(x, y, z);
        }
        gl.glEnd();
    }
}
