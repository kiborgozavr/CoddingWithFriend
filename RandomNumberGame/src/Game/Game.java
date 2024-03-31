package Game;

import java.util.Random;

public class Game {

    private int NumberToGuess = 0;
    private int GuessCount = 0;

    public void init(int bound) {
        Random randomNumb = new Random();
        NumberToGuess = randomNumb.nextInt(bound);
    }

    public GuessResult makeAGuess(int guess) {
        incCount();

        if (guess > NumberToGuess) {
            return GuessResult.GREATER;
        } else if (guess < NumberToGuess) {
            return GuessResult.LESS;
        } else {
            return GuessResult.EQUAL;
        }
    }

    public int getCount() {
        return GuessCount;
    }

    private void incCount() {
        GuessCount++;
    }

    public int getNumberToGuess() {
        return NumberToGuess;
    }
}
