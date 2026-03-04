package org.example;

import org.example.pieces.Knight;
import org.example.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {

    public final int TILESIZE = 85;

    private Color whitesqr =  new Color(255, 255, 223);
    private Color blacksqr = new Color(133, 167, 101);

    private int cols = 8;
    private int rows = 8;

    private ArrayList<Piece> pieces = new ArrayList<>();

    public Board() {
        this.setPreferredSize(new Dimension(cols * TILESIZE, rows * TILESIZE));
        this.addPiece(new Knight(this, 2, 0, false));
    }

    public void addPiece(Piece p){
        this.pieces.add(p);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g2d.setColor((i+j) % 2 == 0 ? this.whitesqr : this.blacksqr);
                g2d.fillRect(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE);
            }

            for (Piece p : pieces) {
                p.paint(g2d);
            }
        }
    }

}
