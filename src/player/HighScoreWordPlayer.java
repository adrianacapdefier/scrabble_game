package player;

import board.Board;

/**
 * Class representing a player who aims to form the highest-scoring words in the Scrabble game.
 * This class extends the abstract Player class and implements its abstract methods.
 */
public class HighScoreWordPlayer extends Player {

	public HighScoreWordPlayer(Board board, String name) {
		super(board, name);
	}



	@Override
	public void resetParameters() {}

	@Override
	public String getName() {
		return super.getName();
	}


}
