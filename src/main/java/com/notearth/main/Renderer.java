package com.notearth.main;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.Vec3f;
import com.notearth.inputHandler.InputHandler;
import com.notearth.mesh.Entity;
import com.notearth.mesh.OBJLoader;
import com.notearth.mesh.Mesh;
import com.notearth.mesh.Plane;
import com.notearth.terrain.Terrain;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.GL2ES1.GL_RESCALE_NORMAL;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

public class Renderer implements GLEventListener {

    private long lastFrameTime = System.nanoTime();

    private GLU glu;
    private final InputHandler input;
    private final Camera camera;

    public Renderer(InputHandler input) {
        this.input = input;

        camera = new Camera(input, new Vec3f(0.0f, 0.0f, 10.0f), 1.5f);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.3f, 0.0f, 0.3f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL_RESCALE_NORMAL); // I enable this so scaling will also rescale the normal otherwise lighting will be off
        gl.glEnable(GL_CULL_FACE);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH);

        start(gl);
    }

    OBJLoader objLoader = new OBJLoader();

    // Entity
    Entity bunny, terrain1;

    public void start(GL2 gl) {

        // Light
        Light sun = new Light(gl, new Vec3f(100f, 100f, 100f));
        sun.enable(gl);

        // How to use Mesh alone to create object (I replicate openGL core profile mode design)
//        float[] vertexData = {
//                1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
//                1.0f, -1.0f, 0.0f, 1.0f, 0.0f,
//                -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
//                -1.0f, -1.0f, 0.0f, 0.0f, 0.0f
//        };
//
//        int[] indices = {
//                0, 2, 1,
//                1, 2, 3
//        };
//
//        square = new Mesh(vertexData, indices);

        Mesh bunnyMesh = objLoader.loadOBJ("stanford-bunny");
        bunny = new Entity(gl, bunnyMesh, "yellow.png", new Vec3f(0.0f, 0.0f, 0.0f));
        bunny.scale(8.0f);

        Plane plane1 = new Plane(new Vec3f(-5.0f, -2.0f, -3.0f), 10, 10, 15);
        terrain1 = new Terrain(gl, plane1, "Heightmap2.png", 5.0f, "rough-purple.png").getEntity();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    float angle = 0;
    float bunnyY = 0;
    float bunnyAngle = 0;

    @Override
    public void display(GLAutoDrawable drawable) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) / 1000000000.0f;

        lastFrameTime = currentTime;

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); // Reset model view matrix


        glu.gluLookAt(camera.position.x(), camera.position.y(), camera.position.z(),
                camera.position.x() + camera.front.x(),
                camera.position.y() + camera.front.y(),
                camera.position.z() + camera.front.z(),
                camera.up.x(), camera.up.y(), camera.up.z());


        camera.update();
        input(deltaTime);

//        bunny.rotateLocal(angle, new Vec3f(0.0f, 1.0f, 0.0f));
//        bunnyY = 0.2f * (float)Math.sin(Math.toRadians(bunnyAngle));
//        bunny.setPosition(bunny.position.x(), bunnyY, bunny.position.z());
//        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        terrain1.render();

//        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        // I use this instead of angle += 25f * deltaTime to avoid potential precision error and to make life easier in the future
//        angle = (angle + 25f * deltaTime) % 360;
//        bunnyAngle = (bunnyAngle + 60 * deltaTime) % 360;
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

    private void input(float deltaTime) {
        camera.input(deltaTime);
    }
}
