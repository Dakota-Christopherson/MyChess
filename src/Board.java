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

        whitePieces.add(new Rook(this,'w',new Move("00")));
        whitePieces.add(new Rook(this,'w',new Move("07")));
        whitePieces.add(new Knight(this,'w',new Move("01")));
        whitePieces.add(new Knight(this,'w',new Move("06")));
        whitePieces.add(new Bishop(this,'w',new Move("02")));
        whitePieces.add(new Bishop(this,'w',new Move("05")));
        whitePieces.add(new King(this,'w', new Move("03")));
        whitePieces.add(new Queen(this,'w', new Move("04")));
        for(int i = 0; i < 8; i++) {
            whitePieces.add(new Pawn(this,'w', new Move("1" + i)));
        }

        blackPieces.add(new Rook(this,'b',new Move("70")));
        blackPieces.add(new Rook(this,'b',new Move("77")));
        blackPieces.add(new Knight(this,'b',new Move("71")));
        blackPieces.add(new Knight(this,'b',new Move("76")));
        blackPieces.add(new Bishop(this,'b',new Move("72")));
        blackPieces.add(new Bishop(this,'b',new Move("75")));
        blackPieces.add(new King(this,'b', new Move("73")));
        blackPieces.add(new Queen(this,'b', new Move("74")));
        for(int i = 0; i < 8; i++) {
            blackPieces.add(new Pawn(this,'b', new Move("6" + i)));
        }
        for(Piece p:whitePieces) {
            gameBoard[p.getLocation().row()][p.getLocation().col()] = p;
        }
        for(Piece p:blackPieces) {
            gameBoard[p.getLocation().row()][p.getLocation().col()] = p;
        }
        for(int i = 2; i < 6; i++) {
            for(int j = 0; j < 8; j++) {
                gameBoard[i][j] = new EmptySquare(gameBoard,new Move("" + i + "" + j));
            }
        }
    }

    public boolean endangersKing(char color) {
        ArrayList<Piece> list;
        if (color == 'w') {
            list = blackPieces;
        } else {
            list = whitePieces;
        }
        for (Piece p : list) {
            ArrayList<Move> moveList = p.genMoves();
            for (Move m : moveList) {
                if (p.killKing(m))
                    return true;
            }
        }
        return false;
    }

    public void remove(Piece p) {
        if(whitePieces.contains(p))
            whitePieces.remove(p);
        else if(blackPieces.contains(p))
            blackPieces.remove(p);
    }

    public void printBoard() {
        System.out.println("   0 1 2 3 4 5 6 7");
        for(int r = 0; r < 8; r++) {
            System.out.print(r + ": ");
            for(int c = 0; c < 8; c++) {
                System.out.print(gameBoard[r][c].toChar() + " ");
            }
            System.out.println();
        }
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
        if(gameBoard[move1.row()][move1.col()].validMove(move2)) {
            gameBoard[move1.row()][move1.col()].move(move2);
            return true;
        }
        return false;
    }
}
