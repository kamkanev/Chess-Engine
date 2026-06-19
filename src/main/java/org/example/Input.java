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

        int col = board.getBoardColFromMouseX(e.getX());
        int row = board.getBoardRowFromMouseY(e.getY());

        Piece pieceXY = board.getPieceAt(col, row);
        if(pieceXY != null){
            board.selectedPiece = pieceXY;
            board.selectedPiece.setxPos(board.getVisualX(board.selectedPiece.getCol()));
            board.selectedPiece.setyPos(board.getVisualY(board.selectedPiece.getRow()));
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        int col = board.getBoardColFromMouseX(e.getX());
        int row = board.getBoardRowFromMouseY(e.getY());

        if(board.selectedPiece != null){
            Move move = new Move(board, board.selectedPiece, col, row);

            if(board.isValidMove(move)){
                board.makeMove(move);
            }else{
                board.selectedPiece.setxPos(board.getVisualX(board.selectedPiece.getCol()));
                board.selectedPiece.setyPos(board.getVisualY(board.selectedPiece.getRow()));
            }

        }

        board.selectedPiece = null;
        board.repaint();

    }
}
