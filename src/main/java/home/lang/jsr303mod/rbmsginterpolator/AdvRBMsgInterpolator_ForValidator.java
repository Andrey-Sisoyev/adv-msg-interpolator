package home.lang.jsr303mod.rbmsginterpolator;

import org.hibernate.validator.resourceloading.CachingResourceBundleLocator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.resourceloading.ResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.MessageInterpolator;
import java.util.Locale;

public class AdvRBMsgInterpolator_ForValidator implements MessageInterpolator {
    private static final Logger logger = LoggerFactory.getLogger(AdvRBMsgInterpolator_ForValidator.class.getSimpleName());

    private static final String USER_VALIDATION_MESSAGES = "ValidationMessages";
    private static final String DEFAULT_VALIDATION_MESSAGES = "org.hibernate.validator.ValidationMessages";
    private static final ResourceBundleLocator USER_DFLT_LOCATOR_SINGLETON  = new PlatformResourceBundleLocator( USER_VALIDATION_MESSAGES );
    private static final ResourceBundleLocator INNER_DFLT_LOCATOR_SINGLETON = new PlatformResourceBundleLocator( DEFAULT_VALIDATION_MESSAGES );
    // ================================
    // NON-STATIC STUFF

    private final AdvRBMsgInterpolator delegate;

    // ================================
    // CONSTRUCTORS
    public AdvRBMsgInterpolator_ForValidator() {
        this(USER_DFLT_LOCATOR_SINGLETON);
    }
    public AdvRBMsgInterpolator_ForValidator(ResourceBundleLocator _userValidationMessagesRBLocator) {
        ResourceBundleLocator uvmrbl;
        if ( _userValidationMessagesRBLocator == null )
             uvmrbl = USER_DFLT_LOCATOR_SINGLETON;
        else if(_userValidationMessagesRBLocator instanceof CachingResourceBundleLocator)
             uvmrbl = _userValidationMessagesRBLocator; // hopefully, it's not some subclass that ruins caching
        else uvmrbl = new CachingResourceBundleLocator(_userValidationMessagesRBLocator);

        delegate = new AdvRBMsgInterpolator(new ResourceBundleLocator[] {uvmrbl, INNER_DFLT_LOCATOR_SINGLETON}, true);
    }

    // ================================
    // GETTERS/SETTERS

    // ================================
    // METHODS

    @Override
    public String interpolate(String s, Context _context) {
        return delegate.interpolate(s, _context);
    }

    @Override
    public String interpolate(String s, Context _context, Locale _locale) {
        return delegate.interpolate(s, _context, _locale);
    }

    // ================================
    // LOW-LEVEL OVERRIDES
}
