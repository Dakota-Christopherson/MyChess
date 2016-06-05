import java.util.Scanner;

/**
 * Created by Cody on 6/4/2016.
 */
public class Main {
    public static void main(String[] args) {
        Board b1 = new Board();
        b1.printBoard();
        Scanner scan = new Scanner(System.in);
        boolean valid;
        while(!b1.gameOver()) {
            valid = false;
            while(!valid) {
                System.out.println("Please enter your move: ");
                valid = b1.parseMove(scan.nextLine());
            }


            b1.printBoard();
        }
    }
}
