package dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


/**
 * Class representing the alphabet used in the Scrabble game.
 * This class manages the initialization of the alphabet, including the points and amount of each letter.
 */


public class Alphabet {
	public static HashMap<Character, Integer> letterAmounts = new HashMap<Character, Integer>();
	static HashMap<Character, Integer> letterPoints = new HashMap<Character, Integer>();

	public static char[] alphabet = new char[] {
			'A', 'Ă', 'Â', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'Î',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'Ș', 'T',
			'Ț', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};


	/**
	 * Initializes the alphabet with points and amount of each letter depending on the language.
	 * Reads information from a file representing the chosen language and puts info into hashmaps.
	 *
	 * @param language the language for which to initialize the alphabet
	 */

	public static void initializeAlphabet(String language) {
		String filename = "languages/" + language + ".alphabet";
		System.out.println("Trying to read file: " + filename); // Debug message
		try {
			readFromFile(filename);
			System.out.println("File read successfully."); // Debug message
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: Could not find file: " + filename);
		} catch (IOException e) {
			System.err.println("IOException: Could not read from file!");
		}
	}

	/**
	 * Reads info about each letter from file.
	 * The format of the file is comments in the beginning that start with '#'.
	 * Each following row represents a letter and is written as [letter] [amount] [points].
	 * Example:
	 * # Comments in the beginning of file.
	 * # Another comment.
	 * A 1 2
	 * B 3 4
	 * etc.
	 *
	 * @param filename the name of the file to read from
	 * @throws IOException if an I/O error occurs
	 */


	private static void readFromFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename), StandardCharsets.UTF_8));

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("#") || line.trim().isEmpty())
				continue;

			String[] lineArguments = line.split(" ");
			if (lineArguments.length < 3) {
				System.err.println("Invalid line format: " + line); // Debug message
				continue;
			}

			char letter = lineArguments[0].toCharArray()[0];
			int amount = Integer.parseInt(lineArguments[1]);
			int points = Integer.parseInt(lineArguments[2]);

			letterAmounts.put(letter, amount);
			letterPoints.put(letter, points);
			System.out.println("Read letter: " + letter + ", amount: " + amount + ", points: " + points); // Debug message
		}
		reader.close(); // Make sure to close the reader
	}


	public static int getLetterPoint(char letter) {
		return letterPoints.get(letter);
	}

	public static int getLetterAmount(char letter) {
		Integer amount = letterAmounts.get(letter);
		if (amount == null) {
			System.err.println("Letter not found in letterAmounts: " + letter);
			return 0; // Or throw an exception
		}
		return amount;
	}


}
