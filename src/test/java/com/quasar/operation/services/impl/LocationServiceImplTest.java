package com.quasar.operation.services.impl;

import com.quasar.operation.config.SatellitesConfig;
import com.quasar.operation.domain.Position;
import com.quasar.operation.domain.Satellite;
import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.exceptions.RequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class LocationServiceImplTest {

    @Mock
    private SatellitesConfig config;
    @InjectMocks
    private LocationServiceImpl locationService;
    private List<SatelliteInput> satellitesList;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        Satellite satellite1 = new Satellite("sat-1", new Position(10, 10));
        Satellite satellite2 = new Satellite("sat-2", new Position(20, 20));
        when(config.getSatellites()).thenReturn(asList(satellite1, satellite2));
        locationService = new LocationServiceImpl(config);
        SatelliteInput input1 = new SatelliteInput("sat-1", 100, asList("this", ""));
        SatelliteInput input2 = new SatelliteInput("sat-2", 120, asList("", "message"));
        satellitesList = asList(input1, input2);
    }

    @Test
    void shouldThrowExceptionBecauseInsufficientSatellitesInfo() {
        boolean testPassed = false;
        try {
            ReflectionTestUtils.setField(locationService, "satellites", emptyList());
            locationService.getSpaceShipPosition(emptyList());
        } catch (RequestException ignored) {
            testPassed = true;
        }

        assertTrue(testPassed);
    }

    @Test
    void shouldThrowExceptionBecauseOfNegativeDistance() {
        boolean testPassed = false;
        try {
            SatelliteInput input1 = new SatelliteInput("sat-1", 100, asList("this", ""));
            SatelliteInput input2 = new SatelliteInput("sat-2", -100, asList("", "message"));
            List<SatelliteInput> satellites2 = asList(input1, input2);
            locationService.getSpaceShipPosition(satellites2);
        } catch (RequestException ignored) {
            testPassed = true;
        }

        assertTrue(testPassed);
    }

    @Test
    void shouldReturnSpaceshipPosition() {
        Position position = locationService.getSpaceShipPosition(satellitesList);
        assertEquals(-62.69874599592781, position.getX());
        assertEquals(-62.69878628995701, position.getY());
    }
}
