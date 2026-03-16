package org.example;

import org.example.pieces.Piece;

public class CheckScanner {

    private Board board;

    public CheckScanner(Board board) {
        this.board = board;
    }

    public boolean isKingChecked(Move move){

        Piece king = board.findKing(move.piece.isWhite());
        assert king != null;

        int kingCol = king.getCol();
        int kingRow = king.getRow();

        if(board.selectedPiece != null && board.selectedPiece.getName().equals("King")){
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        return hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) ||
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) ||
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) ||
                hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByKing(king, kingCol, kingRow);

    }

    private boolean hitByRook(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal){

        for(int i = 1; i < 8; i++) {
            if (kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row) {
                break;
            }


            Piece piece = board.getPieceAt(kingCol + (i * colVal), kingRow + (i * rowVal));
            if (piece != null && piece != board.selectedPiece) {
                if(!board.sameTeam(piece, king) && (piece.getName().equals("Rook") || piece.getName().equals("Queen"))){
                    return true;
                }
                break;
            }
        }

        return false;
    }

    private boolean hitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal){

        for(int i = 1; i < 8; i++) {
            if (kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row) {
                break;
            }


            Piece piece = board.getPieceAt(kingCol - (i * colVal), kingRow - (i * rowVal));
            if (piece != null && piece != board.selectedPiece) {
                if(!board.sameTeam(piece, king) && (piece.getName().equals("Bishop") || piece.getName().equals("Queen"))){
                    return true;
                }
                break;
            }
        }

        return false;
    }

    private boolean hitByKnight(int col, int row, Piece king, int kingCol, int  kingRow){
        return checkKnight(board.getPieceAt(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPieceAt(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPieceAt(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPieceAt(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPieceAt(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPieceAt(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPieceAt(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPieceAt(kingCol - 2, kingRow - 1), king, col, row);

    }

    private boolean checkKnight(Piece p, Piece k, int col, int row){
        return p != null && !board.sameTeam(p, k) && p.getName().equals("Knight") && !(p.getCol() == col && p.getRow() == row);
    }

    private boolean hitByKing(Piece king, int kingCol, int kingRow){
        return checkKing(board.getPieceAt(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPieceAt(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPieceAt(kingCol, kingRow - 1), king) ||
                checkKing(board.getPieceAt(kingCol - 1, kingRow), king) ||
                checkKing(board.getPieceAt(kingCol + 1, kingRow), king) ||
                checkKing(board.getPieceAt(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPieceAt(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPieceAt(kingCol, kingRow + 1), king);

    }

    private boolean checkKing(Piece p, Piece k){
        return p != null && !board.sameTeam(p, k) && p.getName().equals("King");
    }

    private boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow){
        int colorVal = king.isWhite() ? -1 : 1;
        return checkPawn(board.getPieceAt(kingCol + 1, kingRow + colorVal), king, col, row) ||
                checkPawn(board.getPieceAt(kingCol - 1, kingRow + colorVal), king, col, row);
    }

    private boolean checkPawn(Piece p, Piece k, int col, int row){
        return p != null && !board.sameTeam(p, k) && p.getName().equals("Pawn") && !(p.getCol() == col && p.getRow() == row);
    }

}
