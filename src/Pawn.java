import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class Pawn extends Piece {
    private Piece[][] board;
    private Board classBoard;
    private char name;
    private float value = 1;
    char color;
    boolean enPassantPoss = false;

    public Pawn(Board board, char color, Move location) {
        super(board, color, location);
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
        this.color = color;
        pickColor(color);
        value = Const.kPAWN;
    }

    public Pawn(Board board, char color, Move location, float val) {
        super(board, color, location);
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
        this.color = color;
        pickColor(color);
        value = val;
    }

    private void pickColor(char color) {
        if (color == 'w') {
            name = 'P';
        } else {
            name = 'p';
        }
    }

    //legal as far as the piece's movement is concerned
    public boolean legalMove(Move move) {
        // |          same column     |                      nothing in front of it          |     only moving up one or two if it hasn't moved
        if (move.col() == location.col() && board[move.row()][move.col()].toChar() == '-' &&
                (((location.row() - move.row() == 1 || (location.row() - move.row() == 2 && !hasMoved)) && color == 'b') || //black
                        ((location.row() - move.row() == -1 || (location.row() - move.row() == -2 && !hasMoved)) && color == 'w'))) { //white
            return true;
        }
        // |           move up one           |         column shifts one
        char colorDest = board[move.row()][move.col()].getColor();
        if ((location.row() - move.row() == 1 && Math.abs(move.col() - location.col()) == 1 && color == 'b' && colorDest == 'w') || (location.row() - move.row() == -1 && Math.abs(move.col() - location.col()) == 1 && color == 'w' && colorDest == 'b')) {
            return true;
        }
        //enpassant checks
        if (move.row() + 1 == 3) {
            Piece blackTarget = board[move.row() + 1][move.col()];
            if (location.row() - move.row() == 1 && Math.abs(move.col() - location.col()) == 1 && color == 'b' && blackTarget instanceof Pawn && ((Pawn) blackTarget).enPassantPoss) {
                return true;
            }
        }
        if (move.row() - 1 == 4) {
            Piece whiteTarget = board[move.row() - 1][move.col()];
            if (location.row() - move.row() == -1 && Math.abs(move.col() - location.col()) == 1 && color == 'w' && whiteTarget instanceof Pawn && ((Pawn) whiteTarget).enPassantPoss) {
                return true;
            }
        }
        return false;
    }


    //Required because checking endangersKing() in any of the other methods causes infinite loop
    public boolean finalMoveCheck(Move move) {
        return validMove(move) && legalMove(move) && !classBoard.endangersKing(color, move, this);
    }


    //Used to find which pieces it can attack or defend
    public ArrayList<Move> genMovesScoring() {
        ArrayList<Move> tentativeList = new ArrayList<>();
        int offset;
        if (color == 'w') {
            offset = 1;
        } else {
            offset = -1;
        }
        //captures only
        if (location.col() + 1 <= 7 && location.row() + offset >= 0 && location.row() + offset <= 7) {
            tentativeList.add(new Move("" + (location.row() + offset) + "" + (location.col() + 1)));
        }
        if (location.col() - 1 >= 0 && location.row() + offset >= 0 && location.row() + offset <= 7) {
            tentativeList.add(new Move("" + (location.row() + offset) + "" + (location.col() - 1)));
        }
        return tentativeList;
    }

    public ArrayList<Move> genMoves() {
        ArrayList<Move> moveList = new ArrayList<>();
        ArrayList<Move> tentativeList = new ArrayList<>();
        int offset;
        if (color == 'w') {
            offset = 1;
        } else {
            offset = -1;
        }
        //captures evaluated first
        tentativeList.add(new Move("" + (location.row() + offset) + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + (location.row() + offset) + "" + (location.col() - 1)));
        //2*offset is for the pawn's first move
        tentativeList.add(new Move("" + (location.row() + 2 * offset) + "" + location.col()));
        tentativeList.add(new Move("" + (location.row() + offset) + "" + location.col()));

        for (Move m : tentativeList) {
            if (validMove(m) && legalMove(m)) {
                moveList.add(new Move(m.row() + "" + m.col(), location));
            }
        }
        return moveList;
    }

    public void move(Move move) {
        if (board[move.row()][location.row()] instanceof EmptySquare && Math.abs(move.col() - location.col()) == 1) {
            Piece removed;
            if (color == 'w') {
                removed = board[move.row() - 1][move.col()];
            } else {
                removed = board[move.row() + 1][move.col()];
            } //color == 'b'
            board[removed.getLocation().row()][removed.getLocation().col()] = new EmptySquare(classBoard, removed.getLocation());
            classBoard.remove(removed);
        }

        Move oldLoc = location;
        super.move(move);
        if (Math.abs(move.row() - oldLoc.row()) == 2) {
            enPassantPoss = true;
        }
        hasMoved = true;

        if (color == 'w' && location.row() == 7) {
            Queen newQ = new Queen(classBoard, 'w', location);
            board[location.row()][location.col()] = newQ;
            classBoard.remove(this);
            classBoard.whitePieces.add(newQ);
        }

        if (color == 'b' && location.row() == 0) {
            Queen newQ = new Queen(classBoard, 'b', location);
            board[location.row()][location.col()] = newQ;
            classBoard.remove(this);
            classBoard.blackPieces.add(newQ);
        }

    }

    public float getValue() {
        return value;
    }

    public char toChar() {
        return name;
    }

    public Piece clone(Board newBoard) {
        return new Pawn(newBoard, color, location, value);
    }

}
