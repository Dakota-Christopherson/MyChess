import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class King extends Piece {
    private Piece[][] board;
    private Board classBoard;
    private char name;
    private Move location;
    private int value = 10000;
    char color;

    public King(Board board, char color, Move location) {
        super(board, color, location);
        this.location = location;
        classBoard = board;
        this.board = board.gameBoard; //please get better at naming things in advance
        this.color = color;
        pickColor(color);
    }

    private void pickColor(char color) {
        if(color == 'w')
            name = 'K';
        else name = 'k';
    }
    public boolean legalMove(Move move) {
        if(Math.abs(move.row() - location.row()) <= 1 && Math.abs(move.col() - location.col()) <= 1) { //valid king move
            return true;
        }
        //move two to the left or to the right           no vertical movement                hasn't moved      not castling out of check
        if(Math.abs(move.col() - location.col()) == 2 && move.row() - location.row() == 0 && !pieceHasMoved && !classBoard.endangersKing(color, location, this)) {

            int duringCastlePos = move.col() + (location.col() - move.col())/2;

            boolean rookStationary = false;
            Rook r1 = null;
            Rook r2 = null;
            boolean rookFound = false;
            ArrayList<Piece> pieces;
            if(color == 'b')
                pieces = classBoard.blackPieces;
            else pieces = classBoard.whitePieces;
            for(Piece p: pieces) {
                if(!rookFound && p instanceof Rook) {
                    r1 = (Rook) p;
                    rookFound = true;
                }
                if(rookFound && p instanceof Rook)
                    r2 = (Rook) p;
            }

            if(r1 != null && Math.abs(r1.getLocation().col() - move.col()) <= 2 && !r1.pieceHasMoved)
                rookStationary = true;
            if(r2 != null && Math.abs(r2.getLocation().col() - move.col()) <= 2 && !r2.pieceHasMoved)
                rookStationary = true;
            //checks for not castling through check
            if(rookStationary && !classBoard.endangersKing(color, new Move("" + location.row() + "" + duringCastlePos), this)) {
                return true;
            }
        }
        return false;
    }

    public boolean validLegalMove(Move move) {
        return validMove(move) && legalMove(move) && !classBoard.endangersKing(color, move, this);
    }

    public ArrayList<Move> genMoves() {
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

        for(Move move : tentativeList) {
            if (validMove(move) && legalMove(move))
                moveList.add(move);
        }
        return moveList;
    }

    public void move(Move move) {
        if(Math.abs(move.col() - location.col()) == 2)
            castle(move);
        else {
            super.move(move);
            updateLocation(move);
        }
    }

    private void castle(Move move) {
        Rook r1 = null;
        Rook r2 = null;
        boolean rookFound = false;
        ArrayList<Piece> pieces;
        if(color == 'b')
            pieces = classBoard.blackPieces;
        else pieces = classBoard.whitePieces;
        for(Piece p: pieces) {
            if(!rookFound && p instanceof Rook) {
                r1 = (Rook) p;
                rookFound = true;
            }
            if(rookFound && p instanceof Rook)
                r2 = (Rook) p;
        }
        // saved because this will be the rook's destination
        int duringCastlePos = move.col() + (location.col() - move.col())/2;

        board[move.row()][move.col()] = this;
        board[location.row()][location.col()] = new EmptySquare(classBoard, location);
        updateLocation(move);

        Rook r = null;
        if(r1 != null && Math.abs(r1.getLocation().col() - move.col()) <= 2 && !r1.pieceHasMoved)
            r = r1;
        if(r2 != null && Math.abs(r2.getLocation().col() - move.col()) <= 2 && !r2.pieceHasMoved)
            r = r2;

        if(r != null) {
            System.out.println("r isn't null!");
            board[r.getLocation().row()][r.getLocation().col()] = new EmptySquare(classBoard, r.getLocation());
            System.out.println(r.getLocation().toString());
            board[location.row()][duringCastlePos] = r;
            System.out.println("During castle position: " + duringCastlePos);
            r.updateLocation(new Move("" + r.getLocation().row() + "" + duringCastlePos));
            System.out.println(r.getLocation().toString());
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
        return new King(newBoard, color, location);
    }
}
