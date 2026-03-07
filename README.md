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
- Allows captures and prevents capturing your own pieces.

## Current Rules Implemented

- Piece-specific movement rules are **not** implemented yet.
- Turn order is **not** enforced.
- Check/checkmate, castling, promotion, and en passant are **not** implemented.
- A move is currently considered valid as long as it does not capture a same-color piece.

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
- If the destination contains a same-color piece, the move is rejected and the piece snaps back.

## Assets

- `src/main/resources/pieces.png`
- `src/main/resources/Chess_Pieces_Sprite.svg`

## Next Improvements

- Enforce alternating turns.
- Implement special moves (castling, en passant, promotion).

## TODO

- Fix pawn check handling: when the king is in check by a pawn, allow king captures when the capture square is legal and safe (not only running away).
- Review and fix other gameplay/rules bugs found during move-validation and check-detection testing.
- Add GUI promotion selection (let the player choose promoted piece type instead of auto-promoting).
