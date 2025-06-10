package com.notearth.mesh;

import com.notearth.data.TextureCoordinate;
import com.notearth.data.Vertex;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class OBJLoader {


    public RawMeshBuilder loadOBJ(String filename) {
        File file = null;
        try {
            file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("models/" + filename + ".obj")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<Vertex> vertexList = new ArrayList<>();
        List<TextureCoordinate> textureCoordinates = new ArrayList<>();
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

                    processVertex(vertexList, textureCoordinates, indices, vertexData, first, second, third);

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
            int index = idx * 5;
            float[] data = vertexData.get(idx);
            vertexDataArray[index] = data[0];
            vertexDataArray[index + 1] = data[1];
            vertexDataArray[index + 2] = data[2];
            vertexDataArray[index + 3] = data[3];
            vertexDataArray[index + 4] = data[4];
        }

        return new RawMeshBuilder(vertexDataArray, indicesArray);
    }

    private void processVertex(List<Vertex> vertexList, List<TextureCoordinate> textureCoordinates,
                             List<Integer> indices, Map<Integer, float[]> vertexData,
                             String[] first, String[] second, String[] third) {
        int firstIndex = Integer.parseInt(String.valueOf(first[0])) - 1;
        int secondIndex = Integer.parseInt(String.valueOf(second[0])) - 1;
        int thirdIndex = Integer.parseInt(String.valueOf(third[0])) - 1;

        int firstTexIndex = Integer.parseInt(String.valueOf(first[1])) - 1;
        int secondTexIndex = Integer.parseInt(String.valueOf(second[1])) - 1;
        int thirdTexIndex = Integer.parseInt(String.valueOf(third[1])) - 1;

        indices.add(firstIndex);
        indices.add(secondIndex);
        indices.add(thirdIndex);

        Vertex firstVertex = vertexList.get(firstIndex);
        TextureCoordinate firstTex = textureCoordinates.get(firstTexIndex);
        float[] firstVertexData = {firstVertex.x, firstVertex.y, firstVertex.z, firstTex.u, firstTex.v};
        vertexData.put(firstIndex, firstVertexData);

        Vertex secondVertex = vertexList.get(secondIndex);
        TextureCoordinate secondTex = textureCoordinates.get(secondTexIndex);
        float[] secondVertexData = {secondVertex.x, secondVertex.y, secondVertex.z, secondTex.u, secondTex.v};
        vertexData.put(secondIndex, secondVertexData);

        Vertex thirdVertex = vertexList.get(thirdIndex);
        TextureCoordinate thirdTex = textureCoordinates.get(thirdTexIndex);
        float[] thirdVertexData = {thirdVertex.x, thirdVertex.y, thirdVertex.z, thirdTex.u, thirdTex.v};
        vertexData.put(thirdIndex, thirdVertexData);

    }

}
