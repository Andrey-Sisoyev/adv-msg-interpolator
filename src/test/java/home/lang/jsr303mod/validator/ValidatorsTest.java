package home.lang.jsr303mod.validator;

import home.lang.HomeUtils;
import home.lang.jsr303mod.rbmsginterpolator.AdvRBMsgInterpolator;
import home.lang.jsr303mod.rbmsginterpolator.AdvRBMsgInterpolator_ForValidator;
import home.lang.jsr303mod.rbmsginterpolator.RBMsgKeyResolved;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.*;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;

public class ValidatorsTest {
    private static final Logger logger = LoggerFactory.getLogger(ValidatorsTest.class.getSimpleName());
    static Validator validator;

    @BeforeClass
    public static void setUp_val() {
        Locale.setDefault(Locale.ENGLISH);

        Configuration configuration = Validation.byProvider(HibernateValidator.class).configure();
        ValidatorFactory factory = configuration
            .messageInterpolator(new AdvRBMsgInterpolator_ForValidator(new PlatformResourceBundleLocator("ValidationMessages_test")))
            .buildValidatorFactory();

        validator = factory.getValidator();

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

    // ================================
    // TESTS
    class TestCase_1 {
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.EQ)    Integer i1 = 0;
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.NEQ)   Integer i2 = 0; // X
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.LT)    Integer i3 = 0; // X
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.LT_EQ) Integer i4 = 0;
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.GT)    Integer i5 = 0; // X
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.GT_EQ) Integer i6 = 0;
    }

    class TestCase_2 {
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.EQ)    Integer i1 = -1; // X
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.NEQ)   Integer i2 = -1;
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.LT)    Integer i3 = -1;
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.LT_EQ) Integer i4 = -1;
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.GT)    Integer i5 = -1; // X
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.GT_EQ) Integer i6 = -1; // X
    }

    class TestCase_3 {
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.EQ)    Integer i1 = 1;  // X
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.NEQ)   Integer i2 = 1;
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.LT)    Integer i3 = 1;  // X
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.LT_EQ) Integer i4 = 1;  // X
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.GT)    Integer i5 = 1;
        @Cmp(value = 0, prop_rel_cnstr = Cmp.REL.GT_EQ) Integer i6 = 1;
    }

    class TestCase_4 {
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.EQ)    Integer i1 = 0;
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.NEQ)   Integer i2 = 0; // X
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.LT)    Integer i3 = 0; // X
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.LT_EQ) Integer i4 = 0;
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.GT)    Integer i5 = 0; // X
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.GT_EQ) Integer i6 = 0;
    }

    class TestCase_5 {
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.EQ)    Integer i1 = -1; // X
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.NEQ)   Integer i2 = -1;
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.LT)    Integer i3 = -1;
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.LT_EQ) Integer i4 = -1;
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.GT)    Integer i5 = -1; // X
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.GT_EQ) Integer i6 = -1; // X
    }

    class TestCase_6 {
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.EQ)    Integer i1 = 1;  // X
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.NEQ)   Integer i2 = 1;
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.LT)    Integer i3 = 1;  // X
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.LT_EQ) Integer i4 = 1;  // X
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.GT)    Integer i5 = 1;
        @DoubleCmp(value = 0, prop_rel_cnstr = Cmp.REL.GT_EQ) Integer i6 = 1;
    }


    @Test
    public void t_testCmp_1() {
        logger.info("t_testCmp_1 >>>");
        TestCase_1 tobj = new TestCase_1();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_failed = false;
        Throwable validation_exc = null;
        try {
            constraintViolations = (Set) validator.validate(tobj);
            validation_failed = true;
            Assert.assertEquals(constraintViolations.size(), 3);
            logger.info("Must fail NEQ, LT, GT:");
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_exc = e;
        }

        Assert.assertTrue(validation_failed);
        Assert.assertNull(validation_exc);
    }

    @Test(dependsOnMethods = {"t_testCmp_1"})
    public void t_testCmp_2() {
        logger.info("t_testCmp_2 >>>");
        TestCase_2 tobj = new TestCase_2();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_failed = false;
        Throwable validation_exc = null;
        try {
            constraintViolations = (Set) validator.validate(tobj);
            validation_failed = true;
            Assert.assertEquals(constraintViolations.size(), 3);
            logger.info("Must fail EQ, GT, GT_EQ:");
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_exc = e;
        }

        Assert.assertTrue(validation_failed);
        Assert.assertNull(validation_exc);
    }

    @Test(dependsOnMethods = {"t_testCmp_2"})
    public void t_testCmp_3() {
        logger.info("t_testCmp_3 >>>");
        TestCase_3 tobj = new TestCase_3();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_failed = false;
        Throwable validation_exc = null;
        try {
            constraintViolations = (Set) validator.validate(tobj);
            validation_failed = true;
            Assert.assertEquals(constraintViolations.size(), 3);
            logger.info("Must fail EQ, LT, LT_EQ:");
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_exc = e;
        }

        Assert.assertTrue(validation_failed);
        Assert.assertNull(validation_exc);
    }

    @Test(dependsOnMethods = {"t_testCmp_3"})
    public void t_testCmp_4() {
        logger.info("t_testCmp_4 >>>");
        TestCase_4 tobj = new TestCase_4();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_failed = false;
        Throwable validation_exc = null;
        try {
            constraintViolations = (Set) validator.validate(tobj);
            validation_failed = true;
            Assert.assertEquals(constraintViolations.size(), 3);
            logger.info("Must fail NEQ, LT, GT:");
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_exc = e;
        }

        Assert.assertTrue(validation_failed);
        Assert.assertNull(validation_exc);
    }

    @Test(dependsOnMethods = {"t_testCmp_4"})
    public void t_testCmp_5() {
        logger.info("t_testCmp_5 >>>");
        TestCase_5 tobj = new TestCase_5();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_failed = false;
        Throwable validation_exc = null;
        try {
            constraintViolations = (Set) validator.validate(tobj);
            validation_failed = true;
            Assert.assertEquals(constraintViolations.size(), 3);
            logger.info("Must fail EQ, GT, GT_EQ:");
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_exc = e;
        }

        Assert.assertTrue(validation_failed);
        Assert.assertNull(validation_exc);
    }

    @Test(dependsOnMethods = {"t_testCmp_5"})
    public void t_testCmp_6() {
        logger.info("t_testCmp_6 >>>");
        TestCase_6 tobj = new TestCase_6();

        Set<ConstraintViolation> constraintViolations = null;
        boolean validation_failed = false;
        Throwable validation_exc = null;
        try {
            constraintViolations = (Set) validator.validate(tobj);
            validation_failed = true;
            Assert.assertEquals(constraintViolations.size(), 3);
            logger.info("Must fail EQ, LT, LT_EQ:");
            outputValidationResult(constraintViolations);
        } catch (Throwable e) {
            e.printStackTrace();
            validation_exc = e;
        }

        Assert.assertTrue(validation_failed);
        Assert.assertNull(validation_exc);
    }
}
