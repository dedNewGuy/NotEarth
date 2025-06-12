package com.notearth.mesh;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.notearth.texture.TextureLoader;

public class Entity {

    Texture texture;
    Mesh mesh;
    GL2 gl;

    public Entity(GL2 gl, Mesh mesh, String textureFilename) {
        this.gl = gl;
        this.mesh = mesh;
        texture = TextureLoader.loadTexture(gl, textureFilename);
    }

    public void render() {
        texture.enable(gl);
        texture.bind(gl);

        gl.glBegin(GL.GL_TRIANGLES);
        for (int index : mesh.indices) {
            int vertexIdx = index * 8;
            float x = mesh.vertexData[vertexIdx];
            float y = mesh.vertexData[vertexIdx + 1];
            float z = mesh.vertexData[vertexIdx + 2];

            float u = mesh.vertexData[vertexIdx + 3];
            float v = mesh.vertexData[vertexIdx + 4];

            float nx = mesh.vertexData[vertexIdx + 5];
            float ny = mesh.vertexData[vertexIdx + 6];
            float nz = mesh.vertexData[vertexIdx + 7];

            gl.glNormal3f(nx, ny, nz);
            gl.glTexCoord2f(u, v);
            gl.glVertex3f(x, y, z);
        }
        gl.glEnd();
    }
}
