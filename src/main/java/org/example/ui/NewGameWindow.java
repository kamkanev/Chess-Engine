package org.example.ui;

import org.example.Board;

import javax.swing.*;
import java.awt.*;

public class NewGameWindow extends JFrame {

    private Point mouse = null;
    private JFrame mainWindow;
    private Board board = new Board();

    public NewGameWindow(String title, JFrame main) {

        super("New Game - "+title);

        mainWindow = main;

        setSize(600, 500);

//		WORD_SIZE = Fonts.getFontSizeByScreen(this, 25);
//		System.err.println(WORD_SIZE);

        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
//        setUndecorated(true);
//		updateOnResize();

        setResizable(false);
        setMinimumSize(new Dimension(1000, 1000));
        getContentPane().setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        add(board);
    }

}
