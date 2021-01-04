package com.quasar.operation.utils.validators;

import com.quasar.operation.config.SatellitesConfig;
import com.quasar.operation.domain.Satellite;
import com.quasar.operation.utils.annotations.ValidSatelliteName;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SatelliteNameValidator implements ConstraintValidator<ValidSatelliteName, String> {

    private final List<Satellite> satellites;

    @Autowired
    public SatelliteNameValidator(SatellitesConfig satellitesConfig) {
        this.satellites = Optional.ofNullable(satellitesConfig.getSatellites()).orElseGet(Collections::emptyList);
    }

    @Override
    public boolean isValid(@NotNull String name, ConstraintValidatorContext constraintValidatorContext) {
        return satellites
                .stream()
                .map(Satellite::getName)
                .anyMatch(name::equals);
    }
}
