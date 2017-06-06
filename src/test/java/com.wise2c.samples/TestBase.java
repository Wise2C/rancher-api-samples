package com.wise2c.samples;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class TestBase {

    protected File getResources(String fileName) throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new Exception("Reources " + fileName + " Not Found");
        }

        File file = new File(resource.getFile());
        if (!file.exists()) {
            throw new Exception("Reources " + fileName + " Not Found");
        }

        return file;

    }

    protected String getFile(String fileName) throws Exception {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        File file = getResources(fileName);

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }

}
