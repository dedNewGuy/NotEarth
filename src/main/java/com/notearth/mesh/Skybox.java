package com.notearth.mesh;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.notearth.main.Camera;
import com.notearth.texture.TextureLoader;

import static com.jogamp.opengl.GL2ES3.GL_QUADS;

public class Skybox {

    private final Texture[] textures;
    private float x = 0, y = 0, z = 0;
    private final float width, height, length;

    private final Camera camera;
    private final GL2 gl;

    public Skybox(GL2 gl, Camera camera, float size) {
        this.gl = gl;
        this.camera = camera;
        this.width = this.height = this.length = size;
        this.textures = new Texture[6];
        this.x = this.x - this.width / 2;
        this.y = this.y - this.height / 2;
        this.z = this.z - this.length / 2;
        loadCubeMapTexture();
    }

    private void loadCubeMapTexture() {
        textures[0] = TextureLoader.loadCubemapTexture(gl, "back.png");
        textures[1] = TextureLoader.loadCubemapTexture(gl, "bottom.png");
        textures[2] = TextureLoader.loadCubemapTexture(gl, "front.png");
        textures[3] = TextureLoader.loadCubemapTexture(gl, "left.png");
        textures[4] = TextureLoader.loadCubemapTexture(gl, "right.png");
        textures[5] = TextureLoader.loadCubemapTexture(gl, "top.png");
    }

    public void render() {

        gl.glPushMatrix();

        gl.glTranslatef(camera.position.x(), camera.position.y(), camera.position.z());

        // BACK FACE
        textures[0].enable(gl);
        textures[0].bind(gl);
        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x + width, y,  z);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(x + width, y + height, z);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(x, y + height, z);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x, y,  z);
        gl.glEnd();
        textures[0].disable(gl);

        // FRONT FACE
        textures[2].enable(gl);
        textures[2].bind(gl);
        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x, y,  z + length);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(x, y + height, z + length);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(x + width, y + height, z + length);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x + width, y,  z + length);
        gl.glEnd();
        textures[2].disable(gl);

        // BOTTOM FACE
        textures[1].enable(gl);
        textures[1].bind(gl);
        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x, y,  z);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(x, y,  z + length);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(x + width, y,  z + length);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x + width, y,  z);
        gl.glEnd();
        textures[1].disable(gl);

        // TOP FACE
        textures[5].enable(gl);
        textures[5].bind(gl);
        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(x + width, y + height, z);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x + width, y + height, z + length);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x, y + height, z + length);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(x, y + height, z);
        gl.glEnd();
        textures[5].disable(gl);

        // RIGHT FACE
        textures[4].enable(gl);
        textures[4].bind(gl);
        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(x, y + height, z);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(x, y + height, z + length);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x, y,  z + length);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x, y,  z);
        gl.glEnd();
        textures[4].disable(gl);

        // LEFT FACE
        textures[3].enable(gl);
        textures[3].bind(gl);
        gl.glBegin(GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x + width, y,  z);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x + width, y,  z + length);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(x + width, y + height, z + length);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(x + width, y + height, z);
        gl.glEnd();
        textures[3].disable(gl);

        gl.glPopMatrix();
    }
}
