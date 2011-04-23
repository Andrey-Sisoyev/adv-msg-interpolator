package home.lang.jsr303mod;

import home.lang.HomeUtils;
import home.lang.jsr303mod.rbmsginterpolator.AdvRBMsgInterpolatorTest;
import home.lang.jsr303mod.rbmsginterpolator.AdvRBMsgInterpolator_ForValidator;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.*;
import javax.validation.constraints.Size;
import java.util.*;

public class ValidationMessagesLocalizationTest {
    private static final Logger logger = LoggerFactory.getLogger(AdvRBMsgInterpolatorTest.class.getSimpleName());

    private static Validator lv_validator;
    private static Validator ru_validator;


    @BeforeClass
    public static void setUp_val() {
        Locale.setDefault(new Locale("lv"));

        Configuration configuration = Validation.byProvider(HibernateValidator.class).configure();
        ValidatorFactory factory = configuration
            .messageInterpolator(new AdvRBMsgInterpolator_ForValidator(new PlatformResourceBundleLocator("ValidationMessages_test")))
            .buildValidatorFactory();

        lv_validator = factory.getValidator();

        Locale.setDefault(new Locale("ru"));

        configuration = Validation.byProvider(HibernateValidator.class).configure();
        factory = configuration
            .messageInterpolator(new AdvRBMsgInterpolator_ForValidator(new PlatformResourceBundleLocator("ValidationMessages_test")))
            .buildValidatorFactory();

        ru_validator = factory.getValidator();
        Locale.setDefault(new Locale("en"));
    }

    @BeforeMethod
    public static void beforeTest() {
        logger.info("- - - - - -");
    }

    public void outputValidationResult(Set<ConstraintViolation> cnstr_violations) {
        logger.info("Constraints violations:" + String.valueOf(cnstr_violations.size()));
        for(ConstraintViolation cviol : HomeUtils.it2list(cnstr_violations.iterator(), new LinkedList<ConstraintViolation>()))
            logger.info(cviol.getMessage());
    }

    private static class ToFailValidation {
        @Size(min = 3, max = 6)
        List<String> lis;
        private ToFailValidation() { lis = Arrays.asList("111", "222"); }
    }

    @Test
    public void t_lvMsgs() {
    logger.info("t_lvMsgs >>>");
        ToFailValidation tobj = new ToFailValidation();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;

        try {
            logger.info("");
            logger.info("Latvian Size constraint violation message:");
            logger.info("");
            constraintViolations = (Set) lv_validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_msg_failed = true;
        }
        Assert.assertFalse(validation_msg_failed);
        Assert.assertEquals("kollekcijai j\u0101satur no 3 l\u012bdz 6 elementiem", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void t_ruMsgs() {
    logger.info("t_ruMsgs >>>");
        ToFailValidation tobj = new ToFailValidation();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;

        try {
            logger.info("");
            logger.info("Russian Size constraint violation message:");
            logger.info("");
            constraintViolations = (Set) ru_validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_msg_failed = true;
        }
        Assert.assertFalse(validation_msg_failed);
        Assert.assertEquals("\u0440\u0430\u0437\u043c\u0435\u0440 \u043a\u043e\u043b\u043b\u0435\u043a\u0446\u0438\u0438 \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u043e\u0442 3 \u0434\u043e 6 \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u043e\u0432", constraintViolations.iterator().next().getMessage());
    }

}
