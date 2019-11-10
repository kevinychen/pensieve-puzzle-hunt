package com.kyc.pensieve.server.puzzle5;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class BoardState {

    private final boolean isPlayerWhite;
    private final char[][] grid;
    private final boolean isPlayerTurnToMove;

    public Map<Move, BoardState> getMoves() {
        return getMoves(true);
    }

    private Map<Move, BoardState> getMoves(boolean validateCheck) {
        boolean whiteToPlay = isPlayerWhite == isPlayerTurnToMove;

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
        boolean whiteToPlay = isPlayerWhite == isPlayerTurnToMove;

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
        switch (piece) {
            case 'r':
                return getMovesInAllDirections(start, 0, 1, Integer.MAX_VALUE);
            case 'n':
                return getMovesInAllDirections(start, 1, 2, Integer.MAX_VALUE);
            case 'b':
                // TODO
                return ImmutableMap.of();
            case 'q':
                // TODO
                return ImmutableMap.of();
            case 'k':
                return ImmutableMap.<Move, BoardState>builder()
                        .putAll(getMovesInAllDirections(start, 0, 1, 1))
                        .putAll(getMovesInAllDirections(start, 1, 1, 1))
                        .build();
            case 'p':
                // TODO
                return ImmutableMap.of();
            case 'R':
                // TODO
                return ImmutableMap.of();
            case 'N':
                return getMovesInAllDirections(start, 1, 3, Integer.MAX_VALUE);
            case 'B':
                // TODO
                return ImmutableMap.of();
            case 'Q':
                // TODO
                return ImmutableMap.of();
            case 'K':
                return ImmutableMap.<Move, BoardState>builder()
                        .putAll(getMovesInAllDirections(start, 0, 1, 1))
                        .putAll(getMovesInAllDirections(start, 1, 1, 1))
                        .build();
            case 'P':
                // TODO
                return ImmutableMap.of();
            default:
                return ImmutableMap.of();
        }
    }

    private Map<Move, BoardState> getMovesInAllDirections(Location start, int dx, int dy, int limit) {
        Map<Move, BoardState> moves = new HashMap<>();
        for (int sign1 = -1; sign1 <= 1; sign1 += 2)
            for (int sign2 = -1; sign2 <= 1; sign2 += 2) {
                moves.putAll(getEndLocationsInDirection(start, sign1 * dx, sign2 * dy, limit));
                moves.putAll(getEndLocationsInDirection(start, sign1 * dy, sign2 * dx, limit));
            }
        return moves;
    }

    private Map<Move, BoardState> getEndLocationsInDirection(Location start, int dRow, int dCol, int limit) {
        Map<Move, BoardState> moves = new HashMap<>();
        int row = start.getRow();
        int col = start.getCol();
        for (int i = 0; i < limit; i++) {
            row += dRow;
            col += dCol;
            if (row < 0 || row >= grid.length || col < 0 || col >= grid[row].length)
                break;
            if (grid[row][col] != ' ') {
                if (Character.isUpperCase(grid[start.getRow()][start.getCol()]) != Character.isUpperCase(grid[row][col])) {
                    Move move = new Move(start, new Location(row, col));
                    moves.put(move, simpleMove(move));
                }
                break;
            }
            Move move = new Move(start, new Location(row, col));
            moves.put(move, simpleMove(move));
        }
        return moves;
    }

    private BoardState simpleMove(Move move) {
        char[][] newGrid = copyGrid();
        newGrid[move.getEnd().getRow()][move.getEnd().getCol()] = newGrid[move.getStart().getRow()][move.getStart().getCol()];
        newGrid[move.getStart().getRow()][move.getStart().getCol()] = ' ';
        return toBuilder()
                .grid(newGrid)
                .isPlayerTurnToMove(!isPlayerTurnToMove)
                .build();
    }

    private char[][] copyGrid() {
        char[][] newGrid = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new char[grid[i].length];
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
}
