package org.example.ui;

import org.example.ui.components.BackButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadGameWindow extends JFrame {

    private Point mouse = new Point();

    private JFrame back = new JFrame();

    private List<JPanel> games = new ArrayList<JPanel>();

    private JPanel main = new JPanel();

    public LoadGameWindow(JFrame back) {
        super("Load Game");

        this.back = back;

        setSize(600, 500);

        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);

        setResizable(false);

        JPanel jp = new JPanel();
        jp.setLayout(null);
        jp.setBounds(0, 0, this.getWidth(), 30);
        jp.setOpaque(false);

        jp.add(new BackButton(new Rectangle(2, 0, 40, 30), this, back));
        this.add(jp);

//        this.add(BorderPanel.addAllBorderButtons(this, back));

        loadMainCon();
    }

    private void loadMainCon() {

//	       JScrollPane scrollPane = new JScrollPane(main, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


//	        scrollPane.setOpaque(false);

        main.setVisible(true);
        main.setBounds(20, 50, 560, 400);
        main.setBackground(Color.YELLOW);
        loadWorlds();
        main.setLayout(null);
//		this.add(scrollPane);
        this.add(main);

    }

    private void loadWorlds() {

        String savePath = System.getProperty("user.dir");
        File savesFolder = new File(savePath, "saves");
        if (!savesFolder.exists() && !savesFolder.mkdirs()) {
            throw new IllegalStateException("Could not create saves folder: " + savesFolder.getAbsolutePath());
        }

        if (!savesFolder.isDirectory()) {
            throw new IllegalStateException("Save path is not a folder: " + savesFolder.getAbsolutePath());
        }

        File[] saveFiles = savesFolder.listFiles(File::isFile);
        if (saveFiles == null || saveFiles.length == 0) {
            return;
        }

        for (File saveFile : saveFiles) {
            System.out.println("Found save file: " + saveFile.getName());
        }
    }

}
