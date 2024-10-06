package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import engine.Core;
import engine.GameState;
import engine.Score;
import entity.Wallet;

/**
 * Implements the score screen.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class ScoreScreen extends Screen {

	/** Maximum number of high scores. */
	private static final int MAX_HIGH_SCORE_NUM = 3;


	/** Current score. */
	private int score;
	/** Player lives left. */
	private int livesRemaining;
	/** Total bullets shot by the player. */
	private int bulletsShot;
	/** Total ships destroyed by the player. */
	private int shipsDestroyed;
	/** List of past high scores. */
	private List<Score> highScores;
	/** Checks if current score is a new high score. */
	private boolean isNewRecord;
	/** Number of coins earned in the game */
	private int coinsEarned;
	/** Player's name */
	private String name1, name2;


	/**
	 * Constructor, establishes the properties of the screen.
	 * 
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 * @param gameState
	 *            Current game state.
	 */
	public ScoreScreen(final String name1, final int width, final int height, final int fps,
			final GameState gameState, final Wallet wallet) {
		super(width, height, fps);

		this.name1 = name1;
		this.name2 = name2;

		this.score = gameState.getScore();
		this.livesRemaining = gameState.getLivesRemaining();
		this.bulletsShot = gameState.getBulletsShot();
		this.shipsDestroyed = gameState.getShipsDestroyed();
		this.coinsEarned = gameState.getScore()/10;
		wallet.deposit(coinsEarned);

		try {
			this.highScores = Core.getFileManager().loadHighScores();
		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
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
		if (this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
				// Return to main menu.
				this.returnCode = 1;
				this.isRunning = false;
				saveScore();
			} else if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
				// Play again.
				this.returnCode = 2;
				this.isRunning = false;
				saveScore();
			}

		}

	}

	/**
	 * Saves the score as a high score.
	 */
	private void saveScore() {
		if (highScores.size() > MAX_HIGH_SCORE_NUM) {
			int index = 0;
			for (Score loadScore : highScores) {
				if (name1.equals(loadScore.getName())) {
					if (score > loadScore.getScore()) {
						highScores.remove(index);
						highScores.add(new Score(name1, score));
						break;
					}
				}
				index += 1;
			}
		} else {
			boolean checkDuplicate = false;
			int index = 0;
			for (Score loadScore : highScores) {
				if (name1.equals(loadScore.getName())) {
					if (score > loadScore.getScore()) {
						highScores.remove(index);
						checkDuplicate = true;
						break;
					}
				}
				index += 1;
			}
			if (!checkDuplicate) {
				highScores.add(new Score(name1, score));
			}
		}
		Collections.sort(highScores);
		try {
			Core.getFileManager().saveHighScores(highScores);
		} catch (IOException e) {
			logger.warning("Couldn't load high scores!");
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawGameOver(this, this.inputDelay.checkFinished(),
				this.isNewRecord);
		drawManager.drawResults(this, this.score, this.livesRemaining,
				this.shipsDestroyed, (float) this.shipsDestroyed
						/ this.bulletsShot, this.isNewRecord, this.coinsEarned);

		drawManager.completeDrawing(this);
	}
}
