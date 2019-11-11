package com.kyc.pensieve.server.penultima;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class BoardState {

    private boolean playerWhite;
    private char[][] grid;
    private boolean playerTurnToMove;

    @JsonIgnore
    public Set<MoveWithEffects> getMoves() {
        return getMoves(true);
    }

    @JsonIgnore
    public Optional<MoveWithEffects> getBestMove() {
        return minimax(3).move;
    }

    @JsonIgnore
    public MoveWithEffects apply(MoveWithEffects moveWithEffects) {
        Move move = moveWithEffects.getMove();
        char piece = grid[move.getStart().getRow()][move.getStart().getCol()];

        grid[move.getStart().getRow()][move.getStart().getCol()] = ' ';

        Map<Location, Character> undoEffects = new HashMap<>();

        undoEffects.put(move.getEnd(), grid[move.getEnd().getRow()][move.getEnd().getCol()]);
        grid[move.getEnd().getRow()][move.getEnd().getCol()] = piece;

        moveWithEffects.getEffects().forEach((location, effect) -> {
            undoEffects.put(location, grid[location.getRow()][location.getCol()]);
            grid[location.getRow()][location.getCol()] = effect;
        });

        // promotion
        if (piece == 'p' && move.getEnd().getRow() == 0) {
            undoEffects.put(move.getStart(), 'p');
            grid[move.getEnd().getRow()][move.getEnd().getCol()] = 'q';
        } else if (piece == 'P' && move.getEnd().getRow() == grid.length - 1) {
            undoEffects.put(move.getStart(), 'P');
            grid[move.getEnd().getRow()][move.getEnd().getCol()] = 'Q';
        }

        playerTurnToMove ^= true;

        return MoveWithEffects.builder()
                .move(new Move(move.getEnd(), move.getStart()))
                .effects(undoEffects)
                .build();
    }

    private Set<MoveWithEffects> getMoves(boolean validateCheck) {
        boolean whiteToPlay = playerWhite == playerTurnToMove;

        Set<MoveWithEffects> moves = new HashSet<>();
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++) {
                char piece = grid[row][col];
                if (whiteToPlay && Character.isLowerCase(piece))
                    moves.addAll(getMoves(new Location(row, col), validateCheck));
                else if (!whiteToPlay && Character.isUpperCase(piece))
                    moves.addAll(getMoves(new Location(row, col), validateCheck));
            }
        return moves;
    }

    private Set<MoveWithEffects> getMoves(Location start, boolean validateCheck) {
        boolean whiteToPlay = playerWhite == playerTurnToMove;

        Set<MoveWithEffects> moves = getMovesWithoutValidatingCheck(start);
        if (validateCheck) {
            Set<MoveWithEffects> filteredMoves = new HashSet<>();
            for (MoveWithEffects move : moves) {
                boolean inCheck = false;
                MoveWithEffects undoEffects = apply(move);
                for (MoveWithEffects opponentMove : getMoves(false)) {
                    MoveWithEffects opponentUndoEffects = apply(opponentMove);
                    if (!find(whiteToPlay ? 'k' : 'K').isPresent())
                        inCheck = true;
                    apply(opponentUndoEffects);
                }
                if (!inCheck)
                    filteredMoves.add(move);
                apply(undoEffects);
            }
            moves = filteredMoves;
        }
        return moves;
    }

    private Set<MoveWithEffects> getMovesWithoutValidatingCheck(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        for (int dRow = -1; dRow <= 1; dRow++)
            for (int dCol = -1; dCol <= 1; dCol++)
                if (isEnemyPiece(piece, start.getRow() + dRow, start.getCol() + dCol)
                        && grid[start.getRow() + dRow][start.getCol() + dCol] == 'Q') {
                    return ImmutableSet.of(); // immobilized
                }

        switch (piece) {
            case 'r': // Rook
                return getMovesInAllDirections(start, 0, 1, Integer.MAX_VALUE, true);
            case 'n': // Camel
                return getMovesInAllDirections(start, 1, 3, 1, true);
            case 'b': // Eagle
                return ImmutableSet.<MoveWithEffects>builder()
                        .addAll(getMovesInDirection(start, -1, -1, Integer.MAX_VALUE, true))
                        .addAll(getMovesInDirection(start, -1, 1, Integer.MAX_VALUE, true))
                        .addAll(getMovesInDirection(start, 1, 0, Integer.MAX_VALUE, true))
                        .addAll(getMovesInAllDirections(start, 0, 1, 1, true))
                        .addAll(getMovesInDirection(start, 1, -1, 2, true))
                        .addAll(getMovesInDirection(start, 1, 1, 2, true))
                        .build();
            case 'q': // Teleporter
                return getMovesForTeleporter(start);
            case 'k': // King
                return ImmutableSet.<MoveWithEffects>builder()
                        .addAll(getMovesInAllDirections(start, 0, 1, 1, true))
                        .addAll(getMovesInAllDirections(start, 1, 1, 1, true))
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
                return ImmutableSet.<MoveWithEffects>builder()
                        .addAll(getMovesInAllDirections(start, 0, 1, Integer.MAX_VALUE, false))
                        .addAll(getMovesInAllDirections(start, 1, 1, Integer.MAX_VALUE, false))
                        .build();
            case 'K': // King
                return ImmutableSet.<MoveWithEffects>builder()
                        .addAll(getMovesInAllDirections(start, 0, 1, 1, true))
                        .addAll(getMovesInAllDirections(start, 1, 1, 1, true))
                        .build();
            case 'P': // Pawn
                return getMovesForPawn(start);
            default:
                return ImmutableSet.of();
        }
    }

    private Set<MoveWithEffects> getMovesInAllDirections(Location start, int dx, int dy, int limit, boolean canCapture) {
        Set<MoveWithEffects> moves = new HashSet<>();
        for (int sign1 = -1; sign1 <= 1; sign1 += 2)
            for (int sign2 = -1; sign2 <= 1; sign2 += 2) {
                moves.addAll(getMovesInDirection(start, sign1 * dx, sign2 * dy, limit, canCapture));
                moves.addAll(getMovesInDirection(start, sign1 * dy, sign2 * dx, limit, canCapture));
            }
        return moves;
    }

    private Set<MoveWithEffects> getMovesInDirection(Location start, int dRow, int dCol, int limit, boolean canCapture) {
        Set<MoveWithEffects> moves = new HashSet<>();
        int row = start.getRow();
        int col = start.getCol();
        for (int i = 0; i < limit; i++) {
            row += dRow;
            col += dCol;
            if (!inBounds(row, col))
                break;
            if (grid[row][col] != ' ') {
                if (canCapture && Character.isUpperCase(grid[start.getRow()][start.getCol()]) != Character.isUpperCase(grid[row][col]))
                    moves.add(MoveWithEffects.builder().move(new Move(start, new Location(row, col))).build());
                break;
            }
            moves.add(MoveWithEffects.builder().move(new Move(start, new Location(row, col))).build());
        }
        return moves;
    }

    private Set<MoveWithEffects> getMovesForTeleporter(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        Set<MoveWithEffects> moves = new HashSet<>();
        for (int row = 0; row < grid.length; row++)
            for (int col = 0; col < grid[row].length; col++) {
                if ((start.getRow() + start.getCol() + row + col) % 2 == 1 && grid[row][col] == ' '
                        || Math.abs(start.getRow() - row) + Math.abs(start.getCol() - col) == 1 && isEnemyPiece(piece, row, col)) {
                    moves.add(MoveWithEffects.builder().move(new Move(start, new Location(row, col))).build());
                }
            }
        return moves;
    }

    private Set<MoveWithEffects> getMovesForPawn(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        int direction = Character.isLowerCase(piece) ? -1 : 1;
        Set<MoveWithEffects> moves = new HashSet<>();
        moves.addAll(getMovesInDirection(start, direction, 0, 1, false));
        if (start.getRow() == (direction == 1 ? 1 : grid.length - 2)
                && inBounds(start.getRow() + direction, start.getCol())
                && grid[start.getRow() + direction][start.getCol()] == ' ') {
            moves.addAll(getMovesInDirection(start, direction, 0, 2, false));
        }
        for (int sideDirection = -1; sideDirection <= 1; sideDirection += 2)
            if (isEnemyPiece(piece, start.getRow() + direction, start.getCol() + sideDirection)) {
                moves.add(MoveWithEffects.builder()
                    .move(new Move(start, new Location(start.getRow() + direction, start.getCol() + sideDirection)))
                    .build());
            }
        return moves;
    }

    private Set<MoveWithEffects> getMovesForPao(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        Set<MoveWithEffects> moves = new HashSet<>();
        moves.addAll(getMovesInAllDirections(start, 0, 1, Integer.MAX_VALUE, false));
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
                                    moves.add(MoveWithEffects.builder().move(new Move(start, new Location(row, col))).build());
                                }
                                break;
                            } else {
                                hopped = true;
                            }
                        } else if (!hopped) {
                            moves.add(MoveWithEffects.builder().move(new Move(start, new Location(row, col))).build());
                        }
                    }
                }
        return moves;
    }

    private Set<MoveWithEffects> getMovesForOvertaker(Location start) {
        char piece = grid[start.getRow()][start.getCol()];

        Set<MoveWithEffects> moves = new HashSet<>();
        moves.addAll(getMovesInAllDirections(start, 0, 1, 1, false));
        moves.addAll(getMovesInAllDirections(start, 1, 1, 1, false));
        for (int dRow = -1; dRow <= 1; dRow++)
            for (int dCol = -1; dCol <= 1; dCol++)
                if ((dRow != 0 || dCol != 0)
                        && inBounds(start.getRow() + 2 * dRow, start.getCol() + 2 * dCol)
                        && isEnemyPiece(piece, start.getRow() + dRow, start.getCol() + dCol)
                        && grid[start.getRow() + 2 * dRow][start.getCol() + 2 * dCol] == ' ') {
                    moves.add(MoveWithEffects.builder()
                        .move(new Move(start, new Location(start.getRow() + 2 * dRow, start.getCol() + 2 * dCol)))
                        .effects(ImmutableMap.of(new Location(start.getRow() + dRow, start.getCol() + dCol), ' '))
                        .build());
                }
        return moves;
    }

    private ScoredMove minimax(int depth) {
        if (depth == 0)
            return new ScoredMove(Optional.empty(), score());

        boolean whiteToPlay = playerWhite == playerTurnToMove;
        MoveWithEffects bestMove = null;
        double bestScore = whiteToPlay ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        for (MoveWithEffects move : getMoves()) {
            MoveWithEffects undoEffects = apply(move);
            double score = minimax(depth - 1).getScore();
            if (whiteToPlay && score > bestScore || !whiteToPlay && score < bestScore) {
                bestMove = move;
                bestScore = score;
            }
            apply(undoEffects);
        }
        return new ScoredMove(Optional.ofNullable(bestMove), bestScore);
    }

    private double score() {
        double score = 0;

        Map<Character, Double> pieceValues = ImmutableMap.<Character, Double>builder()
                .put('R', -10.)
                .put('N', -9.)
                .put('B', -3.)
                .put('Q', -18.)
                .put('K', -1001.)
                .put('P', -1.)
                .put('r', 11.)
                .put('n', 4.)
                .put('b', 10.)
                .put('q', 14.)
                .put('k', 999.)
                .put('p', 1.)
                .build();
        for (char[] row : grid)
            for (char piece : row)
                score += pieceValues.getOrDefault(piece, 0.);

        return score;
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

    @Data
    private static final class ScoredMove {

        private final Optional<MoveWithEffects> move;
        private final double score;
    }
}
