import java.util.ArrayList;
import java.util.List;

public class FrameStoringBowlingGame implements Game {

    private final List<Frame> frames = new ArrayList<>();

    @Override
    public void roll(int noOfPins) {
        Frame currentFrame;
        if (frames.isEmpty() || frames.get(frames.size() - 1).isComplete()) {
            currentFrame = new Frame();
            if (frames.size() == 9) {
                currentFrame.last = true;
            }
            frames.add(currentFrame);
        }
        else {
            currentFrame = frames.get(frames.size() - 1);
        }
        currentFrame.addScore(noOfPins);
    }

    @Override
    public int score() {
        int score = 0;
        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            score += frame.getScore();
            if (frame.isSpare() && i + 1 < frames.size()) {
                score += frames.get(i + 1).roll1;
            }
            if (frame.isStrike() && i + 1 < frames.size()) {
                Frame nextFrame = frames.get(i + 1);
                score += nextFrame.roll1;
                if (nextFrame.isStrike() && i + 2 < frames.size()) {
                    score += frames.get(i + 2).roll1;
                }
                else if (!nextFrame.isStrike() && nextFrame.isComplete()) {
                    score += nextFrame.roll2;
                }
            }
        }
        return score;
    }

    class Frame {

        private static final int UNKNOWN = -1;
        private static final int UNNECESSARY = -2;

        int roll1 = UNKNOWN;
        int roll2 = UNKNOWN;
        int roll3 = UNKNOWN;
        boolean last = false;


        boolean isSpare() {
            return roll1 != UNKNOWN && roll2 != UNKNOWN && roll1 + roll2 == 10;
        }

        void addScore(int score) {
            if (roll1 == UNKNOWN) {
                roll1 = score;
                if (score == 10 && !last) {
                    roll2 = UNNECESSARY;
                }
            }
            else if (roll2 == UNKNOWN){
                roll2 = score;
            }
            else {
                roll3 = score;
            }
        }

        boolean isStrike() {
            return roll1 != UNKNOWN && roll2 == UNNECESSARY;
        }

        boolean isComplete() {
            return roll1 != UNKNOWN && roll2 != UNKNOWN;
        }

        public int getScore() {
            return roll1 + ((roll2 != UNNECESSARY && roll2 != UNKNOWN) ? roll2 : 0) + (last && roll3 != UNKNOWN ? roll3 : 0);
        }
    }
}
