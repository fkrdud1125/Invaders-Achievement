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
			this.currentPerfectStage = Core.getFileManager().loadCurrentPsAchievement();
		} catch (NumberFormatException | IOException e) {
			logger.warning("Couldn't load Current Perfect Stage");
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
		drawManager.drawAchievements(this);
		drawManager.drawAchievementMenu(this, this.currentPerfectStage, this.currentPerfectStage+1);
		drawManager.drawHighScores(this, this.highScores);
		// 10/14 AJS Draw Total Score
		drawManager.drawTotalScore(this, this.totalScore);
		drawManager.drawTotalPlayTime(this, this.totalPlayTime);
		drawManager.completeDrawing(this);
	}
}
