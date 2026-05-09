package org.example.ui.components;

import org.example.toolbox.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class BackButton extends CustomBorderButton {

    private JFrame win;
    private JFrame back;

    public BackButton(Rectangle r, JFrame win, JFrame back) {
        super(loadBackIcon(r), JLabel.CENTER, r);
        setOpaque(false);
        this.win = win;
        this.back = back;
        addOperation();
    }

    private static ImageIcon loadBackIcon(Rectangle r) {
        try {
            return new ImageIcon(Images.getScaledImage(Images.loadImageIcon("icons/arrows.png").getImage(), r.width, r.height));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load back button icon.", e);
        }
    }

    @Override
    protected void addOperation() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(back != null) {
                    back.setVisible(true);
                    win.dispose();
                    back.setFocusable(true);
                }else {
                    System.err.println("There is no window to go back to!");
                }
            }

        });

    }

}
