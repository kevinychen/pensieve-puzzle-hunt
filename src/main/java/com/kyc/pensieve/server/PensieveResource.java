package com.kyc.pensieve.server;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;

public class PensieveResource implements PensieveService {

    private final String ANSWER = "BANISHED";
    private final int N = ANSWER.length();
    private final int K = 6; // number of dots in Braille
    private final String[] BRAILLE = { "1", "12", "14", "145", "15", "124", "1245", "125", "24", "245", "13",
            "123", "134", "1345", "135", "1234", "12345", "1235", "234", "2345",
            "136", "1236", "2456", "1346", "13456", "1356" };

    private final Set<String> words;

    public PensieveResource() throws IOException {
        words = ImmutableSet.copyOf(Files.readLines(new File("./twl.txt"), StandardCharsets.UTF_8));
    }

    @Override
    public GetAnswerResponse getAnswer(GetAnswerRequest request) {
        boolean[][] grid = new boolean[N][K];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < K; j++)
                grid[i][j] = true;
        for (String word : request.getWords()) {
            if (word.length() > N)
                return new GetAnswerResponse(null, "The word is too long. Please try again.", false, false);
            if (!words.contains(word))
                return new GetAnswerResponse(null, "That is not a valid word. Please try again.", false, false);
            for (int i = 0; i < word.length(); i++) {
                int index = word.charAt(i) - 'A';
                if (index < 0 || index >= 26)
                    return new GetAnswerResponse(null, "The word contains invalid characters. Please try again.", false, false);
                for (char c : BRAILLE[index].toCharArray())
                    grid[i][(c - '1')] ^= true;
            }
        }
        if (allBlank(grid)) {
            if (request.getWords().size() <= N) {
                boolean[][] winGrid = new boolean[N][K];
                for (int i = 0; i < N; i++)
                    for (char c : BRAILLE[ANSWER.charAt(i) - 'A'].toCharArray())
                        winGrid[i][(c - '1')] = true;
                return new GetAnswerResponse(winGrid, null, true, true);
            } else {
                return new GetAnswerResponse(grid, null, true, false);
            }
        }
        return new GetAnswerResponse(grid, null, false, false);
    }

    private boolean allBlank(boolean[][] grid) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < K; j++)
                if (grid[i][j])
                    return false;
        return true;
    }
}
