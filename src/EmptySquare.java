import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class EmptySquare implements Piece {
    private Move location;
    public EmptySquare(Piece[][] board, Move move) {
        location = move;
    }
    public int getValue() {
        return 0;
    }
    public boolean validMove(Move move) {
        return false;
    }
    public void move(Move move) {

    }
    public boolean killKing(Move move) {
        return false;
    }
    public ArrayList<Move> genMoves() {
        return new ArrayList<Move>();
    }
    public char toChar() {
        return '-';
    }
    public Move getLocation(){
        return location;
    }
    public boolean validLegalMove(Move move) {
        return false;
    }
}
