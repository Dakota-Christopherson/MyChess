import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class Queen extends Piece {
    private Piece[][] board;
    private Board classBoard;
    private char name;
    private Move location;
    private int value = 8;
    char color;

    public Queen(Board board, char color, Move location) {
        super(board, color, location);
        this.location = location;
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
        this.color = color;
        pickColor(color);
    }

    private void pickColor(char color) {
        if(color == 'w')
            name = 'Q';
        else name = 'q';
    }
    public boolean legalMove(Move move) {

        if((Math.abs(move.row() - location.row()) <= 1 && Math.abs(move.col() - location.col()) <= 1) || //moves like a king
                (Math.abs(move.row() - location.row()) == Math.abs(move.col() - location.col())) ||      //moves like a bishop
                (move.col() == location.col() || move.row() == location.row())) {                //moves like a rook

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

        //Rook generation
        for(int c = 0; c < 8; c++) {
            tentativeList.add(new Move("" + location.row() + "" + c));
        }
        for(int r = 0; r < 8; r++) {
            tentativeList.add(new Move("" + r + "" + location.col()));
        }

        //Bishop generation
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
            if(move.row() >= 0 && move.row() <= 7 && move.col() >= 0 && move.col() <= 7 && !jump(move))
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
        return new Queen(newBoard, color, location);
    }
}
