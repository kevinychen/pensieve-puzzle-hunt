package com.kyc.pensieve.server.blind;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.Builder;
import lombok.Data;

@Path("/blind")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface BlindService {

    @POST
    @Path("/answer")
    GetAnswerResponse getAnswer(GetAnswerRequest request);

    @Data
    public static class GetAnswerRequest {

        private final List<String> words;
    }

    @Builder
    @Data
    public static class GetAnswerResponse {

        private final boolean[][] grid;
        private final String error;
        private final boolean complete;
        private final boolean win;
    }
}
