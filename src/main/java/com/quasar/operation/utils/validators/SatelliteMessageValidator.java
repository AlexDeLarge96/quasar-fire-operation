package com.quasar.operation.utils.validators;

import com.quasar.operation.utils.annotations.ValidSatelliteMessage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class SatelliteMessageValidator implements ConstraintValidator<ValidSatelliteMessage, List<String>> {

    @Override
    public boolean isValid(List<String> message, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(message)
                .filter(msg -> !msg.isEmpty())
                .filter(msg -> !msg.contains(null))
                .isPresent();
    }
}
