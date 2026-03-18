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

## Assets

- `src/main/resources/pieces.png`
- `src/main/resources/Chess_Pieces_Sprite.svg`

## Next Improvements

- Add GUI promotion selection (let the player choose promoted piece type instead of auto-promoting).
- Expand test coverage for edge cases and regressions.

## TODO

- Review and fix other gameplay/rules bugs found during move-validation and check-detection testing.
- Add GUI promotion selection (let the player choose promoted piece type instead of auto-promoting).
