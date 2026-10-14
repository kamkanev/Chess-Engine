# Chess Engine

A Java Swing chess board project with draggable pieces and basic capture handling.

## Tech Stack

- Java 11
- Maven
- Swing (GUI)

## What It Does

- Renders an 8x8 chess board.
- Loads piece sprites from `src/main/resources/pieces.png`.
- Sets up a standard starting position for both sides.
- Supports drag-and-drop piece movement with mouse input.
- Enforces turn order and prevents capturing your own pieces.
- Validates legal moves per piece, including path collision checks.
- Handles check, checkmate, and stalemate detection.
- Supports castling, en passant, and automatic pawn promotion to queen.

## Current Rules Implemented

- Legal movement rules per piece, including collision checks for sliding pieces.
- Turn order enforcement.
- Check, checkmate, and stalemate detection.
- Castling and en passant.
- Automatic pawn promotion to queen.

## Project Structure

```text
src/main/java/org/example
- Main.java           # App entry point (creates Swing window)
- Board.java          # Board state, rendering, and move application
- Input.java          # Mouse interactions (press/drag/release)
- Move.java           # Move data model

src/main/java/org/example/pieces
- Piece.java          # Base piece class and sprite drawing
- King/Queen/Rook/Bishop/Knight/Pawn.java
```

## Prerequisites

- JDK 11+
- Maven 3.8+

Check versions:

```bash
java -version
mvn -version
```

## Build

```bash
mvn clean package
```

## Test

```bash
mvn test
```

## Run

Option 1 (after build):

```bash
java -cp target/classes org.example.Main
```

Option 2 (IDE):

- Open the project in IntelliJ IDEA.
- Run `org.example.Main`.

## Controls

- Click a piece to select it.
- Drag to move it.
- Release mouse to drop.
- If the move is illegal, the piece snaps back.
- Click **Surrender** and confirm to concede. Against Stockfish, the human loses;
  in two-player mode, the side to move loses. Declining keeps the game going.
- The result screen shows the winner and offers **Back to Menu**. Surrender
  results are saved and shown again when the game is loaded.

## Assets

- `src/main/resources/pieces.png`
- `src/main/resources/Chess_Pieces_Sprite.svg`

## Next Improvements

- Add GUI promotion selection (let the player choose promoted piece type instead of auto-promoting).
- Expand test coverage for edge cases and regressions.

## TODO

- Review and fix other gameplay/rules bugs found during move-validation and check-detection testing.
- Add GUI promotion selection (let the player choose promoted piece type instead of auto-promoting).

## Play against Stockfish

Download and extract a Stockfish executable for your operating system from
https://stockfishchess.org/download/ (on Linux, ensure the file is executable).
Run the app, select **1 Player**, and enter the full path to the executable in
**Stockfish executable**. If Stockfish is on your PATH, `stockfish` is sufficient.
Choose a piece tile for **White** (white king and queen), **Black** (black king
and queen), or **Random** (white king and black queen), then choose a **Stockfish level** from
0 (easiest) to 20 (strongest). Stockfish thinks for about one second per move.
When you play Black, the board flips and Stockfish makes the opening move.
Random assigns your color once when the game starts.
**2 Players** continues to support local play.

The default executable can also be configured with the `STOCKFISH_PATH`
environment variable or the Java VM option `-Dstockfish.path=/path/to/stockfish`.
Saved games retain the opponent mode, assigned player color, level, and executable
path. Loading a computer game on Stockfish's turn requests its next move automatically. Leaving the board
cancels any pending engine search.

The app communicates using UCI, sends `board.getFenString()` as the current
position, and applies the returned move through the board's normal move handling.
Stockfish runs outside Swing's UI thread. The extracted Linux executable at
`stockfish/stockfish-linux-x86-64-universal` is detected automatically when you run
the app from the project directory. Explicit environment or VM settings take
precedence over this detection. Java starts the executable with `ProcessBuilder`,
sends the current FEN and selected UCI `Skill Level`, then reads `bestmove`; you
do not need to start Stockfish manually.

For tests in an environment without a display:

```bash
mvn -Djava.awt.headless=true test
```

## Delete saved games

In **Load Game**, each saved game has its own **Delete** button. Confirm to
permanently remove that game; the list refreshes immediately. Declining or
closing the confirmation keeps the save.
