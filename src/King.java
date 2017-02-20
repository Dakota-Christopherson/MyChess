import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class King extends Piece {
    private Piece[][] board;
    private Board classBoard;
    private char name;
    private int value = 10000;
    char color;

    public King(Board board, char color, Move location) {
        super(board, color, location);
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
        this.color = color;
        pickColor(color);
    }

    private void pickColor(char color) {
        if (color == 'w') {
            name = 'K';
        } else {
            name = 'k';
        }
    }

    //legal as far as the piece's movement is concerned
    public boolean legalMove(Move move) {
        if (Math.abs(move.row() - location.row()) <= 1 && Math.abs(move.col() - location.col()) <= 1) { //valid king move
            return true;
        }
        //move two to the left or to the right           no vertical movement                hasn't moved      not castling out of check
        if (Math.abs(move.col() - location.col()) == 2 && move.row() - location.row() == 0 && !hasMoved && !classBoard.endangersKing(color, location, this)) {

            int duringCastlePos = move.col() + (location.col() - move.col()) / 2;

            boolean rookStationary = false;
            Rook r1 = null;
            Rook r2 = null;
            boolean rookFound = false;
            ArrayList<Piece> pieces;
            if (color == 'b') {
                pieces = classBoard.blackPieces;
            } else {
                pieces = classBoard.whitePieces;
            }
            for (Piece p : pieces) {
                if (!rookFound && p instanceof Rook) {
                    r1 = (Rook) p;
                    rookFound = true;
                }
                if (rookFound && p instanceof Rook) {
                    r2 = (Rook) p;
                }
            }

            if (r1 != null && Math.abs(r1.getLocation().col() - move.col()) <= 2 && !r1.hasMoved) {
                rookStationary = true;
            }
            if (r2 != null && Math.abs(r2.getLocation().col() - move.col()) <= 2 && !r2.hasMoved) {
                rookStationary = true;
            }
            //checks for not castling through check
            if (rookStationary && !classBoard.endangersKing(color, new Move("" + location.row() + "" + duringCastlePos), this)) {
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
        ArrayList<Move> moveList = new ArrayList<>();
        ArrayList<Move> tentativeList = new ArrayList<>();

        //diagonals
        tentativeList.add(new Move("" + (location.row() + 1) + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + (location.row() + 1) + "" + (location.col() - 1)));
        tentativeList.add(new Move("" + (location.row() - 1) + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + (location.row() - 1) + "" + (location.col() - 1)));

        //straights
        tentativeList.add(new Move("" + (location.row() + 1) + "" + location.col()));
        tentativeList.add(new Move("" + (location.row() - 1) + "" + location.col()));
        tentativeList.add(new Move("" + location.row() + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + location.row() + "" + (location.col() - 1)));


        for (Move move : tentativeList) {
            if (move.row() >= 0 && move.row() <= 7 && move.col() >= 0 && move.col() <= 7) {
                moveList.add(move);
            }
        }
        return moveList;
    }

    public ArrayList<Move> genMoves() {
        ArrayList<Move> legalList = new ArrayList<>();
        ArrayList<Move> tentativeList = new ArrayList<>();

        //diagonals
        tentativeList.add(new Move("" + (location.row() + 1) + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + (location.row() + 1) + "" + (location.col() - 1)));
        tentativeList.add(new Move("" + (location.row() - 1) + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + (location.row() - 1) + "" + (location.col() - 1)));

        //straights
        tentativeList.add(new Move("" + (location.row() + 1) + "" + location.col()));
        tentativeList.add(new Move("" + (location.row() - 1) + "" + location.col()));
        tentativeList.add(new Move("" + location.row() + "" + (location.col() + 1)));
        tentativeList.add(new Move("" + location.row() + "" + (location.col() - 1)));

        //castles
        tentativeList.add(new Move("" + location.row() + "" + (location.col() + 2)));
        tentativeList.add(new Move("" + location.row() + "" + (location.col() - 2)));
        for (Move m : tentativeList) {
            if (validMove(m) && legalMove(m)) {
                legalList.add(new Move(m.row() + "" + m.col(), location));
            }
        }
        return legalList;
    }

    public void move(Move move) {
        if (Math.abs(move.col() - location.col()) == 2) {
            castle(move);
        } else {
            super.move(move);
        }
    }

    private void castle(Move move) {
        Rook r1 = null;
        Rook r2 = null;
        boolean rookFound = false;
        ArrayList<Piece> pieces;
        if (color == 'b') {
            pieces = classBoard.blackPieces;
        } else {
            pieces = classBoard.whitePieces;
        }
        for (Piece p : pieces) {
            if (!rookFound && p instanceof Rook) {
                r1 = (Rook) p;
                rookFound = true;
            }
            if (rookFound && p instanceof Rook) {
                r2 = (Rook) p;
            }
        }
        // saved because this will be the rook's destination
        int duringCastlePos = move.col() + (location.col() - move.col()) / 2;

        board[move.row()][move.col()] = this;
        board[location.row()][location.col()] = new EmptySquare(classBoard, location);
        updateLocation(move);

        Rook r = null;
        if (r1 != null && Math.abs(r1.getLocation().col() - move.col()) <= 2 && !r1.hasMoved) {
            r = r1;
        }
        if (r2 != null && Math.abs(r2.getLocation().col() - move.col()) <= 2 && !r2.hasMoved) {
            r = r2;
        }

        if (r != null) {
            //System.out.println("r isn't null!");
            board[r.getLocation().row()][r.getLocation().col()] = new EmptySquare(classBoard, r.getLocation());
            //System.out.println(r.getLocation().toString());
            board[location.row()][duringCastlePos] = r;
            //System.out.println("During castle position: " + duringCastlePos);
            r.updateLocation(new Move("" + r.getLocation().row() + "" + duringCastlePos));
            //System.out.println(r.getLocation().toString());
        }
    }

    public int getValue() {
        return value;
    }

    public char toChar() {
        return name;
    }

    public Piece clone(Board newBoard) {
        return new King(newBoard, color, location);
    }
}
