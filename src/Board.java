import java.math.BigInteger;
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

        whitePieces.add(new Pawn(this, 'w', new Move("13")));
        whitePieces.add(new Pawn(this, 'w', new Move("14")));
        whitePieces.add(new Pawn(this, 'w', new Move("12")));
        whitePieces.add(new Pawn(this, 'w', new Move("15")));
        whitePieces.add(new Pawn(this, 'w', new Move("11")));
        whitePieces.add(new Pawn(this, 'w', new Move("16")));
        whitePieces.add(new Pawn(this, 'w', new Move("10")));
        whitePieces.add(new Pawn(this, 'w', new Move("17")));

        whitePieces.add(new Rook(this, 'w', new Move("00")));
        whitePieces.add(new Rook(this, 'w', new Move("07")));
        whitePieces.add(new Knight(this, 'w', new Move("01")));
        whitePieces.add(new Knight(this, 'w', new Move("06")));
        whitePieces.add(new Bishop(this, 'w', new Move("02")));
        whitePieces.add(new Bishop(this, 'w', new Move("05")));
        whitePieces.add(new King(this, 'w', new Move("04")));
        whitePieces.add(new Queen(this, 'w', new Move("03")));

        blackPieces.add(new Pawn(this, 'b', new Move("63")));
        blackPieces.add(new Pawn(this, 'b', new Move("64")));
        blackPieces.add(new Pawn(this, 'b', new Move("62")));
        blackPieces.add(new Pawn(this, 'b', new Move("65")));
        blackPieces.add(new Pawn(this, 'b', new Move("61")));
        blackPieces.add(new Pawn(this, 'b', new Move("66")));
        blackPieces.add(new Pawn(this, 'b', new Move("60")));
        blackPieces.add(new Pawn(this, 'b', new Move("67")));

        blackPieces.add(new Rook(this, 'b', new Move("70")));
        blackPieces.add(new Rook(this, 'b', new Move("77")));
        blackPieces.add(new Knight(this, 'b', new Move("71")));
        blackPieces.add(new Knight(this, 'b', new Move("76")));
        blackPieces.add(new Bishop(this, 'b', new Move("72")));
        blackPieces.add(new Bishop(this, 'b', new Move("75")));
        blackPieces.add(new King(this, 'b', new Move("74")));
        blackPieces.add(new Queen(this, 'b', new Move("73")));


        for (Piece p : whitePieces) {
            gameBoard[p.getLocation().row()][p.getLocation().col()] = p;
        }
        for (Piece p : blackPieces) {
            gameBoard[p.getLocation().row()][p.getLocation().col()] = p;
        }
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                gameBoard[i][j] = new EmptySquare(this, new Move("" + i + "" + j));
            }
        }
    }

    //Currently only used to make an instance of Board for clone. Much faster than the default constructor
    public Board(int modifier) {
        gameBoard = new Piece[8][8];
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
    }

    public boolean endangersKing(char color, Move move, Piece pieceMoved) {
        boolean killed = false;
        ArrayList<Piece> list;
        if (color == 'w') {
            list = (ArrayList<Piece>) blackPieces.clone();
        } else {
            list = (ArrayList<Piece>) whitePieces.clone();
        }
        Piece taken = gameBoard[move.row()][move.col()];
        Move loc = pieceMoved.getLocation();
        list.remove(taken);
        gameBoard[move.row()][move.col()] = pieceMoved;
        gameBoard[loc.row()][loc.col()] = new EmptySquare(this, loc);
        pieceMoved.updateLocation(move);
        for (Piece p : list) {
            if (!(p instanceof King)) {
                ArrayList<Move> moveList = p.genMoves();
                for (Move m : moveList) {
                    if (p.killKing(m)) {
                        killed = true;
                    }
                }
            }
        }
        gameBoard[loc.row()][loc.col()] = pieceMoved;
        pieceMoved.updateLocation(loc);
        gameBoard[move.row()][move.col()] = taken;
        return killed;
    }

    public void remove(Piece p) {
        if (whitePieces.contains(p)) {
            whitePieces.remove(p);
        } else if (blackPieces.contains(p)) {
            blackPieces.remove(p);
        }
    }

    public void printBoard() {
        System.out.println("   0 1 2 3 4 5 6 7");
        for (int r = 7; r >= 0; r--) {
            System.out.print(r + ": ");
            for (int c = 0; c < 8; c++) {
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
        for (Piece p : whitePieces) {
            if (p instanceof King) {
                whiteKing = true;
            }
        }
        for (Piece p : blackPieces) {
            if (p instanceof King) {
                blackKing = true;
            }
        }
        return !whiteKing || !blackKing;

    }

    public boolean parseMove(String m) {
        if (m.length() != 4) {
            return false;
        }
        Move move1 = new Move("" + m.charAt(0) + "" + m.charAt(1)); //start
        Move move2 = new Move("" + m.charAt(2) + m.charAt(3)); //dest
        if (gameBoard[move1.row()][move1.col()].finalMoveCheck(move2)) {
            gameBoard[move1.row()][move1.col()].move(move2);
            return true;
        }
        return false;
    }


    //bypasses any validity checks, used for optimization
    //TODO: replace body with just move() to fix calculation errors with castling and enpassant
    public boolean forceMove(String m) {
        Move move1 = new Move("" + m.charAt(0) + "" + m.charAt(1)); //start
        Move move2 = new Move("" + m.charAt(2) + "" + m.charAt(3)); //dest
        //manually move
        remove(gameBoard[move2.row()][move2.col()]);
        gameBoard[move2.row()][move2.col()] = gameBoard[move1.row()][move1.col()];
        gameBoard[move2.row()][move2.col()].updateLocation(move2);
        gameBoard[move1.row()][move1.col()] = new EmptySquare(this, move1);
        return true;
    }

    public double getValue() {
        //designed to be small enough that it will never take precedence over captures
        double defendModifier = .000005;
        double attackModifier = .001;


        double blackScore = 0;
        for (Piece p : blackPieces) {
            blackScore += p.getValue();
            //penalize knights on the edge
            /*if(p instanceof Knight && p.getLocation().col() == 0 || p.getLocation().col() == 7)
                blackScore -= .1;
            ArrayList<Move> ml = p.genMovesScoring();
            for(Move m : ml) {
                if(gameBoard[m.row()][m.col()].getColor() == 'w' && !(gameBoard[m.row()][m.col()] instanceof King))
                    blackScore += attackModifier * m.getValue(gameBoard) - (attackModifier*.1*p.getValue());
                if(gameBoard[m.row()][m.col()].getColor() == 'b' && !(gameBoard[m.row()][m.col()] instanceof King))
                    blackScore += defendModifier * m.getValue(gameBoard);
            }*/
        }
        double whiteScore = 0;
        for (Piece p : whitePieces) {
            whiteScore += p.getValue();
            //penalize knights on the edge
            /*if(p instanceof Knight && p.getLocation().col() == 0 || p.getLocation().col() == 7)
                whiteScore -= .1;
            ArrayList<Move> ml = p.genMovesScoring();
            for(Move m : ml) {
                if(gameBoard[m.row()][m.col()].getColor() == 'b' && !(gameBoard[m.row()][m.col()] instanceof King))
                    blackScore += attackModifier * m.getValue(gameBoard) - (attackModifier*.01*p.getValue());
                if(gameBoard[m.row()][m.col()].getColor() == 'w' && !(gameBoard[m.row()][m.col()] instanceof King))
                    blackScore += defendModifier * m.getValue(gameBoard);
            }*/
        }
        return whiteScore - blackScore;


    }

    public Board cloneBoard() {
        long start = System.nanoTime();
        Board clonedBoard = new Board(1);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //set the board state equal
                Piece newPiece = gameBoard[i][j].clone(clonedBoard);
                clonedBoard.gameBoard[i][j] = newPiece;
                //fix the piece lists
                if (newPiece.getColor() != '-') {
                    if (newPiece.getColor() == 'w') {
                        clonedBoard.whitePieces.add(newPiece);
                    } else {
                        clonedBoard.blackPieces.add(newPiece);
                    }
                }
            }
        }

        Stats.cloneTime += System.nanoTime() - start;
        Stats.cloneAmt++;
        return clonedBoard;
    }

    public Move[][] getMoves(char color) {
        ArrayList<Piece> pieceList;
        if (color == 'w') {
            pieceList = whitePieces;
        } else {
            pieceList = blackPieces;
        }
        Move[][] moveList = new Move[pieceList.size()][];
        for (int i = 0; i < moveList.length; i++) {
            ArrayList<Move> pieceMoves = pieceList.get(i).genMoves();
            int moveListLength = pieceMoves.size();
            moveList[i] = new Move[moveListLength];
            for (int j = 0; j < moveListLength; j++) {
                moveList[i][j] = pieceMoves.get(j);
            }
        }
        return moveList;
    }

    public BigInteger toBigInt() {
        String s = "";
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                s += gameBoard[i][j].getValue();
            }
        }
        return new BigInteger(s);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board))
            return false;
        Board board = (Board) obj;
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; i++) {
                if (board.gameBoard[i][j].toChar() != gameBoard[i][j].toChar()) {
                    return false;
                }
            }
        }
        return true;
    }
}
