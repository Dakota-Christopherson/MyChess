import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class Rook extends Piece {
    private Piece[][] board;
    private Board classBoard;
    private char name;
    private int value = 5;
    char color;

    public Rook(Board board, char color, Move location) {
        super(board, color, location);
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

    //legal as far as the piece's movement is concerned
    public boolean legalMove(Move move) {
        if(move.col() == location.col() || move.row() == location.row()) {
            return true;
        }
        return false;
    }

    //Required because checking endangersKing() in any of the other methods causes infinite loop
    public boolean validLegalMove(Move move) {
        return validMove(move) && legalMove(move) && !classBoard.endangersKing(color, move, this);
    }


    //Used to find which pieces it can attack or defend
    public ArrayList<Move> genMovesScoring() {
        ArrayList<Move> moveList = new ArrayList<>();
        ArrayList<Move> tentativeList = new ArrayList<>();
        for(int c = 0; c < 8; c++) {
            tentativeList.add(new Move("" + location.row() + "" + c));
        }
        for(int r = 0; r < 8; r++) {
            tentativeList.add(new Move("" + r + "" + location.col()));
        }
        for(Move move : tentativeList) {
            if (!jump(move))
                moveList.add(move);
        }
        return moveList;
    }

    public ArrayList<Move> genMoves() {
        ArrayList<Move> legalList = new ArrayList<>();
        ArrayList<Move> tentativeList = new ArrayList<>();
        for(int c = 0; c < 8; c++) {
            tentativeList.add(new Move("" + location.row() + "" + c));
        }
        for(int r = 0; r < 8; r++) {
            tentativeList.add(new Move("" + r + "" + location.col()));
        }
        for(Move m : tentativeList) {
            if(validMove(m))
                legalList.add(new Move(m.row() + "" + m.col(), location));
        }
        return legalList;
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
