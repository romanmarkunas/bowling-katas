public class ProceduralBowlingGame implements Game {

    private static final int MAX_FRAMES = 10;

    private final int[] rolls = new int[21];
    private int currentRoll = 0;


    @Override
    public void roll(int pins) {
        rolls[currentRoll++] = pins;
    }

    @Override
    public int score() {
        int score = 0;
        int frameIndex = 0;
        for (int frameNr = 1; frameNr <= MAX_FRAMES && frameIndex < currentRoll; frameNr++) {
            int firstFrameRoll = rolls[frameIndex];
            score += firstFrameRoll;

            if (isStrike(firstFrameRoll)) {
                score += hasNextRoll(frameIndex) ? nextRollScore(frameIndex) : 0;
                score += hasSecondNextRoll(frameIndex) ? secondNextRollScore(frameIndex) : 0;
                frameIndex++;
            }
            else if (hasNextRoll(frameIndex)) {
                int secondFrameRoll = nextRollScore(frameIndex);
                score += secondFrameRoll;
                if (isSpare(firstFrameRoll, secondFrameRoll)) {
                    score += hasSecondNextRoll(frameIndex) ? secondNextRollScore(frameIndex) : 0;
                }
                frameIndex += 2;
            }
            else {
                frameIndex += 2;
            }
        }

        return score;
    }


    private boolean isStrike(int firstFrameRoll) {
        return firstFrameRoll == 10;
    }

    private boolean isSpare(int firstFrameRoll, int secondFrameRoll) {
        return firstFrameRoll + secondFrameRoll == 10;
    }

    private int secondNextRollScore(int frameIndex) {
        return rolls[frameIndex + 2];
    }

    private int nextRollScore(int frameIndex) {
        return rolls[frameIndex + 1];
    }

    private boolean hasSecondNextRoll(int frameIndex) {
        return frameIndex + 2 < currentRoll;
    }

    private boolean hasNextRoll(int frameIndex) {
        return frameIndex + 1 < currentRoll;
    }
}
