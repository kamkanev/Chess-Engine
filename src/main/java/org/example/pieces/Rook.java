package org.example.pieces;

import org.example.Board;

import java.awt.image.BufferedImage;

public class Rook extends Piece {
    public Rook(Board board, int col, int row, boolean white) {
        super(board);
        this.col = col;
        this.row = row;

        this.xPos = col * board.TILESIZE;
        this.yPos = row * board.TILESIZE;

        this.isWhite = white;
        this.name = "Rook";

        this.sprite = sheet.getSubimage(4 * this.sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(this.board.TILESIZE, this.board.TILESIZE, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return this.col == col || this.row == row;
    }

    @Override
    public boolean moveCollideWithPiece(int col, int row) {

        if(this.col > col){
            for(int i = this.col - 1; i > col; i--){
                if(board.getPieceAt(i, this.row) != null){
                    return true;
                }
            }
        }

        if(this.col < col){
            for(int i = this.col + 1; i < col; i++){
                if(board.getPieceAt(i, this.row) != null){
                    return true;
                }
            }
        }

        if(this.row > row){
            for(int i = this.row - 1; i > row; i--){
                if(board.getPieceAt(this.col, i) != null){
                    return true;
                }
            }
        }

        if(this.row < row){
            for(int i = this.row + 1; i < row; i++){
                if(board.getPieceAt(this.col, i) != null){
                    return true;
                }
            }
        }

        return false;
    }
}
