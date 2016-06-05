import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class Knight implements Piece {

    private Piece[][] board;
    private Board classBoard;
    private char name;
    private Move location;
    private int value = 3;
    char color;

    public Knight(Board board, char color, Move location) {
        this.location = location;
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
    public boolean validMove(Move move) {
        if(move.equals(location)) //can't move to the same place
            return false;
        if(move.row() < 0 || move.row() > 7 || move.col() < 0 || move.col() > 7) //out of bounds
            return false;
        if((Math.abs(move.row() - location.row()) == 2 && Math.abs(move.col() - location.col()) == 1) || //the two valid knight move patterns
           (Math.abs(move.row() - location.row()) == 1 && Math.abs(move.col() - location.col()) == 2)) {

            char dest = board[move.row()][move.col()].toChar();
            if(Character.toUpperCase(dest) == dest && Character.toUpperCase(name) == name) //landing on same team White
                return false;
            if(Character.toLowerCase(dest) == dest && Character.toLowerCase(name) == name) //landing on same team Black
                return false;
            if(classBoard.endangersKing(color))
                return false;
            return true;
        }
        return false;
    }

    public ArrayList<Move> genMoves() {
        ArrayList<Move> moveList = new ArrayList<>();
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++) {
                Move move = new Move("" + r + "" + c);
                if(validMove(move))
                    moveList.add(move);
            }
        }
        return moveList;
    }

    public void move(Move move) {
        if(validMove(move)) {
            Piece placeholder = board[move.row()][move.col()];
            board[move.row()][move.col()] = this;
            board[location.row()][location.col()] = new EmptySquare(board, location);
            updateLocation(move);
            classBoard.remove(placeholder);
        }
    }
    public boolean killKing(Move move){
        if(Character.toLowerCase(board[move.row()][move.col()].toChar()) == 'k')
            return true;
        return false;
    }

    private void updateLocation(Move move) {
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
}
