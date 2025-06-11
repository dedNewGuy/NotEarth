package com.notearth.main;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.Vec3f;
import com.jogamp.opengl.util.texture.Texture;
import com.notearth.inputHandler.InputHandler;
import com.notearth.mesh.OBJLoader;
import com.notearth.mesh.RawMeshBuilder;
import com.notearth.texture.TextureLoader;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;

public class Renderer implements GLEventListener {

    private long lastFrameTime = System.nanoTime();

    private GLU glu;
    private final InputHandler input;
    private final Camera camera;

    public Renderer(InputHandler input) {
        this.input = input;

        camera = new Camera(input, new Vec3f(0.0f, 0.0f, 6.0f), 3.0f);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.3f, 0.45f, 0.1f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL_CULL_FACE);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH);

        start(gl);
    }

    RawMeshBuilder square;
    RawMeshBuilder wally;
    RawMeshBuilder world;
    RawMeshBuilder bunny;
    OBJLoader objLoader = new OBJLoader();
    Texture nyanTexture;
    Texture wallyTexture;
    Texture worldmapTexture;
    Texture yellow;

    public void start(GL2 gl) {

        Light sun = new Light(gl, new Vec3f(100f, 100f, 100f));
        sun.enable(gl);

        wally = objLoader.loadOBJ("wally_uv");
        world = objLoader.loadOBJ("world_planet");
        bunny = objLoader.loadOBJ("stanford-bunny");

        nyanTexture = TextureLoader.loadTexture(gl, "nyan_cat.png");
        wallyTexture = TextureLoader.loadTexture(gl, "wally-color.png");
        worldmapTexture = TextureLoader.loadTexture(gl, "worldmap.jpg");
        yellow = TextureLoader.loadTexture(gl, "yellow.png");

        float[] vertexData = {
                1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                1.0f, -1.0f, 0.0f, 1.0f, 0.0f,
                -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                -1.0f, -1.0f, 0.0f, 0.0f, 0.0f
        };

        int[] indices = {
                0, 2, 1,
                1, 2, 3
        };

        square = new RawMeshBuilder(vertexData, indices);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    float angle = 0;

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

        gl.glRotatef(angle, 0.0f, 1.0f, 0.0f);
        gl.glScalef(8.0f, 8.0f, 8.0f);
        bunny.render(gl, yellow);
        angle += 0.5f;
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
