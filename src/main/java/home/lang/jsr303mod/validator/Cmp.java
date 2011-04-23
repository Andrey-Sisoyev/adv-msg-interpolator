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
@Constraint(validatedBy = Cmp.LongCmpValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface Cmp {
    String message() default "{home.lang.jsr303mod.validator.Cmp.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    long value();
    public enum REL { LT,LT_EQ,EQ,GT,GT_EQ;
        @Override
        public String toString() {
            return toString_property();
        }
        public String toString_property() {
            switch(this) {
                case LT   : return "{home.lang.jsr303mod.validator.Cmp.REL.LT}";
                case LT_EQ: return "{home.lang.jsr303mod.validator.Cmp.REL.LT_EQ}";
                case    EQ: return "{home.lang.jsr303mod.validator.Cmp.REL.EQ}";
                case GT   : return "{home.lang.jsr303mod.validator.Cmp.REL.GT}";
                case GT_EQ: return "{home.lang.jsr303mod.validator.Cmp.REL.GT_EQ}";
            }
            throw new UnsupportedOperationException();
        }
        public String toString_common() { return super.toString(); }
        public String toString_math() { switch(this) {
                case LT   : return "<";
                case LT_EQ: return "\u2264";
                case    EQ: return "=";
                case GT   : return ">";
                case GT_EQ: return "\u2265";
            }
            throw new UnsupportedOperationException();
        }
    }
    REL prop_rel_cnstr();

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Cmp[] value();
    }

    class LongCmpValidator implements ConstraintValidator<Cmp, Number> {
        long cnstr_val;
        REL prop_rel_cnstr;

        public void initialize(Cmp constraintAnnotation) {
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
                int cmp = ( ( BigInteger ) _value ).compareTo( BigInteger.valueOf( cnstr_val ) );
                switch(prop_rel_cnstr) {
                    case LT   : return cmp <  0;
                    case LT_EQ: return cmp <= 0;
                    case    EQ: return cmp == 0;
                    case GT   : return cmp >  0;
                    case GT_EQ: return cmp >= 0;
                }
            } else if(_value instanceof BigDecimal) {
                int cmp = ( ( BigInteger ) _value ).compareTo( BigInteger.valueOf( cnstr_val ) );
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

