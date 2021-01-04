package com.quasar.operation.utils.annotations;

import com.quasar.operation.utils.validators.SatelliteNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = SatelliteNameValidator.class)
public @interface ValidSatelliteName {

    String message() default "Invalid Satellite name. ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
