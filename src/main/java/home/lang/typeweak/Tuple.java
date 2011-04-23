package home.lang.typeweak;

import home.lang.ImpossibleException;

import java.util.Map;

// thread safe
public class Tuple<T1, T2> implements ITuple<T1, T2>, Map.Entry<T1,T2>, Cloneable {

    // ================================
    // NON-STATIC STUFF

    private volatile T1 first;
    private volatile T2 second;

    public Tuple () {
        first = null;
        second = null;
    }
    public Tuple (T1 t1, T2 t2) {
        first = t1;
        second = t2;
    }
    public T1 getFirst() { return first; }
    public T2 getSecond() { return second; }
    public void setFirst(T1 t1) { first = t1; }
    public void setSecond(T2 t2) { second = t2; }

    @Override public T1 getKey() { return getFirst(); }
    @Override public T2 getValue() { return getSecond(); }
    @Override public T2 setValue(T2 t2) { T2 old = getSecond(); setSecond(t2); return old; }

    @Override
    public Tuple<T1,T2> clone() {
        try { return (Tuple<T1,T2>) super.clone(); } catch (CloneNotSupportedException e) { throw new ImpossibleException(e); }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tuple<T1, T2> other = (Tuple<T1, T2>) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.first != null ? this.first.hashCode() : 0);
        hash = 59 * hash + (this.second != null ? this.second.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
