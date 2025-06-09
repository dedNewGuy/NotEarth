package com.notearth.main;

import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends GLJPanel {

    private static final int WIN_WIDTH = 800;
    private static final int WIN_HEIGHT = 600;
    private static final String WIN_TITLE = "Not Earth Dev 0.0.1";

    private static final int FPS = 60;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Renderer renderer = new Renderer();
                GLJPanel window = new Window(renderer);
                window.setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));

                FPSAnimator animator = new FPSAnimator(window, FPS);

                final JFrame frame = new JFrame(WIN_TITLE);
                frame.getContentPane().add(window);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (animator.isStarted()) animator.stop();
                            }
                        }.start();
                    }
                });
                frame.pack();
                frame.setVisible(true);
                animator.start();
            }
        });
    }

    public Window(Renderer renderer) {
        addGLEventListener(renderer);
    }
}
