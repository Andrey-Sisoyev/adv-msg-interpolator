package home.lang.jsr303mod.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.logging.Logger;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AlwaysFail.ToFailValidationValidator.class)
@Target({ TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface AlwaysFail {
    String message() default "{${rb=home.lang.jsr303mod.validator.vm)AlwaysFail.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        AlwaysFail[] value();
    }

    public class ToFailValidationValidator implements ConstraintValidator<AlwaysFail, Object> {
        private static final Logger logger = Logger.getLogger(ToFailValidationValidator.class.getSimpleName());

        public void initialize(AlwaysFail _constraintAnnotation) {}

        public boolean isValid(Object _value, ConstraintValidatorContext context) {
            return false;
        }
    }
}