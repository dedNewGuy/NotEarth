package com.notearth.mesh;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Vec3f;
import com.jogamp.opengl.math.Vec4f;
import com.jogamp.opengl.util.texture.Texture;
import com.notearth.texture.TextureLoader;

public class Entity {

    Texture texture;
    Mesh mesh;
    GL2 gl;

    Vec3f position;
    Vec3f scale = new Vec3f(1.0f, 1.0f, 1.0f);
    Vec4f rotate = new Vec4f(0.0f, 0.0f, 0.0f, 0.0f);

    public Entity(GL2 gl, Mesh mesh, String textureFilename, Vec3f position) {
        this.gl = gl;
        this.mesh = mesh;
        texture = TextureLoader.loadTexture(gl, textureFilename);

        this.position = position.copy();
    }

    private void update() {
        gl.glPushMatrix();
        gl.glScalef(scale.x(), scale.y(), scale.z());
        gl.glTranslatef(position.x(), position.y(), position.z());
        gl.glRotatef(rotate.x(), rotate.y(), rotate.z(), rotate.w());
    }

    public void render() {
        texture.enable(gl);
        texture.bind(gl);

        update();
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
        gl.glPopMatrix();
    }
}
