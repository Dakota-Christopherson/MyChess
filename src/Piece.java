import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public abstract class Piece {
    protected boolean hasMoved = false;
    private Piece[][] board;
    private Board classBoard;
    protected Move location;
    private char color;
    private char name;
    private int value;

    public Piece(Board board, char color, Move location) {
        this.color = color;
        this.location = location;
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
    }

    //checks for out of bounds moves, jumping, and capturing own pieces
    public boolean validMove(Move move) {
        if (move.equals(location)) { //can't move to the same place
            return false;
        }

        if (move.row() < 0 || move.row() > 7 || move.col() < 0 || move.col() > 7) { //out of bounds
            return false;
        }


        if (friendlyCapture(move)) {  //land on same colored piece
            return false;
        }

        return this instanceof Knight || !jump(move);

    }

    public boolean friendlyCapture(Move move) {
        return color == board[move.row()][move.col()].getColor();
    }

    public boolean jump(Move move) {

        int initCol = location.col();
        int initRow = location.row();
        int finalCol = move.col();
        int finalRow = move.row();

        while (initCol != finalCol || initRow != finalRow) {

            if (board[initRow][initCol] != this && board[initRow][initCol] != board[move.row()][move.col()] && !(board[initRow][initCol] instanceof EmptySquare)) {
                return true;
            }
            if (initCol < finalCol) {
                initCol++;
            } else if (initCol > finalCol) {
                initCol--;
            }
            if (initRow < finalRow) {
                initRow++;
            } else if (initRow > finalRow) {
                initRow--;
            }
        }
        return false;
    }

    public abstract boolean finalMoveCheck(Move move);

    public abstract ArrayList<Move> genMoves();

    public abstract ArrayList<Move> genMovesScoring();

    public void move(Move move) {
        Piece placeholder = board[move.row()][move.col()];
        board[move.row()][move.col()] = this;
        board[location.row()][location.col()] = new EmptySquare(classBoard, location);
        updateLocation(move);
        classBoard.remove(placeholder);
        hasMoved = true;

        if (color == 'w') {
            for (Piece p : classBoard.blackPieces) {
                if (p instanceof Pawn) {
                    ((Pawn) p).enPassantPoss = false;
                }
            }
        } else if (color == 'b') {
            for (Piece p : classBoard.whitePieces) {
                if (p instanceof Pawn) {
                    ((Pawn) p).enPassantPoss = false;
                }
            }
        }
    }

    public boolean killKing(Move move) {
        return Character.toLowerCase(board[move.row()][move.col()].toChar()) == 'k';
    }

    public void updateLocation(Move move) {
        location = move;
    }

    public Move getLocation() {
        return location;
    }

    public abstract int getValue();

    public char toChar() {
        return name;
    }

    public char getColor() {
        return color;
    }

    public abstract Piece clone(Board newBoard);

}
