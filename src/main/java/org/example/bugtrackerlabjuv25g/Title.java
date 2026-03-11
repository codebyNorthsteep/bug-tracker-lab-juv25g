package org.example.bugtrackerlabjuv25g;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@NotNull
@Size
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
public @interface Title {

    @OverridesAttribute(constraint = Size.class, name = "min")
    int min() default 3;
    @OverridesAttribute(constraint = Size.class, name = "max")
    int max() default 100;

    String message() default "Title can't be empty or less than {min} and bigger than {max} characters";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
