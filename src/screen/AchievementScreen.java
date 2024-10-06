package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import engine.AchievementManager;
import engine.Core;
import engine.Score;

/**
 * Implements the high scores screen, it shows player records.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class AchievementScreen extends Screen {

	/** List of past high scores. */
	private List<Score> highScores;
	private int totalScore;
	private int totalPlayTime;
	private int currentPerfectStage;
	private double accuracyAchievement;
	private boolean checkFlawlessFailure;
	private boolean checkBestFriends;
	private AchievementManager achievementManager;
	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public AchievementScreen(final int width, final int height, final int fps, final AchievementManager achievementManager) {
		super(width, height, fps);
		this.achievementManager = achievementManager;

		this.returnCode = 1;

		try {
			this.highScores = Core.getFileManager().loadHighScores();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load high scores!");
		}
		try {
			this.totalScore = Core.getFileManager().loadTotalScore();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load total scores!");
		}

		try {
			this.totalPlayTime = Core.getFileManager().loadTotalPlayTime();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load total play time!");
		}
		try {
			this.currentPerfectStage = Core.getFileManager().loadPerfectAchievement();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load current perfect stage");
		}
		try {
			this.accuracyAchievement = Core.getFileManager().loadAccuracyAchievement();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load Current accuracy achievement");
		}
		try {
			this.checkFlawlessFailure = Core.getFileManager().loadFlawlessFailureAchievement();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load flawless failure achievement");
		}
		try {
			this.checkBestFriends = Core.getFileManager().loadBestFriendsAchievement();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load best friends achievement");
		}
	}

	/**
	 * Starts the action.
	 * 
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (inputManager.isKeyDown(KeyEvent.VK_SPACE)
				&& this.inputDelay.checkFinished())
			this.isRunning = false;
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);
		drawManager.drawBestFriendsAchievement(this, this.checkBestFriends);
		drawManager.drawAchievementMenu(this, this.currentPerfectStage, this.currentPerfectStage+1);
		drawManager.drawHighScores(this, this.highScores);
		drawManager.drawTotalScore(this, this.totalScore);
		drawManager.drawTotalPlayTime(this, this.totalPlayTime);
		drawManager.drawAccuracyAchievement(this, this.accuracyAchievement);
		drawManager.drawFlawlessFailureAchievement(this, checkFlawlessFailure);
		drawManager.completeDrawing(this);
	}
}
