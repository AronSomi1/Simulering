package task2;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationGUI extends JFrame {
    private final Floor floor;
    private final JPanel[][] gridPanels;

    public SimulationGUI(Floor floor) {
        this.floor = floor;
        this.gridPanels = new JPanel[20][20];
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Student Simulation");
        setSize(800, 800);
        setLayout(new GridLayout(20, 20));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create grid panels
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.setBackground(Color.WHITE); // Default background color
                gridPanels[i][j] = panel;
                add(panel);
            }
        }
    }

    public void updateGrid() {
        // Reset all panels to default
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                gridPanels[i][j].setBackground(Color.WHITE);
            }
        }

        // Update panels based on student positions
        Square[][] squares = floor.getSquares();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                List<Student> students = squares[i][j].getStudentInSquare();
                if (!students.isEmpty()) {
                    gridPanels[i][j].setBackground(Color.BLUE); // Color for occupied squares
                }
            }
        }

        repaint();
    }
}
