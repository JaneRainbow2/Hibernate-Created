package com.softserve.itacademy.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;

@SpringBootTest
public class StateTests {

    private static State validState;

    @BeforeAll
    static void init() {
        validState = new State();
        validState.setName("ValidStateName");
    }

    @Test
    void stateWithValidName() {
        State state = new State();
        state.setName("ValidStateName");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(state);

        assertEquals(0, violations.size());
    }

    @Test
    void createValidState() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(validState);

        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNameState")
    void constraintViolationInvalidName(String input, String errorValue) {
        State state = new State();
        state.setName(input);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(state);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidNameState() {
        return Stream.of(
                Arguments.of("Invalid State Name ExceedingLimitOf20Chars", "Invalid State Name ExceedingLimitOf20Chars"),
                Arguments.of("", ""),  // Empty name
                Arguments.of("invalid@", "invalid@")  // Invalid characters
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidNameState")
    void validNameState(String input) {
        State state = new State();
        state.setName(input);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<State>> violations = validator.validate(state);
        assertEquals(0, violations.size());
    }

    private static Stream<Arguments> provideValidNameState() {
        return Stream.of(
                Arguments.of("ValidStateName"),
                Arguments.of("State-1"),
                Arguments.of("State_Name")
        );
    }
}
