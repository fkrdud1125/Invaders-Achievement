package engine;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AchievementManager {

    private static final Logger logger = Logger.getLogger(AchievementManager.class.getName());

    // 누적 점수
    private int totalScore;
    // 누적 플레이 시간
    private int totalTimePlay;

    // 퍼펙트 업적 관련 변수
    private int psCoins = 0;
    private static int currentPerfectLevel;
    private static int nextPerfectLevel;

    private final int MAX_PERFECT_STAGE = 7;
    private final int[] perfectCoinReward = {2000, 3000, 4000, 5000}; // 퍼펙트 스테이지 리워드

    // 명중률 업적 관련 변수
    private int accuracy; // 명중률 업적 리스트

    public AchievementManager() throws IOException {
        totalScore = FileManager.getInstance().loadTotalScore();
        totalTimePlay = FileManager.getInstance().loadTotalPlayTime();
        accuracy = FileManager.getInstance().loadAccuracyAchievement();
        currentPerfectLevel = FileManager.getInstance().loadPerfectAchievement();
        nextPerfectLevel = currentPerfectLevel + 1;
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
        // 명중률 업적 달성 시, 그 아래 있는 모든 업적을 같이 달성하는 조건.
        if (accuracy >= 100) {
            this.accuracy = 100;
        } else if (accuracy >= 90) {
            this.accuracy = 90;
        } else if (accuracy >= 80) {
            this.accuracy = 80;
        } else if (accuracy >= 70) {
            this.accuracy = 70;
        }
        // 변경된 업적 저장.
        FileManager.getInstance().saveAccuracyAchievement(this.accuracy);
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
            psCoins += perfectCoinReward[currentPerfectLevel];
            currentPerfectLevel += 1;
            nextPerfectLevel = currentPerfectLevel + 1;
            updateCurrentPerfectStage();
        }
    }

    public void updateFlawlessFailureAchievement() throws IOException {}

}
