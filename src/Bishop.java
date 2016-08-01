import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class Bishop extends Piece {
    private Piece[][] board;
    private Board classBoard;
    private char name;
    private Move location;
    private int value = 3;
    char color;

    public Bishop(Board board, char color, Move location) {
        super(board, color, location);
        this.location = location;
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
        this.color = color;
        pickColor(color);
    }

    private void pickColor(char color) {
        if(color == 'w')
            name = 'B';
        else name = 'b';
    }
    public boolean legalMove(Move move) {
        if(Math.abs(move.row() - location.row()) == Math.abs(move.col() - location.col())) { //move up/down and left/right the same amount
            return true;
        }
        return false;
    }

    public boolean validLegalMove(Move move) {
        return validMove(move) && legalMove(move) && !classBoard.endangersKing(color, move, this);
    }

    public ArrayList<Move> genMoves() {
        ArrayList<Move> moveList = new ArrayList<>();
        ArrayList<Move> tentativeList = new ArrayList<>();
        int offsetCol = 1;
        int offsetRow = 1;
        while(location.col() + offsetCol < 8 || location.row() + offsetRow < 8 || location.col() - offsetCol >= 0 || location.row() - offsetRow >= 0) {
            tentativeList.add(new Move("" + (offsetRow + location.row()) + "" + (offsetCol + location.col())));
            tentativeList.add(new Move("" + (location.row() - offsetRow) + "" + (location.col() - offsetCol)));
            tentativeList.add(new Move("" + (location.row() + offsetRow) + "" + (location.col() - offsetCol)));
            tentativeList.add(new Move("" + (location.row() - offsetRow) + "" + (location.col() + offsetCol)));
            offsetCol++;
            offsetRow++;
        }
        for(Move move : tentativeList) {
            if (validMove(move) && legalMove(move))
                moveList.add(move);
        }
        return moveList;
    }

    public void move(Move move) {

            super.move(move);
            updateLocation(move);

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
        return new Bishop(newBoard, color, location);
    }
}
