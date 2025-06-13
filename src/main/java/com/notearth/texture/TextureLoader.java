package com.notearth.texture;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.IOException;
import java.util.Objects;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.*;

public class TextureLoader {

    public static Texture loadTexture(GL2 gl, String textureFileName) {
        Texture texture = null;
        try {
            texture = TextureIO.newTexture(
                    Objects.requireNonNull(TextureLoader.class.getClassLoader().getResource("textures/" + textureFileName)),
                    true, textureFileName.split("\\.")[1]
            );

            // Use linear filter for texture if image is larger than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            // Use linear filter for texture if image is smaller than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        } catch(IOException e) {
            System.err.println("Failed to load Image: " + textureFileName);
            return null;
        }

        return texture;
    }

    public static Texture loadCubemapTexture(GL2 gl, String textureFileName) {
        Texture texture = null;
        try {
            texture = TextureIO.newTexture(
                    Objects.requireNonNull(TextureLoader.class.getClassLoader().getResource("cubemaps/" + textureFileName)),
                    true, textureFileName.split("\\.")[1]
            );

            // Use linear filter for texture if image is larger than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            // Use linear filter for texture if image is smaller than the original texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        } catch(IOException e) {
            System.err.println("Failed to load Image: " + textureFileName);
            return null;
        }

        return texture;
    }

}
