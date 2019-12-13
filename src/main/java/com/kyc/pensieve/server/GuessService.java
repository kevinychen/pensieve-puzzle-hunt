package com.kyc.pensieve.server;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.Builder;
import lombok.Data;

@Path("/guess")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface GuessService {

    @POST
    @Path("/enter")
    GuessResponse enter(EnterRequest request);

    @Data
    public static class EnterRequest {

        private final String guess;
    }

    @POST
    @Path("/")
    GuessResponse guess(GuessRequest request);

    @Data
    public static class GuessRequest {

        private final String puzzle;
        private final String guess;
    }

    @Data
    @Builder
    public static class GuessResponse {

        @Builder.Default
        private final boolean correct = false;
        @Builder.Default
        private final boolean blocked = false;
        @Builder.Default
        private final String answer = null;
        @Builder.Default
        private final String message = null;
    }

    @GET
    @Path("/solved")
    SolvedResponse solved();

    @Data
    public static class SolvedResponse {

        private final Map<String, String> solved;
        private final Map<String, String> messages;
    }
}
