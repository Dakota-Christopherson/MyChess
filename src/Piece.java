import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public interface Piece {
    int getValue();
    boolean validMove(Move move);
    void move(Move move);
    boolean killKing(Move move);
    ArrayList<Move> genMoves();
    char toChar();
    Move getLocation();
    boolean validLegalMove(Move move);
}
