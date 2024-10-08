package entity;

public class Achievement {

    private int totalPlayTime;
    private int totalScore;
    private double getHighAccuracy;
    private int currentPerfectStage;
    private boolean flawlessFailure;
    private boolean bestFriends;

    public Achievement(int totalPlayTime, int totalScore, double getHighAccuracy, int currentPerfectStage,
                       boolean flawlessFailure, boolean bestFriends) {
        this.totalPlayTime = totalPlayTime;
        this.totalScore = totalScore;
        this.getHighAccuracy = getHighAccuracy;
        this.currentPerfectStage = currentPerfectStage;
        this.flawlessFailure = flawlessFailure;
        this.bestFriends = bestFriends;
    }

    // 각 업적 상태를 가져오는 함수들.
    public int getTotalPlayTime() { return totalPlayTime; }
    public int getTotalScore() { return totalScore; }
    public double getHighAccuracy() { return getHighAccuracy; }
    public int getPerfectStage() { return currentPerfectStage; }
    public boolean getFlawlessFailure() { return flawlessFailure; }
    public boolean getBestFriends() { return bestFriends; }

    // 각 업적 상태를 저장하는 함들.
    public void setTotalPlayTime(int totalPlayTime) {
        this.totalPlayTime += totalPlayTime;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore += totalScore;
    }

    public void setHighAccuracy(double highAccuracy) {
        this.getHighAccuracy = highAccuracy;
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