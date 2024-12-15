package task2;

import javax.swing.*;

public class MainSimulation extends Global {

    public static void main(String[] args) {
        Floor f = new Floor();
        f.divideFloorIntoSquares();
        SignalList signalList = new SignalList();
        Student s1 = new Student(f, 10, 10, 1, signalList);
        Student s2 = new Student(f, 11, 10, 1, signalList);
        Student s = new Student(f, 10, 11, 1, signalList);

        // Set up the GUI
        SimulationGUI gui = new SimulationGUI(f);
        SwingUtilities.invokeLater(() -> gui.setVisible(true));

        // Start the simulation
        signalList.SendSignal(MOVE, s, s, time + GLOBAL_STEP_SIZE);
        signalList.SendSignal(MEASURE, s, s, time + 1);

        signalList.SendSignal(MOVE, s1, s1, time + GLOBAL_STEP_SIZE);
        signalList.SendSignal(MEASURE, s1, s1, time + 1);

        signalList.SendSignal(MOVE, s2, s2, time + GLOBAL_STEP_SIZE);
        signalList.SendSignal(MEASURE, s2, s2, time + 1);

        while (time < 100) {
            Signal actSignal = signalList.FetchSignal();
            time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);

            // Update the GUI
            gui.updateGrid();

            try {
                Thread.sleep(10); // Pause for visualization
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
