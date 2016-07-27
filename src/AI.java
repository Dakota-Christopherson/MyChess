import java.util.ArrayList;

/**
 * Created by Cody on 6/15/2016.
 */
public class AI {
    private Board board;
    private char color;
    private int ply;
    private boolean turn;
    public AI(Board board, char color, int ply) {
        this.ply = ply;
        this.board = board;
        this.color = color;

        turn = color == 'b';
    }

    public String aiMove() {
        ArrayList<Piece> pieces;
        if(color == 'w')
            pieces = board.whitePieces;
        else pieces = board.blackPieces;
        Move[][] moveList = board.getMoves(color);

        int[][] moveScores = new int[moveList.length][];
        for(int i = 0; i < moveScores.length; i++) {
            moveScores[i] = new int[moveList[i].length];
        }

        for(int i = 0; i < moveList.length; i++) {
            for(int j = 0; j < moveList[i].length; j++) {
                Board placeholder = board.cloneBoard();
                String locString = pieces.get(i).getLocation().row() + "" + pieces.get(i).getLocation().col();
                placeholder.forceMove("" + locString + "" + moveList[i][j].row() + moveList[i][j].col());

                moveScores[i][j] = miniMax(placeholder, ply, Integer.MIN_VALUE, Integer.MAX_VALUE, turn);
            }
        }

        String bestIndexR = "";
        String bestIndexC = "";
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < moveScores.length; i++) {
            for(int j = 0; j < moveScores[i].length; j++) {
                if(color == 'b' && -moveScores[i][j] > max) {
                    max = -moveScores[i][j];
                    bestIndexR = "" + i;
                    bestIndexC = "" + j;
                }
                else if(color == 'w' && moveScores[i][j] > max) {
                    max = moveScores[i][j];
                    bestIndexR = "" + i;
                    bestIndexC = "" + j;
                }
            }
        }
        int pieceIndex = Integer.parseInt(bestIndexR);
        //System.out.println("Piece Index: " + pieceIndex);
        int moveIndex = Integer.parseInt(bestIndexC);
        //System.out.println("Move Index: " + moveIndex);
        //System.out.println("moveList length: " + moveList.length);
        //System.out.println("moveList[] length: " + moveList[pieceIndex].length);
        //System.out.println("moveScores[] length: " + moveScores[pieceIndex].length);

        String finMove = "" + pieces.get(pieceIndex).getLocation().row() + "" + pieces.get(pieceIndex).getLocation().col();
        finMove += "" + moveList[pieceIndex][moveIndex].row() + "" + moveList[pieceIndex][moveIndex].col();

        for(int i = 0; i < moveScores.length; i++) {
            for (int j = 0; j < moveScores[i].length; j++) {
                System.out.println("Move score: " + moveScores[i][j] + ", move: " + "" +
                        pieces.get(i).getLocation().row() + "" + pieces.get(i).getLocation().col() + "" + moveList[i][j].row() + "" + moveList[i][j].col());
            }
        }

        System.out.println("Score: " + moveScores[pieceIndex][moveIndex] + ", move: " + finMove);


        return finMove;
    }

    public int miniMax(Board board1, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if(depth == 0 || board1.gameOver()) {
            return board1.getValue();
        }

        if(maximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            Move[][] moveList = board1.getMoves('w');
            for(int i = 0; i < moveList.length; i++) {
                for(int j = 0; j < moveList[i].length; j++) {
                    Board placeholder = board1.cloneBoard();
                    Piece p = placeholder.whitePieces.get(i);
                    placeholder.forceMove(p.getLocation().toString() + "" + moveList[i][j].toString());
                    int v = miniMax(placeholder, depth - 1, alpha, beta, false);
                    bestValue = Math.max(bestValue, v);

                    Stats.nodesEval++;
                    alpha = Math.max(alpha, bestValue);
                    if(beta <= alpha)
                        break;
                }
            }
            return bestValue;
        }
        else /*minimizing player*/ {
            int bestValue = Integer.MAX_VALUE;
            Move[][] moveList = board1.getMoves('b');
            for(int i = 0; i < moveList.length; i++) {
                for(int j = 0; j < moveList[i].length; j++) {
                    Board placeholder = board1.cloneBoard();
                    Piece p = placeholder.blackPieces.get(i);
                    placeholder.forceMove(p.getLocation().toString() + "" + moveList[i][j].toString());
                    int v = miniMax(placeholder, depth - 1, alpha, beta, true);
                    bestValue = Math.min(bestValue, v);

                    Stats.nodesEval++;
                    beta = Math.min(beta, bestValue);
                    if(beta <= alpha)
                        break;
                }
            }
            return bestValue;
        }
    }
}
