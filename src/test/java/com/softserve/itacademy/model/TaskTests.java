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
public class TaskTests {

    private static Task validTask;
    private static ToDo validToDo;
    private static State validState;

    @BeforeAll
    static void init(){
        validToDo = new ToDo();
        validToDo.setTitle("Valid ToDo");
        validToDo.setCreatedAt(LocalDateTime.now());

        validState = new State();
        validState.setName("IN_PROGRESS");

        validTask = new Task();
        validTask.setName("Valid Task Name");
        validTask.setPriority(Priority.HIGH);
        validTask.setTodo(validToDo);
        validTask.setState(validState);
    }

    @Test
    void taskWithValidFields() {
        Task task = new Task();
        task.setName("Task Name");
        task.setPriority(Priority.LOW);
        task.setTodo(validToDo);
        task.setState(validState);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertEquals(0, violations.size());
    }

    @Test
    void createValidTask() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> violations = validator.validate(validTask);

        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNameTask")
    void constraintViolationInvalidName(String input, String errorValue) {
        Task task = new Task();
        task.setName(input);
        task.setPriority(Priority.MEDIUM);
        task.setTodo(validToDo);
        task.setState(validState);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidNameTask() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("Ta", "Ta"), // less than 3 characters
                Arguments.of("This task name is way too long and exceeds the 200 character limit that should be enforced by validation.", "This task name is way too long and exceeds the 200 character limit that should be enforced by validation.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPriorityTask")
    void constraintViolationInvalidPriority(Priority input, Priority errorValue) {
        Task task = new Task();
        task.setName("Valid Task Name");
        task.setPriority(input);
        task.setTodo(validToDo);
        task.setState(validState);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        assertEquals(1, violations.size());
        assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidPriorityTask() {
        return Stream.of(
                Arguments.of(null, null) // priority is null
        );
    }
}
