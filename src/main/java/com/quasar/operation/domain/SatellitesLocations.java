package com.quasar.operation.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SatellitesLocations {

    private double[][] positions;
    private double[] distances;
}
