package task1;

import java.io.*;
import java.util.*;

public class ConfigFileReader {
    private int n;
    private int Tp;
    private int ts;
    private int r;
    private List<int[]> coordinates; // List to store the coordinates as {index, x, y}

    public ConfigFileReader(String filePath) throws IOException {
        coordinates = new ArrayList<>();
        parseFile(filePath);
    }

    private void parseFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // Skip empty lines
            if (line.isEmpty())
                continue;

            // Parse the initial parameters
            if (line.startsWith("n =")) {
                n = Integer.parseInt(line.split("=")[1].trim());
            } else if (line.startsWith("Tp =")) {
                Tp = Integer.parseInt(line.split("=")[1].trim());
            } else if (line.startsWith("ts =")) {
                ts = Integer.parseInt(line.split("=")[1].trim());
            } else if (line.startsWith("r =")) {
                r = Integer.parseInt(line.split("=")[1].trim());
            } else {
                // Parse coordinates
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int index = Integer.parseInt(parts[0].trim());
                    int x = Integer.parseInt(parts[1].trim());
                    int y = Integer.parseInt(parts[2].trim());
                    coordinates.add(new int[] { index, x, y });
                }
            }
        }
        reader.close();
    }

    // Getters for the parameters
    public int getN() {
        return n;
    }

    public int getTp() {
        return Tp;
    }

    public int getTs() {
        return ts;
    }

    public int getR() {
        return r;
    }

    public List<int[]> getCoordinates() {
        return coordinates;
    }

    // Debugging: Print out the parsed values
    public void printConfig() {
        System.out.println("n = " + n);
        System.out.println("Tp = " + Tp);
        System.out.println("ts = " + ts);
        System.out.println("r = " + r);
        System.out.println("Coordinates:");
        for (int[] coord : coordinates) {
            System.out.println(coord[0] + "," + coord[1] + "," + coord[2]);
        }
    }
}
