package org.example.toolbox;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Images {

    public static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public static void setBackground(JFrame jf,String name, boolean isFull) {
        ImageIcon img;
        try {
            if (isFull) {
                img = new ImageIcon(getScaledImage(loadImageIcon("backgrounds/" + name).getImage(), jf.getWidth(), jf.getHeight()));
            } else {
                img = loadImageIcon("backgrounds/" + name);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load background image: " + name, e);
        }

        JLabel background = new JLabel("", img, JLabel.CENTER);
        background.setName("back");
        background.setBounds(0, 0, jf.getWidth(), jf.getHeight());


        jf.add(background);

    }

    public static void setBackground(JComponent jc,String name) {
        ImageIcon img;
        try {
            img = loadImageIcon("backgrounds/" + name);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load background image: " + name, e);
        }

        JLabel background = new JLabel("", img, JLabel.CENTER);
        background.setName("back");
        background.setBounds(0, 0, jc.getWidth(), jc.getHeight());


        jc.add(background);

    }

    public static ImageIcon loadImageIcon(String resourcePath) throws IOException {
        URL resource = Images.class.getClassLoader().getResource(resourcePath);
        if (resource == null) {
            throw new IOException("Image resource not found: " + resourcePath);
        }
        return new ImageIcon(resource);
    }

}
