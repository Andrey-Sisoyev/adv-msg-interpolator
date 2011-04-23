package home.lang;

import java.text.MessageFormat;
import java.util.*;

public class HomeUtils {
    protected HomeUtils() {}
    public static <E> List<E> it2list(Iterator<E> it, List<E> list) {
        while(it.hasNext()) list.add(it.next());
        return list;
    }

    // for use in JSF2 EL
    // <h:commandButton value="#{f:geti18nMsg(myBundle , parametricMessage, someParameterValue)}"/>
    public static String geti18nMsg(ResourceBundle bundle ,String msgKey, String paramValue ) {
        String  msgValue = bundle.getString(msgKey);
        MessageFormat messageFormat = new MessageFormat(msgValue);
        Object[] args = {paramValue};
        return messageFormat.format(args);
    }

    public static String prettyPrintMap(Map map, String name ) {
        String ret = name + " {";
        for(Map.Entry me : (Set<Map.Entry>) map.entrySet()) {
            ret += "\n   KEY : " + me.getKey().toString();
            ret += "\n   VAL : " + me.getValue().toString();
            ret += "\n";
        }
        ret += "\n}";
        return ret;
    }
}
