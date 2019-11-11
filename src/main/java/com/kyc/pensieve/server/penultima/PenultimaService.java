package com.kyc.pensieve.server.penultima;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.Data;

@Path("/penultima")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PenultimaService {

    @POST
    @Path("/start")
    StartResponse getStart(StartRequest request);

    @Data
    public static class StartRequest {

        private final boolean playerWhite;
    }

    @Data
    public static class StartResponse {

        private final BoardState state;
        private final byte[] signature;
    }

    @POST
    @Path("/player-move")
    PlayerMoveResponse playerMove(PlayerMoveRequest request);

    @Data
    public static class PlayerMoveRequest {

        private final BoardState startState;
        private final byte[] signature;
        private final Move move;
    }

    @Data
    public static class PlayerMoveResponse {

        private final boolean valid;
        private final BoardState endState;
        private final byte[] signature;
    }

    @POST
    @Path("/computer-move")
    ComputerMoveResponse computerMove(ComputerMoveRequest request);

    @Data
    public static class ComputerMoveRequest {

        private final BoardState startState;
        private final byte[] signature;
    }

    @Data
    public static class ComputerMoveResponse {

        private final Move move;
        private final BoardState endState;
        private final byte[] signature;
        private final String solution;
    }
}
