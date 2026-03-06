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

    public boolean isValidMovement(int col, int row){
        return true;
    }
    public boolean moveCollideWithPiece(int col, int row){
        return false;
    }

    public void paint(Graphics g){

        g.drawImage(sprite, xPos, yPos,null);

    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public String getName() {
        return name;
    }

    public int getxPos() {
        return xPos;
    }
    public int getyPos() {
        return yPos;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
