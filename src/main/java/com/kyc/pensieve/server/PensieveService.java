package com.kyc.pensieve.server;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.Builder;
import lombok.Data;

@Path("/")
public interface PensieveService {

    @POST
    @Path("/answer")
    @Produces(MediaType.APPLICATION_JSON)
    GetAnswerResponse getAnswer(GetAnswerRequest request);

    @Data
    public static class GetAnswerRequest {

        private final List<String> words;
    }

    @Data
    @Builder
    public static class GetAnswerResponse {

        private final boolean[][] grid;
        private String error;
        private boolean win;
    }
}
