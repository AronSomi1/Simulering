
package task2;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class MainSimulation extends Global {

    public static void main(String[] args) {
        Random rand = new Random();
        Floor f = new Floor();
        f.divideFloorIntoSquares();
        SignalList signalList = new SignalList();
        ArrayList<Student> students = new ArrayList<Student>();

        for (int i = 0; i < 20; i++) {
            Student s = new Student(f, rand.nextDouble() * 20, rand.nextDouble() * 20, VELOCITY, signalList);
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
        // Set up the GUI

        // Start the simulation

        while (f.nbrFinishedInteracringStudents < students.size()) {
            Signal actSignal = signalList.FetchSignal();
            time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);

        }

        students.stream().forEach(Student::measure);

        System.out.println("All students have finished interacting at time: " + time);

    }
}
