import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class Rook extends Piece {
    private Piece[][] board;
    private Board classBoard;
    private char name;
    private Move location;
    private int value = 5;
    char color;

    public Rook(Board board, char color, Move location) {
        super(board, color, location);
        this.location = location;
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
        this.color = color;
        pickColor(color);
    }

    private void pickColor(char color) {
        if(color == 'w')
            name = 'R';
        else name = 'r';
    }
    public boolean legalMove(Move move) {
        if(move.col() == location.col() || move.row() == location.row()) {
            return validMove(move);
        }
        return false;
    }

    public boolean validLegalMove(Move move) {
        return legalMove(move) && !classBoard.endangersKing(color, move, this);
    }

    public ArrayList<Move> genMoves() {
        ArrayList<Move> moveList = new ArrayList<>();
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                Move move = new Move("" + r + "" + c);
                if(legalMove(move))
                    moveList.add(move);
            }
        }
        return moveList;
    }

    public void move(Move move) {
        if(legalMove(move)) {
            super.move(move);
            updateLocation(move);
        }
    }

    public void updateLocation(Move move) {
        super.updateLocation(move);
        location = move;
    }

    public Move getLocation() {
        return location;
    }

    public int getValue() {
        return value;
    }

    public char toChar(){
        return name;
    }

    public Piece clone(Board newBoard) {
        return new Rook(newBoard, color, location);
    }
}
