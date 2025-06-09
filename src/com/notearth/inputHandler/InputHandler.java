package com.notearth.inputHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public boolean isWPressed = false;
    public boolean isAPressed = false;
    public boolean isSPressed = false;
    public boolean isDPressed = false;

    public boolean isSpacePressed = false;
    public boolean isShiftPressed = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            this.isWPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            this.isAPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            this.isSPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            this.isDPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.isSpacePressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            this.isShiftPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            this.isWPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            this.isAPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            this.isSPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            this.isDPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.isSpacePressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            this.isShiftPressed = false;
        }
    }
}
