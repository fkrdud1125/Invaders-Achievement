package engine;

import entity.Wallet;

import java.io.IOException;
import java.util.logging.Logger;

public class AchievementManager {

    private static final Logger logger = Logger.getLogger(AchievementManager.class.getName());

    // 누적 점수
    private int totalScore;
    // 누적 플레이 시간
    private int totalTimePlay;
    // 퍼펙트 업적 관련 변수
    private static int currentPerfectLevel;
    private static int nextPerfectLevel;
    private final int MAX_PERFECT_STAGE = 7;
    private final int[] PERFECT_COIN_REWARD = {100, 200, 400, 800, 2000, 3000, 4000, 5000}; // 퍼펙트 스테이지 리워드

    // 명중률 업적 관련 변수
    private double accuracy; // 명중률 업적 리스트
    private final int[] ACCURACY_COIN_REWARD = {2000, 3000, 4000, 5000};

    // Flawless Failure 업적 관련 변수
    private boolean checkFlawlessFailure;
    private final int FLAWLESS_FAILURE_COIN = 1000;
    // Best Friends 업적 관련 변수

    private boolean checkBestFriends;
    private final int BEST_FRIENDS_COIN = 1000;

    // Coin 갱신
    private Wallet wallet;
    private int coinReward;

    public AchievementManager() throws IOException {
        totalScore = FileManager.getInstance().loadTotalScore();
        totalTimePlay = FileManager.getInstance().loadTotalPlayTime();
        accuracy = FileManager.getInstance().loadAccuracyAchievement();
        currentPerfectLevel = FileManager.getInstance().loadPerfectAchievement();
        nextPerfectLevel = currentPerfectLevel + 1;
        checkFlawlessFailure = FileManager.getInstance().loadFlawlessFailureAchievement();
        checkBestFriends = FileManager.getInstance().loadBestFriendsAchievement();
        wallet = new Wallet();
    }

    public void updateTotalTimePlay(int timePlay) throws IOException {
        this.totalTimePlay += timePlay;
        FileManager.getInstance().saveTotalPlayTime(totalTimePlay);
    }

    public void updateTotalScore(int score) throws IOException {
        totalScore += score;
        FileManager.getInstance().saveTotalScore(totalScore);
    }

    /**
     * 명중률 업적을 업데이트 하는 함수.
     */
    public void updateAccuracyAchievement(double accuracy) throws IOException {
        if (this.accuracy > accuracy) {
            return;
        }
        // 명중률 업적 달성 시, 그 아래 있는 모든 업적을 같이 달성하는 조건.
        if (accuracy >= 100) {
            for (int i = 0; i < 4; i++) {
                coinReward += ACCURACY_COIN_REWARD[i];
            }
            this.accuracy = 100;
        } else if (accuracy >= 90) {
            for (int i = 0; i < 3; i++) {
                coinReward += ACCURACY_COIN_REWARD[i];
            }
            this.accuracy = 90;
        } else if (accuracy >= 80) {
            for (int i = 0; i < 2; i++) {
                coinReward += ACCURACY_COIN_REWARD[i];
            }
            this.accuracy = 80;
        } else if (accuracy >= 70) {
            coinReward += ACCURACY_COIN_REWARD[0];
            this.accuracy = 70;
        }
        // 변경된 업적 저장.
        FileManager.getInstance().saveAccuracyAchievement(accuracy);
        wallet.deposit(coinReward);
    }

    public void updateCurrentPerfectStage() throws IOException {
        FileManager.getInstance().savePerfectAchievement(currentPerfectLevel);
    }

    /**
     * 퍼펙트 업적을 달성했는지 확인
     */
    public void updatePerfectAchievement(final int MAX_LIVES, int checkLives, int gameLevel) throws IOException {
        if (checkLives >= MAX_LIVES && currentPerfectLevel < MAX_PERFECT_STAGE && gameLevel > currentPerfectLevel) {
            // 현재 퍼펙트 달성 스테이지가 총 스테이지를 넘지 않았는지 확인.
            currentPerfectLevel += 1;
            nextPerfectLevel = currentPerfectLevel + 1;
            updateCurrentPerfectStage();
            wallet.deposit(PERFECT_COIN_REWARD[currentPerfectLevel-1]);
        }
    }

    public void updateFlawlessFailureAchievement(double accuracy) throws IOException {
        if (!checkFlawlessFailure && accuracy <= 0) {
            checkFlawlessFailure = true;
            FileManager.getInstance().saveFlawlessFailureAchievement(checkFlawlessFailure);
            wallet.deposit(FLAWLESS_FAILURE_COIN);
        }
    }

    public void updateBestFriendsAchievement(boolean checkTwoPlayMode) throws IOException {
        if (!checkBestFriends && checkTwoPlayMode) {
            checkBestFriends = true;
            FileManager.getInstance().saveBestFriendsAchievement(checkBestFriends);
            wallet.deposit(BEST_FRIENDS_COIN);
        }
    }
    public void updatePlaying(int playtime,int max_lives,int LivesRemaining, int level ) throws IOException{
        updateTotalTimePlay(playtime);
        updatePerfectAchievement(max_lives,LivesRemaining,level);
    }

    public void updatePlayed(double accuracy, int score, boolean MultiPlay) throws IOException{
        updateAccuracyAchievement(accuracy);
        updateTotalScore(score);
        updateFlawlessFailureAchievement(accuracy);
        updateBestFriendsAchievement(MultiPlay);
    }
}
