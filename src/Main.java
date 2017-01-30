import java.util.Scanner;

/**
 * Created by Cody on 6/4/2016.
 */


public class Main {
    public static void main(String[] args) {
        Board b1 = new Board();
        AI ai = new AI(b1, 'b', 4 );
        b1.printBoard();
        Scanner scan = new Scanner(System.in);

        boolean valid;
        boolean whiteTurn = false;
        while(!b1.gameOver()) {
            valid = false;
            whiteTurn = !whiteTurn;
            while(!valid) {
                if(!whiteTurn) {
                    b1.parseMove(ai.aiMove());
                    valid = true;
                }
                else {
                    System.out.println("Please enter your move: ");
                    String candidateMove = scan.nextLine();

                    //replace with substring

                    Move m = new Move(candidateMove.charAt(0) + "" + candidateMove.charAt(1));
                    //check to see if you're taking turns correctly
                    if (whiteTurn && b1.blackPieces.contains(b1.gameBoard[m.row()][m.col()]))
                        valid = false;
                    else if (!whiteTurn && b1.whitePieces.contains(b1.gameBoard[m.row()][m.col()]))
                        valid = false;
                        //if so try and do the move
                    else valid = b1.parseMove(candidateMove);
                }
            }

            System.out.println("Nodes evaluated: " + Stats.nodesEval);
            System.out.println("Board evaluation: " + String.format("%8.3f",b1.getValue()));
            if(Stats.sortAmt != 0) {
                long aiMoveTimeRaw = (Stats.aiMoveEnd - Stats.aiMoveStart);
                long aiMoveTime = aiMoveTimeRaw / 1000000;
                System.out.println("AIMove time: " + aiMoveTime + "ms");
                System.out.println(String.format("%2.3f", ((double)Stats.sortTot)*100/aiMoveTimeRaw) + "% of time spent on sort");
                System.out.println(String.format("%2.3f", ((double)Stats.cloneTime)*100/aiMoveTimeRaw) + "% of time spent on clone");
                System.out.println(Stats.cloneTime / 1000000 + "ms spent cloning");
                System.out.println(String.format("%2.3f", ((double)Stats.getMove)*100/aiMoveTimeRaw) + "% of time spent on getMoves");
                System.out.println(String.format("%2.3f", ((double)Stats.getValueTime)*100/aiMoveTimeRaw) + "% of time spent on getValue");
                System.out.println(Stats.hashTableHits + " hash table hits");
                System.out.println(Stats.hashTableSuccess + " hash table successes");
            }
            Stats.reset();
            b1.printBoard();
        }

    }
}
