package com.quasar.operation.services.impl;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.quasar.operation.config.SatellitesConfig;
import com.quasar.operation.domain.Position;
import com.quasar.operation.domain.Satellite;
import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.domain.SatellitesLocations;
import com.quasar.operation.exceptions.RequestException;
import com.quasar.operation.services.LocationService;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class LocationServiceImpl implements LocationService {

    private static final String ERROR_MSG = "The spaceship position cannot be determined.";
    private final List<Satellite> satellites;

    @Autowired
    public LocationServiceImpl(SatellitesConfig satellitesConfig) {
        this.satellites = Optional.ofNullable(satellitesConfig.getSatellites()).orElseGet(Collections::emptyList);
    }

    @Override
    public Position getSpaceShipPosition(List<SatelliteInput> satelliteInputs) {
        SatellitesLocations locations = getSatellitesLocations(satelliteInputs);
        double[][] positions = locations.getPositions();
        double[] distances = locations.getDistances();
        LeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positions, distances);
        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(trilaterationFunction, optimizer);
        double[] coords = solver.solve().getPoint().toArray();
        if (coords != null && coords.length == 2) {
            return new Position(coords[0], coords[1]);
        } else {
            throw new RequestException(NOT_FOUND, ERROR_MSG);
        }
    }

    private SatellitesLocations getSatellitesLocations(@NotNull List<SatelliteInput> satelliteInputs) {
        if (satellites.size() < 2) {
            throw new RequestException(NOT_FOUND, ERROR_MSG);
        }

        Map<String, Double> satellitesDistances = getSatellitesDistances(satelliteInputs);
        double[] distances = new double[satellites.size()];
        double[][] positions = new double[satellites.size()][2];
        for (int i = 0; i < satellites.size(); i++) {
            Satellite satellite = satellites.get(i);
            double distance = satellitesDistances.getOrDefault(satellite.getName(), -1.0);
            if (distance >= 0) {
                distances[i] = satellitesDistances.get(satellite.getName());
            } else {
                throw new RequestException(NOT_FOUND, ERROR_MSG);
            }
            positions[i] = satellite.getPosition().toArray();
        }

        return new SatellitesLocations(positions, distances);
    }

    @NotNull
    private Map<String, Double> getSatellitesDistances(@NotNull List<SatelliteInput> satelliteInputs) {
        return satelliteInputs
                .stream()
                .collect(Collectors.toMap(SatelliteInput::getName, SatelliteInput::getDistance));
    }
}
