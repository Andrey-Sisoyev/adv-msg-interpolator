// $Id: ResourceBundleMessageInterpolator.java 19777 2010-06-21 13:35:31Z hardy.ferentschik $
/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package home.lang.jsr303mod.rbmsginterpolator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.MessageInterpolator;

import home.lang.HomeUtils;
import org.hibernate.validator.resourceloading.CachingResourceBundleLocator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.resourceloading.ResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource bundle backed message interpolator.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 *
 * Modding author:
 * @author Andrey Sisoyev
 */
public class AdvRBMsgInterpolator implements MessageInterpolator {
    private static final Logger logger = LoggerFactory.getLogger(AdvRBMsgInterpolator.class.getSimpleName());

    private static final boolean CFG__DEBUG_MODE = false;

	private static final Pattern PATTERN__RB_PROPERTY    = Pattern.compile( "(\\{(?:\\$\\{rb=([^\\}]+?)\\})?([^\\}]+?)\\})"); // (?:(?:(rb_name))(msg_key))
    private static final Pattern PATTERN__ANNOT_PROPERTY = Pattern.compile( "(\\{(?:\\$@)?([^\\}]+?)\\})"); // (?:(?:)(msg_key)) // in order to separate RB propertiies from annotaion ones, we now provide way for latter ones to be denoted with $@. F.e., {$@min}. Old syntax works as well.

    private static final int CFG__MAX_RECURSION_DEPTH = 50;
    private static final String ERR__MAINRECURSION_STACK_OVERFLOW_MSG = "Reached CFG__MAX_RECURSION_DEPTH (which is %1$d) in the message interpolation recursion, that processes both RB and annotation properties. The resolution was halted with message last state ('#!#!#!#' is used as quote): #!#!#!#%2$s#!#!#!#";
    private static final String ERR__SUBCYCLE_STACK_OVERFLOW_MSG      = "Reached CFG__MAX_RECURSION_DEPTH (which is %1$d) in the message interpolation sub-cycle, that processes only RB properties. The resolution was halted with message last state, that is assummed to have RB property that is involved in an infinite cycle of mutially referring RB properties. This message last state is: '%2$s'.";

    // resource bundles locatable by locators above are considered static
	private static final ConcurrentHashMap<LocalisedMessage, String> CACHE__RESOLVED_MESSAGES = new ConcurrentHashMap<LocalisedMessage, String>();
    public static Map<LocalisedMessage, String> getCACHE__RESOLVED_MESSAGES() { return Collections.unmodifiableMap(CACHE__RESOLVED_MESSAGES); }
    /**
	 * The default locale in the current JVM.
	 */
	public static String prettyPrintMsgsCache()  { return HomeUtils.prettyPrintMap(CACHE__RESOLVED_MESSAGES, "CACHE__RESOLVED_MESSAGES"); }

    // ====================================
    // NON-STATIC STUFF

    private boolean CFG__CACHE_ANNOT_PROP_VALS_TOO = true;
    private final Locale defaultLocale;
    private final ResourceBundleLocator[] rbls;

    // ====================================
    // CONSTRUCTORS

	public AdvRBMsgInterpolator() {
		this( null );
	}

    public AdvRBMsgInterpolator(ResourceBundleLocator[] defaultRBLs) {
        this( defaultRBLs, true );
    }

    /**
     *
     * @param defaultRBLs Default resource bundle locators, where to look for properties if resource bundle is not specified. Usually, it's a locator for ValidationMessages.properties used by user of hibernate-validators, and an inner hibernate-validator locator for org.hibernate.validator.ValidationMessages .
     * @param cache_annot_prop_vals_too You usually want it true, unless you have somehow highly variable annotation parameters and in large amount.
     */
    public AdvRBMsgInterpolator(ResourceBundleLocator[] defaultRBLs, boolean cache_annot_prop_vals_too) {
        defaultLocale = Locale.getDefault();

        rbls = defaultRBLs;

        CFG__CACHE_ANNOT_PROP_VALS_TOO = cache_annot_prop_vals_too;
	}

    // ====================================
    // GETTERS-SETTERS

    // ====================================
    // METHODS

