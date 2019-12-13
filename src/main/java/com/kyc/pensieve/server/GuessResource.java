package com.kyc.pensieve.server;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.base.Preconditions;
import com.kyc.pensieve.server.PensieveFiles.AccountState;
import com.kyc.pensieve.server.PensieveFiles.Config;
import com.kyc.pensieve.server.PensieveFiles.State;

public class GuessResource implements GuessService {

    @Override
    public GuessResponse enter(EnterRequest request) {
        String account = answerize(request.getGuess());
        AtomicBoolean blocked = new AtomicBoolean();
        PensieveFiles.updateState(state -> {
            List<Long> globalGuessTimes = state.getGlobalGuessTimes();
            if (isSpam(globalGuessTimes)) {
                blocked.set(true);
                return state;
            }
            globalGuessTimes.add(Instant.now().getEpochSecond());
            if (globalGuessTimes.size() > 10)
                globalGuessTimes.remove(0);
            return new State(state.getAccounts(), globalGuessTimes);
        });
        if (blocked.get())
            return new GuessResponse(false, true, null);
        else if (PensieveFiles.getConfig().getAccounts().contains(account))
            return new GuessResponse(true, false, account);
        else
            return new GuessResponse(false, false, null);
    }

    @Override
    public GuessResponse guess(String account, GuessRequest request) {
        Config config = PensieveFiles.getConfig();
        Preconditions.checkArgument(config.getAccounts().contains(account));
        String guess = answerize(request.getGuess());
        AtomicBoolean correct = new AtomicBoolean();
        AtomicBoolean blocked = new AtomicBoolean();
        PensieveFiles.updateState(state -> {
            Map<String, AccountState> accounts = new HashMap<>(state.getAccounts());
            AccountState accountState = accounts.getOrDefault(account, new AccountState(new HashMap<>(), new ArrayList<>()));
            Map<String, String> solved = new HashMap<>(accountState.getSolved());
            List<Long> guessTimes = new ArrayList<>(accountState.getGuessTimes());
            if (isSpam(guessTimes)) {
                blocked.set(true);
                return state;
            }
            correct.set(guess.equals(config.getAnswers().get(request.getPuzzle())));
            if (correct.get())
                solved.put(request.getPuzzle(), guess);
            guessTimes.add(Instant.now().getEpochSecond());
            if (guessTimes.size() > 10)
                guessTimes.remove(0);
            accounts.put(account, new AccountState(solved, guessTimes));
            return new State(accounts, state.getGlobalGuessTimes());
        });
        if (blocked.get())
            return new GuessResponse(false, true, null);
        else if (correct.get()) {
            return new GuessResponse(true, false, guess);
        } else
            return new GuessResponse(false, false, null);
    }

    @Override
    public Map<String, String> solved(String account) {
        return PensieveFiles.getState()
            .getAccounts()
            .getOrDefault(account, new AccountState(new HashMap<>(), new ArrayList<>()))
            .getSolved();
    }

    private String answerize(String guess) {
        return guess.toUpperCase().replaceAll("[^A-Z]", "");
    }

    private boolean isSpam(List<Long> guessTimes) {
        for (int i = 1; i <= guessTimes.size(); i++)
            if (guessTimes.get(guessTimes.size() - i) > Instant.now().getEpochSecond() - (10 << i))
                return true;
        return false;
    }
}
