package engine;

import engine.GameState;

import java.io.IOException;

public class AchievementManager {

    private int totalScore;

    public AchievementManager(final int totalScore) throws IOException {
        this.totalScore = FileManager.getInstance().loadTotalScore();
    }

    public final int getTotalScore() { return totalScore; }

    public void updateTotalScore(int score) throws IOException {
        totalScore += score;
        FileManager.getInstance().saveTotalScore(totalScore);
    }
}

