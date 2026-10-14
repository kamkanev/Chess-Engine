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

    private String fenStartingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private boolean playAgainstStockfish;
    private boolean humanPlaysWhite = true;
    private int stockfishLevel = 20;
    private String stockfishExecutable = StockfishEngine.defaultExecutable();
    private SwingWorker<String, Void> engineWorker;
    private long positionVersion;

    private int halfmoveClock = 0;
    private int fullmoveNumber = 1;

    private boolean isWhiteMove = true;
    private boolean isGameOver = false;
    private Boolean surrenderedWhite;
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
        this.surrenderedWhite = gameSave.getSurrenderedWhite();
        this.getEnPassantTile = gameSave.getEnPassantTile();
        this.halfmoveClock = gameSave.getHalfmoveClock();
        this.fullmoveNumber = gameSave.getFullmoveNumber();
        this.playAgainstStockfish = gameSave.isPlayAgainstStockfish();
        this.humanPlaysWhite = gameSave.isHumanPlaysWhite();
        this.stockfishLevel = gameSave.getStockfishLevel();
        if (gameSave.getStockfishExecutable() != null) this.stockfishExecutable = gameSave.getStockfishExecutable();

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
//            this.setBoard();
            loadPositionFromFEN(fenStartingPosition);
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

    public void loadPositionFromFEN(String fenString){
        cancelEngineMove();
        positionVersion++;
        String[] parts = fenString.trim().split("\\s+");
        if (parts.length != 6) {
            throw new IllegalArgumentException("FEN must contain six fields");
        }
        int loadedHalfmoveClock = Integer.parseInt(parts[4]);
        int loadedFullmoveNumber = Integer.parseInt(parts[5]);
        if (loadedHalfmoveClock < 0 || loadedFullmoveNumber < 1) {
            throw new IllegalArgumentException("Invalid FEN move counters");
        }
        pieces.clear();
        selectedPiece = null;
        halfmoveClock = loadedHalfmoveClock;
        fullmoveNumber = loadedFullmoveNumber;
        isGameOver = halfmoveClock >= 100;
        surrenderedWhite = null;

        //set board
        String position = parts[0];
        int row = 0;
        int col = 0;
        for (int i = 0; i < position.length(); i++){
            char ch = position.charAt(i);
            if(ch == '/'){
                row++;
                col = 0;
            }else if(Character.isDigit(ch)){
                col += Character.getNumericValue(ch);
            }else{
                boolean isWhite = Character.isUpperCase(ch);
                char pieceChar = Character.toLowerCase(ch);

                switch (pieceChar){
                    case 'r':
                        pieces.add(new Rook(this, col, row, isWhite));
                        break;
                    case 'n':
                        pieces.add(new Knight(this, col, row, isWhite));
                        break;
                    case 'b':
                        pieces.add(new Bishop(this, col, row, isWhite));
                        break;
                    case 'q':
                        pieces.add(new Queen(this, col, row, isWhite));
                        break;
                    case 'k':
                        pieces.add(new King(this, col, row, isWhite));
                        break;
                    case 'p':
                        pieces.add(new Pawn(this, col, row, isWhite));
                        break;
                }
                col++;
            }
        }

        //whose move
        isWhiteMove = parts[1].equals("w");

        // FEN castling rights are the only evidence that a king or rook has not moved.
        for (Piece piece : pieces) {
            piece.isFirstMove = piece instanceof Pawn && piece.getRow() == (piece.isWhite() ? 6 : 1);
        }
        Piece whiteKing = getPieceAt(4, 7);
        if (whiteKing instanceof King && whiteKing.isWhite()) {
            whiteKing.isFirstMove = parts[2].contains("K") || parts[2].contains("Q");
        }
        Piece blackKing = getPieceAt(4, 0);
        if (blackKing instanceof King && !blackKing.isWhite()) {
            blackKing.isFirstMove = parts[2].contains("k") || parts[2].contains("q");
        }

        //castling
        Piece bqr = getPieceAt(0, 0);
        if(bqr instanceof Rook){
            bqr.isFirstMove = parts[2].contains("q");
        }

        Piece bkr = getPieceAt(7, 0);
        if(bkr instanceof Rook){
            bkr.isFirstMove = parts[2].contains("k");
        }

        Piece wqr = getPieceAt(0, 7);
        if(wqr instanceof Rook){
            wqr.isFirstMove = parts[2].contains("Q");
        }

        Piece wkr = getPieceAt(7, 7);
        if(wkr instanceof Rook){
            wkr.isFirstMove = parts[2].contains("K");
        }

        //en passant sq
        if(parts[3].equals("-")){
            getEnPassantTile = -1;
        }else{
            getEnPassantTile = (7 - (parts[3].charAt(1) - '1')) * 8 + (parts[3].charAt(0) - 'a');
        }

    }
    /** Returns the current position in the six-field format accepted by chess engines. */
    public String getFenString() {
        StringBuilder fen = new StringBuilder();
        for (int row = 0; row < rows; row++) {
            int empty = 0;
            for (int col = 0; col < cols; col++) {
                Piece piece = getPieceAt(col, row);
                if (piece == null) {
                    empty++;
                } else {
                    if (empty > 0) {
                        fen.append(empty);
                        empty = 0;
                    }
                    char symbol = piece.getName().equals("Knight") ? 'n'
                            : Character.toLowerCase(piece.getName().charAt(0));
                    fen.append(piece.isWhite() ? Character.toUpperCase(symbol) : symbol);
                }
            }
            if (empty > 0) fen.append(empty);
            if (row < rows - 1) fen.append('/');
        }
        fen.append(isWhiteMove ? " w " : " b ");
        StringBuilder castling = new StringBuilder();
        if (hasCastlingRight(true, 7)) castling.append('K');
        if (hasCastlingRight(true, 0)) castling.append('Q');
        if (hasCastlingRight(false, 7)) castling.append('k');
        if (hasCastlingRight(false, 0)) castling.append('q');
        fen.append(castling.length() == 0 ? "-" : castling).append(' ');
        if (getEnPassantTile == -1) {
            fen.append('-');
        } else {
            fen.append((char) ('a' + getEnPassantTile % 8));
            fen.append(8 - getEnPassantTile / 8);
        }
        return fen.append(' ').append(halfmoveClock).append(' ').append(fullmoveNumber).toString();
    }

    private boolean hasCastlingRight(boolean white, int rookCol) {
        int row = white ? 7 : 0;
        Piece king = getPieceAt(4, row);
        Piece rook = getPieceAt(rookCol, row);
        return king instanceof King && king.isWhite() == white && king.isFirstMove
                && rook instanceof Rook && rook.isWhite() == white && rook.isFirstMove;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public String getSurrenderingPlayerName() {
        boolean white = playAgainstStockfish ? humanPlaysWhite : isWhiteMove;
        return white ? "White" : "Black";
    }

    public Boolean getSurrenderedWhite() {
        return surrenderedWhite;
    }

    /** The human resigns against Stockfish; otherwise the side to move resigns. */
    public boolean surrender() {
        if (isGameOver) return false;
        surrenderedWhite = playAgainstStockfish ? humanPlaysWhite : isWhiteMove;
        isGameOver = true;
        positionVersion++;
        cancelEngineMove();
        selectedPiece = null;
        saveGame();
        repaint();
        return true;
    }

    public String getSurrenderResultTitle() {
        if (surrenderedWhite == null) throw new IllegalStateException("No surrender result");
        return playAgainstStockfish ? "You lost — Stockfish wins"
                : (surrenderedWhite ? "Black wins" : "White wins");
    }

    public String getSurrenderResultDetail() {
        if (surrenderedWhite == null) throw new IllegalStateException("No surrender result");
        return (surrenderedWhite ? "White" : "Black") + " surrendered.";
    }

    public void enableStockfish(String executable) {
        enableStockfish(executable, true, 20);
    }

    public void enableStockfish(String executable, boolean humanPlaysWhite, int level) {
        if (level < 0 || level > 20) throw new IllegalArgumentException("Stockfish level must be 0–20");
        cancelEngineMove();
        positionVersion++;
        this.humanPlaysWhite = humanPlaysWhite;
        this.stockfishLevel = level;
        playAgainstStockfish = true;
        stockfishExecutable = executable;
        saveGame();
        requestEngineMove();
    }

    public boolean canHumanMove() {
        return !isGameOver && (!playAgainstStockfish || isWhiteMove == humanPlaysWhite);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestEngineMove();
    }

    @Override
    public void removeNotify() {
        cancelEngineMove();
        super.removeNotify();
    }

    private void cancelEngineMove() {
        if (engineWorker != null) {
            engineWorker.cancel(true);
            engineWorker = null;
        }
    }

    private void requestEngineMove() {
        if (!playAgainstStockfish || isWhiteMove == humanPlaysWhite || isGameOver || engineWorker != null || !isDisplayable()) return;
        String fen = getFenString();
        long version = positionVersion;
        String executable = stockfishExecutable;
        int level = stockfishLevel;
        engineWorker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return StockfishEngine.bestMove(executable, fen, level);
            }

            @Override
            protected void done() {
                if (engineWorker != this) return;
                engineWorker = null;
                if (isCancelled() || version != positionVersion || !isDisplayable()) return;
                try {
                    applyEngineMove(get());
                } catch (Exception error) {
                    Throwable cause = error.getCause() == null ? error : error.getCause();
                    JOptionPane.showMessageDialog(Board.this,
                            "Stockfish could not make a move: " + cause.getMessage()
                                    + "\nCheck the executable path and reload the game to retry.",
                            "Stockfish", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        engineWorker.execute();
    }

    void applyEngineMove(String uci) {
        if (uci == null || !uci.matches("[a-h][1-8][a-h][1-8][qrbn]?")) {
            throw new IllegalArgumentException("Invalid engine move: " + uci);
        }
        Piece piece = getPieceAt(uci.charAt(0) - 'a', 8 - (uci.charAt(1) - '0'));
        if (piece == null) throw new IllegalArgumentException("Engine move has no source piece");
        Move move = new Move(this, piece, uci.charAt(2) - 'a', 8 - (uci.charAt(3) - '0'));
        if (piece instanceof Pawn && (move.newRow == 0 || move.newRow == 7)) {
            if (uci.length() != 5) throw new IllegalArgumentException("Missing promotion piece");
            move.promotion = uci.charAt(4);
        } else if (uci.length() == 5) {
            throw new IllegalArgumentException("Unexpected promotion piece");
        }
        selectedPiece = piece;
        try {
            if (!isValidMove(move)) throw new IllegalArgumentException("Illegal engine move: " + uci);
            makeMove(move);
        } finally {
            selectedPiece = null;
            repaint();
        }
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

        paintCheckGlow(g2d, true);
        paintCheckGlow(g2d, false);

        for (Piece p : pieces) {
            if (p == selectedPiece) {
                p.paint(g2d);
            } else {
                g2d.drawImage(p.getSprite(), getVisualX(p.getCol()), getVisualY(p.getRow()), null);
            }
        }

    }

    private void paintCheckGlow(Graphics2D graphics, boolean white) {
        Piece king = findKing(white);
        if (king == null || !checkScanner.isKingInCheck(white)) return;
        float centerX = getVisualX(king.getCol()) + TILESIZE / 2f;
        float centerY = getVisualY(king.getRow()) + TILESIZE / 2f;
        float radius = TILESIZE * 0.6f;
        Graphics2D glow = (Graphics2D) graphics.create();
        try {
            glow.setPaint(new RadialGradientPaint(centerX, centerY, radius,
                    new float[]{0f, 0.55f, 1f},
                    new Color[]{new Color(235, 35, 35, 200), new Color(235, 35, 35, 130),
                            new Color(235, 35, 35, 0)}));
            glow.fillRect((int) (centerX - radius), (int) (centerY - radius),
                    (int) Math.ceil(radius * 2), (int) Math.ceil(radius * 2));
        } finally {
            glow.dispose();
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
        return playAgainstStockfish ? !humanPlaysWhite : rotateBoardEveryTurn && !isWhiteMove;
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

        if (isGameOver) return;
        boolean pawnMove = move.piece instanceof Pawn;

        if(move.piece.getName().equals("Pawn")){
            movePawn(move);
        }else{
            getEnPassantTile = -1;
        }

        if (move.piece.getName().equals("King")) {
            moveKing(move);
        }

            move.piece.setCol(move.newCol);
            move.piece.setRow(move.newRow);
            move.piece.setxPos(move.newCol * TILESIZE);
            move.piece.setyPos(move.newRow * TILESIZE);

            move.piece.isFirstMove = false;

            capture(move.capture);

            halfmoveClock = pawnMove || move.capture != null ? 0 : halfmoveClock + 1;
            if (!isWhiteMove) fullmoveNumber++;
            isWhiteMove = !isWhiteMove;

            updateGameState();
            saveGame();
            positionVersion++;
            requestEngineMove();

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
        } else if (halfmoveClock >= 100) {
            System.out.println("Draw by fifty-move rule");
            isGameOver = true;
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
            rook.isFirstMove = false;

        }

    }

    private void promotePawn(Move move) {
        Piece promotedPiece;
        if (move.promotion != '\0') {
            String name;
            switch (move.promotion) {
                case 'q': name = "Queen"; break;
                case 'r': name = "Rook"; break;
                case 'b': name = "Bishop"; break;
                case 'n': name = "Knight"; break;
                default: throw new IllegalArgumentException("Invalid promotion piece");
            }
            promotedPiece = createPiece(name, move.newCol, move.newRow, move.piece.isWhite());
        } else {
            promotedPiece = choosePromotionPiece(move);
        }
        promotedPiece.isFirstMove = false;
        pieces.add(promotedPiece);

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
                isWhiteMove, isGameOver, getEnPassantTile, savedPieces, halfmoveClock, fullmoveNumber, playAgainstStockfish, stockfishExecutable, humanPlaysWhite, stockfishLevel, surrenderedWhite), saveFile);
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
