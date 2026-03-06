package org.example.pieces;

import org.example.Board;

import java.awt.image.BufferedImage;

public class Bishop extends Piece {
    public Bishop(Board board, int col, int row, boolean white) {
        super(board);
        this.col = col;
        this.row = row;

        this.xPos = col * board.TILESIZE;
        this.yPos = row * board.TILESIZE;

        this.isWhite = white;
        this.name = "Bishop";

        this.sprite = sheet.getSubimage(2 * this.sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(this.board.TILESIZE, this.board.TILESIZE, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    @Override
    public boolean moveCollideWithPiece(int col, int row) {

        if(this.col > col && this.row > row) {
            for(int i = 1; i < Math.abs(this.col - col); i++) {
                if(board.getPieceAt(this.col - i, this.row - i) != null) {
                    return true;
                }
            }
        }

        if(this.col < col && this.row > row) {
            for(int i = 1; i < Math.abs(this.col - col); i++) {
                if(board.getPieceAt(this.col + i, this.row - i) != null) {
                    return true;
                }
            }
        }

        if(this.col > col && this.row < row) {
            for(int i = 1; i < Math.abs(this.col - col); i++) {
                if(board.getPieceAt(this.col - i, this.row + i) != null) {
                    return true;
                }
            }
        }

        if(this.col < col && this.row < row) {
            for(int i = 1; i < Math.abs(this.col - col); i++) {
                if(board.getPieceAt(this.col + i, this.row + i) != null) {
                    return true;
                }
            }
        }

        return false;
    }
}