	public String interpolate(String message, Context context) {
		// probably no need for caching, but it could be done by parameters since the map
		// is immutable and uniquely built per Validation definition, the comparison has to be based on == and not equals though
		return interpolateMessage( message, context.getConstraintDescriptor().getAttributes(), context.getValidatedValue(), defaultLocale );
	}

	public String interpolate(String message, Context context, Locale locale) {
		return interpolateMessage( message, context.getConstraintDescriptor().getAttributes(), context.getValidatedValue(), locale );
	}

    private class MessageResolutionState {
        public String message;

        private MessageResolutionState(String _message) { message = _message; }
        public void reset(String _message) { message = _message; }
        public void takeCareOfEscapedLiterals() {
            this.message = this.message.replace( "\\{", "{" );
            this.message = this.message.replace( "\\}", "}" );
            this.message = this.message.replace( "\\\\", "\\" );
        }
    }

	/**
	 * Runs the message interpolation according to algorithm specified in JSR 303.
	 * <br/>
	 * Note:
	 * <br/>
	 * Look-ups in user bundles is recursive whereas look-ups in default bundle are not!
	 *
	 *
     * @param message the message to interpolate
     * @param annotationParameters the parameters of the annotation for which to interpolate this message
     * @param _validatedValue The value, that was validated.
     * @param locale the {@code Locale} to use for the resource bundle.
     * @return the interpolated message.
	 */
	private String interpolateMessage(String message, Map<String, Object> annotationParameters, Object _validatedValue, Locale locale) {
        // _validatedValue currently not used here.
        // It would be cool, if there was a @ValueDescriptor annotation,
        // that would be applicable just like a common validator constraint annotation,
        // but accessible from here. I could contain value representation formatter property, that would be
        // resolvable just like annotations here.

        ResourceBundle[] dflt_rbs = new ResourceBundle[this.rbls.length];
        for(int i = 0; i < this.rbls.length; i++) dflt_rbs[i] = this.rbls[i].getResourceBundle(locale);

        MessageResolutionState walker = new MessageResolutionState(message); // optimization

        InnerResolver ir = new InnerResolver(walker, dflt_rbs, locale, annotationParameters);
        ir.performResolution();

		walker.takeCareOfEscapedLiterals();

		return walker.message;
	}

    private class InnerResolver { // recursion context incapsulation
        final MessageResolutionState walker;
        final ResourceBundle[] dflt_bundles; // assumed, that first is userValidationMessagesRBLocator, second - innerCoreValidationMessagesRBLocator
        final Locale locale;
        final Map<String, Object> annotationParameters;

        public InnerResolver(MessageResolutionState _walker, ResourceBundle[] _dflt_bundles, Locale _locale, Map<String, Object> _annotationParameters) {
            walker = _walker;
            dflt_bundles = _dflt_bundles;
            locale = _locale;
            annotationParameters = _annotationParameters;
        }

        public void performResolution() { this.processRBProperties(0); }

        private void processRBProperties(int cur_depth) {
            if(CFG__DEBUG_MODE) {
                logger.info(String.format("Entered processRBProperties(%1$d)", cur_depth));
                logger.info(String.format("Walker state: %1$s", walker.message));
            }

            if(cur_depth > CFG__MAX_RECURSION_DEPTH) throw new StackOverflowError(String.format(ERR__MAINRECURSION_STACK_OVERFLOW_MSG, CFG__MAX_RECURSION_DEPTH, walker.message));

            String ret;
            LocalisedMessage localisedMessage = new LocalisedMessage( walker.message, locale, dflt_bundles );
            ret = CACHE__RESOLVED_MESSAGES.get( localisedMessage );
            if(ret != null) {
                walker.message = ret;
                this.processAnnotationProperties(cur_depth + 1);
                return;
            }

            Matcher matcher = PATTERN__RB_PROPERTY.matcher( walker.message );
            StringBuffer sb;
            boolean resolved_some_rb_props;

            int pass = 0;
            do {
                pass++; if(pass > CFG__MAX_RECURSION_DEPTH) throw new StackOverflowError(String.format(ERR__SUBCYCLE_STACK_OVERFLOW_MSG, CFG__MAX_RECURSION_DEPTH, walker.message));
                resolved_some_rb_props = false;

                // logger.info("Before processRBProperties pass " + pass + ":" + origMsg);

                sb = new StringBuffer();
                while ( matcher.find() ) {
                    String rb_name = matcher.group( 2 );
                    String msg_key = matcher.group( 3 );
                    RBMsgKeyResolved resolved_key = RBMsgKeyResolved.resolve(new RBKeyedMsgKey(rb_name, msg_key), locale, dflt_bundles);

                    if(resolved_key.isMsgResolved()) {
                        if(CFG__DEBUG_MODE) {
                            logger.info(String.format("Resolved RB property '%1$s'.", matcher.group( 1 )));
                            logger.info(String.format("Resolution: %1$s", resolved_key.toString()));
                        }

                        resolved_some_rb_props = true;
                        matcher.appendReplacement( sb, Matcher.quoteReplacement( resolved_key.getMsg() ) );
                    }
                }
                matcher.appendTail( sb );

                if(!resolved_some_rb_props) {
                    walker.message = sb.toString();
                    break;
                }

                matcher.reset(sb.toString());
                // logger.info("After processRBProperties pass " + pass + ":" + origMsg);
            } while(true);

            if(CFG__CACHE_ANNOT_PROP_VALS_TOO || cur_depth == 0)
                ret = CACHE__RESOLVED_MESSAGES.putIfAbsent( localisedMessage, walker.message );
            if(ret != null) walker.message = ret;

            processAnnotationProperties(cur_depth + 1);
        }

