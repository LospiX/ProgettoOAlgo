package xpKernelSearch;

import kernel.Item;
import kernel.Model;
import xpKernelSearch.Variabile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KernelSetState {
    private Map<String, List<String>> itemsOfFamilyPartOfKS;
    private Map<String, FamilyVar> familiesMap;
    public KernelSetState() {
        itemsOfFamilyPartOfKS = new HashMap<String, List<String>>();
        familiesMap = new HashMap<String, FamilyVar>();
    }
    public void initFamilies(List<FamilyVar> families) {
        for(FamilyVar f: families) {
            this.familiesMap.put(f.varName(), f);
            this.itemsOfFamilyPartOfKS.put(f.varName(), new ArrayList<>());
        }
    }
    public void init(List<Item> itmesOfKS, Model model){
        for(Item it : itmesOfKS){
            if(it instanceof FamilyVar){
                this.itemsOfFamilyPartOfKS.put(((FamilyVar) it).varName(), new ArrayList<>());
            } else {

            }
        }

    }
    public void update(){

    }
    public void addNewVars(List<Variabile> vars){

    }
}
