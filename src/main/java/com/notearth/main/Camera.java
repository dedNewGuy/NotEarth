package com.notearth.main;

import com.jogamp.opengl.math.Vec3f;
import com.notearth.inputHandler.InputHandler;

public class Camera {
    private final InputHandler inputHandler;

    public Vec3f position;

    public Vec3f target;
    public Vec3f direction;

    public Vec3f right;
    public Vec3f up;

    public Vec3f front;

    private float camSpeed;

    private double yaw = -90.0, pitch = 0;

    public Camera(InputHandler input, Vec3f position, float camSpeed) {
        this.inputHandler = input;
        this.position = position.copy();
        this.camSpeed = camSpeed;

        target = new Vec3f(0.0f, 0.0f, 0.0f);
        direction = position.copy().sub(target).normalize();

        Vec3f worldUp = new Vec3f(0.0f, 1.0f, 0.0f);
        right = worldUp.cross(direction).normalize();

        up = direction.cross(right).normalize();

        front = new Vec3f(0.0f, 0.0f, -1.0f);
    }

    public void input(float deltaTime) {
        float speed = camSpeed * deltaTime;
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
        if (inputHandler.isShiftPressed) {
            position.sub(new Vec3f(0.0f, speed, 0.0f));
        }
        if (inputHandler.isSpacePressed) {
            position.add(new Vec3f(0.0f, speed, 0.0f));
        }

        if (inputHandler.isLeftPressed) {
            yaw -= camSpeed;
        }
        if (inputHandler.isRightPressed) {
            yaw += camSpeed;
        }
        if (inputHandler.isUpPressed) {
            pitch += camSpeed;
        }
        if (inputHandler.isDownPressed) {
            pitch -= camSpeed;
        }
    }

    public void update() {
        direction.setX((float) Math.cos(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch)));
        direction.setY((float) Math.sin(Math.toRadians(pitch)));
        direction.setZ((float) Math.sin(Math.toRadians(yaw)) * (float)Math.cos(Math.toRadians(pitch)));
        front = direction.copy().normalize();
    }
}