        private void processAnnotationProperties(int cur_depth) {
            if(CFG__DEBUG_MODE) {
                logger.info(String.format("Entered processAnnotationProperties(%1$d)", cur_depth));
                logger.info(String.format("Walker state: %1$s", walker.message));
                logger.info(HomeUtils.prettyPrintMap(this.annotationParameters, "Annotation parameters"));
            }

            if(cur_depth > CFG__MAX_RECURSION_DEPTH) throw new StackOverflowError(String.format(ERR__MAINRECURSION_STACK_OVERFLOW_MSG, CFG__MAX_RECURSION_DEPTH, walker.message));

            Matcher matcher = PATTERN__ANNOT_PROPERTY.matcher( walker.message );
            StringBuffer sb = new StringBuffer();
            while ( matcher.find() ) {
                String resolvedParameterValue;
                String parameter = matcher.group( 2 );

                Object variable = annotationParameters.get(parameter);
                if ( variable != null ) {
                    walker.reset(variable.toString()); // temporarily reuse current walker for deeper resolution
                    this.processRBProperties(cur_depth + 1);
                    resolvedParameterValue = walker.message;
                    if(CFG__DEBUG_MODE) {
                        logger.info(String.format("Resolved annotation property '%1$s'.", variable.toString()));
                        logger.info(String.format("Resolution: %1$s", resolvedParameterValue));
                    }
                }
                else {
                    boolean contains_key = annotationParameters.containsKey(parameter);
                    if(!contains_key)
                         resolvedParameterValue = matcher.group( 1 );
                    else resolvedParameterValue = "-null-";

                }
                resolvedParameterValue = Matcher.quoteReplacement( resolvedParameterValue );
                matcher.appendReplacement( sb, resolvedParameterValue );
            }
            matcher.appendTail( sb );
            walker.message = sb.toString();
        }
    }

    private static class LocalisedMessage { // used for caching
		private final String message;
        private final Locale locale;
        private final ResourceBundle[] user_rbs;

		LocalisedMessage(String _message, Locale _locale, ResourceBundle[] _user_rbs) {
			this.message = _message;
			this.locale  = _locale;
            this.user_rbs = _user_rbs;
		}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LocalisedMessage)) return false;

            LocalisedMessage that = (LocalisedMessage) o;

            if (!locale.equals(that.locale)) return false;
            if (!message.equals(that.message)) return false;
            if (!Arrays.equals(user_rbs, that.user_rbs)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = message.hashCode();
            result = 31 * result + locale.hashCode();
            result = 31 * result + (user_rbs != null ? Arrays.hashCode(user_rbs) : 0);
            return result;
        }

        @Override
        public String toString() {
            return "LocalisedMessage{" +
                    "message='" + message + '\'' +
                    ", locale=" + locale +
                    ", user_rbs=" + (user_rbs == null ? null : Arrays.asList(user_rbs)) +
                    '}';
        }
    }
}

