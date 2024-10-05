package engine;

import engine.GameState;

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
    private int checkPerfectAchievement = 1;
    private int psCoins = 0;
    private static int currentPerfectLevel = 0;
    private static int nextPerfectLevel = currentPerfectLevel + 1;
    private static boolean checkPerfect = true;
    private final int[] perfectStage = {0, 1, 2, 3, 4, 5, 6, 7}; // 퍼펙트 스테이지
    private final int[] perfectCoinReward = {2000, 3000, 4000, 5000}; // 퍼펙트 스테이지 리워드

    // 명중률 업적 관련 변수
    private List<String> accuracy; // 명중률 업적 리스트

    public AchievementManager() throws IOException {
        totalScore = FileManager.getInstance().loadTotalScore();
        totalTimePlay = FileManager.getInstance().loadTotalPlayTime();
        accuracy = FileManager.getInstance().loadAccuracyAchievement();

    }

    public void updateTotalTimePlay(int timePlay) throws IOException {
        this.totalTimePlay += timePlay;
        FileManager.getInstance().saveTotalPlayTime(totalTimePlay);
    }

    public void updateTotalScore(int score) throws IOException {
        totalScore += score;
        FileManager.getInstance().saveTotalScore(totalScore);
    }

    /** 명중률 업적을 업데이트 하는 함수. */
    public void updateAccuracyAchievement(double accuracy) throws IOException {
        // 명중률 업적 달성 시, 그 아래 있는 모든 업적을 같이 달성하는 조건.
        if (accuracy >= 100) {
            for (int i = 1; i <= 7; i += 2) {
                this.accuracy.set(i, "true");
            }
        } else if (accuracy >= 90) {
            for (int i = 1; i <= 5; i += 2) {
                this.accuracy.set(i, "true");
            }
        } else if (accuracy >= 80) {
            for (int i = 1; i <= 3; i += 2) {
                this.accuracy.set(i, "true");
            }
        } else if (accuracy >= 70) {
            this.accuracy.set(1, "true");
        }
        // 변경된 업적 저장.
        FileManager.getInstance().saveAccuracyAchievement(this.accuracy);
    }

    /** 퍼펙트 조건을 위반했는지 확인 */
    public void checkPerfect(final int checkPerfectAchievement) {
        if (checkPerfectAchievement < this.checkPerfectAchievement) {
            checkPerfect = false;
        }
    }

    /** 퍼펙트 업적을 달성했는지 확인 */
    public void checkPerfectAchievement(final int livesRemaining, final int gameLevel) throws IOException {
        checkPerfect(livesRemaining);
        // 현재 퍼펙트 달성 스테이지가 총 스테이지를 넘지 않았는지 확인.
        if (currentPerfectLevel < perfectStage.length) {
            // 마지막에 달성한 퍼펙트 스테이지 변수 저장.
            int requiredStage = perfectStage[currentPerfectLevel];
            // 현재 게임 스테이지가 마지막에 달성된 퍼펙트 스테이지보다 높고, 퍼펙트 조건을 위반했는지 확인.
            if (gameLevel > requiredStage && checkPerfect) {
                psCoins += perfectCoinReward[currentPerfectLevel];
                currentPerfectLevel++;
                nextPerfectLevel = currentPerfectLevel + 1;
                updateCurrentPerfectStage();
            }
        }
    }

    public void updateCurrentPerfectStage() throws IOException {
        engine.FileManager.getInstance().saveCurrentPsAchievement(currentPerfectLevel);
    }

    public static int getCurrentPerfectLevel() {
        return currentPerfectLevel;
    }

    public static int getNextPerfectLevel() {
        return nextPerfectLevel;
    }



}
