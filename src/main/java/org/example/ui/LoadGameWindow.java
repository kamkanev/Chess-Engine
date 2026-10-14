package org.example.ui;

import org.example.Board;
import org.example.save.GameSave;
import org.example.save.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class LoadGameWindow extends JFrame {

    private final JFrame mainWindow;
    private final JPanel listPanel = new JPanel();

    public LoadGameWindow(JFrame mainWindow) {
        super("Load Game");

        this.mainWindow = mainWindow;

        setSize(700, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        showLoadList();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            showLoadList();
        }
        super.setVisible(visible);
    }

    private void showLoadList() {
        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(new Color(28, 28, 28));
        content.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Load Game", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        content.add(title, BorderLayout.NORTH);

        listPanel.removeAll();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(38, 38, 38));

        loadGames();

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        content.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBackToMainMenu());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);
        content.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(content);
        revalidate();
        repaint();
    }

    private void loadGames() {
        List<File> saveFiles = SaveManager.listSaveFiles();

        if (saveFiles.isEmpty()) {
            JLabel emptyLabel = new JLabel("No saved games yet.");
            emptyLabel.setForeground(Color.WHITE);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            listPanel.add(emptyLabel);
            return;
        }

        for (File saveFile : saveFiles) {
            GameSave gameSave = SaveManager.load(saveFile);
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));
            row.setPreferredSize(new Dimension(600, 82));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.add(createGameButton(saveFile, gameSave), BorderLayout.CENTER);
            JButton deleteButton = new JButton("Delete");
            deleteButton.setToolTipText("Delete " + gameSave.getGameName());
            deleteButton.addActionListener(event -> deleteSavedGame(saveFile, gameSave.getGameName()));
            row.add(deleteButton, BorderLayout.EAST);
            listPanel.add(row);
            listPanel.add(Box.createVerticalStrut(8));
        }
    }

    private void deleteSavedGame(File saveFile, String gameName) {
        int answer = JOptionPane.showConfirmDialog(this,
                "Delete \"" + gameName + "\"? This cannot be undone.",
                "Delete saved game", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) return;
        try {
            SaveManager.delete(saveFile);
            showLoadList();
        } catch (IllegalStateException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "Could not delete game", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createGameButton(File saveFile, GameSave gameSave) {
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(saveFile.lastModified());
        String playerMode = gameSave.isPlayAgainstStockfish()
                ? "1 Player · You: " + (gameSave.isHumanPlaysWhite() ? "White" : "Black")
                    + " · Stockfish level " + gameSave.getStockfishLevel()
                : "2 Players";

        JButton button = new JButton("<html><b>" + gameSave.getGameName() + "</b><br>"
                + playerMode + " · " + (gameSave.getSurrenderedWhite() != null
                    ? (gameSave.getSurrenderedWhite() ? "Black wins" : "White wins") + " by surrender"
                    : (gameSave.isWhiteMove() ? "White" : "Black") + " to move")
                + "<br>Saved: " + modifiedDate + "</html>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        button.setFocusPainted(false);
        button.addActionListener(e -> openSavedGame(saveFile));
        return button;
    }

    private void openSavedGame(File saveFile) {
        GameSave gameSave = SaveManager.load(saveFile);
        setTitle("Chess - " + gameSave.getGameName());
        Board board = new Board(gameSave, saveFile);
        if (board.getSurrenderedWhite() != null) {
            GameResultScreen.show(this, board, this::goBackToMainMenu);
            return;
        }

        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> goBackToMainMenu());
        topPanel.add(backButton);
        if (gameSave.isPlayAgainstStockfish()) {
            JLabel opponent = new JLabel("You: " + (gameSave.isHumanPlaysWhite() ? "White" : "Black")
                    + " · Stockfish level " + gameSave.getStockfishLevel());
            opponent.setForeground(Color.WHITE);
            topPanel.add(opponent);
        }

        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setBackground(Color.BLACK);
        topPanel.add(GameResultScreen.createSurrenderButton(board, this,
                () -> GameResultScreen.show(this, board, this::goBackToMainMenu)));
        boardPanel.add(board);

        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(boardPanel, BorderLayout.CENTER);

        setContentPane(gamePanel);
        revalidate();
        repaint();
        pack();
        setLocationRelativeTo(null);
    }

    private void goBackToMainMenu() {
        setVisible(false);
        mainWindow.setVisible(true);
        mainWindow.setFocusable(true);
    }
}
