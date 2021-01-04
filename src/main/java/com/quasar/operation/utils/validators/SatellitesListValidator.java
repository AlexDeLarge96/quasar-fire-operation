package com.quasar.operation.utils.validators;

import com.quasar.operation.config.SatellitesConfig;
import com.quasar.operation.domain.Satellite;
import com.quasar.operation.domain.SatelliteInput;
import com.quasar.operation.utils.annotations.ValidSatellitesList;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import java.util.*;

public class SatellitesListValidator implements ConstraintValidator<ValidSatellitesList, List<SatelliteInput>> {

    private final List<Satellite> satellites;

    @Autowired
    public SatellitesListValidator(SatellitesConfig satellitesConfig) {
        this.satellites = Optional.ofNullable(satellitesConfig.getSatellites()).orElseGet(Collections::emptyList);
    }

    @Override
    public boolean isValid(@NotNull List<SatelliteInput> satelliteInputs, ConstraintValidatorContext constraintValidatorContext) {
        Set<String> satelliteNames = new HashSet<>();
        Set<Long> messagesSizes = new HashSet<>();
        for (SatelliteInput satelliteInput : satelliteInputs) {
            Optional<String> satelliteName = Optional.ofNullable(satelliteInput).map(SatelliteInput::getName);
            satelliteName.ifPresent(satelliteNames::add);

            long messageSize = getMessageSize(satelliteInput);
            messagesSizes.add(messageSize);
        }

        boolean validMessagesSize = messagesSizes.size() == 1 && !messagesSizes.contains(0L);
        boolean validNumberOfSatellites = satelliteNames.size() == satellites.size();

        return validNumberOfSatellites && validMessagesSize;
    }

    private long getMessageSize(SatelliteInput satelliteInput) {
        List<String> message = Optional.ofNullable(satelliteInput)
                .map(SatelliteInput::getMessage)
                .orElseGet(Collections::emptyList);
        return !message.contains(null) ? message.size() : 0;
    }
}
