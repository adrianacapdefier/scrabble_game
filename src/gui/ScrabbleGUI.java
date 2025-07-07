
package gui;

import board.Board;
import board.Square;
import dictionary.Alphabet;
import dictionary.Trie;
import player.Player;

import javax.swing.*;
		import java.awt.*;
		import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing the graphical user interface for the Scrabble game.
 * This class initializes and manages the game board, score panel, and user interactions.
 */
public class ScrabbleGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int SQUARE_SIZE = 50;

	private HashSet<String> formedWords = new HashSet<>();


	private String player1Name;
	private String player2Name;

	private boolean isFirstTurn = true;

	private char selectedLetter;

	private int turn;
	private HashSet<String> currentTurnWords = new HashSet<>();

	private ArrayList<Point> newLetterPositions = new ArrayList<>();


	Board board;
	Player player1;
	Player player2;

	JPanel scorePanel = new JPanel();
	JLabel score1Label = new JLabel();
	JLabel score2Label = new JLabel();
	JLabel turnLabel = new JLabel();

	JPanel gridPanel = new JPanel();
	JLabel[][] grid = new JLabel[Board.BOARD_SIZE][Board.BOARD_SIZE];
	JPanel[][] squares = new JPanel[Board.BOARD_SIZE][Board.BOARD_SIZE];

	JButton openKeyboardButton = new JButton("Open Keyboard");

	/**
	 * Constructs a ScrabbleGUI with the specified board and players.
	 *
	 * @param board the game board
	 * @param player1 the first player
	 * @param player2 the second player
	 * @param player1Name the name of the first player
	 * @param player2Name the name of the second player
	 */
	public ScrabbleGUI(Board board, Player player1, Player player2, String player1Name, String player2Name) {
		this.board = board;
		this.player1 = player1;
		this.player2 = player2;
		this.player1Name = player1Name;
		this.player2Name = player2Name;
		this.turn = -1;
		initGUI();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	private void initGUI() {
		initGrid();
		initScorePanel();
		add(gridPanel, BorderLayout.CENTER);
		add(scorePanel, BorderLayout.NORTH);

		openKeyboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openKeyboard();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(openKeyboardButton);
		add(buttonPanel, BorderLayout.SOUTH);
		JButton submitWordButton = new JButton("Submit Word");
		submitWordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitWord();
			}
		});
		buttonPanel.add(submitWordButton);
	}


	private void openKeyboard() {
		JFrame keyboardFrame = new JFrame("Romanian Alphabet Keyboard");
		keyboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		keyboardFrame.setSize(400, 300);
		keyboardFrame.setLayout(new GridLayout(3, 10));

		char[] romanianAlphabet = {'A', 'Ă', 'Â', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'Î', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'Ș', 'T', 'Ț', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		for (char letter : romanianAlphabet) {
			JButton button = new JButton(String.valueOf(letter));
			button.setFont(new Font("Arial", Font.PLAIN, 18));
			button.setPreferredSize(new Dimension(50, 50));
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedLetter = letter;
				}
			});
			keyboardFrame.add(button);
		}

		keyboardFrame.pack();
		keyboardFrame.setVisible(true);
	}


	private void initScorePanel() {
		scorePanel.setLayout(new BorderLayout());
		score1Label.setFont(new Font("Arial", Font.BOLD, 18));
		score2Label.setFont(new Font("Arial", Font.BOLD, 18));
		turnLabel.setFont(new Font("Arial", Font.BOLD, 18));

		score1Label.setText(player1Name); // Initialize score display
		score2Label.setText(player2Name); // Initialize score display
		turnLabel.setText("Turn: " + player1Name);

		JPanel playerScores = new JPanel(new GridLayout(1, 2));
		playerScores.add(score1Label);
		playerScores.add(score2Label);

		scorePanel.add(playerScores, BorderLayout.NORTH);
		scorePanel.add(turnLabel, BorderLayout.SOUTH);
	}


	private void initGrid() {
		gridPanel.setLayout(new GridLayout(Board.BOARD_SIZE, Board.BOARD_SIZE));
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int column = 0; column < Board.BOARD_SIZE; column++) {
				Square square = board.getSquare(row, column);
				JPanel panel = new JPanel();
				JLabel label = new JLabel();
				label.setFont(new Font("Arial", Font.PLAIN, 18));
				label.setOpaque(true);
				label.setBackground(getSquareColor(square));
				panel.setBackground(getSquareColor(square));
				panel.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
				panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				panel.add(label);
				panel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						placeLetter(square, label);
					}
				});
				squares[row][column] = panel;
				grid[row][column] = label;
				gridPanel.add(panel);
			}
		}
	}


	/**
	 * Places a letter on the board.
	 *
	 * @param square the square where the letter is placed
	 * @param label the label to update with the letter
	 */
	private void placeLetter(Square square, JLabel label) {
		if (selectedLetter != '\0' && !square.containsLetter()) {
			square.setLetter(selectedLetter);
			label.setText(String.valueOf(selectedLetter));
			label.setForeground(Color.BLACK);
			newLetterPositions.add(new Point(square.getRow(), square.getCol()));
			selectedLetter = '\0'; // Resetează litera selectată după plasare
		}
	}


	/**
	 * Checks if a position is part of a new word.
	 *
	 * @param row the row of the position
	 * @param col the column of the position
	 * @return true if the position is part of a new word, false otherwise
	 */


	private boolean isPartOfNewWord(int row, int col) {
		for (Point p : newLetterPositions) {
			if (p.x == row && p.y == col) {
				return true;
			}
		}
		return false;
	}



	private void switchTurn() {
		turn = -turn; // Schimbăm turul
		if (turn < 0) {
			turnLabel.setText("Turn: " + player1Name);
		} else {
			turnLabel.setText("Turn: " + player2Name);
		}
	}


	private void checkAndScoreWords() {
		currentTurnWords.clear(); // Reset the list of words formed in the current turn
		HashSet<String> horizontalWords = new HashSet<>();
		HashSet<String> verticalWords = new HashSet<>();

		// Check for horizontal words
		for (Point p : newLetterPositions) {
			int row = p.x;
			int col = p.y;

			StringBuilder word = new StringBuilder();
			// Traverse left
			int tempCol = col;
			while (tempCol >= 0 && board.getSquare(row, tempCol).containsLetter()) {
				tempCol--;
			}
			tempCol++; // Adjust to the first letter of the word
			// Traverse right
			while (tempCol < Board.BOARD_SIZE && board.getSquare(row, tempCol).containsLetter()) {
				word.append(board.getSquare(row, tempCol).getLetter());
				tempCol++;
			}
			if (word.length() > 1) {
				horizontalWords.add(word.toString());
			}

			word.setLength(0); // Reset the word for vertical checking

			// Traverse up
			int tempRow = row;
			while (tempRow >= 0 && board.getSquare(tempRow, col).containsLetter()) {
				tempRow--;
			}
			tempRow++; // Adjust to the first letter of the word
			// Traverse down
			while (tempRow < Board.BOARD_SIZE && board.getSquare(tempRow, col).containsLetter()) {
				word.append(board.getSquare(tempRow, col).getLetter());
				tempRow++;
			}
			if (word.length() > 1) {
				verticalWords.add(word.toString());
			}
		}

		// Combine horizontal and vertical words
		currentTurnWords.addAll(horizontalWords);
		currentTurnWords.addAll(verticalWords);

		// Print formed words for debugging
		for (String word : currentTurnWords) {
			System.out.println("Formed word: " + word);
		}
	}



	/**
	 * Submits the word formed in the current turn, calculates the score, and updates the game state.
	 */

	private void submitWord() {
		checkAndScoreWords();
		int totalPoints = 0;
		Player currentPlayer = (turn < 0) ? player1 : player2;

		System.out.println("Current Player: " + currentPlayer.getName());

		for (String word : currentTurnWords) {
			if (isFirstTurn && !isWordStartingFromCenter(word)) {
				System.out.println("First word must start from the center.");
				return; // Skip the word placed incorrectly
			}

			if (!isValidWordPlacement(word)) {
				System.out.println("Invalid word placement: " + word);
				continue;
			}

			boolean isVertical = isWordVertical(word);
			Square startSquare = getStartSquare(word, isVertical);

			if (startSquare == null) {
				System.out.println("No valid start square found for word: " + word);
				continue;
			}

			int wordPoints = getNewWord(startSquare, isVertical);
			totalPoints += wordPoints;
			System.out.println("Points for word '" + word + "': " + wordPoints);
		}

		// Check if all seven letters were used
		if (newLetterPositions.size() == 7) {
			totalPoints += 50; // Add the 50 points bonus
			System.out.println("Bonus of 50 points for using all seven letters!");
		}

		currentPlayer.addPointsToScore(totalPoints);
		System.out.println(currentPlayer.getName() + " score: " + currentPlayer.getScore());
		updateScores(currentPlayer, currentPlayer == player1);

		formedWords.addAll(currentTurnWords); // Add the words formed in the current turn to the general list
		isFirstTurn = false; // Change the state of the first turn
		switchTurn(); // Switch the turn
		newLetterPositions.clear(); // Clear the list of new letter positions for the next turn
		currentTurnWords.clear(); // Clear the list of words formed
		resetFormedWords();
	}

	/**
	 * Checks if the word starts from the center of the board.
	 *
	 * @param word the word to check
	 * @return true if the word starts from the center, false otherwise
	 */

	private boolean isWordStartingFromCenter(String word) {
		int centerRow = Board.BOARD_SIZE / 2;
		int centerCol = Board.BOARD_SIZE / 2;
		for (Point p : newLetterPositions) {
			if (p.x == centerRow && p.y == centerCol) {
				return true;
			}
		}
		return false;
	}



	private boolean isValidWordPlacement(String word) {
		boolean isVertical = isWordVertical(word);
		Square startSquare = getStartSquare(word, isVertical);
		if (startSquare == null) return false;

		int row = startSquare.getRow();
		int col = startSquare.getCol();

		for (int i = 0; i < word.length(); i++) {
			int currentRow = isVertical ? row + i : row;
			int currentCol = isVertical ? col : col + i;

			if (currentRow >= Board.BOARD_SIZE || currentCol >= Board.BOARD_SIZE) {
				return false; // Depășește limitele tablei
			}

			Square currentSquare = board.getSquare(currentRow, currentCol);
			if (currentSquare == null || (!currentSquare.containsLetter() && !isPartOfNewWord(currentRow, currentCol))) {
				return false; // Pătratul nu conține literă și nu este parte din noul cuvânt
			}
		}

		return isContinuous(word, row, col, !isVertical);
	}


	private boolean isContinuous(String word, int startRow, int startCol, boolean horizontal) {
		for (int i = 0; i < word.length(); i++) {
			int row = horizontal ? startRow : startRow + i;
			int col = horizontal ? startCol + i : startCol;
			if (!board.getSquare(row, col).containsLetter() && !isPartOfNewWord(row, col)) {
				return false;
			}
		}
		return true;
	}



	private void resetFormedWords() {
		formedWords.clear();
	}


	/**
	 * Gets the starting square of the word.
	 *
	 * @param word the word to check
	 * @param vertical true if the word is vertical, false if horizontal
	 * @return the starting square of the word, or null if not found
	 */
	private Square getStartSquare(String word, boolean vertical) {
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int col = 0; col < Board.BOARD_SIZE; col++) {
				Square square = board.getSquare(row, col);
				if (square != null && square.containsLetter() && formsPartOfWord(square, word, vertical)) {
					return square;
				}
			}
		}
		return null; // Daca nu gasim nicio caseta, returnam null
	}



	// Verifica daca un cuvant este plasat vertical pe tabla
	private boolean isWordVertical(String word) {
		// Căutăm primele două litere adiacente ale cuvântului pentru a determina direcția
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int col = 0; col < Board.BOARD_SIZE; col++) {
				Square square = board.getSquare(row, col);
				if (square != null && square.containsLetter() && formsPartOfWord(square, word, true)) {
					// Verificăm dacă următoarea literă este verticală
					if (row + 1 < Board.BOARD_SIZE) {
						Square nextSquare = board.getSquare(row + 1, col);
						if (nextSquare != null && nextSquare.containsLetter() && formsPartOfWord(nextSquare, word, true)) {
							return true; // Cuvântul este vertical
						}
					}
					// Verificăm dacă următoarea literă este orizontală
					if (col + 1 < Board.BOARD_SIZE) {
						Square nextSquare = board.getSquare(row, col + 1);
						if (nextSquare != null && nextSquare.containsLetter() && formsPartOfWord(nextSquare, word, false)) {
							return false; // Cuvântul este orizontal
						}
					}
				}
			}
		}
		return false; // Implicit, dacă nu găsim nicio literă adiacentă, considerăm că este orizontal
	}



	private boolean formsPartOfWord(Square square, String word, boolean vertical) {
		int row = square.getRow();
		int col = square.getCol();
		int wordLength = word.length();

		for (int i = 0; i < wordLength; i++) {
			char letter = word.charAt(i);
			int newRow = vertical ? row + i : row;
			int newCol = vertical ? col : col + i;

			if (newRow >= Board.BOARD_SIZE || newCol >= Board.BOARD_SIZE) {
				return false; // Depășește limitele tablei
			}

			Square current = board.getSquare(newRow, newCol);
			if (current == null || !current.containsLetter() || current.getLetter() != letter) {
				return false; // Litera nu se potrivește
			}
		}

		return true;
	}

	/**
	 * Gets the new word formed and calculates its score.
	 *
	 * @param startSquare the starting square of the word
	 * @param vertical true if the word is vertical, false if horizontal
	 * @return the score of the new word
	 */

	private int getNewWord(Square startSquare, boolean vertical) {
		StringBuilder word = new StringBuilder();
		int wordMultiplier = 1;
		int wordScore = 0;

		int row = startSquare.getRow();
		int col = startSquare.getCol();

		System.out.println("Starting at position: (" + row + "," + col + ")");

		// Move to the beginning of the word
		while (true) {
			int newRow = vertical ? row - 1 : row;
			int newCol = vertical ? col : col - 1;

			if (newRow < 0 || newCol < 0) break;

			Square current = board.getSquare(newRow, newCol);
			if (current == null || !current.containsLetter()) {
				break;
			}
			row = newRow;
			col = newCol;
		}

		System.out.println("Moved to start of the word at position: (" + row + "," + col + ")");

		// Build the word and calculate the score from start to end
		while (true) {
			if (row >= Board.BOARD_SIZE || col >= Board.BOARD_SIZE) {
				break;
			}

			Square current = board.getSquare(row, col);
			if (current == null || !current.containsLetter()) {
				break;
			}

			word.append(current.getLetter());
			int letterScore = Alphabet.getLetterPoint(current.getLetter());

			System.out.println("Letter: " + current.getLetter() + ", Base score: " + letterScore + ", Position: (" + row + "," + col + "), Type: " + current.getType());

			// Calculate score for all letters in the word
			switch (current.getType()) {
				case DOUBLE_LETTER:
					letterScore *= 2;
					break;
				case TRIPLE_LETTER:
					letterScore *= 3;
					break;
				case DOUBLE_WORD:
					wordMultiplier *= 2;
					break;
				case TRIPLE_WORD:
					wordMultiplier *= 3;
					break;
				default:
					break;
			}

			System.out.println("Letter score after multiplier: " + letterScore);

			wordScore += letterScore;

			// Move to the next square
			if (vertical) {
				row++;
			} else {
				col++;
			}
		}

		wordScore *= wordMultiplier;
		String formedWord = word.toString();
		if (Trie.findWord(formedWord)) {
			System.out.println("Formed word: " + formedWord + ", Total score: " + wordScore);
		}

		return wordScore;
	}


	/**
	 * Gets the color of a square based on its type.
	 *
	 * @param square the square whose color to get
	 * @return the color of the square
	 */

	public Color getSquareColor(Square square) {
		switch (square.getType()) {
			case DOUBLE_LETTER:
				return Color.CYAN; // Light Blue for Double Letter
			case TRIPLE_LETTER:
				return Color.BLUE; // Dark Blue for Triple Letter
			case DOUBLE_WORD:
				return Color.YELLOW; // Yellow for Double Word
			case TRIPLE_WORD:
				return Color.RED; // Red for Triple Word
			case START:
				return  Color.BLACK;
			default:
				return new Color(221, 160, 221); // Light purple for Regular
		}
	}


	public void updateScores(Player player, boolean isPlayer1) {
		if (isPlayer1) {
			score1Label.setText(player1Name + ": " + player1.getScore() + " points");
		} else {
			score2Label.setText(player2Name + ": " + player2.getScore() + " points");
		}
		turnLabel.setText("Turn: " + (isPlayer1 ? player2Name : player1Name));

	}


	public void updateBoard() {
		for (int row = 0; row < Board.BOARD_SIZE; row++) {
			for (int column = 0; column < Board.BOARD_SIZE; column++) {
				JLabel label = grid[row][column];
				Square square = board.getSquare(row, column);
				if (square.containsLetter()) {
					label.setText(String.valueOf(square.getLetter()));
					label.setForeground(Color.BLACK); // Set text color to black for placed letters
					squares[row][column].setBackground(Color.WHITE);
				} else {

					label.setForeground(new Color(105, 105, 105)); // Dark grey color for text
					squares[row][column].setBackground(getSquareColor(square));
				}
			}
		}
	}

}