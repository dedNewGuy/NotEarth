package com.notearth.mesh;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

public class OBJLoader {

    public void loadOBJ(String filename) {
        File file = null;
        try {
            file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("models/" + filename + ".obj")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {
                if (st.startsWith("vt")) {
                    System.out.println("Texture Coordinate");
                } else if (st.startsWith("vn")) {
                    System.out.println("Normal Coordinate");
                } else if (st.startsWith("v")) {
                    System.out.println("Vertex Coordinate");
                } else if (st.startsWith("f")) {
                    System.out.println("Face");
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
