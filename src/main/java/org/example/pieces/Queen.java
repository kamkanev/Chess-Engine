package org.example.pieces;

import org.example.Board;

import java.awt.image.BufferedImage;

public class Queen extends Piece {
    public Queen(Board board, int col, int row, boolean white) {
        super(board);
        this.col = col;
        this.row = row;

        this.xPos = col * board.TILESIZE;
        this.yPos = row * board.TILESIZE;

        this.isWhite = white;
        this.name = "Queen";

        this.sprite = sheet.getSubimage(1 * this.sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(this.board.TILESIZE, this.board.TILESIZE, BufferedImage.SCALE_SMOOTH);
    }
}
