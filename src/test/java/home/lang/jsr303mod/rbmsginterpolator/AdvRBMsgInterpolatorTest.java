package home.lang.jsr303mod.rbmsginterpolator;

import home.lang.HomeUtils;
import home.lang.jsr303mod.ValidatorAnnotationTest;
import home.lang.jsr303mod.validator.AlwaysFail;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.validation.*;
import javax.validation.Configuration;
import javax.validation.constraints.Size;
import java.util.*;

public class AdvRBMsgInterpolatorTest {
    private static final Logger logger = LoggerFactory.getLogger(AdvRBMsgInterpolatorTest.class.getSimpleName());
    private static       Validator validator;
    private static       ResourceBundle testobj_1;
    private static       ResourceBundle testobj_2;
    private static       ResourceBundle uvm;

    @BeforeClass
    public static void setUp_rbs() {
        testobj_1 = new PlatformResourceBundleLocator("test_rbmi.businessobj1.messages").getResourceBundle(Locale.getDefault());
        testobj_2 = new PlatformResourceBundleLocator("test_rbmi.businessobj2.messages").getResourceBundle(Locale.getDefault());
        uvm       = new PlatformResourceBundleLocator("ValidationMessages_test").getResourceBundle(Locale.getDefault());
        System.out.println("++++++++++++");
        System.out.println(testobj_1);
        System.out.println(testobj_2);
        System.out.println(uvm);
        System.out.println("++++++++++++");
    }

    @BeforeClass
    public static void setUp_val() {
        Locale.setDefault(Locale.ENGLISH);

        Configuration configuration = Validation.byProvider(HibernateValidator.class).configure();
        ValidatorFactory factory = configuration
            .messageInterpolator(new AdvRBMsgInterpolator_ForValidator(new PlatformResourceBundleLocator("ValidationMessages_test")))
            .buildValidatorFactory();

        validator = factory.getValidator();

    }

