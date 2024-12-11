package task1;

import java.io.*;
import java.util.Random;

public class ConfigurationFileGenerator {
    public static void main(String[] args) {
        String inputFile = "task1/config.txt"; // The
        // input
        // configuration
        // file
        try {
            // Read the input configuration
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            int n = 0;
            StringBuilder configContent = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                configContent.append(line).append("\n");
                if (line.startsWith("n =")) {
                    n = Integer.parseInt(line.split("=")[1].trim());
                }
            }
            reader.close();

            if (n == 0) {
                System.out.println("Invalid or missing 'n' value in the configuration file.");
                return;
            }

            // Generate the output file name and open the writer
            String outputFile = "task1/inputs/" + n + "config";
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            // Write the original configuration
            writer.write(configContent.toString());

            // Generate and write coordinates
            Random random = new Random();
            for (int i = 0; i < n; i++) {
                int x = random.nextInt(10000); // Random x coordinate (0-100)
                int y = random.nextInt(10000); // Random y coordinate (0-100)
                writer.write(i + "," + x + "," + y + "\n");
            }

            writer.close();
            System.out.println("New configuration file created: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
