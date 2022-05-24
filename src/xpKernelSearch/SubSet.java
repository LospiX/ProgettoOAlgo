package xpKernelSearch;

import java.util.List;

public class SubSet {
    private final List<Variabile> set;

    public SubSet(List<Variabile> set) {
        this.set= set;
    }
    public List<Variabile> getSet() {
        return this.set;
    }
    public int getDim() {
        return this.set.size();
    }
}
