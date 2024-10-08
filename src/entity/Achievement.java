package entity;

public class Achievement {

    private int totalPlayTime;
    private int totalScore;
    private double getHighAccuracy;
    private int currentPerfectStage;
    private boolean flawlessFailure;
    private boolean bestFriends;

    private final int[] ACCURACY_COIN_REWARD = {500, 1500, 2000, 2500};
    private final int[] PERFECT_COIN_REWARD = {200, 400, 800, 2000, 3000, 4000, 5000};
    private final int FLAWLESS_FAILURE_COIN = 1000;
    private final int BEST_FRIENDS_COIN = 1000;

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

    public int[] getAccuracyReward() {
        return ACCURACY_COIN_REWARD;
    }

    public int[] getPerfectReward() {
        return PERFECT_COIN_REWARD;
    }

    public int getFlawlessFailureReward() {
        return FLAWLESS_FAILURE_COIN;
    }

    public int getBestFriendsReward() {
        return BEST_FRIENDS_COIN;
    }

}