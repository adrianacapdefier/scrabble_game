package DataforPlayer;

import board.Board;
import game.Game;
import player.HighScoreWordPlayer;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the login interface for the Scrabble game.
 * This class handles user authentication and initialization of the game.
 */
public class LoginGUI extends JFrame {
    private JTextField player1NameField;
    private JPasswordField player1PasswordField;
    private JTextField player2NameField;
    private JPasswordField player2PasswordField;
    private JButton modifyPlayer1Button;
    private JButton modifyPlayer2Button;
    private JButton startGameButton;

    private JTextField player1CurrentNameField;
    private JTextField player2CurrentNameField;


    private Map<String, String> userCredentials;

    public LoginGUI() {
        userCredentials = loadUserCredentials();
        initComponents();
    }

    /**
     * Initializes the components of the login interface.
     */
    private void initComponents() {
        setTitle("Scrabble Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); // Set the layout to BoxLayout

        JPanel player1Panel = new JPanel(new GridLayout(2, 2));
        JLabel player1NameLabel = new JLabel("Player 1 Name:");
        JLabel player1PasswordLabel = new JLabel("Player 1 Password:");
        player1NameField = new JTextField();
        player1PasswordField = new JPasswordField();
        player1Panel.add(player1NameLabel);
        player1Panel.add(player1NameField);
        player1Panel.add(player1PasswordLabel);
        player1Panel.add(player1PasswordField);

        JPanel player2Panel = new JPanel(new GridLayout(2, 2));
        JLabel player2NameLabel = new JLabel("Player 2 Name:");
        JLabel player2PasswordLabel = new JLabel("Player 2 Password:");
        player2NameField = new JTextField();
        player2PasswordField = new JPasswordField();
        player2Panel.add(player2NameLabel);
        player2Panel.add(player2NameField);
        player2Panel.add(player2PasswordLabel);
        player2Panel.add(player2PasswordField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        modifyPlayer1Button = new JButton("Modify Player 1");
        modifyPlayer2Button = new JButton("Modify Player 2");
        startGameButton = new JButton("Start Game");
        buttonPanel.add(modifyPlayer1Button);
        buttonPanel.add(modifyPlayer2Button);
        buttonPanel.add(startGameButton);

        add(player1Panel);
        add(player2Panel);
        add(buttonPanel);

        modifyPlayer1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showModifyDialog("player1");
            }
        });

        modifyPlayer2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showModifyDialog("player2");
            }
        });

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        setVisible(true);
    }

    /**
     * Loads user credentials from a file.
     *
     * @return a map containing user credentials
     */
    private Map<String, String> loadUserCredentials() {
        Map<String, String> credentials = new HashMap<>();
        String filePath = "src/DataforPlayer/user_data.txt"; // Ajustează calea aici
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    credentials.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded credentials: " + credentials); // Debug print
        return credentials;
    }


    private void showModifyDialog(String player) {
        JDialog dialog = new JDialog(this, "Modify " + player + " Credentials", true);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setSize(300, 200);

        JLabel currentNameLabel = new JLabel("Current Name:");
        JTextField currentNameField = new JTextField();
        JLabel currentPasswordLabel = new JLabel("Current Password:");
        JPasswordField currentPasswordField = new JPasswordField();
        JLabel newNameLabel = new JLabel("New Name:");
        JTextField newNameField = new JTextField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        JButton saveButton = new JButton("Save");

        dialog.add(currentNameLabel);
        dialog.add(currentNameField);
        dialog.add(currentPasswordLabel);
        dialog.add(currentPasswordField);
        dialog.add(newNameLabel);
        dialog.add(newNameField);
        dialog.add(newPasswordLabel);
        dialog.add(newPasswordField);
        dialog.add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentPassword = new String(currentPasswordField.getPassword());
                String newName = newNameField.getText();
                String newPassword = new String(newPasswordField.getPassword());

                System.out.println("Expected current password: " + userCredentials.get(currentNameField.getText()));
                System.out.println("Entered current password: " + currentPassword);

                if (userCredentials.containsKey(currentNameField.getText()) && userCredentials.get(currentNameField.getText()).equals(currentPassword)) {
                    userCredentials.put(newName, newPassword);
                    updateUserDataFile(currentNameField.getText(), newName, newPassword);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Current password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.setVisible(true);
    }


    private void updateUserDataFile(String oldName, String newName, String newPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/DataforPlayer/user_data.txt"))) {
            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // In LoginGUI class

    // In LoginGUI class

    private void startGame() {
        String player1Name = player1NameField.getText();
        String player1Password = new String(player1PasswordField.getPassword());
        String player2Name = player2NameField.getText();
        String player2Password = new String(player2PasswordField.getPassword());

        if (userCredentials.containsKey(player1Name) && userCredentials.get(player1Name).equals(player1Password) &&
                userCredentials.containsKey(player2Name) && userCredentials.get(player2Name).equals(player2Password)) {

            Board boardType = new Board();
            Player player1Type = new HighScoreWordPlayer(boardType, player1Name);
            Player player2Type = new HighScoreWordPlayer(boardType, player2Name);
            boolean player1StartsPlaying = true;

            SwingUtilities.invokeLater(() -> {
                new Game(player1StartsPlaying, player1Type, player2Type, boardType).play();
            });

            dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid login credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }











}
