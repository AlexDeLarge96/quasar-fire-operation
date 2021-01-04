package com.quasar.operation.utils.validators;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class SatelliteMessageValidatorTest {

    private static ConstraintValidatorContext context;
    private static SatelliteMessageValidator messageValidator;

    @BeforeAll
    static void setup() {
        messageValidator = spy(SatelliteMessageValidator.class);
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValidMessage() {
        boolean result = messageValidator.isValid(singletonList(""), context);
        assertTrue(result);
    }

    @Test
    void areInvalidMessages() {
        boolean result1 = messageValidator.isValid(emptyList(), context);
        boolean result2 = messageValidator.isValid(Arrays.asList("", null), context);
        assertFalse(result1);
        assertFalse(result2);
    }
}