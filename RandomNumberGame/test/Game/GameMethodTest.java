package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameMethodTest {
    Game game = new Game();


    @Test
    void initTest() {
        game.init(200);

        int numberToGuess = game.getNumberToGuess();

        assertTrue(numberToGuess >= 0 && numberToGuess <= 200);

    }
    @Test
    void makeAGuessBiggerTest() {
        int minValue = -1;

        GuessResult result = game.makeAGuess(minValue);
        GuessResult expectedResult = GuessResult.LESS;

        assertEquals(expectedResult, result);
    }
    @Test
    void makeAGuessLowerTest() {
        int maxValue = 201;

        GuessResult result = game.makeAGuess(maxValue);
        GuessResult expectedResult = GuessResult.GREATER;

        assertEquals(expectedResult, result);
    }
    @Test
    void makeAGuessEqualsTest() {
        int realNumb = game.getNumberToGuess();

        GuessResult result = game.makeAGuess(realNumb);
        GuessResult expectedResult = GuessResult.EQUAL;

        assertEquals(expectedResult, result);
    }

    @Test
    void incCountTest() {
        game.setGuessCount(5);
        game.incCount();

        assertEquals(6, game.getCount());
    }
}