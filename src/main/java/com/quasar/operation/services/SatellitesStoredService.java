package com.quasar.operation.services;

import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.domain.SatellitesWrapper;

import javax.validation.constraints.NotNull;

public interface SatellitesStoredService {

    SatellitesWrapper getStoredSatellites(@NotNull String satellitesCookie);

    SatellitesWrapper storeSatellite(@NotNull String satellitesCookie, @NotNull SatelliteInput satellite);
}
