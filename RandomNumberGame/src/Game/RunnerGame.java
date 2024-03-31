package Game;

import java.util.Scanner;

public class RunnerGame {


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int bound = 200;
        Game game = new Game();
        game.init(bound);


        System.out.println("Try to guess the number! The range of guessed numbers is from 0 to " + bound);
        System.out.print("Input your answer: ");

        GuessResult result;
        do {
            int temp = scanner.nextInt();
            result = game.makeAGuess(temp);
            if (result == GuessResult.GREATER) {
                System.out.println("Too high, try again");
            } else if (result == GuessResult.LESS) {
                System.out.println("Too low, try again");
            }
        } while (result != GuessResult.EQUAL);
        System.out.println("YOU WIN!!!");
        System.out.println("You guessed right on the " + game.getCount() + "th try.");
        scanner.close();
    }
}
