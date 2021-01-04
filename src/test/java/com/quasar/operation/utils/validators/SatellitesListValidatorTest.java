package com.quasar.operation.utils.validators;

import com.quasar.operation.config.SatellitesConfig;
import com.quasar.operation.domain.Position;
import com.quasar.operation.domain.Satellite;
import com.quasar.operation.domain.SatelliteInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SatellitesListValidatorTest {

    @Mock
    private SatellitesConfig config;
    private ConstraintValidatorContext context;
    @InjectMocks
    private SatellitesListValidator listValidator;
    private List<SatelliteInput> satellitesList;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        Satellite satellite1 = new Satellite("sat-1", new Position(10, 10));
        Satellite satellite2 = new Satellite("sat-2", new Position(20, 20));
        when(config.getSatellites()).thenReturn(asList(satellite1, satellite2));
        listValidator = new SatellitesListValidator(config);
        context = mock(ConstraintValidatorContext.class);
        SatelliteInput input1 = new SatelliteInput("sat-1", 100, asList("this", ""));
        SatelliteInput input2 = new SatelliteInput("sat-2", 120, asList("", "message"));
        satellitesList = asList(input1, input2);
    }

    @Test
    void isValidSatellitesList() {
        boolean result = listValidator.isValid(satellitesList, context);
        assertTrue(result);
    }

    @Test
    void isInvalidSatelliteList() {
        List<SatelliteInput> satellites2 = new LinkedList<>(satellitesList);
        satellites2.remove(0);
        boolean result = listValidator.isValid(satellites2, context);
        assertFalse(result);
    }
}