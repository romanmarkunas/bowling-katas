package com.romanmarkunas;

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
                new Object[]{
                        (Supplier<Game>) ProceduralSingleDataStructureBowlingGame::new,
                        ProceduralSingleDataStructureBowlingGame.class.getSimpleName()
                },
                new Object[]{
                        (Supplier<Game>) InheritanceAndFactoryBowlingGame::new,
                        InheritanceAndFactoryBowlingGame.class.getSimpleName()
                },
                new Object[]{
                        (Supplier<Game>) ProceduralBowlingGame::new,
                        ProceduralBowlingGame.class.getSimpleName()
                }
        );
    }

    @Test
    public void initialScore() {
        assertEquals(0, gameUnderTest.score());
    }

    @Test
    public void worstGameEver() {
        for (int i = 0; i < 20; i++) {
            gameUnderTest.roll(0);
        }
        assertEquals(0, gameUnderTest.score());
    }

    @Test
    public void singleOpenFrame() {
        gameUnderTest.roll(5);
        gameUnderTest.roll(3);
        assertEquals(5 + 3, gameUnderTest.score());
    }

    @Test
    public void canAskScoreMidFrame() {
        gameUnderTest.roll(5);
        assertEquals(5, gameUnderTest.score());
    }

    @Test
    public void spareFollowedByRoll() {
        gameUnderTest.roll(7);
        gameUnderTest.roll(3);
        gameUnderTest.roll(6);
        assertEquals((7 + 3 + 6) + (6), gameUnderTest.score());
    }

    @Test
    public void spareFollowedByOpenFrameProvesOnlyOneRollIsBonus() {
        gameUnderTest.roll(7);
        gameUnderTest.roll(3);
        gameUnderTest.roll(6);
        gameUnderTest.roll(1);
        assertEquals((7 + 3 + 6) + (6 + 1), gameUnderTest.score());
    }

    @Test
    public void spareWith10In2ndRoll() {
        gameUnderTest.roll(0);
        gameUnderTest.roll(10);
        gameUnderTest.roll(6);
        gameUnderTest.roll(1);
        assertEquals((0 + 10 + 6) + (6 + 1), gameUnderTest.score());
    }

    @Test
    public void strikeFollowedByOpenFrame() {
        gameUnderTest.roll(10);
        gameUnderTest.roll(3);
        gameUnderTest.roll(6);
        assertEquals((10 + 3 + 6) + (3 + 6), gameUnderTest.score());
    }

    @Test
    public void canAskScoreMidFrameAfterStrike() {
        gameUnderTest.roll(10);
        gameUnderTest.roll(4);
        assertEquals((10 + 4) + (4), gameUnderTest.score());
    }

    @Test
    public void twoStrikesFollowedByOpenFrame() {
        gameUnderTest.roll(10);
        gameUnderTest.roll(10);
        gameUnderTest.roll(3);
        gameUnderTest.roll(6);
        assertEquals((10 + 10 + 3) + (10 + 3 + 6) + (3 + 6), gameUnderTest.score());
    }

    @Test
    public void turkey() {
        gameUnderTest.roll(10);
        gameUnderTest.roll(10);
        gameUnderTest.roll(10);
        gameUnderTest.roll(1);
        gameUnderTest.roll(1);
        assertEquals((10 + 10 + 10) + (10 + 10 + 1) + (10 + 1 + 1) + (1 + 1), gameUnderTest.score());
    }

    @Test
    public void perfectGame() {
        for (int i = 0; i < 12; i++) {
            gameUnderTest.roll(10);
        }
        assertEquals(300, gameUnderTest.score());
    }

    @Test
    public void almostPerfectGame() {
        for (int i = 0; i < 11; i++) {
            gameUnderTest.roll(10);
        }
        gameUnderTest.roll(2);
        assertEquals(292, gameUnderTest.score());
    }

    @Test
    public void openFrameInTheLastFrame() {
        for (int i = 0; i < 9; i++) {
            gameUnderTest.roll(3);
            gameUnderTest.roll(2);
        }
        gameUnderTest.roll(2);
        gameUnderTest.roll(2);
        assertEquals(9 * (2 + 3) + (2 + 2), gameUnderTest.score());
    }
}