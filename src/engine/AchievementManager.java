package engine;

import engine.GameState;

import java.io.IOException;

public class AchievementManager {

    private int totalScore;
    private int totalTimePlay;

    public AchievementManager(final int totalScore) throws IOException {
        this.totalScore = FileManager.getInstance().loadTotalScore();
        this.totalTimePlay = FileManager.getInstance().loadTotalPlayTime();
    }

    public final int getTotalScore() { return totalScore; }

    public void updateTotalTimePlay(int timePlay) throws IOException {
        this.totalTimePlay += timePlay;
        FileManager.getInstance().saveTotalPlayTime(totalTimePlay);
    }

    public void updateTotalScore(int score) throws IOException {
        totalScore += score;
        FileManager.getInstance().saveTotalScore(totalScore);
    }


}

