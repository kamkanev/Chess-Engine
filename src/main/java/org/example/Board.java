package org.example;

import org.example.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {

    public final int TILESIZE = 85;

    private Color whitesqr =  new Color(255, 255, 223);
    private Color blacksqr = new Color(133, 167, 101);
    private Color validMoveColor = new Color(59, 154, 26, 168);

    private int cols = 8;
    private int rows = 8;

    private ArrayList<Piece> pieces = new ArrayList<>();

    public Piece selectedPiece;

    private int getEnPassantTile = -1;

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public Board() {
        this.setPreferredSize(new Dimension(cols * TILESIZE, rows * TILESIZE));

        addMouseListener(input);
        addMouseMotionListener(input);

        this.setBoard();
    }

    public Piece getPieceAt(int col, int row){
        for(Piece p : pieces){
            if(p.getCol() == col && p.getRow() == row){
                return p;
            }
        }

        return null;
    }

    public void addPiece(Piece p){
        this.pieces.add(p);
    }

    Piece findKing(boolean iswhite){
        for(Piece p : pieces){
            if(p.isWhite() == iswhite && p.getName().equals("King")){
                return p;
            }
        }

        return null;
    }

    public int getTileNumber(int col, int row){
        return row * rows + col;
    }

    public void setBoard(){

        //Black side set
        this.addPiece(new Rook(this, 0, 0, false));
        this.addPiece(new Knight(this, 1, 0, false));
        this.addPiece(new Bishop(this, 2, 0, false));
        this.addPiece(new Queen(this, 3, 0, false));
        this.addPiece(new King(this, 4, 0, false));
        this.addPiece(new Bishop(this, 5, 0, false));
        this.addPiece(new Knight(this, 6, 0, false));
        this.addPiece(new Rook(this, 7, 0, false));

        this.addPiece(new Pawn(this, 0, 1, false));
        this.addPiece(new Pawn(this, 1, 1, false));
        this.addPiece(new Pawn(this, 2, 1, false));
        this.addPiece(new Pawn(this, 3, 1, false));
        this.addPiece(new Pawn(this, 4, 1, false));
        this.addPiece(new Pawn(this, 5, 1, false));
        this.addPiece(new Pawn(this, 6, 1, false));
        this.addPiece(new Pawn(this, 7, 1, false));

        //white side set
        this.addPiece(new Rook(this, 0, 7, true));
        this.addPiece(new Knight(this, 1, 7, true));
        this.addPiece(new Bishop(this, 2, 7, true));
        this.addPiece(new Queen(this, 3, 7, true));
        this.addPiece(new King(this, 4, 7, true));
        this.addPiece(new Bishop(this, 5, 7, true));
        this.addPiece(new Knight(this, 6, 7, true));
        this.addPiece(new Rook(this, 7, 7, true));

        this.addPiece(new Pawn(this, 0, 6, true));
        this.addPiece(new Pawn(this, 1, 6, true));
        this.addPiece(new Pawn(this, 2, 6, true));
        this.addPiece(new Pawn(this, 3, 6, true));
        this.addPiece(new Pawn(this, 4, 6, true));
        this.addPiece(new Pawn(this, 5, 6, true));
        this.addPiece(new Pawn(this, 6, 6, true));
        this.addPiece(new Pawn(this, 7, 6, true));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g2d.setColor((i+j) % 2 == 0 ? this.whitesqr : this.blacksqr);
                g2d.fillRect(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE);
            }
        }

        if (selectedPiece != null) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {

                    if (isValidMove(new Move(this, selectedPiece, j, i))) {

                        g.setColor(validMoveColor);
//                        g.fillRect(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE);
                        g.fillRoundRect(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE, 5, 5);
                    }

                }
            }
        }

        for (Piece p : pieces) {
            p.paint(g2d);
        }

    }

    public boolean isValidMove(Move move) {

        if(sameTeam(move.piece, move.capture)){
            return false;
        }

        if(!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }

        if(move.piece.moveCollideWithPiece(move.newCol, move.newRow)){
            return false;
        }

        if(checkScanner.isKingChecked(move)){
            return false;
        }

        return true;
    }

    public void makeMove(Move move) {

        if(move.piece.getName().equals("Pawn")){
            movePawn(move);
        } else if (move.piece.getName().equals("King")) {

            moveKing(move);

        }

            move.piece.setCol(move.newCol);
            move.piece.setRow(move.newRow);
            move.piece.setxPos(move.newCol * TILESIZE);
            move.piece.setyPos(move.newRow * TILESIZE);

            move.piece.isFirstMove = false;

            capture(move.capture);

    }

    private void movePawn(Move move) {

        int colorIdx = move.piece.isWhite() ? 1 : -1;

        if(getTileNumber(move.newCol, move.newRow) == getEnPassantTile){
            move.capture = getPieceAt(move.newCol, move.newRow + colorIdx);
        }

        if(Math.abs(move.piece.getRow() - move.newRow) == 2) {
            getEnPassantTile = getTileNumber(move.newCol, move.newRow + colorIdx);
        } else {
            getEnPassantTile = -1;
        }

        colorIdx = move.piece.isWhite() ? 0 : 7;

        if(move.newRow == colorIdx){
            promotePawn(move);
        }


    }

    private void moveKing(Move move) {

        if(Math.abs(move.piece.getCol() - move.newCol) == 2) {

            Piece rook;
            if(move.piece.getCol() < move.newCol){
                rook = getPieceAt(7, move.piece.getRow());
                rook.setCol(5);
            } else {
                rook = getPieceAt(0, move.piece.getRow());
                rook.setCol(3);
            }

            rook.setxPos(rook.getCol() * TILESIZE);

        }

    }

    private void promotePawn(Move move) {
        pieces.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite()));

        capture(move.piece);
    }

    private void capture(Piece p) {
        pieces.remove(p);
    }

    protected boolean sameTeam(Piece p1, Piece p2) {
        if(p1 == null || p2 == null){
            return false;
        }

        return p1.isWhite() == p2.isWhite();
    }

    public int getGetEnPassantTile() {
        return getEnPassantTile;
    }
}
