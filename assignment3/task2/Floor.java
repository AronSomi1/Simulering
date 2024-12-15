package task2;

public class Floor {
    public int nbrFinishedInteracringStudents = 0;

    public static void main(String[] args) {
        Floor f = new Floor();
        f.divideFloorIntoSquares();
        var matrix = f.squares;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                System.out.print("(" + matrix[i][j].getX_coord() + "," + matrix[i][j].getY_coord() + ")");
            }
            System.out.println();
        }
    }

    private Square[][] squares;

    public Floor() {
        this.squares = new Square[20][20];
    }

    public void divideFloorIntoSquares() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                squares[i][j] = new Square(i + 0.5, j + 0.5);
            }
        }
    }

    public Square[][] getSquares() {
        return squares;
    }

    public void studentFinishedInteracting() {
        // System.out.println("Student finished interacting");
        nbrFinishedInteracringStudents++;
    }

}
