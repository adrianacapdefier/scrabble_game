package dictionary;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Class representing a trie (prefix tree) used to store and search for words.
 * This class is essential for validating words in the Scrabble game.
 */
public class Trie {

	private static Set<String> dictionary = new HashSet<>();

	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	public static void loadDictionary(String filename) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String word;
			while ((word = reader.readLine()) != null) {
				dictionary.add(word.trim().toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Finds a word in the dictionary set.
	 *
	 * @param word the word to search for
	 * @return true if the word is found, false otherwise
	 */
	public static boolean findWord(String word) {
		return dictionary.contains(word.toLowerCase());
	}


	private static class TrieNode {
		private Map<Character, TrieNode> children = new HashMap<>();
		private boolean isEndOfWord;

		public TrieNode() {
		}
	}

}
