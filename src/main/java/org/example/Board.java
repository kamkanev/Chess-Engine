package org.example;

import org.example.pieces.*;

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
        this.setBoard();
    }

    public void addPiece(Piece p){
        this.pieces.add(p);
    }

    public void setBoard(){

        //Black side set
        this.addPiece(new Rook(this, 0, 0, false));
        this.addPiece(new Knight(this, 1, 0, false));
        this.addPiece(new Bishop(this, 2, 0, false));
        this.addPiece(new Queen(this, 3, 0, false));
        this.addPiece(new King(this, 4, 0, false));
        this.addPiece(new Bishop(this, 5, 0, false));
        this.addPiece(new Knight(this, 6, 0, false));
        this.addPiece(new Rook(this, 7, 0, false));

        this.addPiece(new Pawn(this, 0, 1, false));
        this.addPiece(new Pawn(this, 1, 1, false));
        this.addPiece(new Pawn(this, 2, 1, false));
        this.addPiece(new Pawn(this, 3, 1, false));
        this.addPiece(new Pawn(this, 4, 1, false));
        this.addPiece(new Pawn(this, 5, 1, false));
        this.addPiece(new Pawn(this, 6, 1, false));
        this.addPiece(new Pawn(this, 7, 1, false));

        //white side set
        this.addPiece(new Rook(this, 0, 7, true));
        this.addPiece(new Knight(this, 1, 7, true));
        this.addPiece(new Bishop(this, 2, 7, true));
        this.addPiece(new Queen(this, 3, 7, true));
        this.addPiece(new King(this, 4, 7, true));
        this.addPiece(new Bishop(this, 5, 7, true));
        this.addPiece(new Knight(this, 6, 7, true));
        this.addPiece(new Rook(this, 7, 7, true));

        this.addPiece(new Pawn(this, 0, 6, true));
        this.addPiece(new Pawn(this, 1, 6, true));
        this.addPiece(new Pawn(this, 2, 6, true));
        this.addPiece(new Pawn(this, 3, 6, true));
        this.addPiece(new Pawn(this, 4, 6, true));
        this.addPiece(new Pawn(this, 5, 6, true));
        this.addPiece(new Pawn(this, 6, 6, true));
        this.addPiece(new Pawn(this, 7, 6, true));
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
