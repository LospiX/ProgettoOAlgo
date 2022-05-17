package xpKernelSearch;

import kernel.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Famiglia implements Item {
    private double RC;
    private final String id;

    private boolean isPartOfSolution = false;
    private int indexOfLastSubsetSelected;
    private final List<Variabile> variablesOrdered;
    private final int setUpCost;
    private final int pesoFamiglia;
    private List<List<Variabile>> subsetItems;

    private Comparator< Variabile> comparator = (v1, v2) -> {
        if(v1.getRapportoProfPeso()>v2.getRapportoProfPeso())
            return  -1;
        else
            return 1;
    };
    private double Xr;

    public Famiglia(String id, List<Variabile> variables, int setUpCost, int pesoFamiglia) {
        this.id = id;
        this.variablesOrdered = variables.stream().sorted(comparator).collect(Collectors.toList());;
        this.setUpCost = setUpCost;
        this.pesoFamiglia = pesoFamiglia;
        this.subsetItems= new ArrayList<>();
        this.subSetGenerator();
        this.indexOfLastSubsetSelected=0;
        //this.var = new FamilyVar(id, variables.stream().map(v -> new ItemVar(v.id(), this.var)).collect(Collectors.toList()));
        //this.var = new FamilyVar(id, variables.stream().map(v -> new ItemVar(v.id(), this.var)).collect(Collectors.toList()));

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
                this.subsetItems.add(subSet);
                subSet = new ArrayList<>();
                sum = 0;
            }
        }
        /*System.out.println("Famiglie.susetGen  Dim of all subset  "+this.subsetItems.size());
        for(var v1 : this.subsetItems){
            System.out.println("\t subset dim: "+v1.size());
        }*/
        if(subSet.size()>0)
            this.subsetItems.add(subSet);
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

    public List<Variabile> getNextSubset() {
        if(this.indexOfLastSubsetSelected <= this.subsetItems.size()-1){
            return this.subsetItems.get(this.indexOfLastSubsetSelected++);
        }
        return new ArrayList<>();
    }

    @Override
    public String getVarName() {
        return this.id;
    }

    @Override
    public double getXr() {
        return Xr;
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
}
