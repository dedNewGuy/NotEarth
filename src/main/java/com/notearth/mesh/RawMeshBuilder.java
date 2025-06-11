package com.notearth.mesh;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;

public class RawMeshBuilder {

    private final float[] vertexData;
    private final int[] indices;

    public RawMeshBuilder(float[] vertexData, int[] indices) {
        this.vertexData = vertexData;
        this.indices = indices;
    }

    public void render(GL2 gl, Texture texture) {
        texture.enable(gl);
        texture.bind(gl);

        gl.glBegin(GL.GL_TRIANGLES);
        for (int index : indices) {
            int vertexIdx = index * 8;
            float x = vertexData[vertexIdx];
            float y = vertexData[vertexIdx + 1];
            float z = vertexData[vertexIdx + 2];

            float u = vertexData[vertexIdx + 3];
            float v = vertexData[vertexIdx + 4];

            float nx = vertexData[vertexIdx + 5];
            float ny = vertexData[vertexIdx + 6];
            float nz = vertexData[vertexIdx + 7];

            gl.glNormal3f(nx, ny, nz);
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(x, y, z);
        }
        gl.glEnd();
    }
}
