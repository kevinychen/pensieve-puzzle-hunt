package com.kyc.pensieve.server.penultima;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class BoardState {

    private final boolean playerWhite;
    private final char[][] grid;
    private final boolean playerTurnToMove;

    @JsonIgnore
    public Map<Move, BoardState> getMoves() {
        return getMoves(true);
    }

    private Map<Move, BoardState> getMoves(boolean validateCheck) {
        boolean whiteToPlay = playerWhite == playerTurnToMove;

        Map<Move, BoardState> moves = new HashMap<>();
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++) {
                char piece = grid[row][col];
                if (whiteToPlay && Character.isLowerCase(piece))
                    moves.putAll(getMoves(new Location(row, col), validateCheck));
                else if (!whiteToPlay && Character.isUpperCase(piece))
                    moves.putAll(getMoves(new Location(row, col), validateCheck));
            }
        return moves;
    }

    private Map<Move, BoardState> getMoves(Location start, boolean validateCheck) {
        boolean whiteToPlay = playerWhite == playerTurnToMove;

        Map<Move, BoardState> moves = getMovesWithoutValidatingCheck(start);
        if (validateCheck) {
            moves = ImmutableMap.copyOf(Maps.filterValues(
                moves,
                endState -> endState.getMoves(false)
                    .values()
                    .stream()
                    .allMatch(state -> state.find(whiteToPlay ? 'k' : 'K').isPresent())));
        }
        return moves;
    }

    private Map<Move, BoardState> getMovesWithoutValidatingCheck(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        for (int dRow = -1; dRow <= 1; dRow++)
            for (int dCol = -1; dCol <= 1; dCol++)
                if (isEnemyPiece(piece, start.getRow() + dRow, start.getCol() + dCol)
                        && grid[start.getRow() + dRow][start.getCol() + dCol] == 'Q') {
                    return ImmutableMap.of(); // immobilized
                }

        switch (piece) {
            case 'r': // Rook
                return getMovesInAllDirections(start, 0, 1, Integer.MAX_VALUE, true);
            case 'n': // Camel
                return getMovesInAllDirections(start, 1, 3, 1, true);
            case 'b': // Eagle
                return ImmutableMap.<Move, BoardState>builder()
                        .putAll(getMovesInDirection(start, -1, -1, Integer.MAX_VALUE, true))
                        .putAll(getMovesInDirection(start, -1, 1, Integer.MAX_VALUE, true))
                        .putAll(getMovesInAllDirections(start, 0, 1, 1, true))
                        .putAll(getMovesInDirection(start, 1, -1, 2, true))
                        .putAll(getMovesInDirection(start, 1, 1, 2, true))
                        .build();
            case 'q': // Teleporter
                return getMovesForTeleporter(start);
            case 'k': // King
                return ImmutableMap.<Move, BoardState>builder()
                        .putAll(getMovesInAllDirections(start, 0, 1, 1, true))
                        .putAll(getMovesInAllDirections(start, 1, 1, 1, true))
                        .build();
            case 'p': // Pawn
                return getMovesForPawn(start);
            case 'R': // Pao
                return getMovesForPao(start);
            case 'N': // Nightrider
                return getMovesInAllDirections(start, 1, 2, Integer.MAX_VALUE, true);
            case 'B': // Overtaker
                return getMovesForOvertaker(start);
            case 'Q': // Immobilizer
                return ImmutableMap.<Move, BoardState>builder()
                        .putAll(getMovesInAllDirections(start, 0, 1, Integer.MAX_VALUE, false))
                        .putAll(getMovesInAllDirections(start, 1, 1, Integer.MAX_VALUE, false))
                        .build();
            case 'K': // King
                return ImmutableMap.<Move, BoardState>builder()
                        .putAll(getMovesInAllDirections(start, 0, 1, 1, true))
                        .putAll(getMovesInAllDirections(start, 1, 1, 1, true))
                        .build();
            case 'P': // Pawn
                return getMovesForPawn(start);
            default:
                return ImmutableMap.of();
        }
    }

    private Map<Move, BoardState> getMovesInAllDirections(Location start, int dx, int dy, int limit, boolean canCapture) {
        Map<Move, BoardState> moves = new HashMap<>();
        for (int sign1 = -1; sign1 <= 1; sign1 += 2)
            for (int sign2 = -1; sign2 <= 1; sign2 += 2) {
                moves.putAll(getMovesInDirection(start, sign1 * dx, sign2 * dy, limit, canCapture));
                moves.putAll(getMovesInDirection(start, sign1 * dy, sign2 * dx, limit, canCapture));
            }
        return moves;
    }

    private Map<Move, BoardState> getMovesInDirection(Location start, int dRow, int dCol, int limit, boolean canCapture) {
        Map<Move, BoardState> moves = new HashMap<>();
        int row = start.getRow();
        int col = start.getCol();
        for (int i = 0; i < limit; i++) {
            row += dRow;
            col += dCol;
            if (!inBounds(row, col))
                break;
            if (grid[row][col] != ' ') {
                if (canCapture && Character.isUpperCase(grid[start.getRow()][start.getCol()]) != Character.isUpperCase(grid[row][col])) {
                    Move move = new Move(start, new Location(row, col));
                    moves.put(move, simpleMove(move, Optional.empty()));
                }
                break;
            }
            Move move = new Move(start, new Location(row, col));
            moves.put(move, simpleMove(move, Optional.empty()));
        }
        return moves;
    }

    private Map<Move, BoardState> getMovesForTeleporter(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        Map<Move, BoardState> moves = new HashMap<>();
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++) {
                if ((start.getRow() + start.getCol() + row + col) % 2 == 1 && grid[row][col] == ' '
                        || Math.abs(start.getRow() - row) + Math.abs(start.getCol() - col) == 1 && isEnemyPiece(piece, row, col)) {
                    Move move = new Move(start, new Location(row, col));
                    moves.put(move, simpleMove(move, Optional.empty()));
                }
            }
        return moves;
    }

    private Map<Move, BoardState> getMovesForPawn(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        int direction = Character.isLowerCase(piece) ? -1 : 1;
        Map<Move, BoardState> moves = new HashMap<>();
        moves.putAll(getMovesInDirection(start, direction, 0, 1, false));
        if (start.getRow() == (direction == 1 ? 1 : grid.length - 2)
                && inBounds(start.getRow() + direction, start.getCol())
                && grid[start.getRow() + direction][start.getCol()] == ' ') {
            moves.putAll(getMovesInDirection(start, direction, 0, 2, false));
        }
        for (int sideDirection = -1; sideDirection <= 1; sideDirection += 2)
            if (isEnemyPiece(piece, start.getRow() + direction, start.getCol() + sideDirection)) {
                Move move = new Move(start, new Location(start.getRow() + direction, start.getCol() + sideDirection));
                moves.put(move, simpleMove(move, Optional.empty()));
            }
        return moves;
    }

    private Map<Move, BoardState> getMovesForPao(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        Map<Move, BoardState> moves = new HashMap<>();
        moves.putAll(getMovesInAllDirections(start, 0, 1, Integer.MAX_VALUE, false));
        for (int dRow = -1; dRow <= 1; dRow++)
            for (int dCol = -1; dCol <= 1; dCol++)
                if (Math.abs(dRow) + Math.abs(dCol) == 1) {
                    int row = start.getRow();
                    int col = start.getCol();
                    boolean hopped = false;
                    while (true) {
                        row += dRow;
                        col += dCol;
                        if (!inBounds(row, col))
                            break;
                        if (grid[row][col] != ' ') {
                            if (hopped) {
                                if (isEnemyPiece(piece, row, col)) {
                                    Move move = new Move(start, new Location(row, col));
                                    moves.put(move, simpleMove(move, Optional.empty()));
                                }
                                break;
                            } else {
                                hopped = true;
                            }
                        } else if (hopped) {
                            Move move = new Move(start, new Location(row, col));
                            moves.put(move, simpleMove(move, Optional.empty()));
                        }
                    }
                }
        return moves;
    }

    private Map<Move, BoardState> getMovesForOvertaker(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        Map<Move, BoardState> moves = new HashMap<>();
        moves.putAll(getMovesInAllDirections(start, 0, 1, 1, false));
        moves.putAll(getMovesInAllDirections(start, 1, 1, 1, false));
        for (int dRow = -1; dRow <= 1; dRow++)
            for (int dCol = -1; dCol <= 1; dCol++)
                if ((dRow != 0 || dCol != 0)
                        && inBounds(start.getRow() + 2 * dRow, start.getCol() + 2 * dCol)
                        && isEnemyPiece(piece, start.getRow() + dRow, start.getCol() + dCol)
                        && grid[start.getRow() + 2 * dRow][start.getCol() + 2 * dCol] == ' ') {
                    Move move = new Move(start, new Location(start.getRow() + 2 * dRow, start.getCol() + 2 * dCol));
                    moves.put(move, simpleMove(move, Optional.of(new Location(start.getRow() + dRow, start.getCol() + dCol))));
                }
        return moves;
    }

    private BoardState simpleMove(Move move, Optional<Location> capture) {
        char piece = grid[move.getStart().getRow()][move.getStart().getCol()];

        char[][] newGrid = copyGrid();
        newGrid[move.getEnd().getRow()][move.getEnd().getCol()] = piece;
        newGrid[move.getStart().getRow()][move.getStart().getCol()] = ' ';

        capture.ifPresent(loc -> newGrid[loc.getRow()][loc.getCol()] = ' ');

        // promotion
        if (piece == 'p' && move.getEnd().getRow() == 0)
            newGrid[move.getEnd().getRow()][move.getEnd().getCol()] = 'q';
        else if (piece == 'P' && move.getEnd().getRow() == grid.length - 1)
            newGrid[move.getEnd().getRow()][move.getEnd().getCol()] = 'Q';

        return toBuilder()
            .grid(newGrid)
            .playerTurnToMove(!playerTurnToMove)
            .build();
    }

    private char[][] copyGrid() {
        char[][] newGrid = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            newGrid[i] = new char[grid[i].length];
            for (int j = 0; j < grid[i].length; j++)
                newGrid[i][j] = grid[i][j];
        }
        return newGrid;
    }

    private Optional<Location> find(char piece) {
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++)
                if (grid[row][col] == piece)
                    return Optional.of(new Location(row, col));
        return Optional.empty();
    }

    private boolean isEnemyPiece(char piece, int row, int col) {
        if (!inBounds(row, col)) {
            return false;
        }
        if (piece == ' ' || grid[row][col] == ' ')
            return false;
        return Character.isUpperCase(piece) ^ Character.isUpperCase(grid[row][col]);
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[row].length;
    }
}
