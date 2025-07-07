package player;

import board.Board;

import java.io.Serializable;

/**
 * Abstract class representing a player in the Scrabble game.
 * This class provides the basic attributes and methods for a player.
 */


public abstract class Player implements Serializable {

	private Board board;
	private int score;
	private String name;


	public Player(Board board, String name) {
		this.board = board;
		this.name = name;
		resetParameters();
	}



	// Getters and setters for new fields
	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}


	/**
	 * Adds the specified points to the player's score.
	 *
	 * @param points the points to be added to the player's score
	 */
	public void addPointsToScore(int points) {
		score += points;
		System.out.println("Added points: " + points + " | New score: " + score);
	}

	public abstract void resetParameters();



}
