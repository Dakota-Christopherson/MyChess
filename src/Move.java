/**
 * Created by Cody on 6/4/2016.
 */
public class Move{
    private String move;
    private Move loc;
    public Move(String move) {
        this.move = move;
    }

    public Move(String move, Move loc) {
        this.loc = loc;
        this.move = move;
    }

    public int row() {
        return Character.getNumericValue(move.charAt(0));
    }

    public int col() {
        return Character.getNumericValue(move.charAt(1));
    }

    public Move getLoc() {
        return loc;
    }

    public boolean equals(Move m) {
        return row() == m.row() && col() == m.col();
    }

    public int getValue(Piece[][] gameBoard) {
        return gameBoard[row()][col()].getValue();
    }

    public int compareTo(Piece[][] gameBoard, Move m) {
        return gameBoard[row()][col()].getValue() - gameBoard[m.row()][m.col()].getValue();
    }

    @Override
    public String toString() {
        return "" + row() + "" + col();
    }

}
