package xpKernelSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Candidato {
    private List<SubSet> sets;
    private boolean failed;
    private Famiglia famiglia;
    public Candidato(Famiglia famiglia) {
        this.sets= new ArrayList<>();
        this.famiglia = famiglia;
        failed= false;
    }

    public void addSubSet() {
        this.sets.add(this.famiglia.getNextSubset());
    }
    public void removeSubSet(SubSet s) {
        this.sets.remove(s);
    }
    public int getDim(){
        int dim= 0;
        for(SubSet s : this.sets){
            dim+= s.getDim();
        }
        return dim;
        //return sets.parallelStream().mapToInt(SubSet::getDim).sum();
    }
    public List<SubSet> getSubsets(){
        return this.sets;
    }
    public void setFailed(){
        this.failed = true;
    }

    /**
     *
     * @return true if has failed the last time otherwise return false
     */
    public boolean hasFailed(){
        return this.failed;
    }

    public void resetFailed() {
        this.failed = false;
    }
}
