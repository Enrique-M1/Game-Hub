package application;



import javafx.application.Application;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.event.EventHandler;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.input.*;
import javafx.animation.*;
import javafx.geometry.*;

import java.util.Random;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;


public class gameHub extends Application {
	private Scene menuScene; // Store reference to the main menu scene

	// tic-tac-toe
	private int playerXWins = 0; // Track Player X's wins
	private int playerOWins = 0; // Track Player O's wins
	private int drawGames = 0;// track draws
	private boolean isXTurn = true; // Track whose turn it is

	// Hangman
	private List<String> words = new ArrayList<>(); // The list where the words will be stored
	private int wrongCount = 0; // The amount of times the player was incorrect
	private String answer; // The word the player is trying to get.
	private int answerLength; // Length of the answer (Determines amount of blanks for the answer)
	private StringBuilder hiddenWord = new StringBuilder();
	private int hangManScore = 0;
	private Group hangManGroup = new Group();
  
	// Dodger
	private Group stickManGroup = new Group();
	private final double move = 10;
	private int shapeCount = 0; // The amount of shapes that will be printed per round.
	private int timeElapsed = 0;
	private int roundCount = 1;
	private int dodgerScore = 0;

	// platformer
	private Group stickManGroup2 = new Group();
	
	// Tetris / Simon Says
	public static final int MOVE = 25 ;
	public static final int SIZE = 25 ;
	public static int XMAX = SIZE * 12 ;
	public static int YMAX = SIZE * 24 ;
	public static int[][] GRID = new int[XMAX / SIZE][YMAX / SIZE] ;
	private static Pane group = new Pane() ;
	private static Piece object ;
	private static Scene scene = new Scene(group, XMAX + 150, YMAX) ;
	public static int score = 0 ;
	private static int top = 0 ;
	private static boolean game = true ;
	private static Piece nextObj = Controller.makeRect() ;
	private static int linesNo = 0 ;
	
	private static final int SEQUENCE_LENGTH = 5;
	private final List<Color> colorSequence = new ArrayList<>();
	private final List<Color> playerSequence = new ArrayList<>();
	private final Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
	private int currentStep = 0;

	
	

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Game Hub");

