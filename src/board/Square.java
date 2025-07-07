package board;

/**
 * This class represents a square on the game board.
 * A square can hold a letter and has specific properties such as multipliers.
 */
public class Square {
	public static final char WALL = 48;

	private char letter;
	private int row;
	private int col;

	/**
	 * Enum representing the type of the square.
	 */
	public enum Type {
		REGULAR,
		DOUBLE_LETTER,
		TRIPLE_LETTER,
		DOUBLE_WORD,
		TRIPLE_WORD,
		START
	}

	private Type type;

	/**
	 * Constructs a Square with the specified letter, row, and column.
	 *
	 * @param letter the letter in the square
	 * @param row the row position of the square
	 * @param col the column position of the square
	 */
	public Square(char letter, int row, int col) {
		this.letter = '\0'; // Empty square
		this.row = row;
		this.col = col;
		this.type = Type.REGULAR;
	}
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return row;
	}

	public void setCol(int row) {
		this.row = row;
	}
	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public boolean containsLetter() {
		return letter != '\0';
	}

	@Override
	public String toString() {
		return Character.toString(letter);
	}







}
