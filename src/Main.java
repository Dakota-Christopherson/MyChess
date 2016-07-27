import java.util.Scanner;

/**
 * Created by Cody on 6/4/2016.
 */


//TODO: remove all location data from individual classes, remove endangersKing from King validMove, remove Move from all classes
public class Main {
    public static void main(String[] args) {
        Board b1 = new Board();
        AI ai = new AI(b1, 'b', 3);
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
            System.out.println("Board evaluation: " + b1.getValue());
            Stats.nodesEval = 0;
            b1.printBoard();
        }

    }
}
