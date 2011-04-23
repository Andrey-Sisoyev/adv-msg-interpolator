package home.lang;

public class Nothing {
    public static final Nothing IS = new Nothing();

    protected Nothing() {}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
