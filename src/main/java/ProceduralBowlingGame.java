public class ProceduralBowlingGame implements Game {

    private final int[] rolls = new int[21];
    private int currentRoll = 0;

    @Override
    public void roll(int noOfPins) {
        rolls[currentRoll++] = noOfPins;
    }

    @Override
    public int score() {
        int score = 0;
        int frameIndex = 0;
        for (int frameNumber = 1; frameNumber <= 10; frameNumber++) {
            if (frameIndex >= currentRoll) {
                return score;
            }

            if (rolls[frameIndex] == 10) {
                score += 10;
                if (frameIndex + 1 < currentRoll) {
                    score += rolls[frameIndex + 1];
                }
                if (frameIndex + 2 < currentRoll) {
                    score += rolls[frameIndex + 2];
                }
                frameIndex++;
            }
            else if (frameIndex + 1 < currentRoll) {
                int secondFrameRoll = rolls[frameIndex + 1];
                score += (rolls[frameIndex] + secondFrameRoll);
                if (rolls[frameIndex] + secondFrameRoll == 10) {
                    if (frameIndex + 2 < currentRoll) {
                        score += rolls[frameIndex + 2];
                    }
                }
                frameIndex += 2;
            }
            else {
                score += rolls[frameIndex];
                frameIndex += 2;
            }
        }

        return score;
    }
}
