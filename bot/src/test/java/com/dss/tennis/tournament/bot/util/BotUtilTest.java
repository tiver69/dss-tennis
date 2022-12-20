package com.dss.tennis.tournament.bot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class BotUtilTest {

    @ParameterizedTest
    @MethodSource
    public void shouldReturnTrueWhenCommand(String command){
        boolean result = BotUtil.isCommand(command);

        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource
    public void shouldReturnFalseWhenNotCommand(String command){
        boolean result = BotUtil.isCommand(command);

        Assertions.assertFalse(result);
    }

    private static Stream<Arguments> shouldReturnTrueWhenCommand() {
        return Stream.of(
                Arguments.of("/start"),
                Arguments.of("  /start  "),
                Arguments.of("/other_command")
        );
    }

    private static Stream<Arguments> shouldReturnFalseWhenNotCommand() {
        return Stream.of(
                Arguments.of("start"),
                Arguments.of("not_a_command")
        );
    }
}
