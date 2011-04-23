package home.lang.jsr303mod.rbmsginterpolator;

public class RBKeyedMsgKey {
    // ================================
    // NON-STATIC STUFF
    private String msg_key;
    private String rb_name;

    // ================================
    // CONSTRUCTORS

    public RBKeyedMsgKey(String _rb_name, String _msg_key) {
        msg_key = _msg_key;
        rb_name = _rb_name;
    }


    // ================================
    // GETTERS/SETTERS
    public String getMsgKey() {
        return msg_key;
    }

    public String getRBName() {
        return rb_name;
    }

    // ================================
    // METHODS

    // ================================
    // LOW-LEVEL OVERRIDES

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RBKeyedMsgKey)) return false;

        RBKeyedMsgKey rbKeyedMsgKey = (RBKeyedMsgKey) o;

        if (!msg_key.equals(rbKeyedMsgKey.msg_key)) return false;
        if (rb_name != null ? !rb_name.equals(rbKeyedMsgKey.rb_name) : rbKeyedMsgKey.rb_name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = msg_key.hashCode();
        result = 31 * result + (rb_name != null ? rb_name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RBKeyedMsgKey{" +
                "msg_key='" + msg_key + '\'' +
                ", rb_name='" + rb_name + '\'' +
                '}';
    }
}
