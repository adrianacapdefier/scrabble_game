package board;

import javax.swing.*;
import java.util.Arrays;

/**
 * Class representing the game board for Scrabble.
 * The board is initialized with special tiles based on the Scrabble board rules.
 */

public class Board extends JPanel {

	Square[][] board;
	public static final int BOARD_SIZE = 15;
	private Square[][] square;


	public Board()
	{
		initBoard();
	}

	/**
	 * Initializes the board with special tiles.
	 */
	private void initBoard() {
		// Create square objects for each place
		board = new Square[BOARD_SIZE][BOARD_SIZE];

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board[row][col] = new Square(Square.WALL, row, col);

				// Define special tiles based on Scrabble board
				if ((row == 0 || row == 14) && (col == 0 || col == 14) ||
						(row == 0 || row == 14) && col == 7 ||
						row == 7 && (col == 0 || col == 14)) {
					board[row][col].setType(Square.Type.TRIPLE_WORD);
				} else if ((row == 1 || row == 13) && (col == 5 || col == 9) ||
						(row == 5 || row == 9) && (col == 1 || col == 13) ||
						(row == 5 || row == 9) && (col == 5 || col == 9)) {
					board[row][col].setType(Square.Type.TRIPLE_LETTER);
				} else if ((row == 2 || row == 12) && (col == 6 || col == 8) ||
						(row == 6 || row == 8) && (col == 2 || col == 12) ||
						(row == 7) && (col == 3 || col == 11) ||
						(row == 3 || row == 11) && col == 7) {
					board[row][col].setType(Square.Type.DOUBLE_WORD);
				} else if ((row == 1 || row == 13) && (col == 1 || col == 13) ||
						(row == 2 || row == 12) && (col == 2 || col == 12) ||
						(row == 3 || row == 11) && (col == 0 || col == 14) ||
						(row == 4 || row == 10) && (col == 4 || col == 10) ||
						(row == 1 || row == 13) && (col == 5 || col == 9) ||
						(row == 5 || row == 9) && (col == 1 || col == 13) ||
						(row == 6 || row == 8) && (col == 6 || col == 8)) {
					board[row][col].setType(Square.Type.DOUBLE_LETTER);
				} else if ((row == 3 || row == 11) && (col == 3 || col == 11) ||
						(row == 4 || row == 10) && (col == 4 || col == 10) ||
						(row == 5 || row == 9) && (col == 5 || col == 9) ||
						(row == 6 || row == 8) && (col == 6 || col == 8)) {
					board[row][col].setType(Square.Type.DOUBLE_WORD);
				}else if (row == 7 && col == 7) {
						board[row][col].setType(Square.Type.START);
				} else {
					board[row][col].setType(Square.Type.REGULAR);
				}
			}
		}
	}

	public Square getSquare(int row, int column) {
		return board[row][column];
	}


	@Override
	public String toString() {
		return Arrays.deepToString(square);
	}

}
