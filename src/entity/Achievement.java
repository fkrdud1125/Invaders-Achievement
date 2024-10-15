package entity;

public class Achievement {

    private int totalPlayTime;
    private int totalScore;
    private int maxCombo;
    private int currentPerfectStage;
    private boolean flawlessFailure;
    private boolean bestFriends;

    public Achievement(int totalPlayTime, int totalScore, int maxCombo, int currentPerfectStage,
                       boolean flawlessFailure, boolean bestFriends) {
        this.totalPlayTime = totalPlayTime;
        this.totalScore = totalScore;
        this.maxCombo = maxCombo;
        this.currentPerfectStage = currentPerfectStage;
        this.flawlessFailure = flawlessFailure;
        this.bestFriends = bestFriends;
    }

    // Functions to get the status of each achievement.
    public int getTotalPlayTime() { return totalPlayTime; }
    public int getTotalScore() { return totalScore; }
    public int getHighmaxCombo() { return maxCombo; }
    public int getPerfectStage() { return currentPerfectStage; }
    public boolean getFlawlessFailure() { return flawlessFailure; }
    public boolean getBestFriends() { return bestFriends; }

    // Functions to store the status of each achievement.
    public void setTotalPlayTime(int totalPlayTime) {
        this.totalPlayTime += totalPlayTime;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore += totalScore;
    }

    public void setHighMaxcombo(int maxCombo) {
        this.maxCombo = maxCombo;
    }

    public void setCurrentPerfectStage(int currentPerfectStage) {
        this.currentPerfectStage = currentPerfectStage;
    }

    public void setFlawlessFailure(boolean flawlessFailure) {
        this.flawlessFailure = flawlessFailure;
    }

    public void setBestFriends(boolean bestFriends) {
        this.bestFriends = bestFriends;
    }

}