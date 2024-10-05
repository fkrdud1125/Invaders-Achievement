package engine;

import engine.GameState;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AchievementManager {

    private static final Logger logger = Logger.getLogger(AchievementManager.class.getName());

    private int totalScore;
    private int initialLives = 1; // 시작 시의 라이프를 저장
    private int totalTimePlay;
    private List<String> achievemets;
    private double highestAccuracy = 0;
    private int achievementCount = 0;

    public AchievementManager(final int totalScore) throws IOException {
        this.totalScore = FileManager.getInstance().loadTotalScore();
        this.totalTimePlay = FileManager.getInstance().loadTotalPlayTime();
        //업적 파일이 없을 경우 기본파일 생성
        FileManager.getInstance().createDefaultAchievementsFile();
        //업적 파일 로드
        achievemets = FileManager.getInstance().loadAchievements();

    }

    public final int getTotalScore() {
        return totalScore;
    }

    public void updateTotalTimePlay(int timePlay) throws IOException {
        this.totalTimePlay += timePlay;
        FileManager.getInstance().saveTotalPlayTime(totalTimePlay);
    }

    public void updateTotalScore(int score) throws IOException {
        totalScore += score;
        FileManager.getInstance().saveTotalScore(totalScore);
    }

    public void updateAccuracyAchievement(float accuracy) throws IOException {
        if (accuracy >= 100) {
            achievemets.set(7, "true"); // 100% 업적 달성
            achievemets.set(5, "true"); // 90% 업적 자동 달성
            achievemets.set(3, "true"); // 80% 업적 자동 달성
            achievemets.set(1, "true"); // 70% 업적 자동 달성
            achievementCount = 8; // 카운트는 100%까지 달성
        } else if (accuracy >= 90) {
            achievemets.set(5, "true"); // 90% 업적 달성
            achievemets.set(3, "true"); // 80% 업적 자동 달성
            achievemets.set(1, "true"); // 70% 업적 자동 달성
            achievementCount = 6; // 카운트는 90%까지 달성
        } else if (accuracy >= 80) {
            achievemets.set(3, "true"); // 80% 업적 달성
            achievemets.set(1, "true"); // 70% 업적 자동 달성
            achievementCount = 4; // 카운트는 80%까지 달성
        } else if (accuracy >= 70) {
            achievemets.set(1, "true"); // 70% 업적 달성
            achievementCount = 2; // 카운트는 70%까지 달성
        }

        FileManager.getInstance().saveAchievements(achievemets); // 변경된 업적 상태 저장
    }

    // 각각의 업적이 달성되었는지 확인하는 메서드들
    public boolean isAccuracy70Achieved() {
        return achievemets.get(1).equals("true");
    }

    public boolean isAccuracy80Achieved() {
        return achievemets.get(3).equals("true");
    }

    public boolean isAccuracy90Achieved() {
        return achievemets.get(5).equals("true");
    }

    public boolean isAccuracy100Achieved() {
        return achievemets.get(7).equals("true");
    }

    public int getAchievementCount() {
        return achievementCount;
    }

    public double getHighestAccuracy() {
        return highestAccuracy;
    }



    private int psCoins;

    private final int[] psStageCondition = {0, 1, 2, 3, 4, 5, 6};
    private final int[] psCoinRewards = {500, 1000, 1500, 2000, 3000, 4000, 5000};

    private static int currentPsAchievement = 0;
    private static int nextPsAchievement = currentPsAchievement + 1;
    private boolean checkPerfect = true;

    // Constructor to initialize with stages and coins
    public AchievementManager(int psCoins, final GameState gameState) {
        this.psCoins = psCoins;
        this.initialLives = gameState.getLivesRemaining();  // 시작 시 생명 수 저장
    }

    public void updateCheckPerfect(final int livesRemaining) {
        // 라이프가 줄어든 적이 한 번이라도 있으면 checkPerfect를 다시 true로 바꾸지 않음
        if (livesRemaining < this.initialLives) {
            checkPerfect = false;  // 한 번이라도 라이프가 줄어들었으면 false로 고정
        }
        // 만약 한 번도 라이프가 줄어든 적이 없으면 유지
    }


    public void checkPsAchievement(final int livesRemaining, final int gameLevel) {
        updateCheckPerfect(livesRemaining); // 생명 상태 확인

        if (currentPsAchievement < psStageCondition.length) {
            int requiredStage = psStageCondition[currentPsAchievement];

            if (gameLevel > requiredStage && checkPerfect) {
                psCoins += psCoinRewards[currentPsAchievement];
                currentPsAchievement++;
                nextPsAchievement = currentPsAchievement + 1;
            }
        }
    }


    public static int getCurrentPsAchievement() {
        return currentPsAchievement;
    }

    public static int getNextPsAchievement() {
        return nextPsAchievement;
    }



}
