package game;

import DataforPlayer.LoginGUI;
import board.Board;
import dictionary.Alphabet;
import dictionary.Trie;
import gui.ScrabbleGUI;
import player.Player;

import javax.swing.*;

/**
 * Class representing the Scrabble game.
 * This class manages the initialization of the game, player turns, and interactions between components.
 */

public class Game {
	public static final String GAME_LANGUAGE = "romanian";

	ScrabbleGUI gui;
	private static Board board;

	private static Player player1;
	private static Player player2;
	private int turn;

	private boolean player1StartsPlaying;
	private Trie dictionary;

	/**
	 * Constructs a Game with the specified players and board.
	 *
	 * @param player1StartsPlaying indicates if player 1 starts the game
	 * @param player1 the first player
	 * @param player2 the second player
	 * @param board the game board
	 */
	public Game(boolean player1StartsPlaying, Player player1, Player player2, Board board) {
		this.board = board;
		this.player1 = player1;
		this.player2 = player2;
		this.player1StartsPlaying = player1StartsPlaying;
		this.turn = (player1StartsPlaying) ? -1 : 1;

		dictionary = new Trie();
		dictionary.loadDictionary("dictionary/romanian_dictionary.txt");

		gui = new ScrabbleGUI(board, player1, player2, player1.getName(), player2.getName());
		gui.updateBoard();
	}


	public void play() {
		Player player = (turn < 0) ? player1 : player2;
		player.resetParameters();
		gui.updateBoard();
		gui.updateScores(turn < 0 ? player1 : player2, turn < 0);

		turn = -turn;

		gui.updateBoard();
		gui.updateScores(player1, true);
		gui.updateScores(player2, false);
	}

	/**
	 * The main method that initializes the login interface, alphabet, and dictionary.
	 *
	 * @param args command-line arguments
	 */

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LoginGUI();
				Alphabet.initializeAlphabet(GAME_LANGUAGE);
				Trie.loadDictionary("dictionary/romanian_dictionary.txt");
			}
		});
	}



}
