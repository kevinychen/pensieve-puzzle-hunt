package com.kyc.pensieve.server.penultima;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MoveWithEffects {

    private final Move move;

    @Builder.Default
    private Map<Location, Character> effects = ImmutableMap.of();
}
