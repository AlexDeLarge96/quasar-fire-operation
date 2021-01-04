package com.quasar.operation.services;

import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.domain.SpaceShip;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface SpaceShipService {

    SpaceShip getSpaceShip(@NotNull List<SatelliteInput> satelliteInputs);
}
