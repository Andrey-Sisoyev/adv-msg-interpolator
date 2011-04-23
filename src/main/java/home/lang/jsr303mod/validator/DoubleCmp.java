package home.lang.jsr303mod.validator;

import javax.validation.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Documented
@Constraint(validatedBy = DoubleCmp.DoubleCmpValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface DoubleCmp {
    String message() default "{home.lang.jsr303mod.validator.Cmp.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    double value();
    Cmp.REL prop_rel_cnstr();

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        DoubleCmp[] value();
    }

    class DoubleCmpValidator implements ConstraintValidator<DoubleCmp, Number> {
        double cnstr_val;
        Cmp.REL prop_rel_cnstr;

        public void initialize(DoubleCmp constraintAnnotation) {
            cnstr_val = constraintAnnotation.value();
            prop_rel_cnstr = constraintAnnotation.prop_rel_cnstr();
        }

        public boolean isValid(Number _value, ConstraintValidatorContext context) {
            if(_value == null) return true;

            if(       _value instanceof Integer) {
                int value = _value.intValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            } else if(_value instanceof Long) {
                long value = _value.longValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            } else if(_value instanceof Double) {
                double value = _value.doubleValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            } else if(_value instanceof Float) {
                float value = _value.floatValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            } else if(_value instanceof AtomicLong) {
                long value = _value.longValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            } else if(_value instanceof AtomicInteger) {
                int value = _value.intValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            } else if(_value instanceof BigInteger) {
                double value = _value.doubleValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            } else if(_value instanceof BigDecimal) {
                int cmp = ( ( BigDecimal ) _value ).compareTo( BigDecimal.valueOf( cnstr_val ) );
                switch(prop_rel_cnstr) {
                    case LT   : return cmp <  0;
                    case LT_EQ: return cmp <= 0;
                    case    EQ: return cmp == 0;
                    case GT   : return cmp >  0;
                    case GT_EQ: return cmp >= 0;
                }
            } else if(_value instanceof Byte) {
                byte value = _value.byteValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            } else if(_value instanceof Short) {
                short value = _value.shortValue();
                switch(prop_rel_cnstr) {
                    case LT   : return value <  cnstr_val;
                    case LT_EQ: return value <= cnstr_val;
                    case    EQ: return value == cnstr_val;
                    case GT   : return value >  cnstr_val;
                    case GT_EQ: return value >= cnstr_val;
                }
            }
            return true;
        }
    }
}