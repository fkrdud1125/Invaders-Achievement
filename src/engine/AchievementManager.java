package engine;

import entity.Achievement;
import entity.Wallet;

import java.io.IOException;
import java.util.logging.Logger;

public class AchievementManager {

    private static final Logger logger = Logger.getLogger(AchievementManager.class.getName());


    private Achievement achievement;

    // 퍼펙트 업적 관련 변수
    private static int currentPerfectLevel;
    private final int MAX_PERFECT_STAGE = 7;

    // 명중률 업적 관련 변수
    private double highAccuracy; // 명중률 업적 리스트

    // Flawless Failure 업적 관련 변수
    private boolean checkFlawlessFailure;

    // Best Friends 업적 관련 변수
    private boolean checkBestFriends;

    // Coin 갱신
    private int coinReward = 0;

    // 각 업적에 필요한 변수들을 파일을 통해 입력 받음.
    public AchievementManager() throws IOException {
        achievement = FileManager.getInstance().loadAchievement();
        this.currentPerfectLevel = achievement.getPerfectStage();
        this.highAccuracy = achievement.getHighAccuracy();
        this.checkFlawlessFailure = achievement.getFlawlessFailure();
        this.checkBestFriends = achievement.getBestFriends();
    }

    public int getAchievementReward() {
        return coinReward;
    }

    public void updateTotalPlayTime(int playTime) throws IOException {
        achievement.setTotalPlayTime(playTime);
    }

    public void updateTotalScore(int score) throws IOException {
        achievement.setTotalScore(score);
    }

    /**
     * 명중률 업적을 업데이트 하는 함수.
     */
    public void updateAccuracy(double accuracy) throws IOException {
        if (highAccuracy > accuracy) {
            return;
        }
        int[] accuracyReward = achievement.getAccuracyReward();
        highAccuracy = accuracy;
        // 명중률 업적 달성 시, 그 아래 있는 모든 업적을 같이 달성하는 조건.
        if (highAccuracy >= 100) {
            for (int i = 0; i < 4; i++) {
                coinReward += accuracyReward[i];
            }
        } else if (highAccuracy >= 90) {
            for (int i = 0; i < 3; i++) {
                coinReward += accuracyReward[i];
            }
        } else if (highAccuracy >= 80) {
            for (int i = 0; i < 2; i++) {
                coinReward += accuracyReward[i];
            }
        } else if (highAccuracy >= 70) {
            coinReward += accuracyReward[0];
        }
        // 변경된 업적 저장.
        achievement.setHighAccuracy(highAccuracy);
    }
    /**
     * 퍼펙트 업적을 달성했는지 확인
     */
    public void updatePerfect(final int MAX_LIVES, int checkLives, int gameLevel) throws IOException {
        if (checkLives >= MAX_LIVES && currentPerfectLevel < MAX_PERFECT_STAGE && gameLevel > currentPerfectLevel) {
            // 현재 퍼펙트 달성 스테이지가 총 스테이지를 넘지 않았는지 확인.
            currentPerfectLevel += 1;
            coinReward += achievement.getPerfectReward()[currentPerfectLevel-1];
            achievement.setCurrentPerfectStage(currentPerfectLevel);
        }
    }

    public void updateFlawlessFailure(double accuracy) throws IOException {
        if (!checkFlawlessFailure && accuracy <= 0) {
            checkFlawlessFailure = true;
            coinReward += achievement.getFlawlessFailureReward();
            achievement.setFlawlessFailure(true);
        }
    }

    public void updateBestFriends(boolean checkTwoPlayMode) throws IOException {
        if (!checkBestFriends && checkTwoPlayMode) {
            checkBestFriends = true;
            coinReward += achievement.getBestFriendsReward();
            achievement.setBestFriends(true);
        }
    }

    public void updateAllAchievements() throws IOException {
        FileManager.getInstance().saveAchievement(achievement);
    }

    public void updatePlaying(int playtime, int max_lives, int LivesRemaining, int level ) throws IOException{
        updateTotalPlayTime(playtime);
        updatePerfect(max_lives,LivesRemaining,level);
    }

    public void updatePlayed(double accuracy, int score, boolean multiPlay) throws IOException{
        updateAccuracy(accuracy);
        updateTotalScore(score);
        updateFlawlessFailure(accuracy);
        updateBestFriends(multiPlay);
    }
}
