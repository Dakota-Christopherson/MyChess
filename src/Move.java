/**
 * Created by Cody on 6/4/2016.
 */
public class Move {
    private String move;

    public Move(String move) {
        this.move = move;
    }

    public int row() {
        return Character.getNumericValue(move.charAt(0));
    }

    public int col() {
        return Character.getNumericValue(move.charAt(1));
    }

    public boolean equals(Move m) {
        return row() == m.row() && col() == m.col();
    }
    public String toString() {
        return "" + row() + "" + col();
    }

}
