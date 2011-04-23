package home.lang.typeweak;

// import home.lang.Nothing;
// import home.lang.functional.units.ILa1;

import java.util.*;

// not thread safe
public class TuplesList<F,S> {
    // ================================
    // NON-STATIC STUFF

    private List<Tuple<F,S>> ptList;

    public TuplesList() {
        ptList = new LinkedList();
    }

    public TuplesList<F,S> add(F f, S s) {
        Tuple<F,S> pt = new Tuple(f, s);
        ptList.add(pt);
        return this;
    }

    public List<Tuple<F, S>> getList() {
        return ptList;
    }

    public TuplesList<F,S> setList(List<Tuple<F, S>> ptList) {
        this.ptList = ptList;
        return this;
    }

    public Map<F,S> toMap(Map<F,S> initialMap) {
        Iterator<Tuple<F, S>> it = ptList.iterator();
        Tuple<F,S> entry;
        while(it.hasNext()) {
            entry = it.next();
            initialMap.put(entry.getFirst(), entry.getSecond());
        }
        return initialMap;
    }

    public Map<F,S> toTreeMap() {
        return this.toMap(new TreeMap());
    }

    public TuplesList<F,S> zip(F[] fArr, S[] sArr) {
        int farr_len = fArr.length;
        int sarr_len = sArr.length;
        int len = Math.max(farr_len, sarr_len);
        for(int i = 0; i < len ; i++) {
            if(i >= farr_len) {
                add(null, sArr[i]);
            } else if (i >= sarr_len) {
                add(fArr[i], null);
            } else
                add(fArr[i], sArr[i]);
        }
        return this;
    }

    /*
    public TuplesList<F,S> mapZip(F[] fArr, ILa1<S,F> la) {
        int farr_len = fArr.length;
        for(F el : fArr) add(el, la.eval_1(el));
        return this;
    }

    public TuplesList<F,S> mapZipR(S[] fArr, ILa1<F,S> la) {
        int farr_len = fArr.length;
        for(S el : fArr) add(la.eval_1(el), el);
        return this;
    }
    */
            
}
