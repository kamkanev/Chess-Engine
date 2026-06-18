package org.example.ui;

import org.example.Board;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NewGameWindow extends JFrame {

    private final JFrame mainWindow;
    private final String title;
    private final JTextField gameName = new JTextField(generateGameName(), 18);
    private final JPanel palettePanel = new JPanel(new GridLayout(1, 4, 14, 14));
    private Color selectedWhiteSquare = Board.DEFAULT_WHITE_SQUARE;
    private Color selectedBlackSquare = Board.DEFAULT_BLACK_SQUARE;
    private PalettePreview selectedPalette;

    private final Color[][] palettes = {
            {Board.DEFAULT_WHITE_SQUARE, Board.DEFAULT_BLACK_SQUARE},
            {new Color(240, 217, 181), new Color(181, 136, 99)},
            {new Color(234, 242, 194), new Color(239, 116, 116)},
            {new Color(217, 226, 226), new Color(117, 171, 201)},
            {new Color(224, 212, 230), new Color(149, 123, 182)}
    };

    public NewGameWindow(String title, JFrame main) {
        super("New Game - " + title);

        mainWindow = main;
        this.title = title;

        setSize(900, 780);
        setMinimumSize(new Dimension(900, 780));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        showNewGameOptions();
    }

    private void showNewGameOptions() {
        setTitle("New Game - " + title);
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(new Color(28, 28, 28));
        content.setBorder(BorderFactory.createEmptyBorder(35, 45, 35, 45));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(8, 0, 8, 0);

        JLabel title = new JLabel("New Game", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Serif", Font.BOLD, 42));
        content.add(title, constraints);

        JLabel nameLabel = new JLabel("Game name");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        content.add(nameLabel, constraints);

        gameName.setFont(new Font("SansSerif", Font.PLAIN, 18));
        gameName.setText(generateGameName());
        content.add(gameName, constraints);

        JLabel paletteLabel = new JLabel("Choose board colors");
        paletteLabel.setForeground(Color.WHITE);
        paletteLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        content.add(paletteLabel, constraints);

        loadPalettePreviews();
        content.add(palettePanel, constraints);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> goBackToMainMenu());

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());

        buttonPanel.add(backButton);
        buttonPanel.add(startButton);

        content.add(buttonPanel, constraints);

        setContentPane(content);
        revalidate();
        repaint();
    }

    private void loadPalettePreviews() {
        palettePanel.removeAll();
        palettePanel.setOpaque(false);

        for (Color[] palette : palettes) {
            PalettePreview preview = new PalettePreview(palette[0], palette[1]);
            preview.addActionListener(e -> selectPalette(preview));
            palettePanel.add(preview);

            if (palette[0].equals(selectedWhiteSquare) && palette[1].equals(selectedBlackSquare)) {
                selectPalette(preview);
            }
        }
    }

    private void selectPalette(PalettePreview palette) {
        if (selectedPalette != null) {
            selectedPalette.setSelected(false);
        }

        selectedPalette = palette;
        selectedWhiteSquare = palette.getWhiteSquare();
        selectedBlackSquare = palette.getBlackSquare();
        selectedPalette.setSelected(true);
    }

    private void startGame() {
        String name = gameName.getText().trim();
        if (name.isEmpty()) {
            name = generateGameName();
            gameName.setText(name);
        }

        setTitle("Chess - " + name);

        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> goBackToMainMenu());
        topPanel.add(backButton);

        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setBackground(Color.BLACK);
        boardPanel.add(new Board(selectedWhiteSquare, selectedBlackSquare));

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
        showNewGameOptions();
        mainWindow.setVisible(true);
        mainWindow.setFocusable(true);
    }

    private static String generateGameName() {
        return "Game-" + (1000 + new Random().nextInt(9000));
    }

    private static class PalettePreview extends JButton {
        private final Color whiteSquare;
        private final Color blackSquare;

        private PalettePreview(Color whiteSquare, Color blackSquare) {
            this.whiteSquare = whiteSquare;
            this.blackSquare = blackSquare;
            setPreferredSize(new Dimension(110, 110));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        }

        private Color getWhiteSquare() {
            return whiteSquare;
        }

        private Color getBlackSquare() {
            return blackSquare;
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            setBorder(BorderFactory.createLineBorder(selected ? Color.WHITE : Color.GRAY, selected ? 4 : 2));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int tileWidth = getWidth() / 2;
            int tileHeight = getHeight() / 2;

            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    g.setColor((row + col) % 2 == 0 ? whiteSquare : blackSquare);
                    g.fillRect(col * tileWidth, row * tileHeight, tileWidth, tileHeight);
                }
            }
        }
    }
}
