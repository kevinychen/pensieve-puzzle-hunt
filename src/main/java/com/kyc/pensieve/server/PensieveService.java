package com.kyc.pensieve.server;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PensieveService {

    @POST
    @Path("/answer")
    GetAnswerResponse getAnswer(GetAnswerRequest request);

    public static class GetAnswerRequest {

        private final List<String> words;

        @JsonCreator
        public GetAnswerRequest(@JsonProperty("words") List<String> words) {
            this.words = words;
        }

        public List<String> getWords() {
            return words;
        }
    }

    public static class GetAnswerResponse {

        private final boolean[][] grid;
        private final String error;
        private final boolean win;

        public GetAnswerResponse(boolean[][] grid, String error, boolean win) {
            this.grid = grid;
            this.error = error;
            this.win = win;
        }

        @JsonProperty("grid")
        public boolean[][] getGrid() {
            return grid;
        }

        @JsonProperty("error")
        public String getError() {
            return error;
        }

        @JsonProperty("win")
        public boolean isWin() {
            return win;
        }
    }
}
