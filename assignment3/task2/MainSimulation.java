
package task2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainSimulation extends Global {
    private static ArrayList<Double> data = new ArrayList<Double>();

    public static void main(String[] args) {
        int i = 10;
        double range = Double.MAX_VALUE;
        confidenceInterval ci = new confidenceInterval();

        // while (range > 500 || i > 0) {
        // runSimulation();
        // range = ci.calculate95ConfidenceInterval(data)[3];
        // System.out.println("Range: " + range);
        // i--;
        // }

        for (int j = 0; j < 300; j++) {
            runSimulation();
            if (j % 10 == 0) {
                System.out.println("Iteration: " + j);
            }
        }

        double[] confidenceInterval = ci.calculate95ConfidenceInterval(data);
        System.out
                .println("Mean time for interaction: " + confidenceInterval[2] + " \n with an upper bound of: "
                        + confidenceInterval[1] + "\n confidence interval: " + confidenceInterval[3]);
    }

    public static void runSimulation() {
        time = 0;
        Random rand = new Random();
        Floor f = new Floor();
        f.divideFloorIntoSquares();
        SignalList signalList = new SignalList();
        ArrayList<Student> students = new ArrayList<Student>();

        for (int i = 0; i < 20; i++) {
            Student s = new Student(f, rand.nextDouble() * 20, rand.nextDouble() * 20, 2,
                    signalList);
            students.add(s);
            signalList.SendSignal(MOVE, s, s, time + GLOBAL_STEP_SIZE);
            // signalList.SendSignal(MEASURE, s, s, time + 1);
            signalList.SendSignal(CALCULATEFINISHED, s, s, time + 100);

        }

        for (Student student : students) {
            for (Student student2 : students) {
                if (student != student2) {
                    student.addStudentToHashSet(student2);
                }
            }
        }

        while (f.nbrFinishedInteracringStudents < students.size()) {
            Signal actSignal = signalList.FetchSignal();
            time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);

        }

        data.add(time);
        for (Student student : students) {
            saveHashMapValuesToFile(student.getStudentsInInteraction(),
                    "assignment3/task2/data/data1.txt", true);
        }
        // students.stream().forEach(Student::measure);

        // System.out.println("All students have finished interacting at time: " +
        // time);

    }

    public static void saveHashMapValuesToFile(HashMap<?, ?> map, String filePath, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (Object value : map.values()) {
                writer.write(value.toString());
                writer.newLine();
            }
        } catch (IOException e) {
        }
    }
}
