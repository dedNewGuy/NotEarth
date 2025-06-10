package com.notearth.main;

import com.jogamp.opengl.math.Vec3f;
import com.jogamp.opengl.math.VectorUtil;
import com.notearth.inputHandler.InputHandler;

public class Camera {
    private final InputHandler inputHandler;

    public Vec3f position;

    public Vec3f target;
    public Vec3f direction;

    public Vec3f right;
    public Vec3f up;

    public Vec3f front;

    public float speed = 0.05f;

    public Camera(InputHandler input, Vec3f position) {
        this.inputHandler = input;
        this.position = position.copy();

        target = new Vec3f(0.0f, 0.0f, 0.0f);
        direction = position.copy().sub(target).normalize();

        Vec3f worldUp = new Vec3f(0.0f, 1.0f, 0.0f);
        right = worldUp.cross(direction).normalize();

        up = direction.cross(right).normalize();

        front = new Vec3f(0.0f, 0.0f, -1.0f);
    }

    public void input() {
        if (inputHandler.isWPressed) {
            position.add(front.mul(speed));
        }
        if (inputHandler.isSPressed) {
            position.sub(front.mul(speed));
        }
        if (inputHandler.isAPressed) {
            position.sub(front.cross(up).normalize().mul(speed));
        }
        if (inputHandler.isDPressed) {
            position.add(front.cross(up).normalize().mul(speed));
        }
    }

    public void update() {
    }
}
