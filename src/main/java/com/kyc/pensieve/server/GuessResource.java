package com.kyc.pensieve.server;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.kyc.pensieve.server.PensieveFiles.AccountState;
import com.kyc.pensieve.server.PensieveFiles.Config;
import com.kyc.pensieve.server.PensieveFiles.State;

public class GuessResource implements GuessService {

    @Override
    public GuessResponse enter(EnterRequest request) {
        String guess = answerize(request.getGuess());
        Config config = PensieveFiles.getConfig();
        AtomicBoolean blocked = new AtomicBoolean();
        AtomicBoolean correct = new AtomicBoolean();
        AtomicReference<String> account = new AtomicReference<>();
        PensieveFiles.updateState(state -> {
            Map<String, AccountState> accounts = new HashMap<>(state.getAccounts());
            List<Long> globalGuessTimes = state.getGlobalGuessTimes();
            if (isSpam(globalGuessTimes)) {
                blocked.set(true);
                return state;
            }
            if (guess.equals(config.getIntroAnswer())) {
                correct.set(true);
                account.set(UUID.randomUUID().toString());
                accounts.put(account.get(), new AccountState(new HashMap<>()));
            } else {
                globalGuessTimes.add(Instant.now().getEpochSecond());
                if (globalGuessTimes.size() > 10)
                    globalGuessTimes.remove(0);
            }
            return new State(accounts, globalGuessTimes);
        });
        if (blocked.get())
            return GuessResponse.builder().blocked(true).build();
        else if (correct.get())
            return GuessResponse.builder()
                    .correct(true)
                    .answer(account.get())
                    .message(config.getIntroMessage())
                    .build();
        else
            return GuessResponse.builder().build();
    }

    @Override
    public GuessResponse guess(GuessRequest request) {
        String account = AuthFilter.getAccount();
        Config config = PensieveFiles.getConfig();
        String guess = answerize(request.getGuess());
        AtomicBoolean blocked = new AtomicBoolean();
        AtomicBoolean correct = new AtomicBoolean();
        PensieveFiles.updateState(state -> {
            Map<String, AccountState> accounts = new HashMap<>(state.getAccounts());
            Preconditions.checkState(accounts.containsKey(account));
            List<Long> globalGuessTimes = state.getGlobalGuessTimes();
            if (isSpam(globalGuessTimes)) {
                blocked.set(true);
                return state;
            }
            Map<String, String> solved = new HashMap<>(accounts.get(account).getSolved());
            if (guess.equals(config.getAnswers().get(request.getPuzzle()))) {
                correct.set(true);
                solved.put(request.getPuzzle(), guess);
            } else {
                globalGuessTimes.add(Instant.now().getEpochSecond());
                if (globalGuessTimes.size() > 10)
                    globalGuessTimes.remove(0);
            }
            accounts.put(account, new AccountState(solved));
            return new State(accounts, state.getGlobalGuessTimes());
        });
        if (blocked.get())
            return GuessResponse.builder().blocked(true).build();
        else if (correct.get()) {
            return GuessResponse.builder()
                    .correct(true)
                    .answer(guess)
                    .message(config.getMessages().get(request.getPuzzle()))
                    .build();
        } else
            return GuessResponse.builder().build();
    }

    @Override
    public SolvedResponse solved() {
        Map<String, String> solved = PensieveFiles.getState()
            .getAccounts()
            .getOrDefault(AuthFilter.getAccount(), new AccountState(new HashMap<>()))
            .getSolved();
        Map<String, String> messages = Maps.filterKeys(PensieveFiles.getConfig().getMessages(), solved::containsKey);
        return new SolvedResponse(solved, messages);
    }

    private String answerize(String guess) {
        return guess.toUpperCase().replaceAll("[^A-Z]", "");
    }

    private boolean isSpam(List<Long> guessTimes) {
        for (int i = 3; i <= guessTimes.size(); i++)
            if (guessTimes.get(guessTimes.size() - i) > Instant.now().getEpochSecond() - (4 << i))
                return true;
        return false;
    }
}
