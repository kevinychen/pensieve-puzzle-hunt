package com.kyc.pensieve.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.Data;

public class PensieveFiles {

    private static final File CONFIG_FILE = new File("./data/config");
    private static final File STATE_FILE = new File("./data/state");
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    public static Config getConfig() {
        try {
            return MAPPER.readValue(CONFIG_FILE, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static State getState() {
        if (!STATE_FILE.exists())
            return new State(new HashMap<>(), new ArrayList<>());
        try {
            return MAPPER.readValue(STATE_FILE, State.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void updateState(Function<State, State> changeState) {
        State state = getState();
        State newState = changeState.apply(state);
        try {
            MAPPER.writeValue(STATE_FILE, newState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class Config {

        private final Set<String> accounts;
        private final Map<String, String> answers;
    }

    @Data
    public static class State {

        private final Map<String, AccountState> accounts;
        private final List<Long> globalGuessTimes;
    }

    @Data
    public static class AccountState {

        private final Map<String, String> solved;
        private final List<Long> guessTimes;
    }

    private PensieveFiles() {}
}
