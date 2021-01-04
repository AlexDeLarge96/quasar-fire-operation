package com.quasar.operation.services.impl;

import com.quasar.operation.domain.Position;
import com.quasar.operation.domain.SpaceShip;
import com.quasar.operation.services.LocationService;
import com.quasar.operation.services.MessageDecryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class SpaceShipServiceImplTest {

    @Mock
    private LocationService locationService;
    @Mock
    private MessageDecryptionService messageDecryptionService;
    @InjectMocks
    private SpaceShipServiceImpl spaceShipService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        when(locationService.getSpaceShipPosition(anyList())).thenReturn(new Position(100, 200));
        when(messageDecryptionService.decryptMessage(anyList())).thenReturn("this is a test");
        spaceShipService = new SpaceShipServiceImpl(locationService, messageDecryptionService);
    }

    @Test
    void shouldReturnSpaceship() {
        SpaceShip spaceShip = spaceShipService.getSpaceShip(emptyList());
        assertEquals(100.0, spaceShip.getPosition().getX());
        assertEquals(200.0, spaceShip.getPosition().getY());
        assertEquals("this is a test", spaceShip.getMessage());
    }
}