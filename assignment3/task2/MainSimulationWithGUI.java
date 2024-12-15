package task2;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class MainSimulationWithGUI extends Global {

    public static void main(String[] args) {
        Random rand = new Random();
        Floor f = new Floor();
        f.divideFloorIntoSquares();
        SignalList signalList = new SignalList();
        ArrayList<Student> students = new ArrayList<Student>();

        for (int i = 0; i < 10; i++) {
            Student s = new Student(f, rand.nextDouble() * 20, rand.nextDouble() * 20, VELOCITY, signalList);
            students.add(s);
            signalList.SendSignal(MOVE, s, s, time + GLOBAL_STEP_SIZE);
            signalList.SendSignal(MEASURE, s, s, time + 1);
        }

        for (Student student : students) {
            for (Student student2 : students) {
                if (student != student2) {
                    student.addStudentToHashSet(student2);
                }
            }
        }
        // Set up the GUI
        SimulationGUI gui = new SimulationGUI(f);
        SwingUtilities.invokeLater(() -> gui.setVisible(true));

        // Start the simulation

        while (f.nbrFinishedInteracringStudents < students.size()) {
            Signal actSignal = signalList.FetchSignal();
            time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);

            // Update the GUI
            gui.updateGrid();

            try {
                Thread.sleep(1); // Pause for visualization
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All students have finished interacting at time: " + time);
    }
}
