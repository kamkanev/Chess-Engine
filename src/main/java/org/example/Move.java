package org.example;

import org.example.pieces.Piece;

public class Move {

    int oldCol;
    int oldRow;
    int newCol;
    int newRow;

    Piece piece;
    Piece capture;

    public Move(Board board, Piece piece, int newCol, int newRow) {

        this.oldCol = piece.getCol();
        this.oldRow = piece.getRow();
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = piece;
        this.capture = board.getPieceAt(newCol, newRow);
    }
}
