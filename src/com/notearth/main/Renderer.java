package com.notearth.main;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.notearth.inputHandler.InputHandler;

public class Renderer implements GLEventListener {

    private static class Camera {
        public float x, y, z;
        public float speed;
        public Camera(float x, float y, float z, float speed) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.speed = speed;
        }
    }

    private GLU glu;
    private final InputHandler inputHandler;
    private Camera camera;

    private float angle = 0;

    public Renderer(InputHandler input) {
        this.inputHandler = input;
        camera = new Camera(0.0f, 0.0f, -6.0f, 0.2f);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.3f, 0.45f, 0.1f, 0.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity(); // Reset model view matrix

        input();

        gl.glTranslatef(camera.x, camera.y, camera.z);
        gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glEnd();

        angle += 1f;

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        if (height == 0) height = 1;
        float aspect = (float)width/height;

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    private void input() {
        if (this.inputHandler.isWPressed) {
            camera.z += camera.speed;
        }
        if (this.inputHandler.isAPressed) {
            camera.x += camera.speed;
        }
        if (this.inputHandler.isSPressed) {
            camera.z += -camera.speed;
        }
        if (this.inputHandler.isDPressed) {
            camera.x += -camera.speed;
        }
        if (this.inputHandler.isSpacePressed) {
            camera.y += -camera.speed;
        }
        if (this.inputHandler.isShiftPressed) {
            camera.y += camera.speed;
        }
    }
}
