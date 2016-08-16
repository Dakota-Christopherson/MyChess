import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class Knight extends Piece {

    private Piece[][] board;
    private Board classBoard;
    private char name;
    private int value = 3;
    char color;

    public Knight(Board board, char color, Move location) {
        super(board, color, location);
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
        this.color = color;
        pickColor(color);
    }

    private void pickColor(char color) {
        if(color == 'w')
            name = 'N';
        else name = 'n';
    }
    public boolean legalMove(Move move) {
        if((Math.abs(move.row() - location.row()) == 2 && Math.abs(move.col() - location.col()) == 1) || //the two valid knight move patterns
           (Math.abs(move.row() - location.row()) == 1 && Math.abs(move.col() - location.col()) == 2)) {
            return true;
        }
        return false;
    }

    public boolean validLegalMove(Move move) {
        return validMove(move) && legalMove(move) && !classBoard.endangersKing(color, move, this);
    }

    public ArrayList<Move> genMovesScoring() {
        ArrayList<Move> moveList = new ArrayList<>();
        ArrayList<Move> tentativeList = new ArrayList<>();

        tentativeList.add(new Move("" + (location.row() + 1) + "" + (location.col() + 2)));
        tentativeList.add(new Move("" + (location.row() - 1) + "" + (location.col() + 2)));
        tentativeList.add(new Move("" + (location.row() + 1) + "" + (location.col() - 2)));
        tentativeList.add(new Move("" + (location.row() - 1) + "" + (location.col() - 2)));

        tentativeList.add(new Move("" + (location.row() + 2) + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + (location.row() - 2) + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + (location.row() + 2) + "" + (location.col() - 1)));
        tentativeList.add(new Move("" + (location.row() - 2) + "" + (location.col() - 1)));

        for(Move move : tentativeList) {
            if (move.row() >= 0 && move.row() <= 7 && move.col() >= 0 && move.col() <= 7)
                moveList.add(move);
        }
        return moveList;
    }

    public ArrayList<Move> genMoves() {
        ArrayList<Move> ml = genMovesScoring();
        ArrayList<Move> legalList = new ArrayList<>();
        for(Move m : ml) {
            if(validMove(m) && legalMove(m)) //should add a check against capturing own piece outside of validMove
                legalList.add(m);
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
        return new Knight(newBoard, color, location);
    }
}
