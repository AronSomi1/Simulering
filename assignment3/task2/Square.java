package task2;

import java.util.ArrayList;

public class Square {
    private double x_coord, y_coord;
    private ArrayList<Student> studentInSquare;

    public Square(double x_coord, double y_coord) {
        this.x_coord = x_coord;
        this.y_coord = y_coord;
        this.studentInSquare = new ArrayList<>();
    }

    public double getX_coord() {
        return x_coord;
    }

    public double getY_coord() {
        return y_coord;
    }

    public ArrayList<Student> getStudentInSquare() {
        return studentInSquare;
    }

    public void addStudent(Student student) {
        studentInSquare.add(student);
    }
}
