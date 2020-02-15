import java.util.ArrayList;
import java.util.List;

public class InheritanceAndFactoryBowlingGame implements Game {

    private final List<Frame> frames = new ArrayList<>();
    private int currentFrame = 1;
    private boolean thisFrameRolled = false;

    @Override
    public void roll(int noOfPins) {
        if (currentFrame == 10) {
            Frame frame;
            if (frames.size() < 10) {
                frame = new LastFrame();
                frames.add(frame);
            }
            else {
                frame = frames.get(9);
            }
            frame.roll(noOfPins);
        }
        else if (noOfPins == 10) {
            frames.add(new StrikeFrame());
            currentFrame++;
        }
        else {
            Frame frame;
            if (currentFrame > frames.size()) {
                frame = new TwoRollFrame();
                frames.add(frame);
            }
            else {
                frame = frames.get(currentFrame - 1);
            }
            frame.roll(noOfPins);

            if (thisFrameRolled) {
                thisFrameRolled = false;
                currentFrame++;
            }
            else {
                thisFrameRolled = true;
            }
        }
    }

    @Override
    public int score() {
        int score = 0;
        for (int i = 0; i < frames.size(); i++) {
            score += frames.get(i).score(frames.subList(i + 1, frames.size()));
        }
        return score;
    }

    private interface Frame {
        void roll(int pins);

        int getRoll1();

        int getRoll2();

        int score(List<Frame> consecutiveFrames);
    }

    private static class LastFrame implements Frame {
        private int[] rolls = new int[3];
        private int currentRoll = 1;

        @Override
        public void roll(int pins) {
            rolls[currentRoll++ - 1] = pins;
        }

        @Override
        public int getRoll1() {
            return rolls[0];
        }

        @Override
        public int getRoll2() {
            return rolls[1];
        }

        @Override
        public int score(List<Frame> consecutiveFrames) {
            int score = 0;
            for (int roll : rolls) {
                score += roll;
            }
            return score;
        }
    }

    private static class StrikeFrame implements Frame {
        @Override
        public void roll(int pins) {
            throw new IllegalStateException("Creating StrikeFrame assumes single roll of 10");
        }

        @Override
        public int getRoll1() {
            return 10;
        }

        @Override
        public int getRoll2() {
            throw new IllegalStateException("No second roll on strike frame");
        }

        @Override
        public int score(List<Frame> consecutiveFrames) {
            int score = 10;
            if (consecutiveFrames.size() > 0) {
                if (consecutiveFrames.get(0) instanceof StrikeFrame) {
                    score += 10;
                    if (consecutiveFrames.size() > 1) {
                        score += consecutiveFrames.get(1).getRoll1();
                    }
                }
                else {
                    score += consecutiveFrames.get(0).getRoll1();
                    score += consecutiveFrames.get(0).getRoll2();
                }
            }
            return score;
        }
    }

    private static class TwoRollFrame implements Frame {
        private int roll1 = 0;
        private int roll2 = 0;
        private boolean rolledFirst = false;

        @Override
        public void roll(int pins) {
            if (!rolledFirst) {
                rolledFirst = true;
                roll1 = pins;
            }
            else {
                roll2 = pins;
            }
        }

        @Override
        public int getRoll1() {
            return roll1;
        }

        @Override
        public int getRoll2() {
            return roll2;
        }

        @Override
        public int score(List<Frame> consecutiveFrames) {
            int score = roll1 + roll2;
            if (score == 10 && consecutiveFrames.size() > 0) {
                score += consecutiveFrames.get(0).getRoll1();
            }
            return score;
        }
    }
}
