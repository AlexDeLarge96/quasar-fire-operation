package com.quasar.operation.services.impl;

import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.domain.SatellitesWrapper;
import com.quasar.operation.exceptions.RequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SatellitesStoredServiceImplTest {

    @Mock
    private Validator validator;
    @InjectMocks
    private SatellitesStoredServiceImpl storedService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        storedService = new SatellitesStoredServiceImpl(validator);
    }

    @Test
    void shouldThrowExceptionBecauseThereAreNotStoredSatellites() {
        boolean testPassed = false;
        try {
            storedService.getStoredSatellites("");
        } catch (RequestException ignored) {
            testPassed = true;
        }

        assertTrue(testPassed);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldThrowExceptionBecauseStoredSatellitesInfoIsInvalid() {
        boolean testPassed = false;
        try {
            String cookie = "ew0KICJzYXRlbGxpdGVzIjogWw0KICAgICAgICB7DQogICAgICAgICAgICAibmFtZSI6ICJrZW5v" +
                    "YmlzIiwNCiAgICAgICAgICAgICJkaXN0YW5jZSI6IC0xLA0KICAgICAgICAgICAgIm1lc3NhZ2UiOiBbInRo" +
                    "aXMiLG51bGwsIiIsInNlY3JldCIsIiJdDQogICAgICAgIH0NCiAgICBdDQp9";
            ConstraintViolation<SatellitesWrapper> violation = mock(ConstraintViolation.class);
            when(violation.getMessage()).thenReturn("failed");
            when(validator.validate(any(SatellitesWrapper.class))).thenReturn(singleton(violation));
            storedService.getStoredSatellites(cookie);
        } catch (RequestException ignored) {
            testPassed = true;
        }

        assertTrue(testPassed);
    }

    @Test
    void shouldReturnStoredSatellites() {
        String cookie = "ew0KICJzYXRlbGxpdGVzIjogWw0KICAgICAgICB7DQogICAgICAgICAgICAibmFtZSI6ICJrZW5v" +
                "YmkiLA0KICAgICAgICAgICAgImRpc3RhbmNlIjogMTAwLA0KICAgICAgICAgICAgIm1lc3NhZ2UiOiBbInR" +
                "oaXMiLCIiLCIiLCJzZWNyZXQiLCIiXQ0KICAgICAgICB9DQogICAgXQ0KfQ==";
        SatellitesWrapper satellitesWrapper = storedService.getStoredSatellites(cookie);
        List<SatelliteInput> satellites = satellitesWrapper.getSatellites();

        assertEquals(1, satellites.size());
        assertEquals("kenobi", satellites.get(0).getName());
        assertEquals(100.0, satellites.get(0).getDistance());
        assertEquals("this secret", satellites.get(0).getMessage()
                .stream().filter(word -> !word.isEmpty()).collect(Collectors.joining(" ")));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldThrowConstraintViolationException() {
        ConstraintViolation<SatelliteInput> violation = mock(ConstraintViolation.class);
        when(validator.validate(any(SatelliteInput.class))).thenReturn(singleton(violation));
        boolean testPassed = false;

        try {
            storedService.storeSatellite("", new SatelliteInput());
        } catch (ConstraintViolationException ignored) {
            testPassed = true;
        }

        assertTrue(testPassed);
    }

    @Test
    void shouldStoreFirstSatellite() {
        SatelliteInput newSatellite = new SatelliteInput("sato", 120, asList("this", "", "a", "", ""));
        SatellitesWrapper satellitesWrapper = storedService.storeSatellite("", newSatellite);
        List<SatelliteInput> satellites = satellitesWrapper.getSatellites();

        assertEquals(1, satellites.size());
        assertEquals("sato", satellites.get(0).getName());
        assertEquals(120.0, satellites.get(0).getDistance());
        assertEquals("this a", satellites.get(0).getMessage()
                .stream().filter(word -> !word.isEmpty()).collect(Collectors.joining(" ")));
    }

    @Test
    void shouldUpdateStoredSatellites() {
        String cookie = "ew0KICJzYXRlbGxpdGVzIjogWw0KICAgICAgICB7DQogICAgICAgICAgICAibmFtZSI6ICJrZW5v" +
                "YmkiLA0KICAgICAgICAgICAgImRpc3RhbmNlIjogMTAwLA0KICAgICAgICAgICAgIm1lc3NhZ2UiOiBbInR" +
                "oaXMiLCIiLCIiLCJzZWNyZXQiLCIiXQ0KICAgICAgICB9DQogICAgXQ0KfQ==";
        SatelliteInput newSatellite = new SatelliteInput("sato", 120, asList("this", "", "a", "", ""));
        SatellitesWrapper satellitesWrapper = storedService.storeSatellite(cookie, newSatellite);
        List<SatelliteInput> satellites = satellitesWrapper.getSatellites();

        assertEquals(2, satellites.size());
        assertEquals("kenobi", satellites.get(0).getName());
        assertEquals(100.0, satellites.get(0).getDistance());
        assertEquals("this secret", satellites.get(0).getMessage()
                .stream().filter(word -> !word.isEmpty()).collect(Collectors.joining(" ")));
        assertEquals("sato", satellites.get(1).getName());
        assertEquals(120.0, satellites.get(1).getDistance());
        assertEquals("this a", satellites.get(1).getMessage()
                .stream().filter(word -> !word.isEmpty()).collect(Collectors.joining(" ")));
    }
}