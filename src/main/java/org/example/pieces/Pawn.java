package org.example.pieces;

import org.example.Board;

import java.awt.image.BufferedImage;

public class Pawn extends Piece {
    public Pawn(Board board, int col, int row, boolean white) {
        super(board);
        this.col = col;
        this.row = row;

        this.xPos = col * board.TILESIZE;
        this.yPos = row * board.TILESIZE;

        this.isWhite = white;
        this.name = "Pawn";

        this.sprite = sheet.getSubimage(5 * this.sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(this.board.TILESIZE, this.board.TILESIZE, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        int colorIdx = isWhite ? 1 : -1;

        if(this.col == col && row == this.row - colorIdx && board.getPieceAt(col, row) == null) {
            return true;
        }

        if(isFirstMove && this.col == col && row == this.row - 2 * colorIdx && board.getPieceAt(col, row) == null && board.getPieceAt(col, row + colorIdx) == null) {
            return true;
        }

        if(this.col == col - 1 && row == this.row - colorIdx && board.getPieceAt(col, row) != null) {
            return true;
        }

        if(this.col == col + 1 && row == this.row - colorIdx && board.getPieceAt(col, row) != null) {
            return true;
        }

        if(board.getTileNumber(col, row) == board.getGetEnPassantTile() && col == this.col - 1 && row == this.row -colorIdx && board.getPieceAt(col, row + colorIdx) != null){
            return true;
        }

        if(board.getTileNumber(col, row) == board.getGetEnPassantTile() && col == this.col + 1 && row == this.row -colorIdx && board.getPieceAt(col, row + colorIdx) != null){
            return true;
        }

        return false;
    }
}
