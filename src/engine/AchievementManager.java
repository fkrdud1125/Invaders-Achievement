package engine;

import entity.Achievement;
import entity.Wallet;

import java.io.IOException;
import java.util.logging.Logger;

public class AchievementManager {

    /** 업적과 관련된 부분을 관리를 쉽게하기 위해 AchievementManager Class 생성 */

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

    private final int[] ACCURACY_COIN_REWARD = {500, 1500, 2000, 2500};
    private final int[] PERFECT_COIN_REWARD = {200, 400, 800, 2000, 3000, 4000, 5000};
    private final int FLAWLESS_FAILURE_COIN = 1000;
    private final int BEST_FRIENDS_COIN = 1000;

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

    public void updateTotalPlayTime(int playTime) {
        achievement.setTotalPlayTime(playTime);
    }

    public void updateTotalScore(int score) {
        achievement.setTotalScore(score);
    }

    /**
     * 명중률 업적을 업데이트 하는 함수.
     */
    public void updateAccuracy(double accuracy) {
        if (highAccuracy > accuracy) {
            return;
        }
        highAccuracy = accuracy;
        // 명중률 업적 달성 시, 그 아래 있는 모든 업적을 같이 달성하는 조건.
        if (highAccuracy >= 100) {
            for (int i = 0; i < 4; i++) {
                coinReward += ACCURACY_COIN_REWARD[i];
            }
        } else if (highAccuracy >= 90) {
            for (int i = 0; i < 3; i++) {
                coinReward += ACCURACY_COIN_REWARD[i];
            }
        } else if (highAccuracy >= 80) {
            for (int i = 0; i < 2; i++) {
                coinReward += ACCURACY_COIN_REWARD[i];
            }
        } else if (highAccuracy >= 70) {
            coinReward += ACCURACY_COIN_REWARD[0];
        }
        // 변경된 업적 저장.
        achievement.setHighAccuracy(highAccuracy);
    }
    /**
     * 퍼펙트 업적을 달성했는지 확인
     */
    public void updatePerfect(final int MAX_LIVES, int checkLives, int gameLevel) {
        if (checkLives >= MAX_LIVES && currentPerfectLevel < MAX_PERFECT_STAGE && gameLevel > currentPerfectLevel) {
            // 현재 퍼펙트 달성 스테이지가 총 스테이지를 넘지 않았는지 확인.
            currentPerfectLevel += 1;
            coinReward += PERFECT_COIN_REWARD[currentPerfectLevel-1];
            achievement.setCurrentPerfectStage(currentPerfectLevel);
        }
    }

    public void updateFlawlessFailure(double accuracy) {
        if (!checkFlawlessFailure && accuracy <= 0) {
            checkFlawlessFailure = true;
            coinReward += FLAWLESS_FAILURE_COIN;
            achievement.setFlawlessFailure(true);
        }
    }

    public void updateBestFriends(boolean checkTwoPlayMode) {
        if (!checkBestFriends && checkTwoPlayMode) {
            checkBestFriends = true;
            coinReward += BEST_FRIENDS_COIN;
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
