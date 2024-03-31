package Game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameMethodTest {
    Game game = new Game();


    @Test
    void initTest() {
        // TODO: Figure out best practices to test methods with random in them
        game.init(200);

        int numberToGuess = game.getNumberToGuess();

        assertTrue(numberToGuess >= 0 && numberToGuess <= 200);

    }
    @Test
    void makeAGuessBiggerTest() {
        // TODO: minValue should be informed by something, at least by boundaries which can be set from variables.
        int minValue = -1;

        GuessResult result = game.makeAGuess(minValue);
        GuessResult expectedResult = GuessResult.LESS;

        assertEquals(expectedResult, result);
    }
    @Test
    void makeAGuessLowerTest() {
        // TODO: maxValue should be informed by something, at least by boundaries which can be set from variables.
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

        var a = game.getCount();
        game.makeAGuess(5);
        var b = game.getCount();

        assertEquals(b - a, 1);
    }

    // TODO: Guarantee that game state is valid for every test (Probably recreate the game before every test,
    //  or inside tests where necessary.

    // TODO: Test example of the game
    // SetNumberToGuess(10)
    // makeAGuess(5)
    // makeAGuess(15)
    // makeAGuess(10)
    // assert guessed
    // assert count
}