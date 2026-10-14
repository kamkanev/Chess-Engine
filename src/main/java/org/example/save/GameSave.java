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
    private final Boolean surrenderedWhite;
    private final Boolean humanPlaysWhite;
    private final Integer stockfishLevel;
    private final boolean playAgainstStockfish;
    private final String stockfishExecutable;
    private final int halfmoveClock;
    private final int fullmoveNumber;

    public GameSave(String gameName, int whiteSquareRgb, int blackSquareRgb, boolean rotateBoardEveryTurn,
                    boolean whiteMove, boolean gameOver, int enPassantTile, List<SavedPiece> pieces) {
        this(gameName, whiteSquareRgb, blackSquareRgb, rotateBoardEveryTurn, whiteMove, gameOver,
                enPassantTile, pieces, 0, 1);
    }

    public GameSave(String gameName, int whiteSquareRgb, int blackSquareRgb, boolean rotateBoardEveryTurn,
                    boolean whiteMove, boolean gameOver, int enPassantTile, List<SavedPiece> pieces,
                    int halfmoveClock, int fullmoveNumber) {
        this(gameName, whiteSquareRgb, blackSquareRgb, rotateBoardEveryTurn, whiteMove, gameOver,
                enPassantTile, pieces, halfmoveClock, fullmoveNumber, false, null);
    }

    public GameSave(String gameName, int whiteSquareRgb, int blackSquareRgb, boolean rotateBoardEveryTurn,
                    boolean whiteMove, boolean gameOver, int enPassantTile, List<SavedPiece> pieces,
                    int halfmoveClock, int fullmoveNumber, boolean playAgainstStockfish, String stockfishExecutable) {
        this(gameName, whiteSquareRgb, blackSquareRgb, rotateBoardEveryTurn, whiteMove, gameOver,
                enPassantTile, pieces, halfmoveClock, fullmoveNumber, playAgainstStockfish, stockfishExecutable, true, 20);
    }

    public GameSave(String gameName, int whiteSquareRgb, int blackSquareRgb, boolean rotateBoardEveryTurn,
                    boolean whiteMove, boolean gameOver, int enPassantTile, List<SavedPiece> pieces,
                    int halfmoveClock, int fullmoveNumber, boolean playAgainstStockfish, String stockfishExecutable,
                    boolean humanPlaysWhite, int stockfishLevel) {
        this(gameName, whiteSquareRgb, blackSquareRgb, rotateBoardEveryTurn, whiteMove, gameOver,
                enPassantTile, pieces, halfmoveClock, fullmoveNumber, playAgainstStockfish, stockfishExecutable,
                humanPlaysWhite, stockfishLevel, null);
    }

    public GameSave(String gameName, int whiteSquareRgb, int blackSquareRgb, boolean rotateBoardEveryTurn,
                    boolean whiteMove, boolean gameOver, int enPassantTile, List<SavedPiece> pieces,
                    int halfmoveClock, int fullmoveNumber, boolean playAgainstStockfish, String stockfishExecutable,
                    boolean humanPlaysWhite, int stockfishLevel, Boolean surrenderedWhite) {
        this.surrenderedWhite = surrenderedWhite;
        this.humanPlaysWhite = humanPlaysWhite;
        this.stockfishLevel = stockfishLevel;
        this.playAgainstStockfish = playAgainstStockfish;
        this.stockfishExecutable = stockfishExecutable;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
        this.gameName = gameName;
        this.whiteSquareRgb = whiteSquareRgb;
        this.blackSquareRgb = blackSquareRgb;
        this.rotateBoardEveryTurn = rotateBoardEveryTurn;
        this.whiteMove = whiteMove;
        this.gameOver = gameOver;
        this.enPassantTile = enPassantTile;
        this.pieces = new ArrayList<>(pieces);
    }

    public Boolean getSurrenderedWhite() {
        return surrenderedWhite;
    }

    public boolean isHumanPlaysWhite() {
        // Missing fields in older saves preserve the original White / level 20 defaults.
        return humanPlaysWhite == null || humanPlaysWhite;
    }

    public int getStockfishLevel() {
        return stockfishLevel == null ? 20 : stockfishLevel;
    }

    public boolean isPlayAgainstStockfish() {
        return playAgainstStockfish;
    }

    public String getStockfishExecutable() {
        return stockfishExecutable;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int getFullmoveNumber() {
        // Older serialized saves have zero for this newly added field.
        return Math.max(1, fullmoveNumber);
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
