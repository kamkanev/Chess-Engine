package org.example;

import org.example.pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {

    protected Board board;
    public Input(Board board){
        this.board = board;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if(board.selectedPiece != null){
            board.selectedPiece.setxPos(e.getX() - board.TILESIZE / 2);
            board.selectedPiece.setyPos(e.getY() - board.TILESIZE / 2);

            board.repaint();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int col = e.getX() / board.TILESIZE;
        int row = e.getY() / board.TILESIZE;

        Piece pieceXY = board.getPieceAt(col, row);
        if(pieceXY != null){
            board.selectedPiece = pieceXY;
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int col = e.getX() / board.TILESIZE;
        int row = e.getY() / board.TILESIZE;

        if(board.selectedPiece != null){
            Move move = new Move(board, board.selectedPiece, col, row);

            if(board.isValidMove(move)){
                board.makeMove(move);
            }else{
                board.selectedPiece.setxPos(board.selectedPiece.getCol() * board.TILESIZE);
                board.selectedPiece.setyPos(board.selectedPiece.getRow() * board.TILESIZE);
            }

        }

        board.selectedPiece = null;
        board.repaint();

    }
}
