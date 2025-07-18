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
import java.util.Random;
import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.GL2ES1.GL_RESCALE_NORMAL;
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
    Random random = new Random();
    // Entity
    Entity terrain1;
    Entity water;
    Entity building1, building1Orbit;
    Entity[] tree1, tree1Leaves;                     //array bcus got multiple of the same tree
    Entity[] alienPatrolBottom, alienPatrolTop;     //same here

    Entity alien1;
    Entity[] fungi1;
    Entity bigtree;
    Entity tower;
    Entity alien2;

    Entity[] cloud;
    Entity moon;

    Skybox skybox;

    float[][] tree1Coords = {       //coordinates for tree 1's (4 trees)
            {24.0f, -2.0f, 22.0f},
            {-16f, -5.0f, 29f},
            {12.0f, -1.0f, -5.0f},
            {54.0f, -6.0f, 0f}
    };

    float[][] alienPatrolCoords = {     //coords for alien patrol (2 aliens)
            {20f, -5f, 20f},
            {5f, -10f, 5f}
    };

    float[][] cloudCoords = {       //coordinates for cloud
            {10.0f, 5.0f, 10.0f},
            {15.0f, 5.0f, 40.0f},
            {20.0f, 5.0f, 15.0f},
            {30.0f, 5.0f, 40.0f},
            {15.0f, 5.0f, 20.0f},
            {25.0f, 5.0f, 30.0f},
            {20.0f, 5.0f, -10.0f},
            {20.0f, 5.0f, -20.0f},
            {-25.0f, 5.0f, -40.0f},
            {-24.0f, 5.0f, -20.0f},
            {-10.0f, 5.0f, -10.0f},
            {25.0f, 5.0f, -40.0f},
            {-21.0f, 5.0f, 15.0f},
            {30.0f, 5.0f, -40.0f},
            {-45.0f, 5.0f, 30.0f},
            {40.0f, 5.0f, -30.0f},
            {-20.0f, 5.0f, 10.0f},
            {-20.0f, 5.0f, 20.0f},
            {25.0f, 5.0f, -30.0f},
            {-15.0f, 5.0f, 25.0f},
    };

    public void start(GL2 gl) {

        // Light
        System.out.println("Preparing sun...");
        Light sun = new Light(gl, new Vec3f(1000f, 1000f, 1000f));
        sun.enable(gl);

        //generate terrain
        System.out.println("Generating terrain...");
        Plane plane1 = new Plane(new Vec3f(-5.0f, -2.0f, -3.0f), 30, 30, 63, true);
        terrain1 = new Terrain(gl, plane1, "Heightmap.png", 6.0f, "weird_grass.png").getEntity();

        //generate water
        System.out.println("Generating water...");
        Plane waterPlane = new Plane(new Vec3f(-10.0f, -2.8f, -6.0f), 30, 30, 1, true);
        water = new Entity(gl, waterPlane.getMesh(), "blue.png", new Vec3f(0.0f, 0.0f, 0.0f));

        //generate trees
        System.out.println("Generating tree...");
        tree1 = new Entity[4];      //4 trees
        tree1Leaves = new Entity[4];
        Mesh tree1Mesh = objLoader.loadOBJ("Alien Tree Base");
        Mesh tree1LeavesMesh = objLoader.loadOBJ("Alien Tree Leaves");
        System.out.println("Generating tree loop start...");
        for (int i = 0; i < 4; i++) {
            generateTree1(gl, tree1Mesh, tree1LeavesMesh, tree1Coords, i);
            System.out.println("Tree " + i + "...done");
        }
        System.out.println("Generating tree loop end...");

        //generate skybox
        System.out.println("Generating skybox...");
        skybox = new Skybox(gl, camera, 500);

        //generate skyscraper
        System.out.println("Generating skyscraper...");
        Mesh building1Mesh = objLoader.loadOBJ("alien skyscraper 1");
        building1 = new Entity(gl, building1Mesh, "black_wall.png", new Vec3f(20.0f, -4.0f, 23.0f));
        building1.scale(0.7f);

        //generate alien 1
        System.out.println("Generating alien1...");
        Mesh alien1Mesh =  objLoader.loadOBJ("alien 1");
        alien1 = new Entity(gl, alien1Mesh, "alien texture.jpg", new Vec3f(-14.0f, 4.0f,19.0f));
        alien1.scale(0.25f);

        //generate alien 2
        System.out.println("Generating alien2...");
        Mesh alien2Mesh =  objLoader.loadOBJ("alien2");
        alien2 = new Entity(gl, alien2Mesh, "alien2 texture.jpg", new Vec3f(30.0f, -3.0f,35.0f));
        alien2.scale(0.25f);
        alien2.rotateLocal(180.0f, new Vec3f(0.0f, 4.0f,0.0f)); // setRotation(pitch, yaw, roll)

        //generate fungis
        System.out.println("Generating fungis...");
        fungi1 = new Entity[6];
        Mesh fungi1Mesh = objLoader.loadOBJ("fungi");
        System.out.println("Generating fungi loop start...");
        for (int i = 0; i < fungi1.length; i++) {
            generateFungi(gl, random, fungi1Mesh, i);
            System.out.println("Fungi " + i + "...done");
        }
        System.out.println("Generating fungi loop end...");

        //generate bigtree
        System.out.println("Generating big tree...");
        Mesh treeMesh =  objLoader.loadOBJ("tree");
        bigtree = new Entity(gl, treeMesh, "tree texture.jpg", new Vec3f(-38.0f, -6.0f,-30.0f));
        bigtree.scale(0.05f);

        //generate tower....
        System.out.println("Generating tower...");
        Mesh towerMesh =  objLoader.loadOBJ("tower1");
        tower = new Entity(gl, towerMesh, "blue gray bg.jpg", new Vec3f(-80.0f, -30.0f,185.0f));
        tower.scale(0.1f);

        //generate skyscraper orbit
        System.out.println("Generating skyscraper orbit...");
        Mesh building1OrbitMesh = objLoader.loadOBJ("alien skyscraper 2");
        building1Orbit = new Entity(gl, building1OrbitMesh, "neon_blue.png", new Vec3f(20.0f, 0f, 23.0f)); // setting y here is redundant since it'll be handled by the animation
        building1Orbit.scale(0.7f);

        //generate alien patrols
        System.out.println("Generating alien patrols");
        alienPatrolTop = new Entity[2];      //4 trees
        alienPatrolBottom = new Entity[2];
        Mesh alienPatrolTopMesh = objLoader.loadOBJ("alien patrol top");
        Mesh alienPatrolBottomMesh = objLoader.loadOBJ("alien patrol bottom");
        System.out.println("Generating alien patrols loop start...");
        for (int i = 0; i < 2; i++) {
            generateAlienPatrols(gl, alienPatrolBottomMesh, alienPatrolTopMesh, alienPatrolCoords, i);
            System.out.println("alien patrol  " + i + "...done");
        }
        System.out.println("Generating alien patrols loop end...");

        System.out.println("Starting rendering process...");

        //generate cloud
        cloud = new Entity[20];
        Mesh cloudMesh = objLoader.loadOBJ("cloud");
        for (int i = 0; i < 20; i++) {
            generateCloud(gl, cloudMesh, cloudCoords, i);
        }

        //generate moon
        Mesh moonMesh = objLoader.loadOBJ("moon");
        moon = new Entity(gl, moonMesh, "moon_texture.png", new Vec3f(10.0f, 20.0f, 10.0f));
        moon.scale(20.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }
    
    float buildingOrbitY = 7;
    float buildingOrbitAngle = 0;
    float alienPatrolAngle1 = 0;
    float alienPatrolAngle2 = 0;
    float alienPatrolX1 = 0;
    float alienPatrolZ1 = 0;
    float alienPatrolX2 = 0;
    float alienPatrolZ2 = 0;

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

        // TURN OFF DEPTH TESTING AND LIGHTING FOR SKYBOX ONLY
        gl.glDepthMask(false);
        gl.glDisable(GL_LIGHTING);
        gl.glDisable(GL_DEPTH_TEST);
        skybox.render();
        gl.glDepthMask(true);
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_DEPTH_TEST);
        // TURN IT BACK ON FOR OTHERS

        //building1 orbit animation
        building1Orbit.rotateLocal(buildingOrbitAngle, new Vec3f(0.0f, 1.0f, 0.0f)); //rotation
        buildingOrbitY = 0.8f * (float) Math.sin(Math.toRadians(buildingOrbitAngle)) + 5.0f;    //sin wave movement at y... add 5 at the end to shift position
        building1Orbit.setPosition(building1Orbit.position.x(), buildingOrbitY, building1Orbit.position.z());

        //alien patrol 1 animation(ellipse loop path)
        alienPatrolBottom[0].rotateLocal(alienPatrolAngle1, new Vec3f(0.0f, 1.0f, 0.0f));
        alienPatrolX1 = 5f * (float) Math.sin(Math.toRadians(alienPatrolAngle1)) + 12f;     //uses 1 sin wave & 1 cos wave
        alienPatrolZ1 = 5f * (float) Math.cos(Math.toRadians(alienPatrolAngle1)) + 15f;
        alienPatrolBottom[0].setPosition(alienPatrolX1, alienPatrolBottom[0].position.y(), alienPatrolZ1);
        alienPatrolTop[0].setPosition(alienPatrolX1, alienPatrolBottom[0].position.y(), alienPatrolZ1);

        //alien patrol 2 animation(infinity loop path)
        alienPatrolBottom[1].rotateLocal(alienPatrolAngle2, new Vec3f(0.0f, 1.0f, 0.0f));
        alienPatrolX2 = 5f * (float) Math.sin(Math.toRadians(alienPatrolAngle2)) + 28f;     //uses 2 sin waves, 1 of it has larger wavelength
        alienPatrolZ2 = 5f * (float) Math.sin(Math.toRadians(alienPatrolAngle2) * 2) + 58f;
        alienPatrolBottom[1].setPosition(alienPatrolX2, alienPatrolBottom[1].position.y(), alienPatrolZ2);
        alienPatrolTop[1].setPosition(alienPatrolX2, alienPatrolBottom[1].position.y(), alienPatrolZ2);

        //render all models here
        terrain1.render();
        water.render();
        for (int i = 0; i < 4; i++) {
            tree1[i].render();
            tree1Leaves[i].render();

            if (i < 2) {        // malas buat extra loop :)
                alienPatrolBottom[i].render();
                alienPatrolTop[i].render();
            }
        }
        building1.render();
        building1Orbit.render();
        alien1.render();
        for (int i = 0; i < fungi1.length; i++) {
            Vec3f pos = fungi1[i].position;
            float yOffset = 0.15f * (float) Math.sin(currentTime / 500000000.0 + i); // Add phase shift using i
            fungi1[i].setPosition(pos.x(), pos.y() + yOffset, pos.z());
            fungi1[i].render();
        }
        bigtree.render();
        tower.render();
        alien2.render();


        alienPatrolAngle1 = (alienPatrolAngle1 + 50f * deltaTime) % 360;
        alienPatrolAngle2 = (alienPatrolAngle2 + 50f * deltaTime) % 360;
        buildingOrbitAngle = (buildingOrbitAngle + 25f * deltaTime) % 360;

        moon.render();
        //render cloud
        for (int i = 0; i < 20; i++) {
            cloud[i].render();

        }
    }

    @Override
    public void reshape (GLAutoDrawable drawable,int x, int y, int width, int height){
        GL2 gl = drawable.getGL().getGL2();

        if (height == 0) height = 1;
        float aspect = (float) width / height;

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 1000.0);

        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    private void input (float deltaTime){
        camera.input(deltaTime);
    }

    private void generateCloud (GL2 gl, Mesh cloudMesh,float[][] cloudCoords, int i){
        cloud[i] = new Entity(gl, cloudMesh, "cloud_texture.png", new Vec3f(cloudCoords[i][0], cloudCoords[i][1], cloudCoords[i][2]));
        cloud[i].scale(10.0f);
    }

    //reusable method for generating multiple of the same tree
    private void generateTree1 (GL2 gl, Mesh tree1Mesh, Mesh tree1LeavesMesh,float[][] tree1Coords, int i){
        System.out.println("Creating tree " + i + " entity");
        tree1[i] = new Entity(gl, tree1Mesh, "alien_tree_bark.png", new Vec3f(tree1Coords[i][0], tree1Coords[i][1], tree1Coords[i][2]));
        System.out.println("Creating tree " + i + " leave entity");
        tree1Leaves[i] = new Entity(gl, tree1LeavesMesh, "neon_blue.png", new Vec3f(tree1Coords[i][0], tree1Coords[i][1], tree1Coords[i][2]));
        System.out.println("Scaling tree " + i + " entity");
        tree1[i].scale(0.3f);
        System.out.println("Scaling tree " + i + " leave entity");
        tree1Leaves[i].scale(0.3f);
    }

    //same for alien patrols (2 entities bcus separate animation)
    private void generateAlienPatrols (GL2 gl, Mesh alienPatrolBottomMesh, Mesh alienPatrolTopMesh,
                                       float[][] alienPatrolCoords, int i){
        System.out.println("Creating alien patrol " + i + " bottom entity");
        alienPatrolBottom[i] = new Entity(gl, alienPatrolBottomMesh, "white_blue_gradient.jpg", new Vec3f(alienPatrolCoords[i][0], alienPatrolCoords[i][1], alienPatrolCoords[i][2]));
        System.out.println("Creating alien patrol " + i + " top entity");
        alienPatrolTop[i] = new Entity(gl, alienPatrolTopMesh, "white_blue_gradient.jpg", new Vec3f(alienPatrolCoords[i][0], alienPatrolCoords[i][1], alienPatrolCoords[i][2]));
        System.out.println("Scaling alien patrol " + i + " bottom entity");
        alienPatrolBottom[i].scale(0.3f);
        System.out.println("Scaling alien patrol " + i + " top entity");
        alienPatrolTop[i].scale(0.3f);
    }

    private void generateFungi(GL2 gl, Random random, Mesh fungi1Mesh, int i){
        float x,y,z;
         x = random.nextFloat(64);
         y = random.nextFloat(15)- 2;
         z = random.nextFloat(64);
        System.out.println("Creating fungi " + i + " entity");
        fungi1[i] = new Entity(gl, fungi1Mesh, "fungi texture.jpg", new Vec3f(x,y,z));
        System.out.println("Scaling fungi " + i + " entity");
        fungi1[i].scale(0.25f);
    }
}
