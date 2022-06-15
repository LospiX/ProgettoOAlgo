package xpKernelSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Candidato {
    private List<SubSet> sets;
    private boolean failed;
    private int numOfTries = 0;
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
        this.numOfTries++;
    }

    /**
     *
     * @return true if has failed the last time otherwise return false
     */
    public boolean hasFailed(){
        return this.failed;
    }
    public int getNumOfFails(){
        return this.numOfTries;
    }

    public void resetFailed() {
        this.failed = false;
        this.numOfTries = 0;
    }

    public void printCandidatoStatus() {
        System.out.println("Dalla famiglia "+ famiglia.getVarName() +
            "  sono selezionati "+ this.sets.size() + " subsets a parire dal " +
            (famiglia.getIndexOfLastSubset()-this.sets.size()) + " subset. In totale la famiglia conta: "+ famiglia.getNumOfSubsets() );
    }
}
