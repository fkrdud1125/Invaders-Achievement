package engine;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;
import java.util.Properties;

import engine.DrawManager.SpriteType;
import entity.Wallet;

/**
 * Manages files used in the application.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class FileManager {

	/** Singleton instance of the class. */
	private static FileManager instance;
	/** Application logger. */
	private static Logger logger;
	/** Max number of high scores. */
	private static final int MAX_SCORES = 3;

	/**
	 * private constructor.
	 */
	private FileManager() {
		logger = Core.getLogger();
	}

	/**
	 * Returns shared instance of FileManager.
	 * 
	 * @return Shared instance of FileManager.
	 */
	protected static FileManager getInstance() {
		if (instance == null)
			instance = new FileManager();
		return instance;
	}

	/**
	 * Loads sprites from disk.
	 * 
	 * @param spriteMap
	 *            Mapping of sprite type and empty boolean matrix that will
	 *            contain the image.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public void loadSprite(final Map<SpriteType, boolean[][]> spriteMap)
			throws IOException {
		InputStream inputStream = null;
		/**--1-- 첫번째 변경점
		 * res파일을 못찾는 것 같아서 res파일을 리소스파일로 만들어줌
		 */
		try {
			inputStream = DrawManager.class.getClassLoader()
					.getResourceAsStream("graphics");
			char c;

			// Sprite loading.
			for (Map.Entry<SpriteType, boolean[][]> sprite : spriteMap
					.entrySet()) {
				for (int i = 0; i < sprite.getValue().length; i++)
					for (int j = 0; j < sprite.getValue()[i].length; j++) {
						do
							c = (char) inputStream.read();
						while (c != '0' && c != '1');

						if (c == '1')
							sprite.getValue()[i][j] = true;
						else
							sprite.getValue()[i][j] = false;
					}
				logger.fine("Sprite " + sprite.getKey() + " loaded.");
			}
			if (inputStream != null)
				inputStream.close();
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
	}

	/**
	 * Loads a font of a given size.
	 * 
	 * @param size
	 *            Point size of the font.
	 * @return New font.
	 * @throws IOException
	 *             In case of loading problems.
	 * @throws FontFormatException
	 *             In case of incorrect font format.
	 */
	public Font loadFont(final float size) throws IOException,
			FontFormatException {
		InputStream inputStream = null;
		Font font;

		try {
			// Font loading.
			/**--2-- 두번째 변경점
			 * 1. res파일을 못찾는 것 같아서 res파일을 리소스파일로 만들어줌
			 * 2. font파일이 없어서 github에 있는 폰트 다운 및 res파일에 넣고 경로 설정 해줌
			 */
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("space_invaders.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, inputStream).deriveFont(
					size);
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return font;
	}

	/**
	 * Returns the application default scores if there is no user high scores
	 * file.
	 * 
	 * @return Default high scores.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	private List<Score> loadDefaultHighScores() throws IOException {
		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader reader = null;

		try {
			inputStream = FileManager.class.getClassLoader()
					.getResourceAsStream("scores");
			reader = new BufferedReader(new InputStreamReader(inputStream));

			Score highScore = null;
			String name = reader.readLine();
			String score = reader.readLine();

			while ((name != null) && (score != null)) {
				highScore = new Score(name, Integer.parseInt(score));
				highScores.add(highScore);
				name = reader.readLine();
				score = reader.readLine();
			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}

		return highScores;
	}

	/**
	 * Loads high scores from file, and returns a sorted list of pairs score -
	 * value.
	 * 
	 * @return Sorted list of scores - players.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public List<Score> loadHighScores() throws IOException {

		List<Score> highScores = new ArrayList<Score>();
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "scores";

			File scoresFile = new File(scoresPath);
			inputStream = new FileInputStream(scoresFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			logger.info("Loading user high scores.");

			Score highScore = null;
			String name = bufferedReader.readLine();
			String score = bufferedReader.readLine();

			while ((name != null) && (score != null)) {
				highScore = new Score(name, Integer.parseInt(score));
				highScores.add(highScore);
				name = bufferedReader.readLine();
				score = bufferedReader.readLine();
			}

		} catch (FileNotFoundException e) {
			// loads default if there's no user scores.
			logger.info("Loading default high scores.");
			highScores = loadDefaultHighScores();
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
		}

		Collections.sort(highScores);
		return highScores;
	}

	// 10/14 AJS Load Total Score
	public int loadTotalScore() throws IOException {
		int totalScore = 0;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String totalScorePath = new File(jarPath).getParent();
			totalScorePath += File.separator;
			totalScorePath += "totalScore";

			File totalScoreFile = new File(totalScorePath);
			inputStream = new FileInputStream(totalScoreFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			// Load properties from the file
			Properties properties = new Properties();
			properties.load(bufferedReader);

			logger.info("Loading user total score.");

			// Get the value associated with the 'TOTAL_SCORE' key
			String totalScoreStr = properties.getProperty("TOTAL_SCORE", "0"); // Default to "0" if key not found
			totalScore = Integer.parseInt(totalScoreStr);

		} catch (FileNotFoundException e) {
			// loads default if there's no user scores.
			logger.info("File not found. Loading default total score.");
		} catch (NumberFormatException e) {
			logger.warning("Invalid format for total score. Defaulting to 0.");
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return totalScore;
	}

	public int loadTotalPlayTime() throws IOException {
		int totalPlayTime = 0;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String totalPlayTimePath = new File(jarPath).getParent();
			totalPlayTimePath += File.separator;
			totalPlayTimePath += "totalPlayTime"; // Assuming the file is named 'info'

			File totalScoreFile = new File(totalPlayTimePath);
			inputStream = new FileInputStream(totalScoreFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			Properties properties = new Properties();
			properties.load(bufferedReader); // Load properties from the file

			logger.info("Loading user total play time.");

			// Get the value associated with the 'TOTAL_PLAY_TIME' key
			String playTimeStr = properties.getProperty("TOTAL_PLAY_TIME", "0"); // Default to "0" if key not found
			totalPlayTime = Integer.parseInt(playTimeStr);

		} catch (FileNotFoundException e) {
			// Load default if there's no user scores
			logger.info("File not found. Loading default total play time.");
		} catch (NumberFormatException e) {
			logger.warning("Invalid format for total play time. Defaulting to 0.");
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return totalPlayTime;
	}

	public int loadPerfectAchievement() throws IOException {
		int currentPsAchievement = 0;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String currentPsAchievementPath = new File(jarPath).getParent();
			currentPsAchievementPath += File.separator;
			currentPsAchievementPath += "perfectAchievement";

			File currentPsAchievementFile = new File(currentPsAchievementPath);
			inputStream = new FileInputStream(currentPsAchievementFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			Properties properties = new Properties();
			properties.load(bufferedReader); // Load properties from the file

			logger.info("Loading user perfect stage.");

			String currentPsAchievementStr = properties.getProperty("Perfect_Stage", "0"); // Default to "0" if key not found
			currentPsAchievement = Integer.parseInt(currentPsAchievementStr);

		} catch (FileNotFoundException e) {
			// Load default if there's no user scores
			logger.info("File not found. Loading default current perfect stage.");
		} catch (NumberFormatException e) {
			logger.warning("Invalid format for current perfect stage. Defaulting to 0.");
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return currentPsAchievement;
	}

	public void loadDefaultAchievementsFile() throws IOException {
		// 파일 경로 설정
		String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, "UTF-8");

		String achievementPath = new File(jarPath).getParent();
		achievementPath += File.separator;
		achievementPath += "achievements";

		File achievementFile = new File(achievementPath);

		// 파일이 없으면 새로 생성
		if (!achievementFile.exists()) {
			achievementFile.createNewFile();

			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(achievementFile), Charset.forName("UTF-8")))) {

				// 기본 내용 작성
				for (int i = 0; i <= 3; i++) {
					writer.write(String.valueOf(70+(i*10)));
					writer.newLine();
					writer.write("false");
					writer.newLine();
				}

				writer.flush();
				logger.info("Default achievements file created.");
			}
		} else {
			logger.info("Achievements file already exists.");
		}
	}

	public double loadAccuracyAchievement() throws IOException {

		double accuracy = 0;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String achievementPath = new File(jarPath).getParent();
			achievementPath += File.separator;
			achievementPath += "accuracyAchievement";

			File achievementFile = new File(achievementPath);
			inputStream = new FileInputStream(achievementFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			accuracy = Double.parseDouble(bufferedReader.readLine());

		} catch (FileNotFoundException e) {
			loadDefaultAchievementsFile();
			logger.info("File not found.");
		} catch (NumberFormatException e) {
			logger.warning("Invalid format for achievements.");
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

		logger.info("Achievements successfully loaded.");
		return accuracy; // 불러온 업적 데이터를 리스트로 반환
	}

	public boolean loadFlawlessFailureAchievement() throws IOException {
		boolean flawlessFailure = false;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String flawlessFailureAchievementPath = new File(jarPath).getParent();
			flawlessFailureAchievementPath += File.separator;
			flawlessFailureAchievementPath += "flawlessFailureAchievement";

			File flawlessFailureAchievementFile = new File(flawlessFailureAchievementPath);
			inputStream = new FileInputStream(flawlessFailureAchievementFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			Properties properties = new Properties();
			properties.load(bufferedReader); // Load properties from the file

			logger.info("Loading user perfect stage.");

			String flawlessFailureStr = properties.getProperty("Condition_Flawless_Failure", "false"); // Default to "0" if key not found
			flawlessFailure = flawlessFailureStr.equals("true");

		} catch (FileNotFoundException e) {
			// Load default if there's no user scores
			logger.info("File not found. Loading default current perfect stage.");
		} catch (NumberFormatException e) {
			logger.warning("Invalid format for current perfect stage. Defaulting to 0.");
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return flawlessFailure;
	}

	public boolean loadBestFriendsAchievement() throws IOException {
		boolean bestFriends = false;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String bestFriendsAchievementPath = new File(jarPath).getParent();
			bestFriendsAchievementPath += File.separator;
			bestFriendsAchievementPath += "BestFriendsAchievement";

			File bestFriendsAchievementFile = new File(bestFriendsAchievementPath);
			inputStream = new FileInputStream(bestFriendsAchievementFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream, Charset.forName("UTF-8")));

			Properties properties = new Properties();
			properties.load(bufferedReader); // Load properties from the file

			logger.info("Loading user condition of best friends.");

			String bestFriendsStr = properties.getProperty("Condition_Best_Friends", "false"); // Default to "0" if key not found
			bestFriends = bestFriendsStr.equals("true");

		} catch (FileNotFoundException e) {
			// Load default if there's no user scores
			logger.info("File not found. Loading default current perfect stage.");
		} catch (NumberFormatException e) {
			logger.warning("Invalid format for current perfect stage. Defaulting to 0.");
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return bestFriends;
	}

	/**
	 * Saves user high scores to disk.
	 * 
	 * @param highScores
	 *            High scores to save.
	 * @throws IOException
	 *             In case of loading problems.
	 */
	public void saveHighScores(final List<Score> highScores) 
			throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String scoresPath = new File(jarPath).getParent();
			scoresPath += File.separator;
			scoresPath += "scores";

			File scoresFile = new File(scoresPath);

			if (!scoresFile.exists())
				scoresFile.createNewFile();

			outputStream = new FileOutputStream(scoresFile);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user high scores.");

			for (Score score : highScores) {
				bufferedWriter.write(score.getName());
				bufferedWriter.newLine();
				bufferedWriter.write(Integer.toString(score.getScore()));
				bufferedWriter.newLine();
			}

		} finally {
			if (bufferedWriter != null)
				bufferedWriter.close();
		}
	}

	public void saveTotalScore(int totalScore) throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String totalScorePath = new File(jarPath).getParent();
			totalScorePath += File.separator;
			totalScorePath += "totalScore";  // Assuming the file name is 'info'

			File totalScoreFile = new File(totalScorePath);

			// Create the file if it doesn't exist
			if (!totalScoreFile.exists())
				totalScoreFile.createNewFile();

			// Use FileOutputStream with 'false' to overwrite the existing content
			outputStream = new FileOutputStream(totalScoreFile, false);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user total scores.");

			// Write the total score in the format TOTAL_SCORE='value'
			bufferedWriter.write("TOTAL_SCORE=" + totalScore);

		} finally {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public void saveTotalPlayTime(int totalPlayTime) throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String totalPlayTimePath = new File(jarPath).getParent();
			totalPlayTimePath += File.separator;
			totalPlayTimePath += "totalPlayTime";  // Assuming the file name is 'info'

			File totalPlayTimeFile = new File(totalPlayTimePath);

			// Create the file if it doesn't exist
			if (!totalPlayTimeFile.exists())
				totalPlayTimeFile.createNewFile();

			// Use FileOutputStream with 'false' to overwrite the existing content
			outputStream = new FileOutputStream(totalPlayTimeFile, false);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user total scores.");

			// Write the total score in the format TOTAL_SCORE='value'
			bufferedWriter.write("TOTAL_PLAY_TIME=" + totalPlayTime);

		} finally {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public void savePerfectAchievement(int currentPsAchievement) throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String currentPsAchievementPath = new File(jarPath).getParent();
			currentPsAchievementPath += File.separator;
			currentPsAchievementPath += "perfectAchievement";  // Assuming the file name is 'Perfect_Stage'

			File currentPsAchievementFile = new File(currentPsAchievementPath);

			// Create the file if it doesn't exist
			if (!currentPsAchievementFile.exists())
				currentPsAchievementFile.createNewFile();

			// Use FileOutputStream with 'false' to overwrite the existing content
			outputStream = new FileOutputStream(currentPsAchievementFile, false);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user perfect stage.");

			bufferedWriter.write("Perfect_Stage=" + currentPsAchievement);

		} finally {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public void saveWallet(final Wallet newWallet)
			throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8"); // 현재 파일 실행 경로. Current file execution path

			String walletPath = new File(jarPath).getParent(); // 상위 파일 경로. Parent file path
			walletPath += File.separator; // 파일 경로에 '/' 또는 '\' 추가(환경마다 다름). Add '/' or '\' to the file path (depends on the environment)
			walletPath += "wallet";

			File walletFile = new File(walletPath);

			if (!walletFile.exists())
				walletFile.createNewFile(); //파일이 없으면 새로 만듦. If the file does not exist, create a new one.

			outputStream = new FileOutputStream(walletFile); //덮어쓰기. Overwrite
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					outputStream, Charset.forName("UTF-8")));

			logger.info("Saving user wallet.");

			bufferedWriter.write(newWallet.getCoin() + "");
			bufferedWriter.newLine();
			bufferedWriter.write(newWallet.getBullet_lv() + "");
			bufferedWriter.newLine();
			bufferedWriter.write(newWallet.getShot_lv() + "");
			bufferedWriter.newLine();
			bufferedWriter.write(newWallet.getLives_lv() + "");
			bufferedWriter.newLine();
			bufferedWriter.write(newWallet.getCoin_lv() + "");
			bufferedWriter.newLine();

		} finally {
			if (bufferedWriter != null)
				bufferedWriter.close();
		}
	}

	public BufferedReader loadWallet() throws IOException {
		String jarPath = FileManager.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, "UTF-8");

		String walletPath = new File(jarPath).getParent();
		walletPath += File.separator;
		walletPath += "wallet"; // 지갑 파일 경로. Wallet file path

		File walletFile = new File(walletPath);
		if (!walletFile.exists()) {
			Core.getLogger().warning("Wallet file not found at " + walletPath);
			return null; // 파일이 없으면 null 반환. If the file does not exist, return null.
		}

		InputStream inputStream = new FileInputStream(walletFile);
		return new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
	}

	public void saveAccuracyAchievement(double accuracy) throws IOException {
		// 파일 경로 설정
		String jarPath = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		jarPath = URLDecoder.decode(jarPath, "UTF-8");

		String achievementPath = new File(jarPath).getParent();
		achievementPath += File.separator;
		achievementPath += "accuracyAchievement";

		File achievementFile = new File(achievementPath);

		// 업적 파일이 존재하면 내용을 갱신
		if (achievementFile.exists()) {
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(achievementFile), Charset.forName("UTF-8")))) {
				// 리스트의 내용을 파일에 기록
				writer.write(String.valueOf(accuracy));
				writer.flush();
				logger.info("Accuracy achievement successfully saved.");
			}
		} else {
			logger.warning("Achievements file not found at " + achievementPath);
		}
	}

	public void saveFlawlessFailureAchievement(boolean checkFlawlessFailure) throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String flawlessFailureAchievementPath = new File(jarPath).getParent();
			flawlessFailureAchievementPath += File.separator;
			flawlessFailureAchievementPath += "flawlessFailureAchievement";  // Assuming the file name is 'Perfect_Stage'

			File falwlessFailureAchievementFile = new File(flawlessFailureAchievementPath);

			// Create the file if it doesn't exist
			if (!falwlessFailureAchievementFile.exists())
				falwlessFailureAchievementFile.createNewFile();

			// Use FileOutputStream with 'false' to overwrite the existing content
			outputStream = new FileOutputStream(falwlessFailureAchievementFile, false);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

			logger.info("Saving condition flawless failure");

			bufferedWriter.write("Condition_Flawless_Failure=" + checkFlawlessFailure);

		} finally {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public void saveBestFriendsAchievement(boolean checkFlawlessFailure) throws IOException {
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;

		try {
			String jarPath = FileManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			String bestFriendsAchievementPath = new File(jarPath).getParent();
			bestFriendsAchievementPath += File.separator;
			bestFriendsAchievementPath += "BestFriendsAchievement";  // Assuming the file name is 'Perfect_Stage'

			File bestFriendsAchievementFile = new File(bestFriendsAchievementPath);

			// Create the file if it doesn't exist
			if (!bestFriendsAchievementFile.exists())
				bestFriendsAchievementFile.createNewFile();

			// Use FileOutputStream with 'false' to overwrite the existing content
			outputStream = new FileOutputStream(bestFriendsAchievementFile, false);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));

			logger.info("Saving condition best friends");

			bufferedWriter.write("Condition_Best_Friends=" + checkFlawlessFailure);

		} finally {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

}
