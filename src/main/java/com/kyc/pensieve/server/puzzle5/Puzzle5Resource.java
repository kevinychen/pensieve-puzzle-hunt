package com.kyc.pensieve.server.puzzle5;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

public class Puzzle5Resource implements Puzzle5Service {

    private final KeyPair keyPair;
    private final ObjectMapper mapper;

    public Puzzle5Resource() throws NoSuchAlgorithmException {
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
                .isPlayerWhite(request.isPlayerWhite())
                .grid(grid)
                .isPlayerTurnToMove(request.isPlayerWhite())
                .build();
        return new StartResponse(state, sign(state));
    }

    @Override
    public PlayerMoveResponse playerMove(PlayerMoveRequest request) {
        BoardState startState = request.getStartState();
        verify(startState, request.getSignature());
        Preconditions.checkArgument(startState.isPlayerTurnToMove());

        BoardState endState = startState.getMoves().get(request.getMove());
        return endState != null
                ? new PlayerMoveResponse(true, endState, sign(endState))
                : new PlayerMoveResponse(false, startState, request.getSignature());
    }

    @Override
    public ComputerMoveResponse computerMove(ComputerMoveRequest request) {
        verify(request.getStartState(), request.getSignature());

        // TODO
        Move move = new Move(new Location(0, 0), new Location(0, 1));
        BoardState state = request.getStartState().toBuilder()
                .isPlayerTurnToMove(true)
                .build();
        return new ComputerMoveResponse(move, state, sign(state));
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
