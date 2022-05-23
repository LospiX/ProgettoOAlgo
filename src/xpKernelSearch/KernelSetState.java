package xpKernelSearch;

import kernel.Bucket;
import kernel.Item;
import kernel.Kernel;
import kernel.Model;
import xpKernelSearch.Variabile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KernelSetState {
    //private Map<String, List<String>> itemsOfFamilyPartOfKS;
    //private Map<String, FamilyVar> familiesMap;
    private List<String> newFamiliesSelected;
    private Map<String, Boolean> familiesPartOfSolution;
    private ArrayList<String> familiesEjected;

    private Bucket lastSubmittedBucket;

    public KernelSetState() {
        this.newFamiliesSelected = new ArrayList<>();
        //itemsOfFamilyPartOfKS = new HashMap<String, List<String>>();
        familiesPartOfSolution = new HashMap<String, Boolean>();

    }
    public void initFamilies(List<Famiglia> families) {
        for(Famiglia f : families) {
            this.familiesPartOfSolution.put(f.getVarName(), false); // Nessuna famiglia parte della soluzione
            //this.itemsOfFamilyPartOfKS.put(f.varName(), new ArrayList<>());
        }
    }
    public void init(List<Item> itemsOfKS, Model model){
        for(Item it : itemsOfKS){
            if(it instanceof FamilyVar){
                //this.itemsOfFamilyPartOfKS.put(((FamilyVar) it).varName(), new ArrayList<>());
            } else {

            }
        }
    }
    public void update(Model model){
        this.newFamiliesSelected = new ArrayList<>();
        this.familiesEjected = new ArrayList<>();
        for(String fam: familiesPartOfSolution.keySet()){
            if( !familiesPartOfSolution.get(fam) && model.getVarValue(fam)>1e-5) {
                this.newFamiliesSelected.add(fam);
            } else if(familiesPartOfSolution.get(fam) && model.getVarValue(fam)<1e-5){
                this.familiesEjected.add(fam);
            }
        }
    }
    public void addNewVars(List<Variabile> vars){
    }
    public List<String> getSelectedFamilies(){
        return this.newFamiliesSelected;
    }
    public List<String> getEjectedFamilies(){
        return this.familiesEjected;
    }


}

