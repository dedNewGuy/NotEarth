package com.notearth.mesh;

import com.notearth.data.NormalCoordinate;
import com.notearth.data.TextureCoordinate;
import com.notearth.data.Vertex;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class OBJLoader {


    public Mesh loadOBJ(String filename) {
        File file = null;
        try {
            file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("models/" + filename + ".obj")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<Vertex> vertexList = new ArrayList<>();
        List<TextureCoordinate> textureCoordinates = new ArrayList<>();
        List<NormalCoordinate> normalCoordinates = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        Map<Integer, float[]> vertexData = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {
                if (st.startsWith("vt")) {
                    String[] texCoords = st.split(" ");
                    float u = Float.parseFloat(String.valueOf(texCoords[1]));
                    float v = Float.parseFloat(String.valueOf(texCoords[2]));
                    TextureCoordinate coord = new TextureCoordinate(u, v);
                    textureCoordinates.add(coord);
                } else if (st.startsWith("vn")) {
                    String[] normals = st.split(" ");
                    float nx = Float.parseFloat(String.valueOf(normals[1]));
                    float ny = Float.parseFloat(String.valueOf(normals[2]));
                    float nz = Float.parseFloat(String.valueOf(normals[3]));
                    NormalCoordinate normal = new NormalCoordinate(nx, ny, nz);
                    normalCoordinates.add(normal);
                } else if (st.startsWith("v")) {
                    String[] vertices = st.split(" ");
                    float x = Float.parseFloat(String.valueOf(vertices[1]));
                    float y = Float.parseFloat(String.valueOf(vertices[2]));
                    float z = Float.parseFloat(String.valueOf(vertices[3]));
                    Vertex vertex = new Vertex(x, y, z);
                    vertexList.add(vertex);
                } else if (st.startsWith("f")) {
                    break;
                }
            }

            while (st != null) {
                if (st.startsWith("f")) {
                    String[] faces = st.split(" ");

                    String[] first = faces[1].split("/");
                    String[] second = faces[2].split("/");
                    String[] third = faces[3].split("/");

                    processVertex(vertexList, textureCoordinates, normalCoordinates,
                            indices, vertexData, first, second, third);

                }

                st = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("ERROR: File name " + filename + " can't be found");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to read file");
        }

        int[] indicesArray = indices.stream().mapToInt(Integer::intValue).toArray();
        float[] vertexDataArray = new float[indices.size() * 2];

        for (int idx: vertexData.keySet()) {
            int index = idx * 8;
            float[] data = vertexData.get(idx);
            // Vertices
            vertexDataArray[index] = data[0];
            vertexDataArray[index + 1] = data[1];
            vertexDataArray[index + 2] = data[2];

            // Texture Coordinates
            vertexDataArray[index + 3] = data[3];
            vertexDataArray[index + 4] = data[4];

            // Normal Coordinates
            vertexDataArray[index + 5] = data[5];
            vertexDataArray[index + 6] = data[6];
            vertexDataArray[index + 7] = data[7];
        }

        return new Mesh(vertexDataArray, indicesArray);
    }

    private void processVertex(List<Vertex> vertexList, List<TextureCoordinate> textureCoordinates,
                             List<NormalCoordinate> normalCoordinates, List<Integer> indices,
                               Map<Integer, float[]> vertexData,
                             String[] first, String[] second, String[] third) {
        int firstIndex = Integer.parseInt(String.valueOf(first[0])) - 1;
        int secondIndex = Integer.parseInt(String.valueOf(second[0])) - 1;
        int thirdIndex = Integer.parseInt(String.valueOf(third[0])) - 1;

        int firstTexIndex = Integer.parseInt(String.valueOf(first[1])) - 1;
        int secondTexIndex = Integer.parseInt(String.valueOf(second[1])) - 1;
        int thirdTexIndex = Integer.parseInt(String.valueOf(third[1])) - 1;

        int firstNormsIndex = Integer.parseInt(String.valueOf(first[2])) - 1;
        int secondNormsIndex = Integer.parseInt(String.valueOf(second[2])) - 1;
        int thirdNormsIndex = Integer.parseInt(String.valueOf(third[2])) - 1;

        indices.add(firstIndex);
        indices.add(secondIndex);
        indices.add(thirdIndex);

        Vertex firstVertex = vertexList.get(firstIndex);
        TextureCoordinate firstTex = textureCoordinates.get(firstTexIndex);
        NormalCoordinate firstNormal = normalCoordinates.get(firstNormsIndex);
        float[] firstVertexData = {firstVertex.x, firstVertex.y, firstVertex.z,
                firstTex.u, firstTex.v,
                firstNormal.nx, firstNormal.ny, firstNormal.nz
        };
        vertexData.put(firstIndex, firstVertexData);

        Vertex secondVertex = vertexList.get(secondIndex);
        TextureCoordinate secondTex = textureCoordinates.get(secondTexIndex);
        NormalCoordinate secondNormal = normalCoordinates.get(secondNormsIndex);
        float[] secondVertexData = {secondVertex.x, secondVertex.y, secondVertex.z,
                secondTex.u, secondTex.v,
                secondNormal.nx, secondNormal.ny, secondNormal.nz
        };
        vertexData.put(secondIndex, secondVertexData);

        Vertex thirdVertex = vertexList.get(thirdIndex);
        TextureCoordinate thirdTex = textureCoordinates.get(thirdTexIndex);
        NormalCoordinate thirdNormal = normalCoordinates.get(thirdNormsIndex);
        float[] thirdVertexData = {thirdVertex.x, thirdVertex.y, thirdVertex.z,
                thirdTex.u, thirdTex.v,
                thirdNormal.nx, thirdNormal.ny, thirdNormal.nz
        };
        vertexData.put(thirdIndex, thirdVertexData);

    }

}
