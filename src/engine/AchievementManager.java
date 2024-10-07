package engine;

import entity.Wallet;

import java.io.IOException;
import java.util.logging.Logger;

public class AchievementManager {

    /** 업적과 관련된 부분을 관리를 쉽게하기 위해 AchievementManager Class 생성 */

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

    // 각 업적에 필요한 변수들을 파일을 통해 입력 받음.
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

    // 누적 플레이 시간을 업데이트하는 함수
    public void updateTotalTimePlay(int timePlay) throws IOException {
        this.totalTimePlay += timePlay;
        FileManager.getInstance().saveTotalPlayTime(totalTimePlay);
    }

    // 누적 점수를 업데이트하는 함수
    public void updateTotalScore(int score) throws IOException {
        totalScore += score;
        FileManager.getInstance().saveTotalScore(totalScore);
    }

    // 명중률 업적을 업데이트하는 함수.
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

    // 퍼펙트 스테이지를 업데이트하는 함수.
    public void updateCurrentPerfectStage() throws IOException {
        FileManager.getInstance().savePerfectAchievement(currentPerfectLevel);
    }

    // 퍼펙트 스테이지 조건을 검사하고, 현재 완료한 업적 레벨을 변경하는 함수.
    public void updatePerfectAchievement(final int MAX_LIVES, int checkLives, int gameLevel) throws IOException {
        if (checkLives >= MAX_LIVES && currentPerfectLevel < MAX_PERFECT_STAGE && gameLevel > currentPerfectLevel) {
            // 현재 퍼펙트 달성 스테이지가 총 스테이지를 넘지 않았는지 확인.
            currentPerfectLevel += 1;
            nextPerfectLevel = currentPerfectLevel + 1;
            updateCurrentPerfectStage();
            wallet.deposit(PERFECT_COIN_REWARD[currentPerfectLevel-1]);
        }
    }

    // 한 대도 못 때리고(명중률 = 0) 게임이 끝날 시 업데이트되는 함수. (Flawless Failure 업적)
    public void updateFlawlessFailureAchievement(double accuracy) throws IOException {
        // 명중률이 0일시 (한 대도 명중 못 할) 업적을 갱신하도록 설정. 이미 갱신 시 업적 업데이트 X
        if (!checkFlawlessFailure && accuracy <= 0) {
            checkFlawlessFailure = true;
            FileManager.getInstance().saveFlawlessFailureAchievement(checkFlawlessFailure);
            wallet.deposit(FLAWLESS_FAILURE_COIN);
        }
    }

    // 최초로 2인용 설정을 하고 게임 시 업데이트되는 함수.
    public void updateBestFriendsAchievement(boolean checkTwoPlayMode) throws IOException {
        // 현재 설정이 2인용 모드인지 확인하고, 이미 업적을 달성했는지 확인.
        if (!checkBestFriends && checkTwoPlayMode) {
            checkBestFriends = true;
            FileManager.getInstance().saveBestFriendsAchievement(checkBestFriends);
            wallet.deposit(BEST_FRIENDS_COIN);
        }
    }
}