    @AfterClass
    public static void finalizeTest() {
        logger.info("///////AdvRBMsgInterpolatorTest TEST_FINALIZATION////////////////////");
        logger.info("AdvRBMsgInterpolator cache 'CACHE__RESOLVED_MESSAGES' content: ");
        logger.info(AdvRBMsgInterpolator.prettyPrintMsgsCache());
        logger.info("RBMsgKeyResolved cache 'CACHE__RBs_BY_NAMES' content: ");
        logger.info(RBMsgKeyResolved.prettyPrintRBsCache());
        logger.info("RBMsgKeyResolved cache 'CACHE__RESOLVED_PROPERTIES' content: ");
        logger.info(RBMsgKeyResolved.prettyPrintMsgsCache());

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

    @AlwaysFail
    private static class ToFailValidation_1 {}

    @Test
    public void t_alwaysFailValidator() {
        logger.info("t_alwaysFailValidator >>>");
        ToFailValidation_1 tobj = new ToFailValidation_1();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_failed = false;
        Throwable validation_exc = null;
        try {
            constraintViolations = (Set) validator.validate(tobj);
            validation_failed = true;
            Assert.assertEquals(constraintViolations.size(), 1);
        } catch (Throwable e) {
            logger.error("", e);
            validation_exc = e;
        }

        Assert.assertTrue(validation_failed);
        Assert.assertNull(validation_exc);
    }

    @AlwaysFail(message = "{test.1.prop1}")
    private static class ToFailValidation_2 {}

    @Test(dependsOnMethods = {"t_alwaysFailValidator"})
    public void t_normalRecursiveInterpolation() {
        logger.info("t_normalRecursiveInterpolation >>>");
        ToFailValidation_2 tobj = new ToFailValidation_2();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;
        Throwable validation_exc = null;
        logger.info("");
        logger.info("Should resolve to ABCDEFGHI:");
        logger.info("");
        try {
            constraintViolations = (Set) validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            validation_msg_failed = true;
            logger.info("", e);
            validation_exc = e;
        }

        Assert.assertFalse(validation_msg_failed);
        Assert.assertNull(validation_exc);
        Assert.assertEquals("ABCDEFGHI", constraintViolations.iterator().next().getMessage());
    }

    @Test(dependsOnMethods = {"t_alwaysFailValidator", "t_normalRecursiveInterpolation"})
    public void t_normalRecursiveInterpolation_UseCache() {
        logger.info("t_normalRecursiveInterpolation_UseCache >>>");
        ToFailValidation_2 tobj = new ToFailValidation_2();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;
        Throwable validation_exc = null;
        try {
            constraintViolations = (Set) validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            validation_msg_failed = true;
            logger.error("", e);
            validation_exc = e;
        }

        Assert.assertEquals("ABCDEFGHI", constraintViolations.iterator().next().getMessage());
        Assert.assertFalse(validation_msg_failed);
        Assert.assertNull(validation_exc);
    }

    @AlwaysFail(message = "{test.2.cyclicproperty}")
    private static class ToFailValidation_3 {}

    @Test(dependsOnMethods = {"t_alwaysFailValidator", "t_normalRecursiveInterpolation", "t_normalRecursiveInterpolation_UseCache"})
    public void t_cyclycPropDependencyStackOverflow() {
        logger.info("t_cyclycPropDependencyStackOverflow >>>");
        ToFailValidation_3 tobj = new ToFailValidation_3();


        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;
        Throwable validation_exc = null;

        try {
            logger.info("");
            logger.info("Following operation must fail with StackOverflowError due to cyclic reference of messages: VMT.test.2.cyclicproperty -> BO_2.test.2.cyclicproperty -> BO_1.test.2.cyclicproperty -> VMT.test.2.cyclicproperty -> ...");
            logger.info("");
            constraintViolations = (Set) validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_msg_failed = true;
            Assert.assertTrue(e instanceof StackOverflowError);
            logger.info("StackOverflowError test successfull!");
            validation_exc = e;
        }

        Assert.assertTrue(validation_msg_failed);
        Assert.assertNotNull(validation_exc);
    }

    private static class ToFailValidation_4 {
        @Size(min = 3, max = 6) List<String> lis;
        private ToFailValidation_4() { lis = Arrays.asList("111","222"); }
    }

    @Test(dependsOnMethods = {"t_alwaysFailValidator", "t_normalRecursiveInterpolation", "t_normalRecursiveInterpolation_UseCache", "t_cyclycPropDependencyStackOverflow"})
    public void t_normalAnnotPropsFail() {
        logger.info("t_normalAnnotPropsFail >>>");
        ToFailValidation_4 tobj = new ToFailValidation_4();


        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;

        try {
            logger.info("");
            logger.info("Following operation must fail to resolve correctly to the 'size must be between {min} and {max}' message (due to conflict of annotation properties and RB properties):");
            logger.info("");
            constraintViolations = (Set) validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_msg_failed = true;
        }
        Assert.assertFalse(validation_msg_failed);
        Assert.assertEquals("size must be between Property \"min\" from user''s ValidationMessages and Property \"max\" from user''s ValidationMessages", constraintViolations.iterator().next().getMessage());
    }

    private static class ToFailValidation_5 {
        @Size(min = 3, max = 6, message="{test.3.minmax.fixed}") List<String> lis;
        private ToFailValidation_5() { lis = Arrays.asList("111","222"); }
    }

    @Test(dependsOnMethods = {"t_alwaysFailValidator", "t_normalRecursiveInterpolation", "t_normalRecursiveInterpolation_UseCache", "t_cyclycPropDependencyStackOverflow", "t_normalAnnotPropsFail"})
    public void t_normalAnnotPropsFixed() {
        logger.info("t_normalAnnotPropsFixed >>>");
        ToFailValidation_5 tobj = new ToFailValidation_5();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;

        try {
            logger.info("");
            logger.info("Following operation is a variant with fixed prev test failure cause - sizes range = {3-6}:");
            logger.info("");
            constraintViolations = (Set) validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_msg_failed = true;
        }
        Assert.assertEquals("size must be between 3 and 6", constraintViolations.iterator().next().getMessage());
        Assert.assertFalse(validation_msg_failed);

    }

    @ValidatorAnnotationTest(message = "{test.4.1.prop1}")
    private static class ToFailValidation_6 {}

    @Test(dependsOnMethods = {"t_alwaysFailValidator", "t_normalRecursiveInterpolation", "t_normalRecursiveInterpolation_UseCache", "t_cyclycPropDependencyStackOverflow", "t_normalAnnotPropsFail", "t_normalAnnotPropsFixed"})
    public void t_complexAdvAnnotPropTest() {
        logger.info("t_complexAdvAnnotPropTest >>>");
        ToFailValidation_6 tobj = new ToFailValidation_6();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;

        try {
            logger.info("");
            logger.info("Must produce message ABCDEFGHI:");
            logger.info("");
            constraintViolations = (Set) validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_msg_failed = true;
        }
        Assert.assertEquals("ABCDEFGHI", constraintViolations.iterator().next().getMessage());
        Assert.assertFalse(validation_msg_failed);

    }

    @ValidatorAnnotationTest(message = "{test.4.2.prop1}")
    private static class ToFailValidation_7 {}

    @Test(dependsOnMethods = {"t_alwaysFailValidator", "t_normalRecursiveInterpolation", "t_normalRecursiveInterpolation_UseCache", "t_cyclycPropDependencyStackOverflow", "t_normalAnnotPropsFail", "t_normalAnnotPropsFixed"})
    public void t_complexLegacyAnnotPropFailingTest() {
        logger.info("t_complexLegacyAnnotPropFailingTest >>>");
        ToFailValidation_7 tobj = new ToFailValidation_7();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_msg_failed = false;

        try {
            logger.info("");
            logger.info("Must fail to produce message ABCDEFGHI, because annotation properties will be treated as RB properties (legacy annotation syntax):");
            logger.info("");
            constraintViolations = (Set) validator.validate(tobj);
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_msg_failed = true;
        }
        Assert.assertEquals("XBCDYFGHZ", constraintViolations.iterator().next().getMessage());
        Assert.assertFalse(validation_msg_failed);
    }
}