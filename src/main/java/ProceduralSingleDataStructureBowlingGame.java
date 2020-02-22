import java.util.ArrayList;
import java.util.List;

public class ProceduralSingleDataStructureBowlingGame implements Game {

    private final List<Frame> frames = new ArrayList<>();
    private Frame currentFrame = null;

    @Override
    public void roll(int pins) {
        if (this.currentFrame == null || this.currentFrame.isComplete()) {
            this.currentFrame = new Frame(frames.size() == 9);
            frames.add(currentFrame);
        }
        currentFrame.roll(pins);
    }

    @Override
    public int score() {
        int score = 0;
        for (int frameNr = 0; frameNr < frames.size(); frameNr++) {
            Frame frame = frames.get(frameNr);
            score += frame.getScore();

            if (frame.isSpare() && hasNextFrame(frameNr)) {
                score += getNextFrame(frameNr).getFirstRollScore();
            }
            else if (frame.isStrike() && hasNextFrame(frameNr)) {
                Frame nextFrame = getNextFrame(frameNr);
                score += nextFrame.getScore();
                if (nextFrame.isStrike() && hasSecondNextFrame(frameNr)) {
                    score += getSecondNextFrame(frameNr).getFirstRollScore();
                }
            }
        }
        return score;
    }


    private boolean hasNextFrame(int frameNr) {
        return frameNr + 1 < frames.size();
    }

    private Frame getNextFrame(int frameNr) {
        return frames.get(frameNr + 1);
    }

    private boolean hasSecondNextFrame(int frameNr) {
        return frameNr + 2 < frames.size();
    }

    private Frame getSecondNextFrame(int frameNr) {
        return frames.get(frameNr + 2);
    }


    private static class Frame {

        private static final int UNKNOWN = -1;
        private static final int UNNECESSARY = -2;

        private final boolean last;
        private int roll1 = UNKNOWN;
        private int roll2 = UNKNOWN;
        private int roll3 = UNKNOWN;


        private Frame(boolean last) {
            this.last = last;
        }


        boolean isSpare() {
            return roll1 != UNKNOWN && roll2 != UNKNOWN && roll1 + roll2 == 10;
        }

        void roll(int pins) {
            if (roll1 == UNKNOWN) {
                roll1 = pins;
                if (pins == 10 && !last) {
                    roll2 = UNNECESSARY;
                }
            }
            else if (roll2 == UNKNOWN){
                roll2 = pins;
            }
            else {
                roll3 = pins;
            }
        }

        boolean isStrike() {
            return roll1 != UNKNOWN && roll2 == UNNECESSARY;
        }

        boolean isComplete() {
            return roll1 != UNKNOWN && roll2 != UNKNOWN;
        }

        public int getScore() {
            int score = roll1;
            score += (roll2 != UNNECESSARY && roll2 != UNKNOWN) ? roll2 : 0;
            score += (last && roll3 != UNKNOWN ? roll3 : 0);
            return score;
        }

        public int getFirstRollScore() {
            return roll1;
        }
    }
}
