package xpKernelSearch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Famiglia {
    private final String id;

    //private final FamilyVar var;
    private final List<Variabile> variables;
    private final int setUpCost;
    private final int pesoFamiglia;

    private Comparator< Variabile> comparator = (v1, v2) -> {
        if(v1.getRapportoProfPeso()>v2.getRapportoProfPeso())
            return  -1;
        else
            return 1;
    };

    public Famiglia(String id, List<Variabile> variables, int setUpCost, int pesoFamiglia) {
        this.id = id;
        this.variables = variables;
        this.setUpCost = setUpCost;
        //this.var = new FamilyVar(id, variables.stream().map(v -> new ItemVar(v.id(), this.var)).collect(Collectors.toList()));
        //this.var = new FamilyVar(id, variables.stream().map(v -> new ItemVar(v.id(), this.var)).collect(Collectors.toList()));
        this.pesoFamiglia = pesoFamiglia;

    }

    public List<Variabile> getVariablesOrdered(){
        return variables.stream().sorted(comparator).collect(Collectors.toList());
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
}
