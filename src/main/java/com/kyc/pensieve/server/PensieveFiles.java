package com.kyc.pensieve.server;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
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

    public static synchronized void updateState(Function<State, State> changeState) {
        try {
            State state = MAPPER.readValue(STATE_FILE, State.class);
            State newState = changeState.apply(state);
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
    }

    @Data
    public static class AccountState {

        private final Map<String, Boolean> solved;
        private final List<Instant> guessTimes;
    }

    private PensieveFiles() {}
}
