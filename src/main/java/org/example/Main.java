package org.example;

import org.example.ui.NewGameWindow;
import org.example.ui.StartMenu;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static final String TITLE = "Chess";
    public static final String SUBTITLE = "";
    public static HashMap<String, JFrame> windows = new HashMap<String, JFrame>();

    public static void main(String[] args) {

        StartMenu stWin = new StartMenu(TITLE, SUBTITLE, windows);

        NewGameWindow newGame = new NewGameWindow(TITLE, stWin);

        windows.put("new game", newGame);

        stWin.setVisible(true);

//        Chess og
//        JFrame frame = new JFrame(TITLE);
//        frame.setMinimumSize(new Dimension(1000, 1000));
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        frame.getContentPane().setBackground(Color.BLACK);
//
//        frame.setLayout(new GridBagLayout());
//
//        Board board = new Board();
//
//        frame.add(board);
//
//        frame.setVisible(true);

    }
}