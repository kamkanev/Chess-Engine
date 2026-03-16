package org.example.pieces;

import org.example.Board;
import org.example.Move;

import java.awt.image.BufferedImage;

public class King extends Piece {
    public King(Board board, int col, int row, boolean white) {
        super(board);
        this.col = col;
        this.row = row;

        this.xPos = col * board.TILESIZE;
        this.yPos = row * board.TILESIZE;

        this.isWhite = white;
        this.name = "King";

        this.sprite = sheet.getSubimage(0, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(this.board.TILESIZE, this.board.TILESIZE, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        return (Math.abs(col - this.col) == 1 && Math.abs(row - this.row) == 1) ||
                Math.abs(col - this.col) + Math.abs(row - this.row) == 1 ||
                canCastle(col, row);
    }

    private boolean canCastle(int col, int row) {

        if(this.row == row) {

            if(col == 6){

                Piece rook = board.getPieceAt(7, row);
                if(rook != null && rook.isFirstMove && isFirstMove) {

                    return board.getPieceAt(5, row) == null &&
                            board.getPieceAt(6, row) == null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 5, row));

                }

            } else if(col == 2){

                Piece rook = board.getPieceAt(0, row);
                if(rook != null && rook.isFirstMove && isFirstMove) {

                    return board.getPieceAt(3, row) == null &&
                            board.getPieceAt(2, row) == null &&
                            board.getPieceAt(1, row) == null &&
                            !board.checkScanner.isKingChecked(new Move(board, this, 3, row));

                }

            }

        }


        return false;
    }
}
