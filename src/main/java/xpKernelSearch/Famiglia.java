package xpKernelSearch;

import kernel.Item;
import kernel.ItemSorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Famiglia implements Item {
    private double RC;
    private final String id;

    private boolean isPartOfSolution = false;
    private int indexOfLastSubsetSelected;
    private final List<Variabile> variablesOrdered;
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
        this.variablesOrdered = sorter.sort(variables);
        this.setUpCost = setUpCost;
        this.pesoFamiglia = pesoFamiglia;
        this.subsetItems= new ArrayList<>();
        this.subSetGenerator();
        this.indexOfLastSubsetSelected=0;
    }
    public void setPartOfSolution(){
        this.isPartOfSolution = true;
    }

    public void subSetGenerator() {
        int sum = 0;
        List<Variabile> subSet = new ArrayList<>();
        Variabile v;
        for(int i = 0; i< this.variablesOrdered.size(); i++){
            v = this.variablesOrdered.get(i);
            sum += v.getProfitto();
            subSet.add(v);
            if(sum > Math.abs(this.setUpCost)) {
                this.subsetItems.add(new SubSet(subSet));
                subSet = new ArrayList<>();
                sum = 0;
            }
        }
        if(subSet.size()>0)
            this.subsetItems.add(new SubSet(subSet));
        System.out.println("Famiglia: "+this.id+ "   contains: "+this.subsetItems.stream().map(e -> e.getDim()).mapToInt(Integer::intValue).sum()+" items in "+this.subsetItems.size()+ " subsets.");
        for(var v1 : this.subsetItems){
            System.out.println("\t subset dim: "+v1.getDim());
            v1.getSet().forEach(v2 -> System.out.println("Var:: "+v2 + "  prof: "+v2.getProfitto()+ "  peso: "+v2.getPeso()+ "  rapporto: "+v2.getRapportoProfPeso()));
        }
    }

    @Override
    public String toString(){
        String result ="";
        result += "Famiglia:: "+ id+ "   setupCost:: "+ setUpCost+ "  pesoFamiglia:: "+pesoFamiglia+"\n";
        for(Variabile vr: this.variablesOrdered){
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
    public List<Variabile> getVariablesOrdered () {
        return this.variablesOrdered;
    }
}
