package com.notearth.mesh;

import java.io.*;
import java.net.URISyntaxException;

public class OBJLoader {

    public void loadOBJ(String filename) {
        File file = null;
        try {
            file = new File(getClass().getClassLoader().getResource("models/" + filename + ".obj").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;

            while ((st = br.readLine()) != null) {
                System.out.println(st);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
