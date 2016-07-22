import java.util.ArrayList;

/**
 * Created by Cody on 6/4/2016.
 */
public class Board {
    ArrayList<Piece> whitePieces;
    ArrayList<Piece> blackPieces;
    Piece[][] gameBoard;
    public Board() {
        gameBoard = new Piece[8][8];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();

        //I add pawns first so those moves are evaluated sooner than the others
        //This keeps the ai from moving rooks back and forth when it can't see a change in value
        for(int i = 0; i < 8; i++) {
            whitePieces.add(new Pawn(this,'w', new Move("1" + i)));
        }
        whitePieces.add(new Rook(this,'w',new Move("00")));
        whitePieces.add(new Rook(this,'w',new Move("07")));
        whitePieces.add(new Knight(this,'w',new Move("01")));
        whitePieces.add(new Knight(this,'w',new Move("06")));
        whitePieces.add(new Bishop(this,'w',new Move("02")));
        whitePieces.add(new Bishop(this,'w',new Move("05")));
        whitePieces.add(new King(this,'w', new Move("04")));
        whitePieces.add(new Queen(this,'w', new Move("03")));


        for(int i = 0; i < 8; i++) {
            blackPieces.add(new Pawn(this,'b', new Move("6" + i)));
        }
        blackPieces.add(new Rook(this,'b',new Move("70")));
        blackPieces.add(new Rook(this,'b',new Move("77")));
        blackPieces.add(new Knight(this,'b',new Move("71")));
        blackPieces.add(new Knight(this,'b',new Move("76")));
        blackPieces.add(new Bishop(this,'b',new Move("72")));
        blackPieces.add(new Bishop(this,'b',new Move("75")));
        blackPieces.add(new King(this,'b', new Move("74")));
        blackPieces.add(new Queen(this,'b', new Move("73")));


        for(Piece p:whitePieces) {
            gameBoard[p.getLocation().row()][p.getLocation().col()] = p;
        }
        for(Piece p:blackPieces) {
            gameBoard[p.getLocation().row()][p.getLocation().col()] = p;
        }
        for(int i = 2; i < 6; i++) {
            for(int j = 0; j < 8; j++) {
                gameBoard[i][j] = new EmptySquare(this,new Move("" + i + "" + j));
            }
        }
    }

    public boolean endangersKing(char color, Move move, Piece pieceMoved) {
        boolean killed = false;
        ArrayList<Piece> list;
        if (color == 'w') {
            list = (ArrayList<Piece>)blackPieces.clone();
        } else {
            list = (ArrayList<Piece>)whitePieces.clone();
        }
        Piece taken = gameBoard[move.row()][move.col()];
        Move loc = pieceMoved.getLocation();
        list.remove(taken);
        gameBoard[move.row()][move.col()] = pieceMoved;
        gameBoard[loc.row()][loc.col()] = new EmptySquare(this, loc);
        pieceMoved.updateLocation(move);
        for (Piece p : list) {
            ArrayList<Move> moveList = p.genMoves();
            for (Move m : moveList) {
                if (p.killKing(m))
                    killed = true;
            }
        }
        gameBoard[loc.row()][loc.col()] = pieceMoved;
        pieceMoved.updateLocation(loc);
        gameBoard[move.row()][move.col()] = taken;
        return killed;
    }

    public void remove(Piece p) {
        if(whitePieces.contains(p))
            whitePieces.remove(p);
        else if(blackPieces.contains(p))
            blackPieces.remove(p);
    }

    public void printBoard() {
        System.out.println("   0 1 2 3 4 5 6 7");
        for(int r = 7; r >= 0; r--) {
            System.out.print(r + ": ");
            for(int c = 0; c < 8; c++) {
                System.out.print(gameBoard[r][c].toChar() + " ");
            }
            System.out.print(":" + r);
            System.out.println();
        }
        System.out.print("   0 1 2 3 4 5 6 7");
        System.out.println();
    }

    public boolean gameOver() {
        boolean whiteKing = false;
        boolean blackKing = false;
        for(Piece p : whitePieces)
            if(p instanceof King)
                whiteKing = true;
        for(Piece p : blackPieces)
            if(p instanceof King)
                blackKing = true;
        if(!whiteKing || !blackKing)
            return true;
        return false;
    }

    public boolean parseMove(String m) {
        Move move1 = new Move("" + m.charAt(0) + "" + m.charAt(1)); //start
        Move move2 = new Move("" + m.charAt(2) + m.charAt(3)); //dest
        if(gameBoard[move1.row()][move1.col()].validLegalMove(move2)) {
            gameBoard[move1.row()][move1.col()].move(move2);
            return true;
        }
        return false;
    }

    public boolean forceMove(String m) {
        Move move1 = new Move("" + m.charAt(0) + "" + m.charAt(1)); //start
        Move move2 = new Move("" + m.charAt(2) + m.charAt(3)); //dest
        //manually move
        remove(gameBoard[move2.row()][move2.col()]);
        gameBoard[move2.row()][move2.col()] = gameBoard[move1.row()][move1.col()];
        gameBoard[move2.row()][move2.col()].updateLocation(move2);
        gameBoard[move1.row()][move1.col()] = new EmptySquare(this, move1);
        return true;
    }

    public int getValue() {
        int blackScore = 0;
        for(Piece p : blackPieces)
            blackScore += p.getValue();
        int whiteScore = 0;
        for(Piece p : whitePieces)
            whiteScore += p.getValue();
        return whiteScore - blackScore;
    }

    public Board cloneBoard() {
        Board clonedBoard = new Board();
        //empty the piece lists
        clonedBoard.blackPieces = new ArrayList<>();
        clonedBoard.whitePieces = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                //set the board state equal
                Piece newPiece = gameBoard[i][j].clone(clonedBoard);
                clonedBoard.gameBoard[i][j] = newPiece;
                //fix the piece lists
                if(newPiece.getColor() == 'w')
                    clonedBoard.whitePieces.add(newPiece);
                else if(newPiece.getColor() == 'b')
                    clonedBoard.blackPieces.add(newPiece);
            }
        }

        return clonedBoard;
    }

    public Move[][] getMoves(char color) {
        ArrayList<Piece> pieceList;
        if(color == 'w')
            pieceList = whitePieces;
        else {
            pieceList = blackPieces;
        }
        Move[][] moveList = new Move[pieceList.size()][];
        for(int i = 0; i < moveList.length; i++) {
            Object[] pieceMoves = pieceList.get(i).genMoves().toArray();
            Move[] placeholder = new Move[pieceMoves.length];
            for(int j = 0; j <  placeholder.length; j++) {
                placeholder[j] = (Move)pieceMoves[j];
            }
            moveList[i] = placeholder;
        }
        return moveList;
    }
}
