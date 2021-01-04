package com.quasar.operation.services;

import com.quasar.operation.domain.Position;
import com.quasar.operation.domain.SatelliteInput;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface LocationService {

    Position getSpaceShipPosition(@NotNull List<SatelliteInput> satelliteInputs);
}
