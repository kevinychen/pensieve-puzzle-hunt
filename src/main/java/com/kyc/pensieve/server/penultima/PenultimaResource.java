package com.kyc.pensieve.server.penultima;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Uninterruptibles;

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
        BoardState startState = request.getStartState();
        verify(startState, request.getSignature());

        BoardState endState = startState.getMoves().get(request.getMove());
        return startState.isPlayerTurnToMove() && endState != null
                ? new PlayerMoveResponse(true, endState, sign(endState))
                : new PlayerMoveResponse(false, startState, request.getSignature());
    }

    @Override
    public ComputerMoveResponse computerMove(ComputerMoveRequest request) {
        verify(request.getStartState(), request.getSignature());

        // TODO
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        Move move = new Move(new Location(0, 0), new Location(0, 1));
        BoardState state = request.getStartState().toBuilder()
                .playerTurnToMove(true)
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
