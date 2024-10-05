package engine;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AchievementManager {

    private static final Logger logger = Logger.getLogger(AchievementManager.class.getName());

    public GameState gameState;
    private int totalScore;
    private int checkPerfect = 3; // 시작 시의 라이프를 저장
    private int totalTimePlay; // 누적 플레이 시간
    private List<String> accuracy; // 명중률 업적 리스트
    private int achievementCount = 0; // 업적 카운팅

    private final int[] psStageCondition = {0, 1, 2, 3, 4, 5, 6, 7}; // 퍼펙트 스테이지
    private final int[] psCoinRewards = {2000, 3000, 4000, 5000}; // 퍼펙트 스테이지 리워드

    private static int currentPsAchievement = 0;
    private static int nextPsAchievement = currentPsAchievement + 1;
    private boolean checkPerfect = true;

    public AchievementManager() throws IOException {
        this.totalScore = FileManager.getInstance().loadTotalScore();
        this.totalTimePlay = FileManager.getInstance().loadTotalPlayTime();
        //업적 파일이 없을 경우 기본파일 생성
        FileManager.getInstance().createDefaultAchievementsFile();
        //업적 파일 로드
        accuracy = FileManager.getInstance().loadAchievements();

    }

    public void updateTotalTimePlay(int timePlay) throws IOException {
        this.totalTimePlay += timePlay;
        FileManager.getInstance().saveTotalPlayTime(totalTimePlay);
    }

    public void updateTotalScore(int score) throws IOException {
        totalScore += score;
        FileManager.getInstance().saveTotalScore(totalScore);
    }

    public void updateAccuracyAchievement(double accuracy) throws IOException {
        if (accuracy >= 100) {
            this.accuracy.set(7, "true"); // 100% 업적 달성
            this.accuracy.set(5, "true"); // 90% 업적 자동 달성
            this.accuracy.set(3, "true"); // 80% 업적 자동 달성
            this.accuracy.set(1, "true"); // 70% 업적 자동 달성
            achievementCount = 8; // 카운트는 100%까지 달성
        } else if (accuracy >= 90) {
            this.accuracy.set(5, "true"); // 90% 업적 달성
            this.accuracy.set(3, "true"); // 80% 업적 자동 달성
            this.accuracy.set(1, "true"); // 70% 업적 자동 달성
            achievementCount = 6; // 카운트는 90%까지 달성
        } else if (accuracy >= 80) {
            this.accuracy.set(3, "true"); // 80% 업적 달성
            this.accuracy.set(1, "true"); // 70% 업적 자동 달성
            achievementCount = 4; // 카운트는 80%까지 달성
        } else if (accuracy >= 70) {
            this.accuracy.set(1, "true"); // 70% 업적 달성
            achievementCount = 2; // 카운트는 70%까지 달성
        }

        FileManager.getInstance().saveAchievements(this.accuracy); // 변경된 업적 상태 저장
    }

    // 각각의 업적이 달성되었는지 확인하는 메서드들

    public boolean isAccuracy70Achieved() {
        return accuracy.get(1).equals("true");
    }

    public boolean isAccuracy80Achieved() {
        return accuracy.get(3).equals("true");
    }

    public boolean isAccuracy90Achieved() {
        return accuracy.get(5).equals("true");
    }

    public boolean isAccuracy100Achieved() {
        return accuracy.get(7).equals("true");
    }

    public int getAchievementCount() {
        return achievementCount;
    }

    public void updateCheckPerfect(final int livesRemaining) {
        // 라이프가 줄어든 적이 한 번이라도 있으면 checkPerfect를 다시 true로 바꾸지 않음
        if (livesRemaining < this.initialLives) {
            checkPerfect = false;  // 한 번이라도 라이프가 줄어들었으면 false로 고정
        }
        // 만약 한 번도 라이프가 줄어든 적이 없으면 유지
    }

    public void checkPsAchievement(final int livesRemaining) {
        if (livesRemaining < this.initialLives) {
            checkPerfect = false;  // 한 번이라도 라이프가 줄어들었으면 false로 고정
        }
        if (currentPsAchievement < psStageCondition.length) {
            int requiredStage = psStageCondition[currentPsAchievement];
            if (currentPsAchievement >= requiredStage) {
                gameState.getInstance().addCoins(psCoinRewards[currentPsAchievement]);
                currentPsAchievement++;
                nextPsAchievement++;
            }
        }

    }

    public static int getCurrentPsAchievement() {
        return currentPsAchievement;
    }

    public static int getNextPsAchievement() {
        return nextPsAchievement;
    }

    // 새로운 함수: 특정 구간(스테이지 배열)을 완벽하게 클리어했는지 확인
    public boolean isPerfectRun(GameState gameState) {
        // psStageCondition 내의 현재 단계가 해당하는 구간인지를 확인
        if (currentPsAchievement < psStageCondition.length) {
            int requiredStage = psStageCondition[currentPsAchievement];

            // 스테이지가 조건에 맞고, 라이프가 한 번도 줄지 않았으면 true 반환
            if (currentPsAchievement >= requiredStage && checkPerfect) {
                return true;
            }
        }
        return false; // 조건을 만족하지 않으면 false
    }


}
