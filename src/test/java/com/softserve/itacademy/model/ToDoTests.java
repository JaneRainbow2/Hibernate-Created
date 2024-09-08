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
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;

@SpringBootTest
public class ToDoTests {

    private static ToDo validToDo;
    private static User validOwner;

    @BeforeAll
    static void init(){
        validOwner = new User();
        validOwner.setFirstName("John");
        validOwner.setLastName("Doe");
        validOwner.setEmail("john.doe@example.com");
        validOwner.setPassword("Qwerty123!");

        validToDo = new ToDo();
        validToDo.setTitle("Valid ToDo Title");
        validToDo.setCreatedAt(LocalDateTime.now());
        validToDo.setOwner(validOwner);
    }

    @Test
    void toDoWithValidFields() {
        ToDo toDo = new ToDo();
        toDo.setTitle("Some Valid ToDo");
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(validOwner);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(toDo);

        assertEquals(0, violations.size());
    }

    @Test
    void createValidToDo() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(validToDo);

        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTitleToDo")
    void constraintViolationInvalidTitle(String input, String errorValue) {
        ToDo toDo = new ToDo();
        toDo.setTitle(input);
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(validOwner);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(toDo);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidTitleToDo() {
        return Stream.of(
                Arguments.of("", ""), // empty title
                Arguments.of("  ", "  "), // blank title
                Arguments.of(null, null) // null title
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCreatedAtToDo")
    void constraintViolationInvalidCreatedAt(LocalDateTime input, LocalDateTime errorValue) {
        ToDo toDo = new ToDo();
        toDo.setTitle("Valid ToDo Title");
        toDo.setCreatedAt(input);
        toDo.setOwner(validOwner);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(toDo);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidCreatedAtToDo() {
        return Stream.of(
                Arguments.of(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1)), // future date
                Arguments.of(null, null) // null createdAt
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidOwnerToDo")
    void constraintViolationInvalidOwner(User input, User errorValue) {
        ToDo toDo = new ToDo();
        toDo.setTitle("Valid ToDo Title");
        toDo.setCreatedAt(LocalDateTime.now());
        toDo.setOwner(input);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ToDo>> violations = validator.validate(toDo);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidOwnerToDo() {
        return Stream.of(
                Arguments.of(null, null) // owner is null
        );
    }
}
