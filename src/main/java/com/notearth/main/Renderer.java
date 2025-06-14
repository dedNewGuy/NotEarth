package com.notearth.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.math.Vec3f;
import com.notearth.inputHandler.InputHandler;
import com.notearth.mesh.*;
import com.notearth.planeMesh.Terrain;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.GL2ES1.GL_RESCALE_NORMAL;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.*;

public class Renderer implements GLEventListener {

    private long lastFrameTime = System.nanoTime();

    private GLU glu;
    private final InputHandler input;
    private final Camera camera;

    public Renderer(InputHandler input) {
        this.input = input;

        camera = new Camera(input, new Vec3f(0.0f, 0.0f, 10.0f), 3.0f);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        glu = new GLU();
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glEnable(GL_RESCALE_NORMAL); // I enable this so scaling will also rescale the normal otherwise lighting will be off
        gl.glEnable(GL_TEXTURE_2D);
        gl.glEnable(GL_CULL_FACE);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_FLAT);

        start(gl);
    }

    OBJLoader objLoader = new OBJLoader();

    // Entity
    Entity terrain1;
    Entity water;
    Entity building1, building1Orbit;
    Entity[] tree1,tree1Leaves;     //array bcus got multiple of the same tree

    Skybox skybox;

    float[][] tree1Coords = {       //coordinates for afzan's trees
            {24.0f, -2.0f, 22.0f},
            {-16f, -5.0f, 29f},
            {12.0f,-1.0f,-5.0f},
            {54.0f,-6.0f,0f}
    };

    public void start(GL2 gl) {

        // Light
        Light sun = new Light(gl, new Vec3f(1000f, 1000f, 1000f));
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

        //generate terrain
        Plane plane1 = new Plane(new Vec3f(-5.0f, -2.0f, -3.0f), 30, 30, 63, true);
        terrain1 = new Terrain(gl, plane1, "Heightmap.png", 6.0f, "rough-purple.png").getEntity();

        //generate water
        Plane waterPlane = new Plane(new Vec3f(-10.0f, -2.8f, -6.0f), 30, 30, 1, true);
        water = new Entity(gl, waterPlane.getMesh(), "blue.png", new Vec3f(0.0f, 0.0f, 0.0f));

        //generate trees
        tree1 = new Entity[4];
        tree1Leaves = new Entity[4];
        Mesh tree1Mesh = objLoader.loadOBJ("Alien Tree Base");
        Mesh tree1LeavesMesh = objLoader.loadOBJ("Alien Tree Leaves");
        for (int i=0;i<4;i++) {
            generateTree1(gl,tree1Mesh, tree1LeavesMesh, tree1Coords, i);
        }

        //generate skybox
        skybox = new Skybox(gl, camera, 500);

        //generate skyscraper
        Mesh building1Mesh =  objLoader.loadOBJ("alien skyscraper 1");
        building1 = new Entity(gl, building1Mesh, "black_wall.jpg", new Vec3f(20.0f, -4.0f,23.0f));
        building1.scale(0.7f);

        //generate skyscraper orbit
        Mesh building1OrbitMesh  = objLoader.loadOBJ("alien skyscraper 2");
        building1Orbit = new Entity(gl, building1OrbitMesh, "neon_blue.jpg", new Vec3f(20.0f, 7f,23.0f));
        building1Orbit.scale(0.7f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    float angle = 0;
    float buildingOrbitY = 7;
    float buildingOrbitAngle = 0;

    @Override
    public void display(GLAutoDrawable drawable) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) / 1000000000.0f;

        lastFrameTime = currentTime;

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity(); // Reset model view matrix


        glu.gluLookAt(camera.position.x(), camera.position.y(), camera.position.z(),
                camera.position.x() + camera.front.x(),
                camera.position.y() + camera.front.y(),
                camera.position.z() + camera.front.z(),
                camera.up.x(), camera.up.y(), camera.up.z());


        camera.update();
        input(deltaTime);

        gl.glDepthMask(false);
        gl.glDisable(GL_LIGHTING);
        gl.glDisable(GL_DEPTH_TEST);
        skybox.render();
        gl.glDepthMask(true);
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_DEPTH_TEST);

        //animations
//        bunny.rotateLocal(angle, new Vec3f(0.0f, 1.0f, 0.0f));
//        bunnyY = 0.2f * (float)Math.sin(Math.toRadians(bunnyAngle));
//        bunny.setPosition(bunny.position.x(), bunnyY, bunny.position.z());
//        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        building1Orbit.rotateLocal(angle, new Vec3f(0.0f, 7.0f, 0.0f)); //rotation
        buildingOrbitY = 0.7f * (float)Math.sin(Math.toRadians(buildingOrbitAngle));    //sin wave movement at y
        building1Orbit.setPosition(building1Orbit.position.x(), buildingOrbitY, building1Orbit.position.z());
        angle = (angle + 25f * deltaTime) % 360;
        buildingOrbitAngle = (buildingOrbitAngle + 60 * deltaTime) % 360;
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);


        //render all models here
        terrain1.render();
        water.render();
        for (int i = 0; i<4; i++){
            tree1[i].render();
            tree1Leaves[i].render();
        }
        building1.render();
        building1Orbit.render();

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
        glu.gluPerspective(45.0, aspect, 0.1, 1000.0);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    private void input(float deltaTime) {
        camera.input(deltaTime);
    }

    //reusable method for generating multiple of the same tree
    private void generateTree1(GL2 gl, Mesh tree1Mesh, Mesh tree1LeavesMesh, float[][] tree1Coords, int i){
        tree1[i] = new Entity(gl, tree1Mesh, "alien_tree_bark.jpg", new Vec3f(tree1Coords[i][0], tree1Coords[i][1], tree1Coords[i][2]));
        tree1Leaves[i] = new Entity(gl, tree1LeavesMesh, "neon_blue.jpg", new Vec3f(tree1Coords[i][0], tree1Coords[i][1], tree1Coords[i][2]));
        tree1[i].scale(0.3f);
        tree1Leaves[i].scale(0.3f);
    }
}
