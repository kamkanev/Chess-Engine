package org.example.save;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSave implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String gameName;
    private final int whiteSquareRgb;
    private final int blackSquareRgb;
    private final boolean rotateBoardEveryTurn;
    private final boolean whiteMove;
    private final boolean gameOver;
    private final int enPassantTile;
    private final List<SavedPiece> pieces;

    public GameSave(String gameName, int whiteSquareRgb, int blackSquareRgb, boolean rotateBoardEveryTurn,
                    boolean whiteMove, boolean gameOver, int enPassantTile, List<SavedPiece> pieces) {
        this.gameName = gameName;
        this.whiteSquareRgb = whiteSquareRgb;
        this.blackSquareRgb = blackSquareRgb;
        this.rotateBoardEveryTurn = rotateBoardEveryTurn;
        this.whiteMove = whiteMove;
        this.gameOver = gameOver;
        this.enPassantTile = enPassantTile;
        this.pieces = new ArrayList<>(pieces);
    }

    public String getGameName() {
        return gameName;
    }

    public int getWhiteSquareRgb() {
        return whiteSquareRgb;
    }

    public int getBlackSquareRgb() {
        return blackSquareRgb;
    }

    public boolean isRotateBoardEveryTurn() {
        return rotateBoardEveryTurn;
    }

    public boolean isWhiteMove() {
        return whiteMove;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getEnPassantTile() {
        return enPassantTile;
    }

    public List<SavedPiece> getPieces() {
        return new ArrayList<>(pieces);
    }

    public static class SavedPiece implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String name;
        private final int col;
        private final int row;
        private final boolean white;
        private final boolean firstMove;

        public SavedPiece(String name, int col, int row, boolean white, boolean firstMove) {
            this.name = name;
            this.col = col;
            this.row = row;
            this.white = white;
            this.firstMove = firstMove;
        }

        public String getName() {
            return name;
        }

        public int getCol() {
            return col;
        }

        public int getRow() {
            return row;
        }

        public boolean isWhite() {
            return white;
        }

        public boolean isFirstMove() {
            return firstMove;
        }
    }
}
