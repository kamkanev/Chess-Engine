package org.example;

import org.example.pieces.*;
import org.example.save.GameSave;
import org.example.save.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board extends JPanel {

    public final int TILESIZE = 85;
    public static final Color DEFAULT_WHITE_SQUARE = new Color(255, 255, 223);
    public static final Color DEFAULT_BLACK_SQUARE = new Color(133, 167, 101);

    private Color whitesqr = DEFAULT_WHITE_SQUARE;
    private Color blacksqr = DEFAULT_BLACK_SQUARE;
    private Color validMoveColor = new Color(59, 154, 26, 168);

    private int cols = 8;
    private int rows = 8;

    private ArrayList<Piece> pieces = new ArrayList<>();

    public Piece selectedPiece;

    private int getEnPassantTile = -1;

    private boolean isWhiteMove = true;
    private boolean isGameOver = false;
    private boolean rotateBoardEveryTurn = false;
    private String gameName;
    private File saveFile;

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public Board() {
        this(DEFAULT_WHITE_SQUARE, DEFAULT_BLACK_SQUARE);
    }

    public Board(Color whitesqr, Color blacksqr) {
        this(whitesqr, blacksqr, false);
    }

    public Board(Color whitesqr, Color blacksqr, boolean rotateBoardEveryTurn) {
        this(whitesqr, blacksqr, rotateBoardEveryTurn, null, null, true);
    }

    public Board(Color whitesqr, Color blacksqr, boolean rotateBoardEveryTurn, String gameName, File saveFile) {
        this(whitesqr, blacksqr, rotateBoardEveryTurn, gameName, saveFile, true);
        saveGame();
    }

    public Board(GameSave gameSave, File saveFile) {
        this(new Color(gameSave.getWhiteSquareRgb()), new Color(gameSave.getBlackSquareRgb()),
                gameSave.isRotateBoardEveryTurn(), gameSave.getGameName(), saveFile, false);
        this.isWhiteMove = gameSave.isWhiteMove();
        this.isGameOver = gameSave.isGameOver();
        this.getEnPassantTile = gameSave.getEnPassantTile();

        for (GameSave.SavedPiece savedPiece : gameSave.getPieces()) {
            Piece piece = createPiece(savedPiece.getName(), savedPiece.getCol(), savedPiece.getRow(), savedPiece.isWhite());
            piece.isFirstMove = savedPiece.isFirstMove();
            pieces.add(piece);
        }
    }

    private Board(Color whitesqr, Color blacksqr, boolean rotateBoardEveryTurn, String gameName, File saveFile, boolean setupStartingPosition) {
        this.whitesqr = whitesqr;
        this.blacksqr = blacksqr;
        this.rotateBoardEveryTurn = rotateBoardEveryTurn;
        this.gameName = gameName;
        this.saveFile = saveFile;
        this.setPreferredSize(new Dimension(cols * TILESIZE, rows * TILESIZE));

        addMouseListener(input);
        addMouseMotionListener(input);

        if (setupStartingPosition) {
            this.setBoard();
        }
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
                g2d.fillRect(getVisualX(j), getVisualY(i), TILESIZE, TILESIZE);
            }
        }

        if (selectedPiece != null) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {

                    if (isValidMove(new Move(this, selectedPiece, j, i))) {

                        g.setColor(validMoveColor);
//                        g.fillRect(j * TILESIZE, i * TILESIZE, TILESIZE, TILESIZE);
                        g.fillRoundRect(getVisualX(j), getVisualY(i), TILESIZE, TILESIZE, 5, 5);
                    }

                }
            }
        }

        for (Piece p : pieces) {
            if (p == selectedPiece) {
                p.paint(g2d);
            } else {
                g2d.drawImage(p.getSprite(), getVisualX(p.getCol()), getVisualY(p.getRow()), null);
            }
        }

    }

    public int getBoardColFromMouseX(int mouseX) {
        int col = mouseX / TILESIZE;
        return isBoardFlipped() ? cols - 1 - col : col;
    }

    public int getBoardRowFromMouseY(int mouseY) {
        int row = mouseY / TILESIZE;
        return isBoardFlipped() ? rows - 1 - row : row;
    }

    public int getVisualX(int col) {
        return (isBoardFlipped() ? cols - 1 - col : col) * TILESIZE;
    }

    public int getVisualY(int row) {
        return (isBoardFlipped() ? rows - 1 - row : row) * TILESIZE;
    }

    private boolean isBoardFlipped() {
        return rotateBoardEveryTurn && !isWhiteMove;
    }

    public boolean isValidMove(Move move) {

        if(isGameOver){
            return false;
        }

        if (move.piece.isWhite() != isWhiteMove) {
            return false;
        }

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

            isWhiteMove = !isWhiteMove;

            updateGameState();
            saveGame();

    }

    private void updateGameState() {

        Piece king = findKing(isWhiteMove);

        if(checkScanner.isGameOver(king)){
            if(checkScanner.isKingChecked(new Move(this, king, king.getCol(), king.getRow()))) {
                System.out.println(isWhiteMove ? "Black wins" : "White wins");
                isGameOver = true;
            }else{
                System.out.println("Stalemate");
                isGameOver = true;
            }
        } else if(insuffiantMaterial(true) && insuffiantMaterial(false)) {
            System.out.println("Stalemate");
            System.out.println("Insufficient Material");
            isGameOver = true;
        }
    }

    private boolean insuffiantMaterial(boolean isWhite){
        ArrayList<String> names = pieces.stream()
                .filter(p -> p.isWhite() == isWhite)
                .map(p -> p.getName())
                .collect(Collectors.toCollection(ArrayList::new));

        if(names.contains("Queen") || names.contains("Rook") || names.contains("Pawn")){
            return false;
        }

        return names.size() < 3;
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
        pieces.add(choosePromotionPiece(move));

        capture(move.piece);
    }

    private Piece choosePromotionPiece(Move move) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Promote pawn", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel panel = new JPanel(new GridLayout(1, 4, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        Piece[] choices = {
                new Queen(this, move.newCol, move.newRow, move.piece.isWhite()),
                new Knight(this, move.newCol, move.newRow, move.piece.isWhite()),
                new Bishop(this, move.newCol, move.newRow, move.piece.isWhite()),
                new Rook(this, move.newCol, move.newRow, move.piece.isWhite())
        };

        final Piece[] selectedPromotion = {choices[0]};

        for (Piece choice : choices) {
            JButton button = new JButton(new ImageIcon(choice.getSprite()));
            button.setToolTipText(choice.getName());
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            button.setBackground(Color.WHITE);
            button.addActionListener(e -> {
                selectedPromotion[0] = choice;
                dialog.dispose();
            });
            panel.add(button);
        }

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setUndecorated(true);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return selectedPromotion[0];
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

    public ArrayList<Piece> getPieces(){
        return pieces;
    }

    public void saveGame() {
        if (saveFile == null || gameName == null) {
            return;
        }

        List<GameSave.SavedPiece> savedPieces = pieces.stream()
                .map(piece -> new GameSave.SavedPiece(piece.getName(), piece.getCol(), piece.getRow(), piece.isWhite(), piece.isFirstMove))
                .collect(Collectors.toList());

        SaveManager.save(new GameSave(gameName, whitesqr.getRGB(), blacksqr.getRGB(), rotateBoardEveryTurn,
                isWhiteMove, isGameOver, getEnPassantTile, savedPieces), saveFile);
    }

    private Piece createPiece(String name, int col, int row, boolean white) {
        switch (name) {
            case "King":
                return new King(this, col, row, white);
            case "Queen":
                return new Queen(this, col, row, white);
            case "Rook":
                return new Rook(this, col, row, white);
            case "Bishop":
                return new Bishop(this, col, row, white);
            case "Knight":
                return new Knight(this, col, row, white);
            case "Pawn":
                return new Pawn(this, col, row, white);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + name);
        }
    }

    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }

    public int getGetEnPassantTile() {
        return getEnPassantTile;
    }
}
