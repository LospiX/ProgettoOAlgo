package xpKernelSearch;

import kernel.Item;
import kernel.ItemSorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Famiglia implements Item {
    private  List<Variabile> variables;
    private double RC;
    private final String id;

    private boolean isPartOfSolution = false;
    private int indexOfLastSubsetSelected;

    private final int setUpCost;
    private final int pesoFamiglia;
    private List<SubSet> subsetItems;

    private ItemSorter sorter;

    private Comparator<Variabile> comparator = (v1, v2) -> {
        if(v1.getRapportoProfPeso()>v2.getRapportoProfPeso())
            return  -1;
        else if(v1.getRapportoProfPeso()<v2.getRapportoProfPeso())
            return 1;
        return 0;
    };
    private double Xr;

    public Famiglia(String id, List<Variabile> variables, int setUpCost, int pesoFamiglia, ItemSorter sorter) {
        this.id = id;
        this.sorter = sorter;
        this.variables = variables;
        this.setUpCost = setUpCost;
        this.pesoFamiglia = pesoFamiglia;
        this.subsetItems= new ArrayList<>();
        this.indexOfLastSubsetSelected=0;
    }
    public void setPartOfSolution(){
        this.isPartOfSolution = true;
    }

    public List<Variabile> getVariables() {
        return this.variables;
    }

    public void generateSubsets(double subsetFactor, double decreasingPercentage, double minPercentage) {
        int sum = 0;
        List<Variabile> subSet = new ArrayList<>();
        Variabile v;
        int dimOfFirstSubset = this.calcNumVarToBeatSetupCost(subsetFactor);
        int count = 0;
        int dimNextSubset = dimOfFirstSubset;
        double  percentage = 1;
        for(int i = 0; i< this.variables.size(); i++){
            v = this.variables.get(i);
            subSet.add(v);
            if(count >= dimNextSubset) {
                this.subsetItems.add(new SubSet(subSet));
                percentage = Math.max(percentage - percentage*decreasingPercentage, minPercentage);
                dimNextSubset = (int) Math.round((double) dimOfFirstSubset * percentage);
                subSet = new ArrayList<>();
                count = 0;
            }
            count++;
        }
        if(subSet.size()>0)
            this.subsetItems.add(new SubSet(subSet));
/*
        System.out.println("Famiglia: "+this.id+ "   contains: "+this.subsetItems.stream().map(e -> e.getDim()).mapToInt(Integer::intValue).sum()+" items in "+this.subsetItems.size()+ " subsets.");
        for(var v1 : this.subsetItems){
            System.out.println("\t subset dim: "+v1.getDim());
            v1.getSet().forEach(v2 -> System.out.println("Var:: "+v2 + "  XR: "+v2.getXr()+ "  RC: "+v2.getRc()+ "  rapporto: "+v2.getRapportoProfPeso()));
        }
*/
    }

    private int calcNumVarToBeatSetupCost(double subsetFactor) {
        int sum = 0;
        for(int i = 0; i< this.variables.size(); i++){
            sum += this.variables.get(i).getProfitto();
            if(sum > Math.abs(this.setUpCost)*subsetFactor)
                return i+1;
        }
        return this.variables.size()+1;
    }

    @Override
    public String toString(){
        String result ="";
        result += "Famiglia:: "+ id+ "   setupCost:: "+ setUpCost+ "  pesoFamiglia:: "+pesoFamiglia+"\n";
        for(Variabile vr: this.variables){
            result += "\t"+ vr.toString()+"\n";
        }
        return result;
    }

    public String getName() {
        return this.id;
    }

    /**
     *
     * @return empty SubSet if thereAreNoMore subsets
     */
    public SubSet getNextSubset() {
        if(this.indexOfLastSubsetSelected <= this.subsetItems.size()-1){
            return this.subsetItems.get(this.indexOfLastSubsetSelected++);
        }
        return new SubSet(new ArrayList<>());
    }

    public SubSet getActualSubset() {
        return this.subsetItems.get(this.indexOfLastSubsetSelected);
    }
    @Override
    public String getVarName() {
        return this.id;
    }

    @Override
    public double getXr() {
        return Xr;
    }

    @Override
    public double getRc() {
        return 0;
    }


    @Override
    public double getRapportoProfPeso() {
        return 0;
    }
    public double getRC(){return RC;}
    public void setXr(double xr) {
        this.Xr= xr;
    }
    public void setRc(double rc) {
        this.RC= rc;
    }

    public int getPeso() {
        return this.pesoFamiglia;
    }

    public void sortVariables() {
        this.variables= this.sorter.sort(this.variables);
    }

    public int getIndexOfLastSubset() {
        return this.indexOfLastSubsetSelected;
    }
    public int getNumOfSubsets() {
        return this.subsetItems.size();
    }
}
