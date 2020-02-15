import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BowlingGameTest {

    private final Game gameUnderTest;

    public BowlingGameTest(Supplier<Game> gameConstructor, String ignoredGameClassName) {
        this.gameUnderTest = gameConstructor.get();
    }

    @Parameterized.Parameters(name = "{index} : {1}")
    public static Collection<Object[]> games() {
        return asList(
                new Object[] {
                        (Supplier<Game>) ProceduralSingleDataStructureBowlingGame::new,
                        ProceduralSingleDataStructureBowlingGame.class.getSimpleName()
                },
                new Object[] {
                        (Supplier<Game>) InheritanceAndFactoryBowlingGame::new,
                        InheritanceAndFactoryBowlingGame.class.getSimpleName()
                }
        );
    }


    @Test
    public void shouldHaveSameScoreAsPinsRolledAfterFirstRoll() {
        gameUnderTest.roll(5);
        assertEquals(5, gameUnderTest.score());
    }

    @Test
    public void shouldHaveSameScoreOfSumOfRollsAfter2RollsNoSpare() {
        gameUnderTest.roll(5);
        gameUnderTest.roll(3);
        assertEquals(5 + 3, gameUnderTest.score());
    }

    @Test
    public void shouldCorrectlyCalculateScoreAfterSpareAndRoll() {
        gameUnderTest.roll(7);
        gameUnderTest.roll(3);
        gameUnderTest.roll(6);
        assertEquals(7 + 3 + 6 + 6, gameUnderTest.score());
    }

    @Test
    public void shouldCorrectlyCalculateScoreAfterStrikeAnd2Rolls() {
        gameUnderTest.roll(10);
        gameUnderTest.roll(3);
        gameUnderTest.roll(6);
        assertEquals(10 + 3 + 6 + 3 + 6, gameUnderTest.score());
    }

    @Test
    public void shouldCorrectlyCalculateScoreAfter2StrikesAnd2Rolls() {
        gameUnderTest.roll(10);
        gameUnderTest.roll(10);
        gameUnderTest.roll(3);
        gameUnderTest.roll(6);
        assertEquals(10 + 10 + 3 + 10 + 3 + 6 + 3 + 6, gameUnderTest.score());
    }

    @Test
    public void shouldCorrectlyCalculateWorstGameEver() {
        for (int i = 0; i < 20; i++) {
            gameUnderTest.roll(0);
        }
        assertEquals(0, gameUnderTest.score());
    }

    @Test
    public void shouldCorrectlyCalculateBestGameEver() {
        for (int i = 0; i < 12; i++) {
            gameUnderTest.roll(10);
        }
        assertEquals(300, gameUnderTest.score());
    }

    @Test
    public void shouldCorrectlyCalculate10StrikesAndRoll() {
        for (int i = 0; i < 11; i++) {
            gameUnderTest.roll(10);
        }
        gameUnderTest.roll(2);
        assertEquals(292, gameUnderTest.score());
    }

    @Test
    public void shouldCorrectlyCalculateScoreIfNoSpareOrStrikeInLastFrame() {
        for (int i = 0; i < 9; i++) {
            gameUnderTest.roll(3);
            gameUnderTest.roll(2);
        }
        gameUnderTest.roll(2);
        gameUnderTest.roll(2);
        assertEquals(49, gameUnderTest.score());
    }
}