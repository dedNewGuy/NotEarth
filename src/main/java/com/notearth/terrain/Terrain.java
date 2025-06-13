package com.notearth.terrain;

import com.jogamp.opengl.GL2;
import com.notearth.mesh.Entity;
import com.notearth.mesh.Plane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import static com.jogamp.opengl.GL.GL_MIRRORED_REPEAT;
import static com.jogamp.opengl.GL.GL_REPEAT;

public class Terrain {

    BufferedImage heightmap = null;
    Plane plane;
    Entity entity;

    float[] lumaNormalize;
    float peakAmplitude;

    public Terrain(GL2 gl, Plane plane, String heightmapFileName, float peakAmplitude, String textureName) {
        this.plane = plane;
        this.peakAmplitude = peakAmplitude;
        loadHeightmap(heightmapFileName);

        lumaNormalize = new float[heightmap.getWidth() * heightmap.getHeight()];

        generateTerrain();
        if (!plane.isSmooth()) {
            plane.calculateVertexNormalsNOTSMOOTH(plane.getMesh().getVertexData(), plane.getMesh().getIndices());
        } else {
            plane.calculateVertexNormals(plane.getMesh().getVertexData(), plane.getMesh().getIndices());
        }
        entity = new Entity(gl, plane.getMesh(), textureName, plane.getPosition(), GL_MIRRORED_REPEAT);
    }

    private int[] findMinMaxRGB() {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int y = 0; y < heightmap.getHeight(); ++y) {
            for (int x = 0; x < heightmap.getWidth(); ++x) {
                int color = heightmap.getRGB(x, y);

                int blue = color & 0xff;

                if (blue < min) {
                    min = blue;
                }

                if (blue > max) {
                    max = blue;
                }
            }
        }
        return new int[]{min, max};
    }

    private void generateTerrain() {
        // In Luma, R = G = B. So I picked B to simplify things
        int[] minMaxRGB = findMinMaxRGB();
        int rangeRGB = minMaxRGB[1] - minMaxRGB[0]; // Max - min
        int idx = 0;
        for (int y = 0; y < heightmap.getHeight(); ++y) {
            for (int x = 0; x < heightmap.getWidth(); ++x) {
                int color = heightmap.getRGB(x, y);
                int blue = color & 0xff;

                lumaNormalize[idx] = (float)(blue - minMaxRGB[0]) / rangeRGB;
                ++idx;
            }
        }

        float[] planeVertexData = plane.getMesh().getVertexData();

        System.out.println(planeVertexData.length + " " + lumaNormalize.length);
        for (int i = 0; i < planeVertexData.length; i += 8) {
            float terrainAmplitude = lumaNormalize[i / 8] * peakAmplitude;
            plane.getMesh().addToVertexData(i + 1, terrainAmplitude);
        }

    }

    // SIDE EFFECT!!! I KNOW! IGNORE IT!! NO ONE CARES!!
    private void loadHeightmap(String fileName) {

        try {

            File imageFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("heightmaps/" + fileName)).toURI());

            heightmap = new BufferedImage(plane.getSegment(), plane.getSegment(), BufferedImage.TYPE_INT_ARGB);

            heightmap = ImageIO.read(imageFile);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error" + e);
        }
    }

    public Entity getEntity() {
        return entity;
    }
}