			// Start Screen
			Label titleLabel = new Label("Game Hub");
			titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36)); // Large font
			titleLabel.setAlignment(Pos.CENTER);

			Button startButton = new Button("Start");
			
			VBox startPane = new VBox(20, titleLabel, startButton); // VBox with spacing
			startPane.setAlignment(Pos.CENTER); // Center content in the VBox
			
			Scene startScene = new Scene(startPane, 400, 300);

			// Second Menu with 8 Options
			VBox menuPane = new VBox(10);
			menuPane.setAlignment(Pos.CENTER);
			menuScene = new Scene(menuPane, 400, 300); // Set menuScene to be used later

			for (int i = 1; i <= 8; i++) {
				Button optionButton;

				if (i == 1) {
					optionButton = new Button("Tic-Tac-Toe");
					optionButton.setOnAction(e -> primaryStage.setScene(createTicTacToeModeSelectionScene(primaryStage)));
				} else if (i == 2) {
					optionButton = new Button("Quick Math");
					optionButton.setOnAction(e -> primaryStage.setScene(createMathGameLevelSelection(primaryStage)));
				} else if (i == 3) {
					optionButton = new Button("Hangman");
					optionButton.setOnAction(e -> primaryStage.setScene(createHangManGameScene(primaryStage)));
				} else if (i == 4) {
					optionButton = new Button("Dodger");
					optionButton.setOnAction(e -> primaryStage.setScene(createDodgerGameScene(primaryStage)));
				} else if (i == 5) {
					optionButton = new Button("Pong");
					optionButton.setOnAction(e -> primaryStage.setScene(createPongStartScene(primaryStage)));
				} else if (i == 6) {
					optionButton = new Button("Platformer");
					optionButton.setOnAction(e -> primaryStage.setScene(createPlatformerStartMenuScene(primaryStage)));
				} else if (i == 7) {
					optionButton = new Button("Simon Says");
					optionButton.setOnAction(e -> {
						try {
							Simonstart(primaryStage);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
				} else {
					optionButton = new Button("Tetris");
					optionButton.setOnAction(e -> {
						try {
							Tetrisstart(primaryStage);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
				}

				menuPane.getChildren().add(optionButton);
			}

			// Set Action to Start Button to Go to the Menu
			startButton.setOnAction(e -> primaryStage.setScene(menuScene));

			// Show Start Screen Initially
			primaryStage.setScene(startScene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ****************************************************************************************************
	// ****************************************************************************************************

	private Timeline timeline; // Timer for the game
	private Timeline countdown; // Timer for the ready screen

	// Precondition: Math game is started
	// Postcondition: user selects level for math game
	// ----------------------------------------------------------------------------------------------------
	// level selection for math game
	private Scene createMathGameLevelSelection(Stage stage) {
		VBox levelSelectionRoot = new VBox(20);
		levelSelectionRoot.setAlignment(Pos.CENTER);

		// Text for the level selection
		Text selectLevelText = new Text("Select Level:");
		selectLevelText.setStyle("-fx-font-size: 18px;");

		// Buttons for each level
		Button level1Button = new Button("Level 1 (Addition)");
		Button level2Button = new Button("Level 2 (Addition, Subtraction)");
		Button level3Button = new Button("Level 3 (Multiplication, Division)");
		Button backButton = new Button("Back to Menu");

		// Set actions for each button
		level1Button.setOnAction(e -> stage.setScene(createMathGameReadyScene(stage, 1)));
		level2Button.setOnAction(e -> stage.setScene(createMathGameReadyScene(stage, 2)));
		level3Button.setOnAction(e -> stage.setScene(createMathGameReadyScene(stage, 3)));

		backButton.setOnAction(e -> stage.setScene(menuScene));

		levelSelectionRoot.getChildren().addAll(selectLevelText, level1Button, level2Button, level3Button, backButton);
		return new Scene(levelSelectionRoot, 400, 400);
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: math game is started and level is started
	// postcondition: countdown starts and game starts
	// ----------------------------------------------------------------------------------------------------
	// create game ready scene
	private Scene createMathGameReadyScene(Stage stage, int level) {
		VBox readyRoot = new VBox(20);
		readyRoot.setAlignment(Pos.CENTER);

		// Text for the ready screen
		Text getReadyText = new Text("Get Ready!");
		getReadyText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		// countdown timer for the game
		Text countdownText = new Text("3");
		countdownText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

		Button backButton = new Button("Back to Level Selection");
		backButton.setOnAction(e -> {
			if (countdown != null)
				countdown.stop();
			stage.setScene(createMathGameLevelSelection(stage));
		});

		readyRoot.getChildren().addAll(getReadyText, countdownText, backButton);

		Scene readyScene = new Scene(readyRoot, 400, 400);

		// start game after countdown
		countdown = new Timeline(

				// get ready screen
				new KeyFrame(Duration.seconds(1), e -> {
					int currentCount = Integer.parseInt(countdownText.getText());

					// decrement countdown
					if (currentCount > 1) {
						countdownText.setText(String.valueOf(currentCount - 1));

						// stop countdown and start game
					} else {
						countdown.stop();
						stage.setScene(createMathGameScene(stage, level));
					}
				}));
		// set cycle count to 3 and play countdown
		countdown.setCycleCount(3);
		countdown.play();

		return readyScene;
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: math game is started
	// postcondition: user plays math game and ends game
	// ----------------------------------------------------------------------------------------------------
	// create math game scene
	private Scene createMathGameScene(Stage stage, int initialLevel) {
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);

		// question texts
		Text questionText = new Text();
		questionText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		// feedback texts
		Text feedbackText = new Text();
		Text timerText = new Text();
		Text scoreText = new Text("Score: 0");
		Text levelText = new Text("Level: " + initialLevel);

		// create input field for answer
		TextField answerInput = new TextField();
		answerInput.setPromptText("Enter your answer...");
		answerInput.setMaxWidth(200);

		// buttons for submit and back
		Button submitButton = new Button("Submit");
		Button backButton = new Button("Back to Level Selection");

		// variables for the game
		int[] score = { 0 };
		int[] timeLeft = { 10 };
		int[] maxTime = { Math.max(10 - initialLevel, 5) };
		int[] currentAnswer = { 0 };
		int[] questionsAnswered = { 0 };
		int[] currentLevel = { initialLevel };

		// timer for the game
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

			// decrement time left and update timer text
			timeLeft[0]--;
			timerText.setText("Time left: " + timeLeft[0] + " seconds");

			// end game if time runs out and show feedback
			if (timeLeft[0] <= 0) {
				feedbackText.setText("Time's up! The correct answer was " + currentAnswer[0]);// show feedback
				timeline.stop();
				endGame(stage, score[0]);
			}
		}));

		// timer runs indefinitely until stopped or game ends
		timeline.setCycleCount(Timeline.INDEFINITE);

		// "Runnable" is used for the lambda expression to avoid the "effectively final"
		// error in Java 8 and later versions. method used to generate question
		Runnable generateQuestion = () -> {
			String[] operators;

			// set operators based on level

			// level 1 only has addition
			if (currentLevel[0] == 1) {
				operators = new String[] { "+" };

				// level 2 has addition and subtraction
			} else if (currentLevel[0] == 2) {
				operators = new String[] { "+", "-" };

				// level 3 has multiplication and division
			} else {
				operators = new String[] { "*", "/" };
			}

			// generate random numbers and operator
			int maxNumber = currentLevel[0] * 10;
			int num1 = (int) (Math.random() * maxNumber) + 1;
			int num2 = (int) (Math.random() * maxNumber) + 1;
			String operator = operators[(int) (Math.random() * operators.length)];

			// avoid division by zero
			if (operator.equals("/")) {
				num2 = Math.max(1, num2);
				num1 = num2 * ((int) (Math.random() * 10) + 1);
			}

			// calculate answer
			currentAnswer[0] = calculateAnswer(num1, num2, operator);

			// display question
			questionText.setText("What is " + num1 + " " + operator + " " + num2 + "?");

			// reset timer and update texts
			timeLeft[0] = maxTime[0];
			timerText.setText("Time left: " + timeLeft[0] + " seconds");
			feedbackText.setText("");
		};

		// handle submission of answer
		Runnable handleSubmission = () -> {
			try {

				// get user answer and check if correct
				int userAnswer = Integer.parseInt(answerInput.getText());
				if (userAnswer == currentAnswer[0]) {
					feedbackText.setText("Correct!");

					// increment score and update texts
					score[0]++;
					scoreText.setText("Score: " + score[0]);
					questionsAnswered[0]++;

					// check if level should be increased every 5 questions
					if (currentLevel[0] < 3 && questionsAnswered[0] % 5 == 0) {
						currentLevel[0]++;
						maxTime[0] = Math.max(5, 10 - currentLevel[0]);
						levelText.setText("Level: " + currentLevel[0]);
					}

					// generate new question
					generateQuestion.run();

					// show feedback if answer is incorrect and end game
				} else {
					feedbackText.setText("Incorrect! The correct answer was " + currentAnswer[0]);
					timeline.stop();
					endGame(stage, score[0]);
				}

				// handle invalid input
			} catch (NumberFormatException ex) {
				feedbackText.setText("Please enter a valid number!");
			}
			answerInput.clear();
		};

		// set actions for buttons
		submitButton.setOnAction(e -> handleSubmission.run());
		answerInput.setOnAction(e -> handleSubmission.run());

		backButton.setOnAction(e -> {
			if (timeline != null)
				timeline.stop();
			stage.setScene(createMathGameLevelSelection(stage));
		});

		generateQuestion.run();
		timeline.play();

		root.getChildren().addAll(levelText, questionText, timerText, answerInput, submitButton, feedbackText,
				scoreText, backButton);
		return new Scene(root, 400, 400);
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: math game ends
	// postcondition: user returns to level selection and score is shown
	// ----------------------------------------------------------------------------------------------------
	// end game and show final score
	private void endGame(Stage stage, int score) {

		// game ends stop timer
		if (timeline != null)
			timeline.stop();

		// create an alert to show final score
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Game Over");
		alert.setHeaderText("Game Over!");
		alert.setContentText("Your total score is: " + score);
		alert.showAndWait();

		// return to level selection
		stage.setScene(createMathGameLevelSelection(stage));
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: math game is started
	// postcondition: returns answer based on operator
	// ----------------------------------------------------------------------------------------------------
	// calculate answer based on operator
	private int calculateAnswer(int num1, int num2, String operator) {
		return switch (operator) {
			case "+" -> num1 + num2;
			case "-" -> num1 - num2;
			case "*" -> num1 * num2;
			case "/" -> num1 / num2;
			default -> 0;
		};
	}
	// ----------------------------------------------------------------------------------------------------

	// ****************************************************************************************************
	// ****************************************************************************************************

	// precondition: Tic-Tac-Toe game is started
	// postcondition: user selects game mode
	// ----------------------------------------------------------------------------------------------------
	// Scene to select game mode (play against computer or another player)
	private Scene createTicTacToeModeSelectionScene(Stage stage) {
		VBox modePane = new VBox(20);
		modePane.setAlignment(Pos.CENTER); // Center align the mode selection items
		Scene modeScene = new Scene(modePane, 400, 300);

		// Create the mode selection items
		Text selectModeText = new Text("Select Mode:");
		Button vsComputerButton = new Button("Play Against Computer");
		Button vsPlayerButton = new Button("Play Against Another Player");
		Button backButton = new Button("Back to Main Menu");

		// Set actions for each button
		vsComputerButton.setOnAction(e -> stage.setScene(createDifficultySelectionScene(stage)));
		vsPlayerButton.setOnAction(e -> stage.setScene(createTicTacToeScene(stage, false, false)));
		backButton.setOnAction(e -> stage.setScene(menuScene));

		modePane.getChildren().addAll(selectModeText, vsComputerButton, vsPlayerButton, backButton);
		return modeScene;
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: Tic-Tac-Toe game is started
	// postcondition: user selects difficulty for computer
	// ----------------------------------------------------------------------------------------------------
	// Scene to select difficulty for playing against computer
	private Scene createDifficultySelectionScene(Stage stage) {

		// Create the difficulty selection items
		VBox difficultyPane = new VBox(20);
		difficultyPane.setAlignment(Pos.CENTER); // Center align the difficulty selection items
		Scene difficultyScene = new Scene(difficultyPane, 400, 300);

		// Create the difficulty selection items
		Text difficultyText = new Text("Select Difficulty:");
		Button easyButton = new Button("Easy");
		Button hardButton = new Button("Hard");
		Button backButton = new Button("Back to Mode Selection");

		// Set actions for each button
		easyButton.setOnAction(e -> stage.setScene(createTicTacToeScene(stage, true, false)));
		hardButton.setOnAction(e -> stage.setScene(createTicTacToeScene(stage, true, true)));
		backButton.setOnAction(e -> stage.setScene(createTicTacToeModeSelectionScene(stage)));

		difficultyPane.getChildren().addAll(difficultyText, easyButton, hardButton, backButton);
		return difficultyScene;
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: Tic-Tac-Toe game is started
	// postcondition: user plays Tic-Tac-Toe game
	// ----------------------------------------------------------------------------------------------------
	private Scene createTicTacToeScene(Stage stage, boolean playAgainstComputer, boolean hardMode) {
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER); // Center align the game elements

		// Text elements for the game to display current turn and score
		Text turnText = new Text("Turn: Player X");
		Text scoreText = new Text(
				"Player X Wins: " + playerXWins + " | Player O Wins: " + playerOWins + " | Draws: " + drawGames);

		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setAlignment(Pos.CENTER); // Center align the game board

		// Create the Tic-Tac-Toe board
		Button[][] board = new Button[3][3];

		// Initialize the Tic-Tac-Toe board
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {

				// Create a button for each cell and add it to the board
				Button cell = new Button();
				cell.setPrefSize(100, 100);
				cell.setStyle("-fx-font-size: 24;");
				int finalRow = row;
				int finalCol = col;
				cell.setOnAction(e -> handleMove(cell, turnText, board, stage, scoreText, playAgainstComputer, hardMode,
						finalRow, finalCol));
				board[row][col] = cell;
				grid.add(cell, col, row);
			}
		}

		Button backButton = new Button("Back to Mode Selection");
		backButton.setOnAction(e -> stage.setScene(createTicTacToeModeSelectionScene(stage)));

		root.getChildren().addAll(turnText, scoreText, grid, backButton);
		return new Scene(root, 400, 400);
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: Tic-Tac-Toe game is started
	// postcondition: checks for winner and ends game
	// ----------------------------------------------------------------------------------------------------
	private void handleMove(Button cell, Text turnText, Button[][] board, Stage stage, Text scoreText,
			boolean playAgainstComputer, boolean hardMode, int row, int col) {

		// ignore if cell is already clicked
		if (!cell.getText().isEmpty())
			return;

		// Set the cell text based on the current turn
		cell.setText(isXTurn ? "X" : "O");
		animateCellClick(cell); // Animate cell click

		// Check for a winner
		String winner = checkWinnerAndHighlight(board);
		if (winner != null) {
			endGame(winner, board, turnText, scoreText, stage);
			return;

			// Check for a draw
		} else if (isBoardFull(board)) {
			endGame("Draw", board, turnText, scoreText, stage);
			return;
		}

		// If playing against the computer, handle the computer's turn
		if (playAgainstComputer && isXTurn) {

			// If it's the computer's turn
			if (hardMode) {

				computerMoveHard(board); // Use hard AI

			} else {
				computerMoveEasy(board); // Use random AI
			}

			// Check for a winner after the computer's turn
			winner = checkWinnerAndHighlight(board);
			if (winner != null) {
				endGame(winner, board, turnText, scoreText, stage);
			} else if (isBoardFull(board)) {
				endGame("Draw", board, turnText, scoreText, stage);
			}
		} else {
			// Alternate the turn for player vs player
			isXTurn = !isXTurn;
			turnText.setText("Turn: Player " + (isXTurn ? "X" : "O"));
		}
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: Tic-Tac-Toe game is started and easy mode is selected
	// postcondition: computer makes a move
	// ----------------------------------------------------------------------------------------------------
	private void computerMoveEasy(Button[][] board) {
		for (Button[] row : board) {
			for (Button cell : row) {
				if (cell.getText().isEmpty()) {
					cell.setText("O"); // Computer uses O
					animateCellClick(cell);
					return;
				}
			}
		}
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: Tic-Tac-Toe game is started and hard mode is selected
	// postcondition: computer makes a move
	// ----------------------------------------------------------------------------------------------------
	private void computerMoveHard(Button[][] board) {

		// Find the best move for the computer
		int[] bestMove = findBestMove(board);
		int row = bestMove[0];
		int col = bestMove[1];
		board[row][col].setText("O"); // Computer uses O
		animateCellClick(board[row][col]);
	}
	// ----------------------------------------------------------------------------------------------------

	// precondition: Tic-Tac-Toe game is started
	// postcondition: finds the best move for the computer
	// ----------------------------------------------------------------------------------------------------
	private int[] findBestMove(Button[][] board) {
		int bestScore = Integer.MIN_VALUE;
		int[] bestMove = { -1, -1 };

		// Try all possible moves and find the best score for the computer
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (board[row][col].getText().isEmpty()) {
					board[row][col].setText("O");

					// Minimax algorithm to find the best move
					int score = minimax(board, false);
					board[row][col].setText("");
					if (score > bestScore) {
						bestScore = score;
						bestMove = new int[] { row, col };
					}
				}
			}
		}
		return bestMove;
	}
	// ----------------------------------------------------------------------------------------------------

	//precondition: Tic-Tac-Toe game is started
	//postcondition: minimax algorithm is used to find the best move for the computer
	// ----------------------------------------------------------------------------------------------------
	private int minimax(Button[][] board, boolean isMaximizing) {

		//use minimax algorithm to find the best move for the computer without highlighting cells
		String winner = checkWinnerWithoutHighlight(board); 
		if (winner != null) {
			if (winner.equals("O"))
				return 10;
			if (winner.equals("X"))
				return -10;
			return 0;
		}

		// If the board is full, return 0
		if (isMaximizing) {
			int bestScore = Integer.MIN_VALUE;
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					if (board[row][col].getText().isEmpty()) {
						board[row][col].setText("O");
						int score = minimax(board, false);
						board[row][col].setText("");
						bestScore = Math.max(score, bestScore);
					}
				}
			}
			return bestScore;
		} else {
			int bestScore = Integer.MAX_VALUE;
			for (int row = 0; row < 3; row++) {
				for (int col = 0; col < 3; col++) {
					if (board[row][col].getText().isEmpty()) {
						board[row][col].setText("X");
						int score = minimax(board, true);
						board[row][col].setText("");
						bestScore = Math.min(score, bestScore);
					}
				}
			}
			return bestScore;
		}
	}
	// ----------------------------------------------------------------------------------------------------

	//precondition: Tic-Tac-Toe game is started
	//postcondition: checks for winner and ends game
	// ----------------------------------------------------------------------------------------------------
	private void endGame(String result, Button[][] board, Text turnText, Text scoreText, Stage stage) {

		//x wins increment player x wins
		if (result.equals("X")) {
			playerXWins++;
			displayWinMessage("Player X wins!");

		//o wins increment player o wins
		} else if (result.equals("O")) {
			playerOWins++;
			displayWinMessage("Player O wins!");
		
		//draw increment draw games
		} else {
			drawGames++;
			displayWinMessage("It's a draw!");
		}

		//reset board
		resetBoard(board, turnText, scoreText);
	}
	// ----------------------------------------------------------------------------------------------------

	//precondition: Tic-Tac-Toe game is started
	//postcondition: checks for winner and highlights winning cells
	// ----------------------------------------------------------------------------------------------------
	private String checkWinnerAndHighlight(Button[][] board) {

		// Check rows and columns
		for (int i = 0; i < 3; i++) {

			// Check rows for wins and highlight winning cells
			if (!board[i][0].getText().isEmpty() && board[i][0].getText().equals(board[i][1].getText())
					&& board[i][0].getText().equals(board[i][2].getText())) {
				highlightWinningCells(board[i][0], board[i][1], board[i][2]);
				return board[i][0].getText();
			}
			// Check columns for wins and highlight winning cells
			if (!board[0][i].getText().isEmpty() && board[0][i].getText().equals(board[1][i].getText())
					&& board[0][i].getText().equals(board[2][i].getText())) {
				highlightWinningCells(board[0][i], board[1][i], board[2][i]);
				return board[0][i].getText();
			}
		}
		// Check diagonals
		if (!board[0][0].getText().isEmpty() && board[0][0].getText().equals(board[1][1].getText())
				&& board[0][0].getText().equals(board[2][2].getText())) {
			highlightWinningCells(board[0][0], board[1][1], board[2][2]);
			return board[0][0].getText();
		}
		if (!board[0][2].getText().isEmpty() && board[0][2].getText().equals(board[1][1].getText())
				&& board[0][2].getText().equals(board[2][0])) {
			highlightWinningCells(board[0][2], board[1][1], board[2][0]);
			return board[0][2].getText();
		}
		return null;
	}
	// ----------------------------------------------------------------------------------------------------

	//precondition: Tic-Tac-Toe game is started
	//postcondition: checks for winner without highlighting winning cells used for min max algorithm
	// ----------------------------------------------------------------------------------------------------
	private String checkWinnerWithoutHighlight(Button[][] board) {
		// Check rows and columns
		for (int i = 0; i < 3; i++) {
			// Check rows
			if (!board[i][0].getText().isEmpty() && board[i][0].getText().equals(board[i][1].getText())
					&& board[i][0].getText().equals(board[i][2].getText())) {
				return board[i][0].getText();
			}
			// Check columns
			if (!board[0][i].getText().isEmpty() && board[0][i].getText().equals(board[1][i].getText())
					&& board[0][i].getText().equals(board[2][i].getText())) {
				return board[0][i].getText();
			}
		}
		// Check diagonals
		if (!board[0][0].getText().isEmpty() && board[0][0].getText().equals(board[1][1].getText())
				&& board[0][0].getText().equals(board[2][2].getText())) {
			return board[0][0].getText();
		}
		if (!board[0][2].getText().isEmpty() && board[0][2].getText().equals(board[1][1].getText())
				&& board[0][2].getText().equals(board[2][0])) {
			return board[0][2].getText();
		}
		return null;
	}
	// ----------------------------------------------------------------------------------------------------

	//precondition: Tic-Tac-Toe game is started
	//postcondition: highlights winning cells
	// ----------------------------------------------------------------------------------------------------
	private void highlightWinningCells(Button... cells) {
		for (Button cell : cells) {
			cell.setStyle("-fx-background-color: yellow; -fx-font-size: 24;");
		}
	}
	// ----------------------------------------------------------------------------------------------------

	//precondition: Tic-Tac-Toe game is started
	//postcondition: checks if the board is full
	// ----------------------------------------------------------------------------------------------------
	private boolean isBoardFull(Button[][] board) {
		for (Button[] row : board) {
			for (Button cell : row) {
				if (cell.getText().isEmpty())
					return false;
			}
		}
		return true;
	}
	// ----------------------------------------------------------------------------------------------------

	//precondition: Tic-Tac-Toe game is started
	//postcondition: resets the board
	// ----------------------------------------------------------------------------------------------------
	private void resetBoard(Button[][] board, Text turnText, Text scoreText) {
		for (Button[] row : board) {
			for (Button cell : row) {
				cell.setText("");
				cell.setStyle(""); // Reset cell style
			}
		}
		isXTurn = true;
		turnText.setText("Turn: Player X");
		scoreText.setText(
				"Player X Wins: " + playerXWins + " | Player O Wins: " + playerOWins + " | Draws: " + drawGames);
	}
	// ----------------------------------------------------------------------------------------------------

	//Postcondition: animates cell click
	// ----------------------------------------------------------------------------------------------------
	private void animateCellClick(Button cell) {
		ScaleTransition scale = new ScaleTransition(Duration.seconds(0.3), cell);
		scale.setFromX(1.0);
		scale.setFromY(1.0);
		scale.setToX(1.2);
		scale.setToY(1.2);
		scale.setAutoReverse(true);
		scale.setCycleCount(2);
		scale.play();
	}
	// ----------------------------------------------------------------------------------------------------

	//precondition: Tic-Tac-Toe game is started
	//postcondition: displays win message
	// ----------------------------------------------------------------------------------------------------
	private void displayWinMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Over");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	
	
	
	/****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************/

	// start screen
	private Scene createPongStartScene(Stage stage) {
		Text titleText = new Text("Pong");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 36)); 
		titleText.setTextAlignment(TextAlignment.CENTER);

		// Create buttons
		Button startButton = new Button("Start");
		Button menuButton = new Button("Back to Menu");

		// Button actions
		startButton.setOnAction(e -> stage.setScene(createPongScene(stage)));
		menuButton.setOnAction(e -> stage.setScene(menuScene)); 

		// Layout for start screen
		VBox startLayout = new VBox(20, titleText, startButton, menuButton);
		startLayout.setAlignment(Pos.CENTER); 
		startLayout.setStyle("-fx-padding: 20;");

		return new Scene(startLayout, 400, 300);
	}

    private Scene createPongScene(Stage stage) {
		final int width = 400;
		final int height = 300;

		// create paddles and ball
		Rectangle paddle1 = new Rectangle(10, 60);
		Rectangle paddle2 = new Rectangle(10, 60);
		Circle ball = new Circle(10);
		paddle1.setLayoutX(20);
		paddle1.setLayoutY(height / 2 - paddle1.getHeight() / 2);
		paddle2.setLayoutX(width - 30);
		paddle2.setLayoutY(height / 2 - paddle2.getHeight() / 2);
		ball.setLayoutX(width / 2);
		ball.setLayoutY(height / 2);

		// ball movement variables 
		double[] ballSpeed = {4, 4};

		// paddle movement variables
		boolean[] paddle1Up = {false};
		boolean[] paddle1Down = {false};

		// counter
		int[] returnCount = {0};

		// text to display return count
		Text returnCountText = new Text("0");
		returnCountText.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
		returnCountText.setLayoutX(width / 2 - 50); 
		returnCountText.setLayoutY(20); 

		// pane for Pong
		Pane pongPane = new Pane(paddle1, paddle2, ball, returnCountText);
		pongPane.setPrefSize(width, height);

		// key event handlers for player paddle controls
		pongPane.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case W -> paddle1Up[0] = true;
				case S -> paddle1Down[0] = true;
			}
		});

		pongPane.setOnKeyReleased(e -> {
			switch (e.getCode()) {
				case W -> paddle1Up[0] = false;
				case S -> paddle1Down[0] = false;
			}
		});

		// animation timeline
		Timeline[] Ptimeline = new Timeline[1];
		Ptimeline[0] = new Timeline(new KeyFrame(Duration.millis(10), e -> {
			// move player's paddle
			if (paddle1Up[0] && paddle1.getLayoutY() > 0) {
				paddle1.setLayoutY(paddle1.getLayoutY() - 5);
			}
			if (paddle1Down[0] && paddle1.getLayoutY() < height - paddle1.getHeight()) {
				paddle1.setLayoutY(paddle1.getLayoutY() + 5);
			}

			// move AI paddle
			paddle2.setLayoutY(ball.getLayoutY() - paddle2.getHeight() / 2);

			// ensure AI paddle stays within bounds
			if (paddle2.getLayoutY() < 0) {
				paddle2.setLayoutY(0);
			} else if (paddle2.getLayoutY() > height - paddle2.getHeight()) {
				paddle2.setLayoutY(height - paddle2.getHeight());
			}

			// move ball
			ball.setLayoutX(ball.getLayoutX() + ballSpeed[0]);
			ball.setLayoutY(ball.getLayoutY() + ballSpeed[1]);

			// bounce off top and bottom walls
			if (ball.getLayoutY() <= 0 || ball.getLayoutY() >= height - ball.getRadius()) {
				ballSpeed[1] *= -1;
			}

			// bounce off paddles
			if (ball.getBoundsInParent().intersects(paddle1.getBoundsInParent())) {
				ballSpeed[0] *= -1;
				returnCount[0]++;
				returnCountText.setText(""+returnCount[0]); 
			} else if (ball.getBoundsInParent().intersects(paddle2.getBoundsInParent())) {
				ballSpeed[0] *= -1;
			}

			// check for game over 
			if (ball.getLayoutX() < 0) {
				Ptimeline[0].stop();
				showGameOverScreen(stage, returnCount[0]);
			}
		}));

		Ptimeline[0].setCycleCount(Timeline.INDEFINITE);
		Ptimeline[0].play();

		// layout 
		VBox gameLayout = new VBox(10, pongPane);
		gameLayout.setStyle("-fx-alignment: center; -fx-padding: 10;");
		Scene pongScene = new Scene(gameLayout, width, height);
		pongScene.setOnKeyPressed(pongPane.getOnKeyPressed());
		pongScene.setOnKeyReleased(pongPane.getOnKeyReleased());

		return pongScene;
	}

	// game over screen
	private void showGameOverScreen(Stage stage, int returnCount) {
		Text gameOverText = new Text("Game Over! Your score: " + returnCount);
		Button restartButton = new Button("Restart");
		Button menuButton = new Button("Return to Menu");

		restartButton.setOnAction(e -> stage.setScene(createPongScene(stage))); 
		menuButton.setOnAction(e -> stage.setScene(menuScene)); 

		VBox gameOverLayout = new VBox(10, gameOverText, restartButton, menuButton);
		gameOverLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		Scene gameOverScene = new Scene(gameOverLayout, 400, 300);

		stage.setScene(gameOverScene);
	}

    // ****************************************************************************************************
	// ****************************************************************************************************

	// start screen
	private Scene createPlatformerStartMenuScene(Stage stage){
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);

		Label titleLabel = new Label("Plateformer");
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));

		Button startButton = new Button("Start");
		startButton.setOnAction(e -> stage.setScene(createPlatformerScene(stage)));

		Button backButton = new Button("Back to menu");
		backButton.setOnAction(e -> stage.setScene(menuScene));

		root.getChildren().addAll(titleLabel, startButton,backButton);

		return new Scene(root, 600, 300);
	}

	private Scene createPlatformerScene(Stage stage) {
		Pane root = new Pane();

		// floor
		Rectangle floor = new Rectangle(600, 50);
		floor.setFill(Color.BLACK);
		floor.setLayoutY(250);

		// player
		drawStickMan2();
		stickManGroup2.setLayoutX(50);
		stickManGroup2.setLayoutY(110);

		// timer
		Label timerLabel = new Label("Time: 0");
		timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		timerLabel.setLayoutX(10);
		timerLabel.setLayoutY(10);

		root.getChildren().addAll(timerLabel, floor, stickManGroup2);

		boolean[] isJumping = {false};
		boolean[] gameOver = {false}; 
		int[] timeSurvived = {0}; 

		// scene setup
		Scene platformerScene = new Scene(root, 600, 300);

		// jumping and ducking
		platformerScene.setOnKeyPressed(event -> {
			// jump
			if (event.getCode() == KeyCode.W && !isJumping[0] && !gameOver[0]) {
				isJumping[0] = true;

				TranslateTransition jump = new TranslateTransition(Duration.millis(300), stickManGroup2);
				jump.setByY(-150); 
				jump.setAutoReverse(true);
				jump.setCycleCount(2);
				jump.setOnFinished(e -> isJumping[0] = false);
				jump.play();

			// duck
			} else if (event.getCode() == KeyCode.S && !gameOver[0]) {
				if (stickManGroup2.getScaleY() == 1) {
					stickManGroup2.setScaleY(0.5); 
					stickManGroup2.setLayoutY(stickManGroup2.getLayoutY() + 30); 
				}
			}
		});
		// stand up after ducking
		platformerScene.setOnKeyReleased(event -> {
			if (event.getCode() == KeyCode.S && !gameOver[0]) { 
				if (stickManGroup2.getScaleY() == 0.5) {
					stickManGroup2.setScaleY(1);
					stickManGroup2.setLayoutY(stickManGroup2.getLayoutY() - 30);
				}
			}
		});

		// start spawning circles
		startGame(root, stickManGroup2, gameOver, timeSurvived, stage);

		// start the timer
		startTimer(timerLabel, gameOver, timeSurvived);

		return platformerScene;
	}

	private void startGame(Pane root, Group player, boolean[] gameOver, int[] timeSurvived, Stage stage) {
		// timeline to spawn random circles
		Timeline spawnCirclesTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> spawnRandomCircle(root, player, gameOver, timeSurvived, stage)));
		spawnCirclesTimeline.setCycleCount(Timeline.INDEFINITE);
		spawnCirclesTimeline.play();
	}

	private void spawnRandomCircle(Pane root, Group player, boolean[] gameOver, int[] timeSurvived, Stage stage) {
		if (gameOver[0]) return; 

		// random position and radius for the circle
		Random rand = new Random();
		int radius = rand.nextInt(31) + 10; 
		double startX = 600; 
		double startY = rand.nextInt(201) + 100;

		// build circle
		Circle circle = new Circle(startX, startY, radius);
		circle.setFill(Color.RED);
		root.getChildren().add(circle);

		// move the circle from right to left
		TranslateTransition moveCircle = new TranslateTransition(Duration.seconds(1.25), circle);
		moveCircle.setByX(-1000); 

		// timeline for collisions
		Timeline[] collisionCheckTimeline = new Timeline[1];
		collisionCheckTimeline[0] = new Timeline(new KeyFrame(Duration.millis(10), event -> {
			if (circle.getBoundsInParent().intersects(player.getBoundsInParent())) {
				gameOver[0] = true;
				stage.setScene(createGameOverScene(stage, timeSurvived[0]));
				collisionCheckTimeline[0].stop(); 
				moveCircle.stop(); 
			}
		}));
		collisionCheckTimeline[0].setCycleCount(Timeline.INDEFINITE);
		collisionCheckTimeline[0].play();
		moveCircle.setOnFinished(e -> {
			root.getChildren().remove(circle);
			collisionCheckTimeline[0].stop(); 
		});

		moveCircle.play();
	}

	// game over screen
	private Scene createGameOverScene(Stage stage, int timeSurvived) {
		StackPane gameOverPane = new StackPane();
		Text gameOverText = new Text("Game Over!\nYou survived: " + timeSurvived + " seconds");

		Button restartButton = new Button("Restart");
		restartButton.setOnAction(e -> {
			stickManGroup2.setScaleY(1); 
			stickManGroup2.setLayoutY(110); 
			stage.setScene(createPlatformerScene(stage));
		});

		Button menuButton = new Button("Return to Menu");
		menuButton.setOnAction(e -> stage.setScene(menuScene)); 

		VBox vbox = new VBox(20, gameOverText, restartButton, menuButton); 
		vbox.setAlignment(Pos.CENTER);

		gameOverPane.getChildren().add(vbox);

		return new Scene(gameOverPane, 600, 300);
	}

	private void startTimer(Label timerLabel, boolean[] gameOver, int[] timeSurvived) {
		Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			if (!gameOver[0]) {
				timeSurvived[0]++;
				timerLabel.setText("Time: " + timeSurvived[0]);
			}
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	private void drawStickMan2() {
		Circle head = new Circle(20, 50, 15); 
		Line body = new Line(20, 65, 20, 115);
		Line leftArm = new Line(20, 75, 0, 55);
		Line rightArm = new Line(20, 75, 40, 55);
		Line leftLeg = new Line(20, 115, 0, 140);
		Line rightLeg = new Line(20, 115, 40, 140);

		head.setFill(Color.WHITE);
		head.setStroke(Color.BLACK);
		head.setStrokeWidth(2);
		body.setStrokeWidth(3);
		leftArm.setStrokeWidth(3);
		rightArm.setStrokeWidth(3);
		leftLeg.setStrokeWidth(3);
		rightLeg.setStrokeWidth(3);
		stickManGroup2.getChildren().addAll(head, body, leftArm, rightArm, leftLeg, rightLeg);
	}

	/****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************/

    // Hang Man Game Creation 
	private Scene createHangManGameScene(Stage stage) {
		// Declare Variables
		Pane game = new Pane();
	    Button submitButton = new Button("Submit");
	    Button backButton = new Button("Back to Menu");
	    Button retryButton = new Button ("Restart");
		TextField wordGuess = new TextField(); // Where the user will input the full 
		TextField charGuess = new TextField(); // Where the user will input one character
		Label charLabel = new Label("Enter a letter here: ");
		Label wordLabel = new Label("Guess full word here: ");
		Label answerBank = new Label("a b c d e f g h i j k l m\nn o p q r s t u v w x y z");
		Label hiddenWordLabel = new Label();
		Label error = new Label();
		Label scoreLabel = new Label("Score: ");
		Polygon structure = new Polygon(49, 17, 
				155, 17,
				155, 60,
				140, 60,
				140, 30, 
				65, 30, 
				65, 212, 					// Structure where the man will be hung on
				140, 212, 
				140, 220, 
				10, 220, 
				10, 212, 
				49, 212, 
				49, 17);
	
		// Add all to the pane
		charLabel.setLayoutX(10);
		charLabel.setLayoutY(290);
		wordLabel.setLayoutX(10);
		wordLabel.setLayoutY(330);
		charGuess.setLayoutX(200);
		charGuess.setLayoutY(290);
		wordGuess.setLayoutX(200);
		wordGuess.setLayoutY(330);
		answerBank.setLayoutX(300);
		answerBank.setLayoutY(100);
		backButton.setLayoutX(300);
		backButton.setLayoutY(380);
		submitButton.setLayoutX(100);
		submitButton.setLayoutY(380);
		retryButton.setLayoutX(200);
		retryButton.setLayoutY(380);
		error.setLayoutX(150);
		error.setLayoutY(200);
		scoreLabel.setLayoutX(10);
		scoreLabel.setLayoutY(390);
		game.getChildren().addAll(structure, hangManGroup, charLabel, wordLabel, charGuess, wordGuess, answerBank, backButton, submitButton, retryButton, error, scoreLabel);
		
		// Get the word for the player to try and guess
		String fileName = "C:\\Users\\emart\\eclipse-workspace\\CSCI3033GroupProject\\src\\application\\WordBank.txt";
		Random random = new Random();

		try {
			File file = new File(fileName); // Open File
			Scanner scanner = new Scanner(file); // Scanner for the file
			while (scanner.hasNextLine()) {
				words.add(scanner.next());
			}
			// Close the Scanner
			scanner.close();
			
			int randIndex = random.nextInt(words.size()); /// Get a random index within the amount of words
			answer = words.get(randIndex); // Use that random index and grab the word that is there
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
		}
		
		// Place that word on the plane and underscores for each letter
		answerLength = answer.length(); // Amount of chars in the answer
		for (int i = 0; i < answerLength; i++) {
			hiddenWord.append("_"); // Add an underscore per character in the answer
		}
		
		// Add the underscores to the pane
		hiddenWordLabel.setText(hiddenWord.toString());
		Font font = Font.font("Georgia", FontWeight.BOLD, 24);
		hiddenWordLabel.setFont(font);
		hiddenWordLabel.setLayoutX(100);
		hiddenWordLabel.setLayoutY(250);
		game.getChildren().add(hiddenWordLabel);
		System.out.println("Answer: " + answer); // DELETE LATER DEBUGGING
		
		// Submit Button
		submitButton.setOnAction(e -> {
			playHangMan(retryButton, wordGuess, charGuess, scoreLabel, error, answerBank, hiddenWordLabel);
			charGuess.clear();
			wordGuess.clear();
		});
	    
	    // Retry Button 
	    retryButton.setOnAction(e -> {
	    	hangManGroup.getChildren().clear(); // Delete previous man
	    	wrongCount = 0; // Reset the count of wrong answer
	    	answerBank.setText("a b c d e f g h i j k l m\nn o p q r s t u v w x y z"); // Reset Answer Bank
	    	
	    	 // Clear textboxes
	    	charGuess.clear();
			wordGuess.clear();
	    	
	    	// Get New Word
	    	int randIndex = random.nextInt(words.size()); // Get a random index within the amount of words
			answer = words.get(randIndex); // Use that random index and grab the word that is there
			System.out.println("Answer: " + answer); // For presenting. In case we use this game.
			// Get new word length for underscores
			answerLength = answer.length(); // Reobtain the length of the answer
			hiddenWord = new StringBuilder(); // Reset the underscores for the hidden word
			// Place that word on the plane and underscores for each letter
			answerLength = answer.length(); // Amount of chars in the answer
			for (int i = 0; i < answerLength; i++) {
				hiddenWord.append("_"); // Add an underscore per character in the answer
			}
			hiddenWordLabel.setText(hiddenWord.toString());
			// Reset the error message
			error.setText("");
	    });
	    
	    // Back to Menu Button
	    backButton.setOnAction(e -> {
	    	hangManGroup.getChildren().clear(); // Delete previous man
	    	stage.setScene(menuScene);
	    	// Clear TextBoxes (In case they imediately come back
	    	charGuess.clear();
			wordGuess.clear();
			// 
	    });
		
		return new Scene (game, 450, 450);
	}
	
	// Hang Man Game
	private void playHangMan(Button retryButton, TextField wordGuess, TextField charGuess, Label scoreLabel, Label error, Label answerBank, Label hiddenWordLabel) {
		if (!charGuess.getText().trim().isEmpty() && !wordGuess.getText().isEmpty()) { // If a char was given and a word was entered, print error
			error.setText("Please only enter an answer in ONE of the text boxes.");
		}
		else if (!charGuess.getText().trim().isEmpty()) {
			String input = charGuess.getText().trim();
			
			if (input.length() == 1 && Character.isLetter(input.charAt(0))) { // Char entered, match against answer
				// remoe that letter from the answer bank
				char guess = input.charAt(0);
				String answerBankText = answerBank.getText();
				String newAnswerBankText = answerBankText.replace(guess, ' ');
				answerBank.setText(newAnswerBankText);
				
				if (answerBankText.indexOf(guess) == -1) { // If char already guessed, return
					return;
				}
				
				if (answer.indexOf(input) != -1) { // If the letter is in the answer, continue
					for (int i = 0; i < answerLength; i++) { // Traverse the answer
						if (guess == answer.charAt(i)) { // If the chars match, replace
							hiddenWord.setCharAt(i, guess); // Replace the '_' with the letter
						}
					}
					if (hiddenWord.indexOf("_") == -1) { // If that letter is the last letter in the answer, continue
						hangManScore += 20;
						error.setText("Conratulations!! + 20 pts");
						scoreLabel.setText("Score: " + hangManScore);
						try { // Set a timeout for the system before restarting
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						retryButton.fire(); // Go to next word
						
					}
					else { // Otherwise, add points and continue game.
						hangManScore += 10; // Add to the score for a correct letter
						error.setText("That letter is in the answer! + 10 pts");
						scoreLabel.setText("Score: " + hangManScore);
					}
					hiddenWordLabel.setText(hiddenWord.toString());
				}
				else { // The guess was wrong
					wrongCount += 1; // Increase wrong count
					drawMan(error, retryButton); // Draw man
				}
			}
			else { // Invalid character entered
				error.setText("Only a letter is allowed here.");
				
			}
		} 
		else if (!wordGuess.getText().isEmpty()) {
			String word = wordGuess.getText().trim();
			if (word.length() >= 1 && Character.isLetter(word.charAt(0))) { /// Full word entered, check chars, and match against the answer
			    for (char c : word.toCharArray()) {
			        if (!Character.isLetter(c)) {
			        	error.setText("Please enter a valid word (only letters allowed).");
			            break;
			        }
			    }
			    error.setText(""); // Reset error message. (Incase we entered it incorrectly beforehand
			    
			    // Check word against the answer
			    if (word.equals(answer)) {
			    	error.setText("Congratulations!!!! + 100 pts");
			    	hangManScore += 100;
			    	scoreLabel.setText("Score: " + hangManScore);
			    	try { // Set a timeout for the system before restarting
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    	retryButton.fire();  // Go to next word
			    }
			    else {
			    	wrongCount += 1;
					drawMan(error, retryButton);
			    }
			} 
			else { // A character or digit was entered
				error.setText("Please enter a word of more than one character. Digits not allowed");
			}
		}
		else {
			error.setText("Please enter a guess.");
		}
	}
	
	// Draw the hangman. 
	public void drawMan(Label error, Button retryButton) {
		
		switch (wrongCount) { // This is the player's POV
			case 1: // If first incorrect answer, draw head
				Circle temp = new Circle(150, 75, 10);
				temp.setFill(Color.WHITE);
				temp.setStroke(Color.BLACK);
				temp.setStrokeWidth(2);
				hangManGroup.getChildren().add(temp);
				break;
			case 2: // If second incorrect answer, draw body 
				hangManGroup.getChildren().add(new Line(150, 85, 150, 185));
				break;
			case 3: // Left arm
				hangManGroup.getChildren().add(new Line(150, 120, 125, 90));
				break;
			case 4: // Right arm
				hangManGroup.getChildren().add(new Line(150, 120, 175, 90));
				break;
			case 5: // Left leg
				hangManGroup.getChildren().add(new Line(150, 185, 125, 200));
				break;
			case 6: // Right Leg, print lost message, reset count for next game
				hangManGroup.getChildren().add(new Line(150, 185, 175, 200));
				error.setText("Too Bad! You Lose!");
				wrongCount = 0;
				retryButton.fire();
				break;
			default:
				error.setText("Error in Draw Function");
				break;
		}
	}
	
	
	// Dodger
	private Scene createDodgerGameScene(Stage stage) {
		// Declare Variables
		Pane game = new Pane();
		Line floor = new Line(0, 338, 400, 338);
		
		Button backButton = new Button("Back to Menu");
		backButton.setLayoutX(300);
		backButton.setLayoutY(350);
		
		Button startButton = new Button("Start");
		startButton.setLayoutX(250);
		startButton.setLayoutY(350);
		
		Label roundLabel = new Label("Round: ");
		roundLabel.setLayoutX(15);
		roundLabel.setLayoutY(350);
		Label roundNum = new Label();
		roundNum.setLayoutX(60);
		roundNum.setLayoutY(350);
		
		Label scoreLabel = new Label("Score: ");
		scoreLabel.setLayoutX(15);
		scoreLabel.setLayoutY(370);
		Label scoreDisplay = new Label();
		scoreDisplay.setLayoutX(50);
		scoreDisplay.setLayoutY(370);
		
		
		Label timeLabel = new Label("Time: ");
		timeLabel.setLayoutX(15);
		timeLabel.setLayoutY(390);
		
		Timeline timer = new Timeline();
		timer.setCycleCount(Timeline.INDEFINITE);
		
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
			timeElapsed += 1;
			timeLabel.setText("Time: " + timeElapsed);
			roundNum.setText(String.valueOf(roundCount));
			scoreDisplay.setText(String.valueOf(dodgerScore));
		});
		
		timer.getKeyFrames().add(keyFrame);		
		
		// Draw the intitial state of the Stick Man
		drawStickMan();
		
		// Add to Pane
		floor.setStrokeWidth(5);
		game.getChildren().addAll(floor, stickManGroup, startButton, backButton, roundLabel, scoreLabel, timeLabel, roundNum, scoreDisplay);
		
		
		// Start button
		startButton.setOnAction(e -> {
			playDodger(game, stage);
			timer.play();
		});
		
		// Back to the Menu
		backButton.setOnAction(e ->{
			stage.setScene(menuScene);
			shapeCount = 0;
			roundCount = 1;
			timeElapsed = 0;
			score = 0;
		});
		
		// Set the Key Listeners
		Scene gameScene = new Scene(game, 400, 450);
		
		// Event Handler for the left and right movement
		gameScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case LEFT: // Move Left
						double moveLeft = stickManGroup.getLayoutX() - move;
						if (moveLeft > -180) { // If the movement will be within the window, continue
							stickManGroup.setLayoutX(moveLeft);
						} // Otherwise, do nothing
						break;
					case RIGHT: // Move Right
						double moveRight = stickManGroup.getLayoutX() + move;
						if (moveRight < 210) { // If the movement will be within the window, continue
							stickManGroup.setLayoutX(moveRight);
						} // Otherwise, do nothing
						break;
					default:
						System.out.println("Error in movement handler.");
						
				}
			}
		});
		
		// Return the game Scene
		return gameScene;
	}
	
	
	public void playDodger(Pane game, Stage stage) {
		// Increase amount of shpaes per round
		shapeCount += 1;
		
		// Iterate through shapes and animate them one by one
		for (int i = 0; i < shapeCount; i++) {
			Shape shape = randomShape();
		    shape.setFill(Color.WHITE);
		    shape.setStroke(Color.BLACK);
		    shape.setStrokeWidth(2);
		    
		    // Give impression that it is coming from the sky instead of spawning and dropping
		    shape.setLayoutY(-50);
		    game.getChildren().add(shape);
		    
		    // Animate shape down
		    animateShape(game, stage, shape);  
		}
		
	}
	
	// Takes the count and randomly
	public Shape randomShape() {
		Random rand = new Random();
		Shape shape;
		int randChoice = rand.nextInt(5) + 1;
		int randX, randX2, randY;
		switch (randChoice) {
			case 1: // Circle
				randX = rand.nextInt(386) + 15;
				shape = new Circle(randX, 15, 30);
			case 2: // Triangle
				randX = rand.nextInt(386) + 15;
				randX2 = randX + 60;
				randY = rand.nextInt(31) + 30;
				shape = new Polygon(randX, 5, randX2, 5, randX + 30, randY);
				break;
			case 3: // Square
				randX = rand.nextInt(386) + 15;
				randX2 = randX + 30;
				shape = new Polygon(randX, 1, randX2, 1, randX2, 30, randX, 30);
				break;
			case 4: // Diamond
				randX = rand.nextInt(386) + 15;
				randX2 = randX + 30;
				randY = rand.nextInt(21) + 10;
				shape = new Polygon(randX, 1, randX2, randY, randX, randY * 3, randX2 - 60, randY);
				break;
			case 5: // Rectangle
				randX = rand.nextInt(386) + 15;
				randX2 = randX + 60;
				randY = rand.nextInt(26) + 10;
				shape = new Polygon(randX, 1, randX2, 1, randX2, randY, randX, randY);
				break;
			default:
				shape = new Circle();
				System.out.println("Error in Random Shape Function. randChoice = " + randChoice);
				break;
		}
		
		return shape;
	}
	
	
	// Draw the Stick Man for Dodger
	private void drawStickMan() {
		// Our Perspective
		Circle head = new Circle(183, 280, 10);
		Line body = new Line(183, 290, 183, 320);
		Line leftArm = new Line(183, 300, 170, 295);
		Line rightArm = new Line(183, 300, 196, 295);
		Line leftLeg = new Line(183, 320, 170, 338);
		Line rightLeg = new Line(183, 320, 196, 338);
		
		// Add to group and format the man to look thicker
		head.setFill(Color.WHITE);
		head.setStroke(Color.BLACK);
		head.setStrokeWidth(2);
		body.setStrokeWidth(3);
		leftArm.setStrokeWidth(3);
		rightArm.setStrokeWidth(3);
		leftLeg.setStrokeWidth(3);
		rightLeg.setStrokeWidth(3);
		stickManGroup.getChildren().addAll(head, body, leftArm, rightArm, leftLeg, rightLeg);		
	}
	
	
	// Animates the Shape down to the floor 
	private void animateShape(Pane game, Stage stage, Shape shape) {
		// Declare Variables
		double speed = 5; // Speed of the shapes moving down
		KeyFrame keyFrame = new KeyFrame(Duration.millis(50), event -> {
			// Update Y
			shape.setLayoutY(shape.getLayoutY() + speed);
			if (intersects(shape)) { // If the player has been touched, game over
				dodgerGameOver(game, stage);
				shape.setLayoutY(338);
				game.getChildren().remove(shape);
			}
			else if (shape.getLayoutY() >= 338) {
				shape.setLayoutY(338);
				game.getChildren().remove(shape);
				dodgerScore++; // Increase score per shape dodged
				
				if (dodgerRoundIsOver(game)) {
					roundCount++;
					playDodger(game, stage);
				}
			}
		});
		
		Timeline timeline = new Timeline(keyFrame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	// Returns true if the shape and player intersect each other
	private boolean intersects(Shape shape) {
		Bounds stickManBounds = stickManGroup.getBoundsInParent();
		
		return shape.getBoundsInParent().intersects(stickManBounds);
	}
	
	// Returns true if the round is over
	private boolean dodgerRoundIsOver(Pane game) {
		long passedShapeCount = game.getChildren().stream()
				.filter(node -> node instanceof Shape) // Shapes only
				.filter(shape -> shape.getLayoutY() >= 338) // Check if shape is at or below floor
				.count();
		return passedShapeCount >= shapeCount;
	}
	
	
	// Handles Game Over
	private void dodgerGameOver(Pane game, Stage stage) {
		Label gameOverLabel = new Label("Game Over!");
		gameOverLabel.setLayoutX(150);
		gameOverLabel.setLayoutY(200);
		gameOverLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: red;");
		
		// After getting a game over, make a retry button
	    Button retryButton = new Button("Retry");
	    retryButton.setLayoutX(170);
	    retryButton.setLayoutY(250);
	    retryButton.setOnAction(e -> {
	        // Reset the game state for retry
	        restartDodger(game, stage);
	    });
	    
	    // Add Game Over message and retry button to the game pane
	    game.getChildren().addAll(gameOverLabel, retryButton);
	}

	
	private void restartDodger(Pane game, Stage stage) {
	    // Clear the game pane (remove game over label, retry button, etc.)
	    game.getChildren().clear();
	    
	    // Reset all necessary game variables (score, round, shape count, etc.)
	    shapeCount = 0;
	    roundCount = 1;
	    timeElapsed = 0;
	    
	    // Reinitialize the game components
	    drawStickMan(); // Redraw stickman
	    
	    // Create a new floor and add it back to the game pane
	    Line floor = new Line(0, 338, 400, 338);
	    floor.setStrokeWidth(5);
	    game.getChildren().add(floor);
	    
	    // Add other game components again (e.g., buttons, labels)
	    Label roundLabel = new Label("Round: ");
	    roundLabel.setLayoutX(15);
	    roundLabel.setLayoutY(350);
	    Label scoreLabel = new Label("Score: ");
	    scoreLabel.setLayoutX(15);
	    scoreLabel.setLayoutY(370);
	    Label timeLabel = new Label("Time: ");
	    timeLabel.setLayoutX(15);
	    timeLabel.setLayoutY(390);
	    
	    Button backButton = new Button("Back to Menu");
	    backButton.setLayoutX(300);
	    backButton.setLayoutY(350);
	    backButton.setOnAction(e -> {
	        stage.setScene(menuScene); // Go back to the menu
	    });
	    
	    Button startButton = new Button("Start");
	    startButton.setLayoutX(250);
	    startButton.setLayoutY(350);
	    startButton.setOnAction(e -> {
	        playDodger(game, stage); // Start the game
	    });
	    
	    game.getChildren().addAll(roundLabel, scoreLabel, timeLabel, backButton, startButton, stickManGroup);
	    
	    // Start the game again
	    playDodger(game, stage);
	}
	
	/****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************/
	public void Tetrisstart(Stage stage) throws Exception {
		
		
		for (int[] a : GRID) {
			Arrays.fill(a, 0) ;
		}

		Line line = new Line(XMAX, 0, XMAX, YMAX) ;
		Text scoretext = new Text("Score: ") ;
		scoretext.setStyle("-fx-font: 20 arial ;") ;
		scoretext.setY(50) ;
		scoretext.setX(XMAX + 5) ;
		Text level = new Text("Lines: ") ;
		level.setStyle("-fx-font: 20 arial ;") ;
		level.setY(100) ;
		level.setX(XMAX + 5) ;
		level.setFill(Color.GREEN) ;
		group.getChildren().addAll(scoretext, line, level) ;

		Piece a = nextObj ;
		group.getChildren().addAll(a.a, a.b, a.c, a.d) ;
		moveOnKeyPress(a) ;
		object = a ;
		nextObj = Controller.makeRect() ;
		stage.setScene(scene) ;
		stage.setTitle("Tetris") ;
		stage.show() ;

		Timer fall = new Timer() ;
		TimerTask task = new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0
								|| object.d.getY() == 0)
							top++ ;
						else
							top = 0 ;

						if (top == 2) {
							// GAME OVER
							Text over = new Text("IT'S JOEVER") ;
							over.setFill(Color.RED) ;
							over.setStyle("-fx-font: 70 arial ;") ;
							over.setY(250) ;
							over.setX(10) ;
							group.getChildren().add(over) ;
							game = false ;
						}
						// Exit
						if (top == 15) {
							System.exit(0) ;
						}

						if (game) {
							MoveDown(object) ;
							scoretext.setText("Score: " + Integer.toString(score)) ;
							level.setText("Lines: " + Integer.toString(linesNo)) ;
						}
					}
				}) ;
			}
		} ;
		fall.schedule(task, 0, 300) ; 
	}

	private void moveOnKeyPress(Piece Piece) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case RIGHT:
					Controller.MoveR(Piece) ;
					break ;
				case DOWN:
					MoveDown(Piece) ;
					score++ ;
					break ;
				case LEFT:
					Controller.MoveL(Piece) ;
					break ;
				case UP:
					MoveTurn(Piece) ;
					break ;
				}
			}
		}) ;
	}


	private void MoveTurn(Piece Piece) {
		int f = Piece.piece ;
		Rectangle a = Piece.a ;
		Rectangle b = Piece.b ;
		Rectangle c = Piece.c ;
		Rectangle d = Piece.d ;
		switch (Piece.getName()) {
		case "j":
			if (f == 1 && cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
				MoveR(Piece.a) ;
				MoveDown(Piece.a) ;
				MoveDown(Piece.c) ;
				MoveL(Piece.c) ;
				MoveDown(Piece.d) ;
				MoveDown(Piece.d) ;
				MoveL(Piece.d) ;
				MoveL(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 2 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, -2, 2)) {
				MoveDown(Piece.a) ;
				MoveL(Piece.a) ;
				MoveL(Piece.c) ;
				MoveUp(Piece.c) ;
				MoveL(Piece.d) ;
				MoveL(Piece.d) ;
				MoveUp(Piece.d) ;
				MoveUp(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 3 && cB(a, -1, 1) && cB(c, 1, 1) && cB(d, 2, 2)) {
				MoveL(Piece.a) ;
				MoveUp(Piece.a) ;
				MoveUp(Piece.c) ;
				MoveR(Piece.c) ;
				MoveUp(Piece.d) ;
				MoveUp(Piece.d) ;
				MoveR(Piece.d) ;
				MoveR(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 2, -2)) {
				MoveUp(Piece.a) ;
				MoveR(Piece.a) ;
				MoveR(Piece.c) ;
				MoveDown(Piece.c) ;
				MoveR(Piece.d) ;
				MoveR(Piece.d) ;
				MoveDown(Piece.d) ;
				MoveDown(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			break ;
		case "l":
			if (f == 1 && cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 2)) {
				MoveR(Piece.a) ;
				MoveDown(Piece.a) ;
				MoveUp(Piece.c) ;
				MoveR(Piece.c) ;
				MoveUp(Piece.b) ;
				MoveUp(Piece.b) ;
				MoveR(Piece.b) ;
				MoveR(Piece.b) ;
				Piece.transform() ;
				break ;
			}
			if (f == 2 && cB(a, -1, -1) && cB(b, 2, -2) && cB(c, 1, -1)) {
				MoveDown(Piece.a) ;
				MoveL(Piece.a) ;
				MoveR(Piece.b) ;
				MoveR(Piece.b) ;
				MoveDown(Piece.b) ;
				MoveDown(Piece.b) ;
				MoveR(Piece.c) ;
				MoveDown(Piece.c) ;
				Piece.transform() ;
				break ;
			}
			if (f == 3 && cB(a, -1, 1) && cB(c, -1, -1) && cB(b, -2, -2)) {
				MoveL(Piece.a) ;
				MoveUp(Piece.a) ;
				MoveDown(Piece.c) ;
				MoveL(Piece.c) ;
				MoveDown(Piece.b) ;
				MoveDown(Piece.b) ;
				MoveL(Piece.b) ;
				MoveL(Piece.b) ;
				Piece.transform() ;
				break ;
			}
			if (f == 4 && cB(a, 1, 1) && cB(b, -2, 2) && cB(c, -1, 1)) {
				MoveUp(Piece.a) ;
				MoveR(Piece.a) ;
				MoveL(Piece.b) ;
				MoveL(Piece.b) ;
				MoveUp(Piece.b) ;
				MoveUp(Piece.b) ;
				MoveL(Piece.c) ;
				MoveUp(Piece.c) ;
				Piece.transform() ;
				break ;
			}
			break ;
		case "o":
			break ;
		case "s":
			if (f == 1 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
				MoveDown(Piece.a) ;
				MoveL(Piece.a) ;
				MoveL(Piece.c) ;
				MoveUp(Piece.c) ;
				MoveUp(Piece.d) ;
				MoveUp(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 2 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
				MoveUp(Piece.a) ;
				MoveR(Piece.a) ;
				MoveR(Piece.c) ;
				MoveDown(Piece.c) ;
				MoveDown(Piece.d) ;
				MoveDown(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 3 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
				MoveDown(Piece.a) ;
				MoveL(Piece.a) ;
				MoveL(Piece.c) ;
				MoveUp(Piece.c) ;
				MoveUp(Piece.d) ;
				MoveUp(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 4 && cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
				MoveUp(Piece.a) ;
				MoveR(Piece.a) ;
				MoveR(Piece.c) ;
				MoveDown(Piece.c) ;
				MoveDown(Piece.d) ;
				MoveDown(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			break ;
		case "t":
			if (f == 1 && cB(a, 1, 1) && cB(d, -1, -1) && cB(c, -1, 1)) {
				MoveUp(Piece.a) ;
				MoveR(Piece.a) ;
				MoveDown(Piece.d) ;
				MoveL(Piece.d) ;
				MoveL(Piece.c) ;
				MoveUp(Piece.c) ;
				Piece.transform() ;
				break ;
			}
			if (f == 2 && cB(a, 1, -1) && cB(d, -1, 1) && cB(c, 1, 1)) {
				MoveR(Piece.a) ;
				MoveDown(Piece.a) ;
				MoveL(Piece.d) ;
				MoveUp(Piece.d) ;
				MoveUp(Piece.c) ;
				MoveR(Piece.c) ;
				Piece.transform() ;
				break ;
			}
			if (f == 3 && cB(a, -1, -1) && cB(d, 1, 1) && cB(c, 1, -1)) {
				MoveDown(Piece.a) ;
				MoveL(Piece.a) ;
				MoveUp(Piece.d) ;
				MoveR(Piece.d) ;
				MoveR(Piece.c) ;
				MoveDown(Piece.c) ;
				Piece.transform() ;
				break ;
			}
			if (f == 4 && cB(a, -1, 1) && cB(d, 1, -1) && cB(c, -1, -1)) {
				MoveL(Piece.a) ;
				MoveUp(Piece.a) ;
				MoveR(Piece.d) ;
				MoveDown(Piece.d) ;
				MoveDown(Piece.c) ;
				MoveL(Piece.c) ;
				Piece.transform() ;
				break ;
			}
			break ;
		case "z":
			if (f == 1 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
				MoveUp(Piece.b) ;
				MoveR(Piece.b) ;
				MoveL(Piece.c) ;
				MoveUp(Piece.c) ;
				MoveL(Piece.d) ;
				MoveL(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 2 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
				MoveDown(Piece.b) ;
				MoveL(Piece.b) ;
				MoveR(Piece.c) ;
				MoveDown(Piece.c) ;
				MoveR(Piece.d) ;
				MoveR(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 3 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
				MoveUp(Piece.b) ;
				MoveR(Piece.b) ;
				MoveL(Piece.c) ;
				MoveUp(Piece.c) ;
				MoveL(Piece.d) ;
				MoveL(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 4 && cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
				MoveDown(Piece.b) ;
				MoveL(Piece.b) ;
				MoveR(Piece.c) ;
				MoveDown(Piece.c) ;
				MoveR(Piece.d) ;
				MoveR(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			break ;
		case "i":
			if (f == 1 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
				MoveUp(Piece.a) ;
				MoveUp(Piece.a) ;
				MoveR(Piece.a) ;
				MoveR(Piece.a) ;
				MoveUp(Piece.b) ;
				MoveR(Piece.b) ;
				MoveDown(Piece.d) ;
				MoveL(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 2 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
				MoveDown(Piece.a) ;
				MoveDown(Piece.a) ;
				MoveL(Piece.a) ;
				MoveL(Piece.a) ;
				MoveDown(Piece.b) ;
				MoveL(Piece.b) ;
				MoveUp(Piece.d) ;
				MoveR(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 3 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
				MoveUp(Piece.a) ;
				MoveUp(Piece.a) ;
				MoveR(Piece.a) ;
				MoveR(Piece.a) ;
				MoveUp(Piece.b) ;
				MoveR(Piece.b) ;
				MoveDown(Piece.d) ;
				MoveL(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			if (f == 4 && cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
				MoveDown(Piece.a) ;
				MoveDown(Piece.a) ;
				MoveL(Piece.a) ;
				MoveL(Piece.a) ;
				MoveDown(Piece.b) ;
				MoveL(Piece.b) ;
				MoveUp(Piece.d) ;
				MoveR(Piece.d) ;
				Piece.transform() ;
				break ;
			}
			break ;
		}
	}


	private void RemoveRows(Pane pane) {
		ArrayList<Node> rects = new ArrayList<Node>() ;
		ArrayList<Integer> lines = new ArrayList<Integer>() ;
		ArrayList<Node> newrects = new ArrayList<Node>() ;
		int full = 0 ;
		for (int i = 0 ; i < GRID[0].length ; i++) {
			for (int j = 0 ; j < GRID.length ; j++) {
				if (GRID[j][i] == 1)
					full++ ;
			}
			if (full == GRID.length)
			lines.add(i) ;
			//lines.add(i + lines.size()) ;
			full = 0 ;
		}
		if (lines.size() > 0)
			do {
				for (Node node : pane.getChildren()) {
					if (node instanceof Rectangle)
						rects.add(node) ;
				}
				score += 50 ;
				linesNo++ ;

				for (Node node : rects) {
					Rectangle a = (Rectangle) node ;
					if (a.getY() == lines.get(0) * SIZE) {
						GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0 ;
						pane.getChildren().remove(node) ;
					} else
						newrects.add(node) ;
				}

				for (Node node : newrects) {
					Rectangle a = (Rectangle) node ;
					if (a.getY() < lines.get(0) * SIZE) {
						GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0 ;
						a.setY(a.getY() + SIZE) ;
					}
				}
				lines.remove(0) ;
				rects.clear() ;
				newrects.clear() ;
				for (Node node : pane.getChildren()) {
					if (node instanceof Rectangle)
						rects.add(node) ;
				}
				for (Node node : rects) {
					Rectangle a = (Rectangle) node ;
					try {
						GRID[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1 ;
					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
				rects.clear() ;
			} while (lines.size() > 0) ;
	}
	

	private void MoveDown(Rectangle rect) {
		if (rect.getY() + MOVE < YMAX)
			rect.setY(rect.getY() + MOVE) ;

	}


	private void MoveR(Rectangle rect) {
		if (rect.getX() + MOVE <= XMAX - SIZE)
			rect.setX(rect.getX() + MOVE) ;
	}


	private void MoveL(Rectangle rect) {
		if (rect.getX() - MOVE >= 0)
			rect.setX(rect.getX() - MOVE) ;
	}


	private void MoveUp(Rectangle rect) {
		if (rect.getY() - MOVE > 0)
			rect.setY(rect.getY() - MOVE) ;
	}


	private void MoveDown(Piece Piece) {
		if (Piece.a.getY() == YMAX - SIZE || Piece.b.getY() == YMAX - SIZE || Piece.c.getY() == YMAX - SIZE
				|| Piece.d.getY() == YMAX - SIZE || moveA(Piece) || moveB(Piece) || moveC(Piece) || moveD(Piece)) {
			GRID[(int) Piece.a.getX() / SIZE][(int) Piece.a.getY() / SIZE] = 1 ;
			GRID[(int) Piece.b.getX() / SIZE][(int) Piece.b.getY() / SIZE] = 1 ;
			GRID[(int) Piece.c.getX() / SIZE][(int) Piece.c.getY() / SIZE] = 1 ;
			GRID[(int) Piece.d.getX() / SIZE][(int) Piece.d.getY() / SIZE] = 1 ;
			RemoveRows(group) ;

			Piece a = nextObj ;
			nextObj = Controller.makeRect() ;
			object = a ;
			group.getChildren().addAll(a.a, a.b, a.c, a.d) ;
			moveOnKeyPress(a) ;
		}

		if (Piece.a.getY() + MOVE < YMAX && Piece.b.getY() + MOVE < YMAX && Piece.c.getY() + MOVE < YMAX
				&& Piece.d.getY() + MOVE < YMAX) {
			int movea = GRID[(int) Piece.a.getX() / SIZE][((int) Piece.a.getY() / SIZE) + 1] ;
			int moveb = GRID[(int) Piece.b.getX() / SIZE][((int) Piece.b.getY() / SIZE) + 1] ;
			int movec = GRID[(int) Piece.c.getX() / SIZE][((int) Piece.c.getY() / SIZE) + 1] ;
			int moved = GRID[(int) Piece.d.getX() / SIZE][((int) Piece.d.getY() / SIZE) + 1] ;
			if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
				Piece.a.setY(Piece.a.getY() + MOVE) ;
				Piece.b.setY(Piece.b.getY() + MOVE) ;
				Piece.c.setY(Piece.c.getY() + MOVE) ;
				Piece.d.setY(Piece.d.getY() + MOVE) ;
			}
		}
	}


	private boolean moveA(Piece Piece) {
		return (GRID[(int) Piece.a.getX() / SIZE][((int) Piece.a.getY() / SIZE) + 1] == 1) ;
	}

	private boolean moveB(Piece Piece) {
		return (GRID[(int) Piece.b.getX() / SIZE][((int) Piece.b.getY() / SIZE) + 1] == 1) ;
	}

	private boolean moveC(Piece Piece) {
		return (GRID[(int) Piece.c.getX() / SIZE][((int) Piece.c.getY() / SIZE) + 1] == 1) ;
	}

	private boolean moveD(Piece Piece) {
		return (GRID[(int) Piece.d.getX() / SIZE][((int) Piece.d.getY() / SIZE) + 1] == 1) ;
	}


	private boolean cB(Rectangle rect, int x, int y) {
		boolean xb = false ;
		boolean yb = false ;
		if (x >= 0)
			xb = rect.getX() + x * MOVE <= XMAX - SIZE ;
		if (x < 0)
			xb = rect.getX() + x * MOVE >= 0 ;
		if (y >= 0)
			yb = rect.getY() - y * MOVE > 0 ;
		if (y < 0)
			yb = rect.getY() + y * MOVE < YMAX ;
		return xb && yb && GRID[((int) rect.getX() / SIZE) + x][((int) rect.getY() / SIZE) - y] == 0 ;
	}

	public void Simonstart(Stage primaryStage) {
		VBox root = new VBox(10);
		root.setStyle("-fx-alignment: center; -fx-padding: 20;");

		// Display area
		Rectangle display = new Rectangle(200, 200);
		display.setFill(Color.LIGHTGRAY);

		// Buttons for user input
		GridPane buttonPane = new GridPane();
		buttonPane.setHgap(10);
		buttonPane.setVgap(10);

		Button[] buttons = new Button[4];
		for (int i = 0; i < colors.length; i++) {
			Color color = colors[i];
			Button button = new Button();
			button.setStyle(
					"-fx-background-color: " + toHexString(color) + "; -fx-min-width: 100px; -fx-min-height: 100px;");
			button.setOnAction(e -> handlePlayerInput(color, display));
			buttons[i] = button;
			buttonPane.add(button, i % 2, i / 2);
		}

		root.getChildren().addAll(display, buttonPane);

		// Setup stage
		Scene scene = new Scene(root, 300, 400);
		primaryStage.setTitle("Simon Says");
		primaryStage.setScene(scene);
		primaryStage.show();

		// Generate and display the sequence
		generateSequence();
		displaySequence(display);
	}

	private void generateSequence() {
		Random random = new Random();
		for (int i = 0; i < SEQUENCE_LENGTH; i++) {
			colorSequence.add(colors[random.nextInt(colors.length)]);
		}
	}

	private void displaySequence(Rectangle display) {
		new Thread(() -> {
			try {
				for (Color color : colorSequence) {
					Thread.sleep(1000);
					javafx.application.Platform.runLater(() -> display.setFill(color));
					Thread.sleep(1000);
					javafx.application.Platform.runLater(() -> display.setFill(Color.LIGHTGRAY));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}
	

	private void handlePlayerInput(Color color, Rectangle display) {
		playerSequence.add(color);
		String musicFile = "buzzer-or-wrong-answer-20582.mp3";     // For example


		if (playerSequence.size() <= colorSequence.size()) {
			if (color.equals(colorSequence.get(currentStep))) {
				currentStep++;
				if (currentStep == colorSequence.size()) {
					display.setFill(Color.GREEN);
					System.out.println("You win!");
					Text over = new Text("IT'S JOEVER") ;
					
				}
			} else {
				display.setFill(Color.RED);
			}
		}
	}

	private String toHexString(Color color) {
		int r = (int) (color.getRed() * 255);
		int g = (int) (color.getGreen() * 255);
		int b = (int) (color.getBlue() * 255);
		return String.format("#%02X%02X%02X", r, g, b);
	}

	
	/****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************
	 ****************************************************************************************************/
	
    /*

		Main Function

    */
    public static void main(String[] args) {
        launch(args);
    }

}
