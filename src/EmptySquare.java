import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class EmptySquare extends Piece {
    private Move location;
    public EmptySquare(Board board, Move move) {
        super(board,'-',move);
        location = move;
    }
    public float getValue() {
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
    public ArrayList<Move> genMovesScoring() { return new ArrayList<Move>(); }
    public char toChar() {
        return '-';
    }
    public Move getLocation(){
        return location;
    }
    public boolean finalMoveCheck(Move move) {
        return false;
    }
    public EmptySquare clone(Board newBoard) {
        return new EmptySquare(newBoard, location);
    }
}
