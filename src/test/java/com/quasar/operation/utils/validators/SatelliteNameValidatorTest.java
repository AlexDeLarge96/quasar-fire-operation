package com.quasar.operation.utils.validators;

import com.quasar.operation.config.SatellitesConfig;
import com.quasar.operation.domain.Position;
import com.quasar.operation.domain.Satellite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SatelliteNameValidatorTest {

    @Mock
    private SatellitesConfig config;
    private ConstraintValidatorContext context;
    @InjectMocks
    private SatelliteNameValidator nameValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        Satellite testSatellite = new Satellite("test-satellite", new Position(10, 10));
        when(config.getSatellites()).thenReturn(singletonList(testSatellite));
        nameValidator = new SatelliteNameValidator(config);
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValidSatelliteName() {
        boolean result = nameValidator.isValid("test-satellite", context);
        assertTrue(result);
    }


    @Test
    void isInvalidSatelliteName() {
        boolean result = nameValidator.isValid("other-satellite", context);
        assertFalse(result);
    }

}