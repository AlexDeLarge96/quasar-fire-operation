package com.quasar.operation.services.impl;

import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.domain.SatellitesWrapper;
import com.quasar.operation.exceptions.RequestException;
import com.quasar.operation.services.SatellitesStoredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.quasar.operation.utils.QuasarUtils.base64Decode;
import static com.quasar.operation.utils.QuasarUtils.getObjectFromJson;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Validated
public class SatellitesStoredServiceImpl implements SatellitesStoredService {

    public static final String ERROR_MSG = "The spaceship message and position couldn't be determined yet. " +
            "Please continue posting (updating) the satellites info.";
    public static final String INVALID_SATELLITES_LIST_ERROR = "Invalid Satellites list. ";
    private final Validator validator;

    @Autowired
    public SatellitesStoredServiceImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public SatellitesWrapper getStoredSatellites(@NotNull String satellitesCookie) {
        Optional<SatellitesWrapper> storedSatellites = getValidStoredSatellites(satellitesCookie, false);
        if (storedSatellites.isPresent()) {
            return storedSatellites.get();
        } else {
            throw new RequestException(NOT_FOUND, ERROR_MSG);
        }
    }

    private Optional<SatellitesWrapper> getValidStoredSatellites(@NotNull String satellitesCookie, boolean ignoreSatelliteListValidation) {
        String decodedCookie = base64Decode(satellitesCookie);
        Optional<SatellitesWrapper> satellitesList = getObjectFromJson(decodedCookie, SatellitesWrapper.class);
        if (satellitesList.isPresent()) {
            SatellitesWrapper satellitesWrapper = satellitesList.get();
            Set<ConstraintViolation<SatellitesWrapper>> violations = validator.validate(satellitesWrapper);
            List<ConstraintViolation<SatellitesWrapper>> violationList = new ArrayList<>(violations);
            boolean ignoreValidationError = isInvalidSatelliteList(violationList) && ignoreSatelliteListValidation;
            if (!violations.isEmpty() && !ignoreValidationError) {
                return Optional.empty();
            } else {
                return Optional.of(satellitesWrapper);
            }
        } else {
            return Optional.empty();
        }
    }

    private boolean isInvalidSatelliteList(@NotNull List<ConstraintViolation<SatellitesWrapper>> violationList) {
        return violationList.size() == 1 && violationList.get(0).getMessage().equals(INVALID_SATELLITES_LIST_ERROR);
    }

    public SatellitesWrapper storeSatellite(@NotNull String satellitesCookie, @NotNull SatelliteInput satellite) {
        Set<ConstraintViolation<SatelliteInput>> violations = validator.validate(satellite);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Optional<SatellitesWrapper> storedSatellites = getValidStoredSatellites(satellitesCookie, true);
        SatellitesWrapper newSatellites = new SatellitesWrapper();
        if (storedSatellites.isPresent()) {
            List<SatelliteInput> newSatelliteInputs = storedSatellites.get().getSatellites();
            newSatelliteInputs.removeIf(satelliteInput -> satelliteInput.getName().equals(satellite.getName()));
            newSatelliteInputs.add(satellite);
            newSatellites.setSatellites(newSatelliteInputs);
        } else {
            newSatellites.setSatellites(singletonList(satellite));
        }

        return newSatellites;
    }
}
