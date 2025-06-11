package com.notearth.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Vec3f;

import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class Light {

    public Light(GL2 gl, Vec3f lightPosition) {

        float[] lightAmbientValue = {0.5f, 0.5f, 0.5f, 1.0f};
        // Diffuse light comes from a particular location. Diffuse's value in RGBA
        float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f};
        // Diffuse light location xyz (in front of the screen).
        float[] lightDiffusePosition = {lightPosition.x(), lightPosition.y(),
            lightPosition.z(), 1.0f};

        gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightAmbientValue, 0);
        gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, lightDiffuseValue, 0);
        gl.glLightfv(GL_LIGHT1, GL_POSITION, lightDiffusePosition, 0);
        gl.glEnable(GL_LIGHT1);
    }

    public void enable(GL2 gl) {
        gl.glEnable(GL_LIGHTING);
    }

    public void disable(GL2 gl) {
        gl.glDisable(GL_LIGHTING);
    }
}
