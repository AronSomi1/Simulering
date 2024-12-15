package task2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Student extends Proc {
    private List<Student> currentlyInteractingWith = new ArrayList<Student>();
    private int nbrSocializings = 0;
    private double[] position;
    private Floor floor;
    private double velocity;
    private int[] direction;
    private int movedSquares = 0;
    private SignalList SignalList;
    private int squaresTomMoveInThisDirection = 10;
    public boolean interacting = false, finishedInteracting = false;
    private HashMap<Student, Double> studentsInInteraction = new HashMap<Student, Double>();

    public Student(Floor floor, double x_coord, double y_coord, double velocity, SignalList SignalList) {
        this.floor = floor;
        this.position = new double[] { x_coord, y_coord };
        this.velocity = velocity;
        this.direction = direction();
        this.SignalList = SignalList;
    }

    @Override
    public void TreatSignal(Signal x) {
        switch (x.signalType) {
            case MOVE: {
                move();
                if (!interacting) {
                    SignalList.SendSignal(MOVE, this, this, time + GLOBAL_STEP_SIZE);
                }
            }
                break;
            case START_SOCIALIZING: {
                if (currentlyInteractingWith.size() > 1) {
                    System.out.println("---------------------------------------------------");
                    System.out.println("Student" + this.toString() + " is interacting with: ");
                    currentlyInteractingWith.stream().forEach(student -> System.out.println(
                            "Student" + student.toString()));
                }
                startSocializing();
            }
                break;
            case STOP_SOCIALIZING: {
                stopSocializing();
                interacting = false;
                SignalList.SendSignal(MOVE, this, this, time + GLOBAL_STEP_SIZE);

            }
                break;
            case MEASURE: {
                measure();
                // System.out.println("Student at position: " + position[0] + ", " + position[1]
                // + " moved " + movedSquares
                // + " squares" + "in square: " + currentSquare(position[0], position[1])[0] +
                // ", "
                // + currentSquare(position[0], position[1])[1]);
                SignalList.SendSignal(MEASURE, this, this, time + 10000);
            }
            case CALCULATEFINISHED: {
                finishedInteracting();
                SignalList.SendSignal(CALCULATEFINISHED, this, this, time + 1);
            }
                break;
        }

    }

    public void addStudentToHashSet(Student student) {
        studentsInInteraction.put(student, 0.0);
    }

    public boolean finishedInteracting() {
        if (finishedInteracting) {
            return true;
        } else {
            finishedInteracting = studentsInInteraction.values().stream().allMatch(value -> value != 0.0);
            if (finishedInteracting)
                floor.studentFinishedInteracting();
            return finishedInteracting;
        }

    }

    public void move() {
        int[] oldSquare = currentSquare(position[0], position[1]);

        if (allowedToMove(position[0] + direction[0] * velocity * GLOBAL_STEP_SIZE,
                position[1] + direction[1] * velocity * GLOBAL_STEP_SIZE)) {
            position[0] += direction[0] * velocity * GLOBAL_STEP_SIZE;
            position[1] += direction[1] * velocity * GLOBAL_STEP_SIZE;
        }
        int[] nextSquare = currentSquare(position[0], position[1]);

        updatePosition(oldSquare, nextSquare);
        checkInteraction();
    }

    private void checkInteraction() {
        if (interacting)
            return;
        int square_x = currentSquare(position[0], position[1])[0];
        int square_y = currentSquare(position[0], position[1])[1];
        List<Student> studentsInSquare = floor.getSquares()[square_x][square_y].getStudentInSquare();
        if (studentsInSquare.size() == 2 && !interacting) {
            interactWithStudents(studentsInSquare);
        }
    }

    public void stopSocializing() {
        currentlyInteractingWith.clear();
        int[] oldSquare = currentSquare(position[0], position[1]);
        leaveSquare(oldSquare);
        if (allowedToMove(oldSquare[0] + direction[0], oldSquare[1] + direction[1])) {
            position[0] += direction[0];
            position[1] += direction[1];
        } else if (allowedToMove(position[0] - direction[0], position[1] - direction[1])) {
            position[0] -= direction[0];
            position[1] -= direction[1];
        } else if (allowedToMove(position[0] + direction[0], position[1] - direction[1])) {
            position[0] += direction[0];
            position[1] -= direction[1];
        } else if (allowedToMove(position[0] - direction[0], position[1] + direction[1])) {
            position[0] -= direction[0];
            position[1] += direction[1];
        }
        enterSquare(currentSquare(position[0], position[1]));

    }

    public boolean allowedToMove(double nextXPostion, double nextYPosition) {
        if (nextXPostion < 0 || nextXPostion >= 20 || nextYPosition < 0 || nextYPosition >= 20) {
            direction = direction();

            return false;
        } else if (movedSquares >= squaresTomMoveInThisDirection) {
            direction = direction();
            movedSquares = 0;
            return false;
        }
        return true;

    }

    public void updatePosition(int[] oldSquare, int[] nextSquare) {
        if (oldSquare[0] != nextSquare[0] || oldSquare[1] != nextSquare[1]) {
            leaveSquare(oldSquare);
            enterSquare(nextSquare);
            movedSquares++;

        }

    }

    private void interactWithStudents(List<Student> studentsInSquare) {
        if (studentsInSquare.size() != 2) {
            return; // Only proceed if exactly two students are in the square
        }
        nbrSocializings++;
        // System.out.println("Interaction detected in square: " +
        // currentSquare(position[0], position[1])[0] + ", "
        // + currentSquare(position[0], position[1])[1]);
        for (Student student : studentsInSquare) {
            student.interacting = true;
        }

        // Pause movement for all students in this square for tt time
        double interactionEndTime = time + INTERACTION_PAUSE_TIME;

        for (Student student : studentsInSquare) {
            student.interacting = true;
            if (student != this)
                currentlyInteractingWith.add(student);
            SignalList.SendSignal(START_SOCIALIZING, student, student, time);
            SignalList.SendSignal(STOP_SOCIALIZING, student, student, interactionEndTime);

        }
    }

    public void startSocializing() {
        int square_x = currentSquare(position[0], position[1])[0];
        int square_y = currentSquare(position[0], position[1])[1];
        List<Student> studentsInSquare = floor.getSquares()[square_x][square_y].getStudentInSquare();
        for (Student student : studentsInSquare) {
            if (student != this) {
                studentsInInteraction.put(student, studentsInInteraction.get(student) + INTERACTION_PAUSE_TIME);
            }
        }

    }

    public void measure() {
        System.out.println("---------------------------------------------------");
        System.out.println("Student" + this.toString() + " has interacted with: ");
        for (Student student : studentsInInteraction.keySet()) {
            System.out.println(
                    "Student" + student.toString() + " for " + studentsInInteraction.get(student)
                            + " seconds");
        }
    }

    public int[] currentSquare(double x_coord, double y_coord) {

        int square_x = (int) Math.floor(x_coord);
        int square_y = (int) Math.floor(y_coord);

        return new int[] { square_x, square_y };
    }

    public void leaveSquare(int[] oldSquare) {
        int square_x = oldSquare[0];
        int square_y = oldSquare[1];
        // Ensure indices are within bounds before accessing the floor matrix
        if (square_x >= 0 && square_x < 20 && square_y >= 0 && square_y < 20) {
            floor.getSquares()[square_x][square_y].getStudentInSquare().remove(this);
        }
    }

    public void enterSquare(int[] nextSquare) {
        int square_x = nextSquare[0];
        int square_y = nextSquare[1];
        // Ensure indices are within bounds before accessing the floor matrix
        if (square_x >= 0 && square_x < 20 && square_y >= 0 && square_y < 20) {
            floor.getSquares()[square_x][square_y].addStudent(this);
        }
    }

    public double[] getPosition() {
        return position;
    }

    public HashMap<Student, Double> getStudentsInInteraction() {
        return studentsInInteraction;
    }

    private int[] direction() {
        // One of nine different directions
        Random rand = new Random();
        squaresTomMoveInThisDirection = rand.nextInt(1, 10);
        switch (rand.nextInt(0, 8)) {
            case 0:
                return new int[] { 1, 0 };
            case 1:
                return new int[] { 1, 1 };
            case 2:
                return new int[] { 0, 1 };
            case 3:
                return new int[] { -1, 1 };
            case 4:
                return new int[] { -1, 0 };
            case 5:
                return new int[] { -1, -1 };
            case 6:
                return new int[] { 0, -1 };
            case 7:
                return new int[] { 1, -1 };
            default:
                return new int[] { -1, -1 };
        }

    }

}
