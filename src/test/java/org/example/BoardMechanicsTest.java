package org.example;

import org.example.pieces.King;
import org.example.pieces.Knight;
import org.example.pieces.Pawn;
import org.example.pieces.Queen;
import org.example.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardMechanicsTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.getPieces().clear();
        board.selectedPiece = null;
        setWhiteToMove(true);
    }

    @Test
    void pawnCanMoveOneOrTwoSquaresOnFirstMove() {
        addKings();
        Pawn pawn = new Pawn(board, 4, 6, true);
        board.addPiece(pawn);

        assertTrue(board.isValidMove(new Move(board, pawn, 4, 5)));
        assertTrue(board.isValidMove(new Move(board, pawn, 4, 4)));
    }

    @Test
    void rookBlockedByPieceIsInvalid() {
        addKings();
        Rook rook = new Rook(board, 0, 7, true);
        Pawn blocker = new Pawn(board, 0, 6, true);
        board.addPiece(rook);
        board.addPiece(blocker);

        assertFalse(board.isValidMove(new Move(board, rook, 0, 5)));
    }

    @Test
    void knightCanJumpOverPieces() {
        addKings();
        Knight knight = new Knight(board, 1, 7, true);
        Pawn blocker = new Pawn(board, 1, 6, true);
        board.addPiece(knight);
        board.addPiece(blocker);

        assertTrue(board.isValidMove(new Move(board, knight, 2, 5)));
        assertTrue(board.isValidMove(new Move(board, knight, 0, 5)));
    }

    @Test
    void enPassantCaptureIsValidRightAfterDoubleMove() {
        addKings();
        Pawn whitePawn = new Pawn(board, 4, 3, true);
        Pawn blackPawn = new Pawn(board, 3, 1, false);
        board.addPiece(whitePawn);
        board.addPiece(blackPawn);

        setWhiteToMove(false);
        Move blackAdvance = new Move(board, blackPawn, 3, 3);
        assertTrue(board.isValidMove(blackAdvance));
        board.makeMove(blackAdvance);

        Move enPassant = new Move(board, whitePawn, 3, 2);
        assertTrue(board.isValidMove(enPassant));
    }

    @Test
    void checkmateIsDetectedByGameOverAndInCheck() {
        King blackKing = new King(board, 0, 0, false);
        Queen whiteQueen = new Queen(board, 1, 1, true);
        King whiteKing = new King(board, 2, 2, true);
        board.addPiece(blackKing);
        board.addPiece(whiteQueen);
        board.addPiece(whiteKing);

        setWhiteToMove(false);

        assertTrue(board.checkScanner.isGameOver(blackKing));
        assertTrue(board.checkScanner.isKingChecked(new Move(board, blackKing, 0, 0)));
    }

    @Test
    void stalemateIsDetectedByGameOverButNotInCheck() {
        King blackKing = new King(board, 0, 0, false);
        Queen whiteQueen = new Queen(board, 1, 2, true);
        King whiteKing = new King(board, 2, 2, true);
        board.addPiece(blackKing);
        board.addPiece(whiteQueen);
        board.addPiece(whiteKing);

        setWhiteToMove(false);

        assertTrue(board.checkScanner.isGameOver(blackKing));
        assertFalse(board.checkScanner.isKingChecked(new Move(board, blackKing, 0, 0)));
    }

    private void setWhiteToMove(boolean whiteToMove) {
        try {
            Field field = Board.class.getDeclaredField("isWhiteMove");
            field.setAccessible(true);
            field.setBoolean(board, whiteToMove);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to set isWhiteMove via reflection", e);
        }
    }

    private void addKings() {
        board.addPiece(new King(board, 4, 7, true));
        board.addPiece(new King(board, 4, 0, false));
    }
}
