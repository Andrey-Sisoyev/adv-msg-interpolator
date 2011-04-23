package home.lang.jsr303mod;

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
@Constraint(validatedBy = ValidatorAnnotationTest.ValidatorAnnotationTestValidator.class)
@Target({ TYPE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface ValidatorAnnotationTest {
    String message() default "{home.lang.jsr303mod.validator.ValidatorAnnotationTest.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String prop_1() default "A";
    String prop_2() default "E";
    String prop_3() default "I";

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ValidatorAnnotationTest[] value();
    }

    public class ValidatorAnnotationTestValidator implements ConstraintValidator<ValidatorAnnotationTest, Object> {
        private static final Logger logger = Logger.getLogger(ValidatorAnnotationTestValidator.class.getSimpleName());

        public void initialize(ValidatorAnnotationTest _constraintAnnotation) {}

        public boolean isValid(Object _value, ConstraintValidatorContext context) {
            return false;
        }
    }
}
