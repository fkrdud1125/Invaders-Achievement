package engine;

import engine.GameState;

import java.io.IOException;
import java.util.logging.Logger;

public class AchievementManager {

    private static final Logger logger = Logger.getLogger(AchievementManager.class.getName());

    private int totalScore;
    private int initialLives; // 시작 시의 라이프를 저장
    private int totalTimePlay;

    public AchievementManager(final int totalScore) throws IOException {
        this.totalScore = FileManager.getInstance().loadTotalScore();
        this.totalTimePlay = FileManager.getInstance().loadTotalPlayTime();
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

    private int psCurrentStage;
    private int psCoins;

    private final int[] psStageCondition = {4, 5, 6, 7};
    private final int[] psCoinRewards = {2000, 3000, 4000, 5000};

    private int currentPsAchievement = 0;
    private boolean checkPerfect = true;

    // Constructor to initialize with stages and coins
    public AchievementManager(int psCurrentStage, int psCoins, final GameState gameState) {
        this.psCurrentStage = psCurrentStage;
        this.psCoins = psCoins;
        this.initialLives = gameState.getLivesRemaining();  // 시작 시 생명 수 저장
    }

    public void updateCheckPerfect(final GameState gameState) {
        // 라이프가 줄어든 적이 한 번이라도 있으면 checkPerfect를 다시 true로 바꾸지 않음
        if (gameState.getLivesRemaining() < this.initialLives) {
            checkPerfect = false;  // 한 번이라도 라이프가 줄어들었으면 false로 고정
        }
        // 만약 한 번도 라이프가 줄어든 적이 없으면 유지
    }


    public void checkPsAchievement(final GameState gameState) {
        updateCheckPerfect(gameState); // 생명 상태 확인

        if (currentPsAchievement < psStageCondition.length) {
            int requiredStage = psStageCondition[currentPsAchievement];

            if (psCurrentStage >= requiredStage && checkPerfect) {
                psCoins += psCoinRewards[currentPsAchievement];
                currentPsAchievement++;
            }
        }
    }

    public void updatePsStage(int newStage, final GameState gameState) {
        this.psCurrentStage = newStage;
        checkPsAchievement(gameState);
    }

    // 새로운 함수: 특정 구간(스테이지 배열)을 완벽하게 클리어했는지 확인
    public boolean isPerfectRun(GameState gameState) {
        // psStageCondition 내의 현재 단계가 해당하는 구간인지를 확인
        if (currentPsAchievement < psStageCondition.length) {
            int requiredStage = psStageCondition[currentPsAchievement];

            // 스테이지가 조건에 맞고, 라이프가 한 번도 줄지 않았으면 true 반환
            if (psCurrentStage >= requiredStage && checkPerfect) {
                return true;
            }
        }
        return false; // 조건을 만족하지 않으면 false
    }


}
