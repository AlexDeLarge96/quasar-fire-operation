package com.quasar.operation.utils.annotations;

import com.quasar.operation.utils.validators.SatellitesListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = SatellitesListValidator.class)
public @interface ValidSatellitesList {

    String message() default "Invalid Satellites list. ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
