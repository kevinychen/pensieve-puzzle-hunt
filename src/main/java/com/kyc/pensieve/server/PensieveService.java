package com.kyc.pensieve.server;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PensieveService {

    @POST
    @Path("/answer")
    GetAnswerResponse getAnswer(GetAnswerRequest request);

    @Data
    public static class GetAnswerRequest {

        private final List<String> words;

        @JsonCreator
        public GetAnswerRequest(@JsonProperty("words") List<String> words) {
            this.words = words;
        }
    }

    @Data
    @Builder
    public static class GetAnswerResponse {

        private final boolean[][] grid;
        private String error;
        private boolean win;
    }
}
