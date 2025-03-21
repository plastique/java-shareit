package ru.practicum.shareit.booking.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = DateValidator.class)
public @interface StartDateIsBeforeEnd {

    String message() default "Start must be before end";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
