package com.kyc.pensieve.server;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public static class GuessResponse {

        private final boolean correct;
        private final boolean blocked;
        private final String answer;
    }

    @GET
    @Path("/solved")
    Map<String, String> solved();
}
