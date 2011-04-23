package home.lang.jsr303mod.rbmsginterpolator;

import home.lang.HomeUtils;
import home.lang.typeweak.Tuple;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

// thread-safety by immutability
public class RBMsgKeyResolved {
    private static final Logger logger = LoggerFactory.getLogger(RBMsgKeyResolved.class.getSimpleName());

    private static final ConcurrentMap<Tuple<String       , Locale>, ResourceBundle> CACHE__RBs_BY_NAMES        = new ConcurrentHashMap<Tuple<String,Locale>,  ResourceBundle>();
    private static final ConcurrentMap<Tuple<RBKeyedMsgKey, Locale>, RBMsgKeyResolved> CACHE__RESOLVED_PROPERTIES = new ConcurrentHashMap<Tuple<RBKeyedMsgKey,Locale>, RBMsgKeyResolved>();
    public static Map<Tuple<String       , Locale>, ResourceBundle> getCACHE__RBs_BY_NAMES()        { return Collections.unmodifiableMap(CACHE__RBs_BY_NAMES); }
    public static Map<Tuple<RBKeyedMsgKey, Locale>, RBMsgKeyResolved> getCACHE__RESOLVED_PROPERTIES() { return Collections.unmodifiableMap(CACHE__RESOLVED_PROPERTIES); }
    public static String prettyPrintRBsCache()  { return HomeUtils.prettyPrintMap(CACHE__RBs_BY_NAMES, "CACHE__RBs_BY_NAMES"); }
    public static String prettyPrintMsgsCache() { return HomeUtils.prettyPrintMap(CACHE__RESOLVED_PROPERTIES, "CACHE__RESOLVED_PROPERTIES"); }

    public static RBMsgKeyResolved resolve(RBKeyedMsgKey key, Locale loc, ResourceBundle[] default_rbs) {
        // logger.info(key.toString());
        if(key.getRBName() == null) {
            RBMsgKeyResolved ret = new RBMsgKeyResolved(key);
            for(ResourceBundle _rb : default_rbs) {
                ret.rb = _rb;
                if(tryResolveForRB(ret)) return ret;
            }
            // ret.msg = ret.key.getOriginalTag();
            return ret; // property not found
        }
        // else \/

        RBMsgKeyResolved ret;

        // try to get property from the cache
        Tuple<RBKeyedMsgKey,Locale> msg_tu = new Tuple<RBKeyedMsgKey,Locale>(key, loc);
        ret = CACHE__RESOLVED_PROPERTIES.get(msg_tu);
        if(ret != null && ret.msg != null) return ret;

        // property not in the cache, try to get property from the specified RB
        if(ret == null) ret = new RBMsgKeyResolved(key);

        // start by getting specified RB
        Tuple<String,Locale> rb_tu = new Tuple<String,Locale>(key.getRBName(), loc);
        ret.rb = CACHE__RBs_BY_NAMES.get(rb_tu);
        if(ret.rb == null) {
            ret.rb = new PlatformResourceBundleLocator(ret.key.getRBName()).getResourceBundle(loc);
            if(ret.rb != null) {
                ResourceBundle rb_tmp = CACHE__RBs_BY_NAMES.putIfAbsent(rb_tu, ret.rb);
                if(rb_tmp != null) ret.rb = rb_tmp;
            }
        }

        if(ret.rb == null) {
            return ret; // no bundle to work with
                        // we aren't using default RB, since RB name is specified
        }

        // put in the cache for global reuse
        if(tryResolveForRB(ret)) {
            assert ret.msg != null;
            RBMsgKeyResolved msg_tmp = CACHE__RESOLVED_PROPERTIES.putIfAbsent(msg_tu, ret);
            if(msg_tmp != null) ret = msg_tmp;
        }

        return ret;
    }

    private static boolean tryResolveForRB(RBMsgKeyResolved _ret) {
        // logger.info(_ret.toString());
        try {
            _ret.msg = _ret.rb.getString(_ret.key.getMsgKey());
            _ret.msg_resolved = true;
        } catch ( MissingResourceException e ) {
            _ret.msg_resolved = false;
        }
        return _ret.msg_resolved;
    }

    // ================================
    // NON-STATIC STUFF
    private RBKeyedMsgKey key;
    private ResourceBundle rb;
    private String msg;
    private boolean msg_resolved;

    // ================================
    // CONSTRUCTORS
    private RBMsgKeyResolved(RBKeyedMsgKey _key) { msg_resolved = false; key = _key; }

    // ================================
    // GETTERS/SETTERS

    public RBKeyedMsgKey getKey() {
        return key;
    }

    public ResourceBundle getRB() {
        return rb;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isMsgResolved() {
        return msg_resolved;
    }

    // ================================
    // METHODS

    // ================================
    // LOW-LEVEL OVERRIDES

    @Override
    public String toString() {
        return "RBMsgKeyResolved{" +
                "key=" + key +
                ", rb=" + rb +
                ", msg='" + msg + '\'' +
                ", msg_resolved=" + msg_resolved +
                '}';
    }
}
