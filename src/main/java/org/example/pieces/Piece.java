package org.example.pieces;

import org.example.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece {

    protected int col,  row;
    protected int xPos, yPos;

    protected boolean isWhite;
    protected String name;
    protected int value;

    BufferedImage sheet;
    {
        try {
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("pieces.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected int sheetScale = sheet.getWidth()/6;

    Image sprite;

    Board board;

    public Piece(Board board){
        this.board = board;
    }

    public void paint(Graphics g){

        g.drawImage(sprite, xPos, yPos,null);

    }

}
