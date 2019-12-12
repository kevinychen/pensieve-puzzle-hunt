package com.kyc.pensieve.server;

public class GuessResource implements GuessService {

    @Override
    public GuessResponse enter(EnterRequest request) {
        String account = answerize(request.getGuess());
        if (PensieveFiles.getConfig().getAccounts().contains(account))
            return new GuessResponse(true, account);
        else
            return new GuessResponse(false, null);
    }

    @Override
    public GuessResponse guess(GuessRequest request) {
        String guess = answerize(request.getGuess());
        if (guess.equals(PensieveFiles.getConfig().getAnswers().get(request.getPuzzle())))
            return new GuessResponse(true, guess);
        else
            return new GuessResponse(false, null);
    }

    private String answerize(String guess) {
        return guess.toUpperCase().replaceAll("[^A-Z]", "");
    }
}
