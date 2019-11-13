package com.kyc.pensieve.server.penultima;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

public class PenultimaResource implements PenultimaService {

    private final KeyPair keyPair;
    private final ObjectMapper mapper;

    public PenultimaResource() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);

        this.keyPair = kpg.genKeyPair();
        this.mapper = new ObjectMapper();
    }

    @Override
    public StartResponse getStart(StartRequest request) {
        char[][] grid = {
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
        };
        BoardState state = BoardState.builder()
                .playerWhite(request.isPlayerWhite())
                .grid(grid)
                .playerTurnToMove(request.isPlayerWhite())
                .build();
        return new StartResponse(state, sign(state));
    }

    @Override
    public PlayerMoveResponse playerMove(PlayerMoveRequest request) {
        BoardState state = request.getStartState();
        verify(state, request.getSignature());

        Optional<MoveWithEffects> allowedMove = state.getMoves().stream()
                .filter(move -> move.getMove().equals(request.getMove()))
                .findAny();
        boolean isValid = state.isPlayerTurnToMove() && allowedMove.isPresent();
        if (isValid)
            state.apply(allowedMove.get());
        return new PlayerMoveResponse(isValid, state, sign(state));
    }

    @Override
    public ComputerMoveResponse computerMove(ComputerMoveRequest request) {
        BoardState state = request.getStartState();
        verify(state, request.getSignature());
        Preconditions.checkArgument(!state.isPlayerTurnToMove());

        return state.getBestMove()
            .map(move -> {
                state.apply(move);
                return new ComputerMoveResponse(move.getMove(), state, sign(state), null);
            })
            .orElseGet(() -> {
                char[][] solution = {
                        { 'A', 'B', 'E', 'C', 'E', 'D', 'A', 'R' },
                        { 'I', 'A', 'N', ' ', 'B', 'I', 'G', ' ' },
                        { 'C', 'H', 'E', 'S', 'S', ' ', ' ', ' ' },
                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                        { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                };
                return new ComputerMoveResponse(null, state, request.getSignature(), solution);
            });
    }

    private byte[] sign(BoardState state) {
        try {
            Signature sig = Signature.getInstance("SHA1WithRSA");
            sig.initSign(keyPair.getPrivate());
            sig.update(mapper.writeValueAsBytes(state));
            return sig.sign();
        } catch (GeneralSecurityException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void verify(BoardState state, byte[] signature) {
        try {
            Signature sig = Signature.getInstance("SHA1WithRSA");
            sig.initVerify(keyPair.getPublic());
            sig.update(mapper.writeValueAsBytes(state));
            Preconditions.checkState(sig.verify(signature));
        } catch (GeneralSecurityException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
