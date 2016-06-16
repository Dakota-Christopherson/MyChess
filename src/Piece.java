import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public abstract class Piece {
    private Piece[][] board;
    private Board classBoard;
    private Move location;
    private char color;
    private char name;
    private int value;
    public Piece(Board board, char color, Move location) {
        this.color = color;
        this.location = location;
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
    }

    public boolean validMove(Move move) {
        if(move.equals(location)) //can't move to the same place
            return false;
        if(move.row() < 0 || move.row() > 7 || move.col() < 0 || move.col() > 7) //out of bounds
            return false;

        Piece dest = board[move.row()][move.col()];
        ArrayList<Piece> pieces;
        if(color == 'w')
            pieces = classBoard.whitePieces;
        else pieces = classBoard.blackPieces;
        if(pieces.contains(dest))  //land on same colored piece
            return false;

        return this instanceof Knight || !jump(move);

    }

    public boolean jump(Move move) {

        int initCol = location.col();
        int initRow = location.row();
        int finalCol = move.col();
        int finalRow = move.row();


        while(initCol != finalCol || initRow != finalRow) {

            if(board[initRow][initCol] != this && board[initRow][initCol] != board[move.row()][move.col()] && !(board[initRow][initCol] instanceof EmptySquare))
                return true;
            if(initCol < finalCol)
                initCol++;
            else if(initCol > finalCol)
                initCol--;
            if(initRow < finalRow)
                initRow++;
            else if(initRow > finalRow)
                initRow--;
        }
        return false;
    }

    public abstract boolean validLegalMove(Move move);

    public abstract ArrayList<Move> genMoves();

    public void move(Move move) {
        if(validLegalMove(move)) {
            Piece placeholder = board[move.row()][move.col()];
            board[move.row()][move.col()] = this;
            board[location.row()][location.col()] = new EmptySquare(classBoard, location);
            updateLocation(move);
            classBoard.remove(placeholder);
        }
    }
    public boolean killKing(Move move){
        if(Character.toLowerCase(board[move.row()][move.col()].toChar()) == 'k')
            return true;
        return false;
    }

    public void updateLocation(Move move) {
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

    public char getColor() {
        return color;
    }

    public abstract Piece clone(Board newBoard);
}
