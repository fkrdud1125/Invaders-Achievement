package engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import entity.Wallet;
import screen.GameSettingScreen;
import screen.Screen;
import entity.Entity;
import entity.Ship;

/**
 * Manages screen drawing.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class DrawManager {
	private GameSettingScreen gameSettingScreen;

	public void drawAchievements(Screen screen) {
		// GameSettingScreen의 isMultiplayer 상태를 확인
		String sampleAchievementsString = "complete!";
		String sampleAchievementsString4 = " Achieved by playing 2-player mode";
		String sampleAchievementsString4_1 = "                for the first time.";
		boolean firstPlayed = true;
		if (gameSettingScreen.getMultiplay() && firstPlayed) {
				backBufferGraphics.setColor(Color.GREEN);
				drawRightSideAchievementBigString(screen, sampleAchievementsString,
						screen.getHeight() /2 + fontRegularMetrics.getHeight()*6+fontBigMetrics.getHeight()*4);
						firstPlayed = false;
		} else {
			backBufferGraphics.setColor(Color.GRAY);
			drawRightSideAchievementSmallString(screen, sampleAchievementsString4,
					screen.getHeight() /2 + fontRegularMetrics.getHeight()*5+fontBigMetrics.getHeight()*4+fontSmallMetrics.getHeight());
			backBufferGraphics.setColor(Color.GRAY);
			drawRightSideAchievementSmallString(screen, sampleAchievementsString4_1,
					screen.getHeight() /2 + fontRegularMetrics.getHeight()*5+fontBigMetrics.getHeight()*4+fontSmallMetrics.getHeight()*2);
		}




	}

	/** Singleton instance of the class. */
	private static DrawManager instance;
	/** Current frame. */
	private static Frame frame;
	/** FileManager instance. */
	private static FileManager fileManager;
	/** Application logger. */
	private static Logger logger;
	/** Graphics context. */
	private static Graphics graphics;
	/** Buffer Graphics. */
	private static Graphics backBufferGraphics;
	/** Buffer image. */
	private static BufferedImage backBuffer;
	/** Small sized font. */
	private static Font fontSmall;
	/** Small sized font properties. */
	private static FontMetrics fontSmallMetrics;
	/** Normal sized font. */
	private static Font fontRegular;
	/** Normal sized font properties. */
	private static FontMetrics fontRegularMetrics;
	/** Big sized font. */
	private static Font fontBig;
	/** Big sized font properties. */
	private static FontMetrics fontBigMetrics;

	/** Sprite types mapped to their images. */
	private static Map<SpriteType, boolean[][]> spriteMap;

	/** Sprite types. */
	public static enum SpriteType {
		/** Player ship. */
		Ship,
		/** Destroyed player ship. */
		ShipDestroyed,
		/** Player bullet. */
		Bullet,
		/** Enemy bullet. */
		EnemyBullet,
		/** First enemy ship - first form. */
		EnemyShipA1,
		/** First enemy ship - second form. */
		EnemyShipA2,
		/** Second enemy ship - first form. */
		EnemyShipB1,
		/** Second enemy ship - second form. */
		EnemyShipB2,
		/** Third enemy ship - first form. */
		EnemyShipC1,
		/** Third enemy ship - second form. */
		EnemyShipC2,
		/** Bonus ship. */
		EnemyShipSpecial,
		/** Destroyed enemy ship. */
		Explosion
	};

	/**
	 * Private constructor.
	 */
	private DrawManager() {
		fileManager = Core.getFileManager();
		logger = Core.getLogger();
		logger.info("Started loading resources.");

		try {
			spriteMap = new LinkedHashMap<SpriteType, boolean[][]>();

			spriteMap.put(SpriteType.Ship, new boolean[13][8]);
			spriteMap.put(SpriteType.ShipDestroyed, new boolean[13][8]);
			spriteMap.put(SpriteType.Bullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyBullet, new boolean[3][5]);
			spriteMap.put(SpriteType.EnemyShipA1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipA2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipB1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipB2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipC1, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipC2, new boolean[12][8]);
			spriteMap.put(SpriteType.EnemyShipSpecial, new boolean[16][7]);
			spriteMap.put(SpriteType.Explosion, new boolean[13][7]);

			fileManager.loadSprite(spriteMap);
			logger.info("Finished loading the sprites.");

			// Font loading.
			fontSmall = fileManager.loadFont(10f);
			fontRegular = fileManager.loadFont(14f);
			fontBig = fileManager.loadFont(24f);
			logger.info("Finished loading the fonts.");

		} catch (IOException e) {
			logger.warning("Loading failed.");
		} catch (FontFormatException e) {
			logger.warning("Font formating failed.");
		}
	}

	/**
	 * Returns shared instance of DrawManager.
	 * 
	 * @return Shared instance of DrawManager.
	 */
	protected static DrawManager getInstance() {
		if (instance == null)
			instance = new DrawManager();
		return instance;
	}

	/**
	 * Sets the frame to draw the image on.
	 * 
	 * @param currentFrame
	 *            Frame to draw on.
	 */
	public void setFrame(final Frame currentFrame) {
		frame = currentFrame;
	}

	/**
	 * First part of the drawing process. Initializes buffers, draws the
	 * background and prepares the images.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	public void initDrawing(final Screen screen) {
		backBuffer = new BufferedImage(screen.getWidth(), screen.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		graphics = frame.getGraphics();
		backBufferGraphics = backBuffer.getGraphics();

		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics
				.fillRect(0, 0, screen.getWidth(), screen.getHeight());

		fontSmallMetrics = backBufferGraphics.getFontMetrics(fontSmall);
		fontRegularMetrics = backBufferGraphics.getFontMetrics(fontRegular);
		fontBigMetrics = backBufferGraphics.getFontMetrics(fontBig);

		// drawBorders(screen);
		// drawGrid(screen);
	}

	/**
	 * Draws the completed drawing on screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 */
	public void completeDrawing(final Screen screen) {
		graphics.drawImage(backBuffer, frame.getInsets().left,
				frame.getInsets().top, frame);
	}

	/**
	 * Draws an entity, using the appropriate image.
	 * 
	 * @param entity
	 *            Entity to be drawn.
	 * @param positionX
	 *            Coordinates for the left side of the image.
	 * @param positionY
	 *            Coordinates for the upper side of the image.
	 */
	public void drawEntity(final Entity entity, final int positionX,
			final int positionY) {
		boolean[][] image = spriteMap.get(entity.getSpriteType());

		backBufferGraphics.setColor(entity.getColor());
		for (int i = 0; i < image.length; i++)
			for (int j = 0; j < image[i].length; j++)
				if (image[i][j])
					backBufferGraphics.drawRect(positionX + i * 2, positionY
							+ j * 2, 1, 1);
	}

	/**
	 * For debugging purposes, draws the canvas borders.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawBorders(final Screen screen) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, 0, screen.getWidth() - 1, 0);
		backBufferGraphics.drawLine(0, 0, 0, screen.getHeight() - 1);
		backBufferGraphics.drawLine(screen.getWidth() - 1, 0,
				screen.getWidth() - 1, screen.getHeight() - 1);
		backBufferGraphics.drawLine(0, screen.getHeight() - 1,
				screen.getWidth() - 1, screen.getHeight() - 1);
	}

	/**
	 * For debugging purposes, draws a grid over the canvas.
	 * 
	 * @param screen
	 *            Screen to draw in.
	 */
	@SuppressWarnings("unused")
	private void drawGrid(final Screen screen) {
		backBufferGraphics.setColor(Color.DARK_GRAY);
		for (int i = 0; i < screen.getHeight() - 1; i += 2)
			backBufferGraphics.drawLine(0, i, screen.getWidth() - 1, i);
		for (int j = 0; j < screen.getWidth() - 1; j += 2)
			backBufferGraphics.drawLine(j, 0, j, screen.getHeight() - 1);
	}

	/**
	 * Draws current score on screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Current score.
	 */
	public void drawScore(final Screen screen, final int score) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		String scoreString = String.format("%04d", score);
		backBufferGraphics.drawString(scoreString, screen.getWidth() - 60, 25);
	}

	/**
	 * Draws number of remaining lives on screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param lives
	 *            Current lives.
	 */
	public void drawLives(final Screen screen, final int lives) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.setColor(Color.WHITE);
		backBufferGraphics.drawString(Integer.toString(lives), 20, 25);
		Ship dummyShip = new Ship(0, 0);
		for (int i = 0; i < lives; i++)
			drawEntity(dummyShip, 40 + 35 * i, 10);
	}

	/**
	 * Draws a thick line from side to side of the screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param positionY
	 *            Y coordinate of the line.
	 */
	public void drawHorizontalLine(final Screen screen, final int positionY) {
		backBufferGraphics.setColor(Color.GREEN);
		backBufferGraphics.drawLine(0, positionY, screen.getWidth(), positionY);
		backBufferGraphics.drawLine(0, positionY + 1, screen.getWidth(),
				positionY + 1);
	}

	/**
	 * Draws game title.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawTitle(final Screen screen) {
		String titleString = "Invaders";
		String instructionsString =
				"select with w+s / arrows, confirm with space";

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 5 * 2);

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, titleString, screen.getHeight() / 5);
	}

	/**
	 * Draws main menu.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param option
	 *            Option selected.
	 */
	public void drawMenu(final Screen screen, final int option, final int coin) {
		String playString = "Play";
		String shopString = "SHOP";
		String coinString = "YOUR COIN: " + coin;
		String achievementString = "ACHIEVEMENT";
		String settingString = "SETTING";
		String exitString = "EXIT";


		if (option == 6) /*option2 => Game Settings */
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, playString,
				screen.getHeight() / 7 * 4);

		if (option == 3) /*option3 => Shop */
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, shopString, screen.getHeight()
				/ 7 * 4 + fontRegularMetrics.getHeight() * 2);

		backBufferGraphics.setColor(Color.ORANGE);
		drawCenteredSmallString(screen, coinString, screen.getHeight()
				/ 7 * 4 + fontRegularMetrics.getHeight() * 3);

		if (option == 4) /*option4 => Achievement */
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, achievementString, screen.getHeight()
				/ 7 * 4 + fontRegularMetrics.getHeight() * 5);


		if (option == 5) /*option5 => Setting */
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, settingString, screen.getHeight()
				/ 7 * 4 + fontRegularMetrics.getHeight() * 7);

		if (option == 0) /*option0 => exit */
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, exitString, screen.getHeight()
				/ 7 * 4 + fontRegularMetrics.getHeight() * 9);
	}



	/**
	 * Draws game results.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param score
	 *            Score obtained.
	 * @param livesRemaining
	 *            Lives remaining when finished.
	 * @param shipsDestroyed
	 *            Total ships destroyed.
	 * @param accuracy
	 *            Total accuracy.
	 * @param isNewRecord
	 *            If the score is a new high score.
	 */
	public void drawResults(final Screen screen, final int score,
			final int livesRemaining, final int shipsDestroyed,
			final float accuracy, final boolean isNewRecord, final int coinsEarned) {
		String scoreString = String.format("score %04d", score);
		String livesRemainingString = "lives remaining " + livesRemaining;
		String shipsDestroyedString = "enemies destroyed " + shipsDestroyed;
		String accuracyString = String
				.format("accuracy %.2f%%", accuracy * 100);
		String coinsEarnedString = "EARNED COIN " + coinsEarned;

		int height = isNewRecord ? 4 : 2;

		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, scoreString, screen.getHeight()
				/ height);
		drawCenteredRegularString(screen, livesRemainingString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 2);
		drawCenteredRegularString(screen, shipsDestroyedString,
				screen.getHeight() / height + fontRegularMetrics.getHeight()
						* 4);
		drawCenteredRegularString(screen, accuracyString, screen.getHeight()
				/ height + fontRegularMetrics.getHeight() * 6);
		backBufferGraphics.setColor(Color.YELLOW);
		drawCenteredRegularString(screen, coinsEarnedString, screen.getHeight()
				/ height + fontRegularMetrics.getHeight() * 9);
	}

	/**
	 * Draws interactive characters for name input.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param name
	 *            Current name selected.
	 * @param nameCharSelected
	 *            Current character selected for modification.
	 */
	public void drawNameInput(final Screen screen, final char[] name,
			final int nameCharSelected) {
		String newRecordString = "New Record!";
		String introduceNameString = "Introduce name:";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredRegularString(screen, newRecordString, screen.getHeight()
				/ 4 + fontRegularMetrics.getHeight() * 10);
		backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, introduceNameString,
				screen.getHeight() / 4 + fontRegularMetrics.getHeight() * 12);

		// 3 letters name.
		int positionX = screen.getWidth()
				/ 2
				- (fontRegularMetrics.getWidths()[name[0]]
						+ fontRegularMetrics.getWidths()[name[1]]
						+ fontRegularMetrics.getWidths()[name[2]]
								+ fontRegularMetrics.getWidths()[' ']) / 2;

		for (int i = 0; i < 3; i++) {
			if (i == nameCharSelected)
				backBufferGraphics.setColor(Color.GREEN);
			else
				backBufferGraphics.setColor(Color.WHITE);

			positionX += fontRegularMetrics.getWidths()[name[i]] / 2;
			positionX = i == 0 ? positionX
					: positionX
							+ (fontRegularMetrics.getWidths()[name[i - 1]]
									+ fontRegularMetrics.getWidths()[' ']) / 2;

			backBufferGraphics.drawString(Character.toString(name[i]),
					positionX,
					screen.getHeight() / 4 + fontRegularMetrics.getHeight()
							* 14);
		}
	}

	/**
	 * Draws basic content of game over screen.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param acceptsInput
	 *            If the screen accepts input.
	 * @param isNewRecord
	 *            If the score is a new high score.
	 */
	public void drawGameOver(final Screen screen, final boolean acceptsInput,
			final boolean isNewRecord) {
		String gameOverString = "Game Over";
		String continueOrExitString =
				"Press Space to play again, Escape to exit";

		int height = isNewRecord ? 4 : 2;

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, gameOverString, screen.getHeight()
				/ height - fontBigMetrics.getHeight() * 2);

		if (acceptsInput)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, continueOrExitString,
				screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 10);
	}


	/**
	 * Draws achievement screen title and instructions.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawAchievementMenu(final Screen screen, final int currentPerfectStage, final int nextPerfectStage) {
		//high score section
		String highScoreString = "High Scores";

		//cumulative section
		String cumulativeScoreString = "Cumulative Score";
		String playTimesString = "-Total  Playtime-";

		// centered strings
		String achievementString = "Achievement";
		String instructionsString = "Press space to return";
		String achievementsStatusString = "Achievements Status";

		// achievements names
		String[] achievements = {"High Accuracy", "{Perfect Clear", "Flawless Failure", "We're Best Friends"};
		final int HIGHACCURACY = 0;
		final int PERFEACTCLEAR = 1;
		final int FLAWLESSFAILURE = 2;
		final int BESTFRIENDS = 3;
		// AchievementManager에서 값을 얻은 후 AchievementMenu를 그릴 때 넘겨줌
		String perfectAchievement = "lv." + currentPerfectStage + " => lv." +
				nextPerfectStage;



		String explainAccuracy_1 = "      Achieved when the game ends";
		String explainAccuracy_2 = "                  with 0% accuracy.";

		String sampleCoin1 = "1000";
		String sampleCoin2 = "1500";
		String sampleCoin3 = "500";
		String sampleCoin4 = "2000";


		// centered Strings
		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, achievementString, screen.getHeight() / 8);

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen, instructionsString,
				screen.getHeight() / 8 + fontBigMetrics.getHeight() );



		// left side HighScore Strings
		backBufferGraphics.setColor(Color.GREEN);
		drawLeftSideScoreRegularString(screen, highScoreString,
				screen.getHeight() / 5+ fontBigMetrics.getHeight());


		// right side cumulative section
		backBufferGraphics.setColor(Color.yellow);
		drawRightSideCumulativeRegularString(screen, cumulativeScoreString,
				screen.getHeight() / 5 + fontBigMetrics.getHeight());

		backBufferGraphics.setColor(Color.yellow);
		drawRightSideCumulativeRegularString(screen, playTimesString,
				screen.getHeight() / 5 + 2*fontRegularMetrics.getHeight()+2* fontBigMetrics.getHeight()+10 );


		//Achievement status Strings
		backBufferGraphics.setColor(Color.MAGENTA);
		drawCenteredBigString(screen, achievementsStatusString,
				screen.getHeight() / 2 + fontBigMetrics.getHeight() );

		// left
		backBufferGraphics.setColor(Color.WHITE);
		drawLeftSideAchievementRegularString(screen, achievements[HIGHACCURACY],
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*3+fontBigMetrics.getHeight());
		backBufferGraphics.setColor(Color.WHITE);
		drawLeftSideAchievementRegularString(screen, achievements[PERFEACTCLEAR],
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*4+fontBigMetrics.getHeight()*2);

		backBufferGraphics.setColor(Color.WHITE);
		drawLeftSideAchievementRegularString(screen, achievements[FLAWLESSFAILURE],
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*5+fontBigMetrics.getHeight()*3);

		backBufferGraphics.setColor(Color.WHITE);
		drawLeftSideAchievementRegularString(screen, achievements[BESTFRIENDS],
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*6+fontBigMetrics.getHeight()*4);

		backBufferGraphics.setColor(Color.WHITE);
		drawRightSideAchievementBigString(screen, perfectAchievement,
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*4+fontBigMetrics.getHeight()*2);
		backBufferGraphics.setColor(Color.GRAY);
		drawRightSideAchievementSmallString(screen, explainAccuracy_1,
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*4+fontBigMetrics.getHeight()*3+fontSmallMetrics.getHeight());

		backBufferGraphics.setColor(Color.GRAY);
		drawRightSideAchievementSmallString(screen, explainAccuracy_2g,
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*4+fontBigMetrics.getHeight()*3+fontSmallMetrics.getHeight()*2);





		backBufferGraphics.setColor(Color.orange);
		drawRightSideAchievementCoinBigString(screen, sampleCoin1,
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*3+fontBigMetrics.getHeight());
		backBufferGraphics.setColor(Color.orange);
		drawRightSideAchievementCoinBigString(screen, sampleCoin2,
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*4+fontBigMetrics.getHeight()*2);

		backBufferGraphics.setColor(Color.orange);
		drawRightSideAchievementCoinBigString(screen, sampleCoin3,
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*5+fontBigMetrics.getHeight()*3);

		backBufferGraphics.setColor(Color.orange);
		drawRightSideAchievementCoinBigString(screen, sampleCoin4,
				screen.getHeight() /2 + fontRegularMetrics.getHeight()*6+fontBigMetrics.getHeight()*4);


	}

	/**
	 * Draws high scores.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param highScores
	 *            List of high scores.
	 */
	public void drawHighScores(final Screen screen,
			final List<Score> highScores) {
		backBufferGraphics.setColor(Color.WHITE);
		int i = 0;
		String scoreString = "";

		final int limitDrawingScore = 3;
		int countDrawingScore = 0;
		for (Score score : highScores) {
			scoreString = String.format("%s        %04d", score.getName(),
					score.getScore());
			drawLeftSideScoreRegularString(screen, scoreString, screen.getHeight()
					/ 4 + fontRegularMetrics.getHeight() * (i + 1) * 2);
			i++;
			countDrawingScore++;
			if(countDrawingScore>=limitDrawingScore){
				break;
			}
		}
	}

	// 10/14 AJS Draw Total Score
	public void drawTotalScore(final Screen screen, final int totalScore) {
		backBufferGraphics.setColor(Color.WHITE);
		String totalScoreString = String.format("%s", totalScore);
        drawRightSideCumulativeBigString(screen, totalScoreString, screen.getHeight() / 3
				- fontRegularMetrics.getHeight() + 10);
	}

	public void drawTotalPlayTime(final Screen screen, final int totalPlayTime) {
		int totalMinutes = totalPlayTime / 60;
		int totalSeconds = totalPlayTime % 60;

		backBufferGraphics.setColor(Color.WHITE);
		String totalScoreString = String.format("%02dm%02ds",totalMinutes,totalSeconds);
		drawRightSideCumulativeBigString(screen, totalScoreString, screen.getHeight() / 2
				- fontRegularMetrics.getHeight() - 15);
	}

	public void drawAccuracyAchievement(final Screen screen, final int accuracy) {
		if (accuracy >= 100) {
			backBufferGraphics.setColor(Color.GREEN);
			drawRightSideAchievementBigString(screen, "You are crazy",
					screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 3 + fontBigMetrics.getHeight());
		} else {
			backBufferGraphics.setColor(Color.WHITE);
			String accuracyAchievement = String.format("%02d%%", accuracy) + " => " + String.format("%02d%%", accuracy+10);
			drawRightSideAchievementBigString(screen, accuracyAchievement,
					screen.getHeight() / 2 + fontRegularMetrics.getHeight() * 3 + fontBigMetrics.getHeight());
		}
	}

	/**
	 * Draws a centered string on small font.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	private void drawCenteredSmallString(final Screen screen, final String string, final int height) {
		backBufferGraphics.setFont(fontSmall);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontSmallMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Draws a centered string on regular font.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredRegularString(final Screen screen,
			final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontRegularMetrics.stringWidth(string) / 2, height);
	}

	/**
	 * Draws a centered string on big font.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param string
	 *            String to draw.
	 * @param height
	 *            Height of the drawing.
	 */
	public void drawCenteredBigString(final Screen screen, final String string,
			final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth() / 2
				- fontBigMetrics.stringWidth(string) / 2, height);
	}

	// left side score
	public void drawLeftSideScoreRegularString(final Screen screen,
											   final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() / 4
				- fontRegularMetrics.stringWidth(string) / 2, height);
	}

	//right side Cumulative score
	public void drawRightSideCumulativeRegularString(final Screen screen,
													 final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() *2/ 5
				+ fontRegularMetrics.stringWidth(string)*3 / 5 , height);
	}
	public void drawRightSideCumulativeBigString(final Screen screen,
												 final String string, final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth() *68/ 100
				- fontRegularMetrics.stringWidth(string)/2 , height);
	}


	// left side achievement
	public void drawLeftSideAchievementRegularString(final Screen screen,
													 final String string, final int height) {
		backBufferGraphics.setFont(fontRegular);
		backBufferGraphics.drawString(string, screen.getWidth() *22/ 100
				- fontRegularMetrics.stringWidth(string) / 2, height);
	}

	// right side achievement(sample)

	public void drawRightSideAchievementBigString(final Screen screen,
												  final String string, final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth() *66/100-
						fontRegularMetrics.stringWidth(string), height);
	}

	public void drawRightSideAchievementSmallString(final Screen screen,
												  final String string, final int height) {
		backBufferGraphics.setFont(fontSmall);
		backBufferGraphics.drawString(string, screen.getWidth() / 2-
				fontRegularMetrics.stringWidth(string) / 8, height);
	}

	public void drawRightSideAchievementCoinBigString(final Screen screen,
													final String string, final int height) {
		backBufferGraphics.setFont(fontBig);
		backBufferGraphics.drawString(string, screen.getWidth()*85/100 , height);
	}

	/**
	 * Countdown to game start.
	 * 
	 * @param screen
	 *            Screen to draw on.
	 * @param level
	 *            Game difficulty level.
	 * @param number
	 *            Countdown number.
	 * @param bonusLife
	 *            Checks if a bonus life is received.
	 */
	public void drawCountDown(final Screen screen, final int level,
			final int number, final boolean bonusLife) {
		int rectWidth = screen.getWidth();
		int rectHeight = screen.getHeight() / 6;
		backBufferGraphics.setColor(Color.BLACK);
		backBufferGraphics.fillRect(0, screen.getHeight() / 2 - rectHeight / 2,
				rectWidth, rectHeight);
		backBufferGraphics.setColor(Color.GREEN);
		if (number >= 4)
			if (!bonusLife) {
				drawCenteredBigString(screen, "Level " + level,
						screen.getHeight() / 2
						+ fontBigMetrics.getHeight() / 3);
			} else {
				drawCenteredBigString(screen, "Level " + level
						+ " - Bonus life!",
						screen.getHeight() / 2
						+ fontBigMetrics.getHeight() / 3);
			}
		else if (number != 0)
			drawCenteredBigString(screen, Integer.toString(number),
					screen.getHeight() / 2 + fontBigMetrics.getHeight() / 3);
		else
			drawCenteredBigString(screen, "GO!", screen.getHeight() / 2
					+ fontBigMetrics.getHeight() / 3);
	}

	/**
	 * Draws the game setting screen.
	 *
	 * @param screen
	 *            Screen to draw on.
	 */
	public void drawGameSetting(final Screen screen) {
		String titleString = "Game Setting";

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, titleString, screen.getHeight() / 100 * 25);
	}

	/**
	 * Draws the game setting row.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param selectedRow
	 *            Selected row.
	 *
	 * @author <a href="mailto:dayeon.dev@gmail.com">Dayeon Oh</a>
	 *
	 */
	public void drawGameSettingRow(final Screen screen, final int selectedRow) {
		int y = 0;
		int height = 0;
		int screenHeight = screen.getHeight();

		if (selectedRow == 0) {
			y = screenHeight / 100 * 35;
			height = screen.getHeight() / 100 * 28;
		} else if (selectedRow == 1) {
			y = screenHeight / 100 * 63;
			height = screen.getHeight() / 100 * 18;
		} else if (selectedRow == 2) {
			y = screenHeight / 100 * 92;
			height = screen.getHeight() / 100 * 10;
		}

		backBufferGraphics.setColor(Color.DARK_GRAY);
		backBufferGraphics.fillRect(0, y, screen.getWidth(), height);
	}

	/**
	 * Draws the game setting elements.
	 *
	 * @param screen
	 *            Screen to draw on.
	 * @param selectedRow
	 *            Selected row.
	 * @param isMultiPlayer
	 *            If the game is multiplayer.
	 * @param name1
	 *            Player 1 name.
	 * @param name2
	 *            Player 2 name.
	 * @param difficultyLevel
	 *            Difficulty level.
	 *
	 * @author <a href="mailto:dayeon.dev@gmail.com">Dayeon Oh</a>
	 *
	 */
	public void drawGameSettingElements(final Screen screen, final int selectedRow,
		final boolean isMultiPlayer, final String name1, final String name2, final int difficultyLevel) {
		String spaceString = " ";
		String player1String = "1 Player";
		String player2String = "2 Player";
		String levelEasyString = "Easy";
		String levelNormalString = "Normal";
		String levelHardString = "Hard";
		String startString = "Start";

		if (!isMultiPlayer) backBufferGraphics.setColor(Color.GREEN);
		else backBufferGraphics.setColor(Color.WHITE);

		drawCenteredRegularString(screen, player1String + spaceString.repeat(40), screen.getHeight() / 100 * 43);
		drawCenteredRegularString(screen, name1 + spaceString.repeat(40), screen.getHeight() / 100 * 58);

		if (!isMultiPlayer) backBufferGraphics.setColor(Color.WHITE);
		else backBufferGraphics.setColor(Color.GREEN);

		drawCenteredRegularString(screen, spaceString.repeat(40) + player2String, screen.getHeight() / 100 * 43);
		drawCenteredRegularString(screen, spaceString.repeat(40) + name2, screen.getHeight() / 100 * 58);

		if (difficultyLevel==0) backBufferGraphics.setColor(Color.GREEN);
		else backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, levelEasyString + spaceString.repeat(60), screen.getHeight() / 100 * 73);

		if (difficultyLevel==1) backBufferGraphics.setColor(Color.GREEN);
		else backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, levelNormalString, screen.getHeight() / 100 * 73);

		if (difficultyLevel==2) backBufferGraphics.setColor(Color.GREEN);
		else backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, spaceString.repeat(60) + levelHardString, screen.getHeight() / 100 * 73);

		if (selectedRow == 2) backBufferGraphics.setColor(Color.GREEN);
		else backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, startString, screen.getHeight() / 100 * 98);
	}

	/**
	 *  draw shop
	 * @param screen
	 * 				Screen to draw on.
	 * @param option
	 * 				selected shop item
	 * @param wallet
	 * 				player's wallet
	 * @param money_alertcooldown
	 * 				cooldown for insufficient coin alert
	 * @param max_alertcooldown
	 * 				cooldown for max level alert
	 */
	public void drawShop(final Screen screen, final int option, final Wallet wallet, final Cooldown money_alertcooldown, final Cooldown max_alertcooldown) {

		String shopString = "Shop";
		String instructionsString = "COIN: " + wallet.getCoin();
		String exitinfo = "press esc to exit";
		String coststring = "cost";
		String[] costs = new String[] {"0", "2000", "4000", "8000", "MAX LEVEL"};

		backBufferGraphics.setColor(Color.GRAY);
		drawCenteredRegularString(screen,exitinfo,(screen.getHeight()/20)*5);
		drawCenteredRegularString(screen, instructionsString,
				(screen.getHeight() / 20)*6);
		drawCenteredRegularString(screen,coststring,screen.getHeight() /20 *8 );

		if(option==1)
		{
			drawCenteredRegularString(screen,costs[wallet.getBullet_lv()],screen.getHeight() /20 *9 );
		}
		else if(option==2)
		{
			drawCenteredRegularString(screen,costs[wallet.getShot_lv()],screen.getHeight() /20 *9 );
		}
		else if(option==3)
		{
			drawCenteredRegularString(screen,costs[wallet.getLives_lv()],screen.getHeight() /20 *9 );
		}
		else if(option==4)
		{
			drawCenteredRegularString(screen,costs[wallet.getCoin_lv()],screen.getHeight() /20 *9 );
		}

		backBufferGraphics.setColor(Color.GREEN);
		drawCenteredBigString(screen, shopString, screen.getHeight() / 20*3);


		String item1String = "bullet_speed";
		String item2String = "shot_interval";
		String item3String = "additional_life";
		String item4String = "coin_gain";

		if (option == 1)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, item1String + "  LV. "+ wallet.getBullet_lv(),
				screen.getHeight() / 3 * 2);

		if (option == 2)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, item2String+ "  LV. "+ wallet.getShot_lv(), screen.getHeight()
				/ 3 * 2 + fontRegularMetrics.getHeight() * 2);

		if (option == 3)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, item3String+ " LV. "+ wallet.getLives_lv() , screen.getHeight() / 3
				* 2 + fontRegularMetrics.getHeight() * 4);

		if (option == 4)
			backBufferGraphics.setColor(Color.GREEN);
		else
			backBufferGraphics.setColor(Color.WHITE);
		drawCenteredRegularString(screen, item4String+ " LV. "+ wallet.getCoin_lv() , screen.getHeight() / 3
				* 2 + fontRegularMetrics.getHeight() * 6);

		if (!money_alertcooldown.checkFinished())
		{
			backBufferGraphics.setColor(Color.red);
			backBufferGraphics.fillRect((screen.getWidth()-300)/2, (screen.getHeight()-100)/2, 300, 80);
			backBufferGraphics.setColor(Color.black);
			drawCenteredBigString(screen, "Insufficient coin", screen.getHeight()/2);
		}
		if(!max_alertcooldown.checkFinished())
		{
			backBufferGraphics.setColor(Color.red);
			backBufferGraphics.fillRect((screen.getWidth()-300)/2, (screen.getHeight()-100)/2, 300, 80);
			backBufferGraphics.setColor(Color.black);
			drawCenteredBigString(screen, "Already max level", screen.getHeight()/2);

		}
	}
}
