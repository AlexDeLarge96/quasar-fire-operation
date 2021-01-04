package com.quasar.operation.services.impl;

import com.quasar.operation.domain.Position;
import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.domain.SpaceShip;
import com.quasar.operation.services.LocationService;
import com.quasar.operation.services.MessageDecryptionService;
import com.quasar.operation.services.SpaceShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class SpaceShipServiceImpl implements SpaceShipService {

    private final LocationService locationService;
    private final MessageDecryptionService decryptionService;

    @Autowired
    public SpaceShipServiceImpl(LocationService locationService, MessageDecryptionService decryptionService) {
        this.locationService = locationService;
        this.decryptionService = decryptionService;
    }

    @Override
    public SpaceShip getSpaceShip(@NotNull List<SatelliteInput> satelliteInputs) {
        Position position = locationService.getSpaceShipPosition(satelliteInputs);
        List<List<String>> messages = getMessages(satelliteInputs);
        String message = decryptionService.decryptMessage(messages);
        return new SpaceShip(position, message);
    }

    private List<List<String>> getMessages(@NotNull List<SatelliteInput> satelliteInputs) {
        return satelliteInputs
                .stream()
                .map(SatelliteInput::getMessage)
                .filter(Objects::nonNull)
                .filter(message -> !message.contains(null))
                .collect(toList());
    }
}
