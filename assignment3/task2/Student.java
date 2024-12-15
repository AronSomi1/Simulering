package task2;

import java.util.List;
import java.util.Random;

public class Student extends Proc {
    private double[] position;
    private Floor floor;
    private double velocity;
    private int[] direction;
    private int movedSquares = 0;
    private SignalList SignalList;
    private int squaresTomMoveInThisDirection = 10;
    public boolean interacting = false;

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
                System.out.println("Student at position: " + position[0] + ", " + position[1] + " started socializing");
            }
                break;
            case STOP_SOCIALIZING: {
                System.out.println("Student at position: " + position[0] + ", " + position[1] + " stopped socializing");
                stopSocializing();
                interacting = false;
                SignalList.SendSignal(MOVE, this, this, time + GLOBAL_STEP_SIZE);

            }
                break;
            case MEASURE: {
                // System.out.println("Student at position: " + position[0] + ", " + position[1]
                // + " moved " + movedSquares
                // + " squares" + "in square: " + currentSquare(position[0], position[1])[0] +
                // ", "
                // + currentSquare(position[0], position[1])[1]);
                SignalList.SendSignal(MEASURE, this, this, time + 1);
            }
                break;
        }

    }

    public void move() {
        int[] oldSquare = currentSquare(position[0], position[1]);

        System.out.println();
        if (allowedToMove(position[0] + direction[0] * velocity * GLOBAL_STEP_SIZE,
                position[1] + direction[1] * velocity * GLOBAL_STEP_SIZE)) {
            position[0] += direction[0] * velocity * GLOBAL_STEP_SIZE;
            position[1] += direction[1] * velocity * GLOBAL_STEP_SIZE;
        }
        int[] nextSquare = currentSquare(position[0], position[1]);

        updatePosition(oldSquare, nextSquare);
    }

    public void stopSocializing() {
        int[] oldSquare = currentSquare(position[0], position[1]);
        leaveSquare(oldSquare);
        if (allowedToMove(oldSquare[0] + direction[0], oldSquare[1] + direction[1])) {
            position[0] += direction[0];
            position[1] += direction[1];
        } else {
            position[0] -= direction[0];
            position[1] -= direction[1];
        }
        enterSquare(currentSquare(position[0], position[1]));

    }

    public boolean allowedToMove(double nextXPostion, double nextYPosition) {
        System.out.println("Trying to move to: " + nextXPostion + ", " + nextYPosition);
        if (nextXPostion < 0 || nextXPostion >= 20 || nextYPosition < 0 || nextYPosition >= 20) {
            direction = direction();
            System.out.println("nextXPostion: " + nextXPostion + " nextYPosition: " + nextYPosition);
            System.out.println("Walking into wall, changing direction");
            return false;
        } else if (movedSquares >= squaresTomMoveInThisDirection) {
            direction = direction();
            movedSquares = 0;
            System.out.println("Moved " + movedSquares + " squares, changing direction");
            return false;
        }
        System.out.println("Allowed to move");
        return true;

    }

    public void updatePosition(int[] oldSquare, int[] nextSquare) {
        if (oldSquare[0] != nextSquare[0] || oldSquare[1] != nextSquare[1]) {
            leaveSquare(oldSquare);
            enterSquare(nextSquare);

            List<Student> studentsInSquare = floor.getSquares()[nextSquare[0]][nextSquare[1]].getStudentInSquare();
            if (studentsInSquare.size() == 2) {
                interactWithStudents(studentsInSquare);
            }
            movedSquares++;
        }
    }

    private void interactWithStudents(List<Student> studentsInSquare) {
        System.out.println("Interaction detected in square: " + currentSquare(position[0], position[1])[0] + ", "
                + currentSquare(position[0], position[1])[1]);

        // Pause movement for all students in this square for tt time
        double interactionEndTime = time + INTERACTION_PAUSE_TIME;

        for (Student student : studentsInSquare) {
            student.interacting = true;

            SignalList.SendSignal(START_SOCIALIZING, student, student, time);
            SignalList.SendSignal(STOP_SOCIALIZING, student, student, interactionEndTime);
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
