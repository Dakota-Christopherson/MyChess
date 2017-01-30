import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Cody on 6/15/2016.
 */
public class AI {
    private Board board;
    private char color;
    private int ply;
    private boolean turn;
    Move[][] killerMoves;
    Hashtable<BigInteger, double[]> transposition; //position 0 in array will be minimax value, 1 will be depth
    private int killerCount = 3;

    public AI(Board board, char color, int ply) {
        this.ply = ply;
        killerMoves = new Move[ply][killerCount];
        this.board = board;
        this.color = color;

        turn = color == 'b';
    }

    public String aiMove() {
        Stats.aiMoveStart = System.nanoTime();
        //arbitrarily large prime as the size
        transposition = new Hashtable<>(1299827);


        ArrayList<Piece> pieces;
        if(color == 'w')
            pieces = board.whitePieces;
        else pieces = board.blackPieces;
        Move[][] moveList = board.getMoves(color);

        double[][] moveScores = new double[moveList.length][];
        for(int i = 0; i < moveScores.length; i++) {
            moveScores[i] = new double[moveList[i].length];
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for(int i = 0; i < moveList.length; i++) {
            for(int j = 0; j < moveList[i].length; j++) {
                Board placeholder = board.cloneBoard();
                String locString = pieces.get(i).getLocation().row() + "" + pieces.get(i).getLocation().col();
                placeholder.forceMove("" + locString + "" + moveList[i][j].row() + moveList[i][j].col());

                //Logic for turn: it will be true when AI is black because it just played all of the black moves above, then it's white's turn
                final int i1 = i;
                final int j1 = j;
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        moveScores[i1][j1] = miniMax(placeholder, ply, -Double.MAX_VALUE, Double.MAX_VALUE, turn);
                    }
                });
                threads.add(t);
            }
        }
        for(Thread t : threads) {
            t.start();
        }
        for(Thread t : threads) {
            try {
                t.join();
            } catch(InterruptedException e) {
                System.out.println("interrupted");
            }
        }

        String bestIndexR = "";
        String bestIndexC = "";
        double max = Integer.MIN_VALUE;
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
                System.out.println("Move score: " + String.format( "%8.3f",moveScores[i][j]) + ", move: " + "" +
                        pieces.get(i).getLocation().row() + "" + pieces.get(i).getLocation().col() + "" + moveList[i][j].row() + "" + moveList[i][j].col());
            }
        }

        System.out.println("Score: " + String.format("%8.3f",moveScores[pieceIndex][moveIndex]) + ", move: " + finMove);

        Stats.aiMoveEnd = System.nanoTime();
        return finMove;
    }

    public double miniMax(Board board1, int depth, double alpha, double beta, boolean maximizingPlayer) {
        Stats.nodesEval++;
        if(depth == 0 || board1.gameOver()) {
            /*BigInteger boardRep = board1.toBigInt();
            if(transposition.containsKey(boardRep)) {
                Stats.hashTableHits++;
                return transposition.get(boardRep)[0];
            }*/
            Stats.getValueAmt += 1;

            long start = System.nanoTime();
            double val = board1.getValue();
            //transposition.put(boardRep, new double[] {val, 0.0});
            Stats.getValueTime += System.nanoTime() - start;
            return val;
        }

        if(maximizingPlayer) {
            double bestValue = -Double.MAX_VALUE;

            long moveTime = System.nanoTime();
            Move[][] moveList = board1.getMoves('w');
            Stats.getMove += System.nanoTime() - moveTime;
            Stats.getMoveAmt++;

            moveOrder(moveList, ply - depth);


            for(int i = 0; i < moveList.length; i++) {
                for(int j = 0; j < moveList[i].length; j++) {
                    Board placeholder = board1.cloneBoard();
                    Piece p = placeholder.whitePieces.get(i);

                    //used to keep value for killer move, if taken after forceMove it will just give the piece value
                    Move move = new Move (moveList[i][j].toString(), placeholder.whitePieces.get(i).getLocation());
                    int moveVal = move.getValue(placeholder.gameBoard); //destination piece value

                    placeholder.forceMove(p.getLocation().toString() + "" + moveList[i][j].toString());
                    //BigInteger placeholderBigInt = placeholder.toBigInt();
                    double v;
                    boolean taken = false;
                    //if it's been hashed and has more depth remaining than this one, use it
                    /*if(transposition.containsKey(placeholderBigInt) && transposition.get(placeholderBigInt)[1] >= depth) {
                        v = transposition.get(placeholderBigInt)[0];
                        Stats.hashTableHits++;
                        taken = true;
                    }*/
                    //else { //otherwise hash it and use minimax to find the value
                        v = miniMax(placeholder, depth - 1, alpha, beta, false);
                        //if (!transposition.containsKey(placeholderBigInt) || transposition.get(placeholderBigInt)[1] < depth)
                         //   transposition.put(placeholderBigInt, new double[]{v, depth * 1.0});
                    //}
                    bestValue = Math.max(bestValue, v);
                    //if (taken && bestValue == v)
                       // Stats.hashTableSuccess++;

                    alpha = Math.max(alpha, bestValue);
                    if(beta <= alpha) {//beta cutoff
                        //captures will be ordered elsewhere
                        if(moveVal == 0) {
                            for (int k = killerCount - 2; k >= 0; k--)
                                killerMoves[ply - depth][k + 1] = killerMoves[ply - depth][k];
                            killerMoves[ply - depth][0] = move;
                        }
                        break;
                    }
                }
            }
            return bestValue;
        }
        else /*minimizing player*/ {
            double bestValue = Double.MAX_VALUE;

            long moveTime = System.nanoTime();
            Move[][] moveList = board1.getMoves('b');
            Stats.getMove += System.nanoTime() - moveTime;
            Stats.getMoveAmt++;

            moveOrder(moveList, ply - depth);


            for(int i = 0; i < moveList.length; i++) {
                for(int j = 0; j < moveList[i].length; j++) {
                    Board placeholder = board1.cloneBoard();
                    Piece p = placeholder.blackPieces.get(i);
                    placeholder.forceMove(p.getLocation().toString() + "" + moveList[i][j].toString());
                    //BigInteger placeholderBigInt = placeholder.toBigInt();

                    double v;
                    //boolean taken = false;
                    //if(transposition.containsKey(placeholderBigInt) && transposition.get(placeholderBigInt)[1] >= depth) {
                        //Stats.hashTableHits++;
                        //v = transposition.get(placeholderBigInt)[0];
                        //taken = true;
                    //}
                   // else {
                        v = miniMax(placeholder, depth - 1, alpha, beta, true);
                       // if (!transposition.containsKey(placeholderBigInt) || transposition.get(placeholderBigInt)[1] < depth)
                         //   transposition.put(placeholderBigInt, new double[]{v, depth * 1.0});
                   // }
                    bestValue = Math.min(bestValue, v);
                    //if(taken && v == bestValue)
                     //   Stats.hashTableSuccess++;

                    beta = Math.min(beta, bestValue);
                    if(beta <= alpha)
                        break;
                }
            }
            return bestValue;
        }
    }

    public void moveOrder(Move[][] moveList, int killerMovesLoc) {
        long startTime = System.nanoTime();
        //sort moves for each piece
        boolean killer = false;
        for(int piece = 0; piece < moveList.length; piece++) {

            double[] moveVals = new double[moveList[piece].length];
            //initialize moveVals
            for(int i = 0; i < moveList[piece].length; i++) {
                for(int j = 0; j < killerCount; j++) {
                    if(killerMoves[killerMovesLoc][j] != null) {
                        if (moveList[piece][i].getLoc() == killerMoves[killerMovesLoc][j].getLoc() && moveList[piece][i].toString().compareTo(killerMoves[killerMovesLoc][j].toString()) == 0) {
                            moveVals[i] = 5.0;
                            killer = true;
                        }
                    }
                }
                if(!killer)
                    moveVals[i] = moveList[piece][i].getValue(board.gameBoard)*1.0;
                killer = false;
            }


            Move tmp;
            double tmpVal;
            //sort moveVals and each piece's moveList
            int j;
            for(int i = 1; i < moveList[piece].length; i++) {
                tmp = moveList[piece][i];
                tmpVal = moveVals[i];
                for(j = i; j >= 1 && tmpVal > moveVals[j-1]; j--) {
                    moveVals[j] = moveVals[j-1];
                    moveList[piece][j] = moveList[piece][j-1];
                }
                moveList[piece][j] = tmp;
                moveVals[j] = tmpVal;
            }
            /*for(int i = 0; i < moveList[piece].length-1; i++) {
                for(int j = 0; j < moveList[piece].length-1; j++) {
                    if(moveVals[j] < moveVals[j+1]) {
                        tmp = moveList[piece][j];
                        tmpVal = moveVals[j];

                        moveList[piece][j] = moveList[piece][j+1];
                        moveVals[j] = moveVals[j+1];

                        moveList[piece][j+1] = tmp;
                        moveVals[j+1] = tmpVal;
                    }
                }
            }*/
        }

        //sort by piece using the highest valued move of each piece
        int[] ptrVals = new int[moveList.length];
        for(int i = 0; i < ptrVals.length; i++) {
            if(moveList[i].length == 0)
                ptrVals[i] = -20000;
            else ptrVals[i] = moveList[i][0].getValue(board.gameBoard);
        }

        Move[] tmp;
        int tmpVal;
        int j;
        for(int i = 1; i < moveList.length; i++) {
            tmp = moveList[i];
            tmpVal = ptrVals[i];
            for(j = i; j >= 1 && tmpVal > ptrVals[j-1]; j--) {
                ptrVals[j] = ptrVals[j-1];
                moveList[j] = moveList[j-1];
            }
            moveList[j] = tmp;
            ptrVals[j] = tmpVal;
        }
        /*for(int i = 0; i < moveList.length-1; i++) {
            for(int j = 0; j < moveList.length-1; j++) {
                if(ptrVals[j] < ptrVals[j+1]){
                    tmp = moveList[j];
                    tmpVal = ptrVals[j];

                    moveList[j] = moveList[j+1];
                    ptrVals[j] = ptrVals[j+1];

                    moveList[j+1] = tmp;
                    ptrVals[j+1] = tmpVal;
                }
            }
        }*/
        Stats.sortTot += (System.nanoTime() - startTime);
        Stats.sortAmt++;
    }
}
