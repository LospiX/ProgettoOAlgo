package xpKernelSearch;

import kernel.Item;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProblemaKnapSackSetup {
    private final List<Famiglia> families;
    private final int numOfFamilies;
    private final int[] dimOfFamilies;
    private final int[] costsSetupOfFamilies;
    private final int[] profits;
    private final int[] costs;
    private final int[] costsOfFamilies;
    private String[] varX;
    private String[] varY;
    private int numOfItems;

    public ProblemaKnapSackSetup(File f) throws IOException {
        this.families = new ArrayList<>();
        List<String> lines = this.extractFromFile(f.toPath());
        this.numOfItems = Integer.parseInt(lines.get(0));
        this.numOfFamilies = Integer.parseInt(lines.get(1));
        int capacity = Integer.parseInt(lines.get(2));
        this.dimOfFamilies =  Arrays.stream(lines.get(3).split("\s+")).mapToInt(Integer::parseInt).toArray();
        this.costsSetupOfFamilies =  Arrays.stream(lines.get(4).split("\s+")).mapToInt(Integer::parseInt).toArray();
        this.costsOfFamilies =  Arrays.stream(lines.get(5).split("\s+")).mapToInt(Integer::parseInt).toArray();
        this.profits= new int[numOfItems];
        this.costs= new int[numOfItems];
        for (int i=0; i<=5; i++)
            lines.remove(0);
        for (int i=0; i< numOfItems; i++){
            //ATTENZIONE SPACING Dipende da istanze ad istanze
            this.profits[i]= Integer.parseInt(lines.get(i).split("\s+")[0].trim());
            this.costs[i]= Integer.parseInt(lines.get(i).split("\s+")[1].trim());
        }
        this.buildVariables();
        this.build();
    }
    public void build() {
        int idxLastRowOfItems= 0;
        int j= 0;
        List<Variabile> variables;

        for(int i = 0; i < numOfFamilies; i++) {
            variables = new ArrayList<>();
            for(; j<idxLastRowOfItems+dimOfFamilies[i]; j++){
                variables.add(new Variabile(this.varX[j], profits[j], costs[j]));
            }
            idxLastRowOfItems= j;
            families.add(new Famiglia(this.varY[i], variables, costsSetupOfFamilies[i], costsOfFamilies[i]));
        }
    }

    public void printFamily(int i) {
        System.out.println("Hello");
        //System.out.println(this.families.get(i).getVariablesOrdered().toString());
    }


    private void buildVariables() {
        int dimX = this.numOfItems;
        this.varX =new String[dimX];
        String name="";
        for (int i=0; i<dimX; i++){
            for (int j=0; j<(8-String.valueOf(i).length()); j++){
                name+="X";
            }
            this.varX[i]=name+String.valueOf(i);
            name="";
        }
        int dimY = this.numOfFamilies;
        this.varY= new String[dimY];
        for (int i=0; i<dimY; i++) {
            for (int j=0; j<(8-String.valueOf(i).length()); j++){
                name+="Y";
            }
            this.varY[i]=name+String.valueOf(i);
            name="";
        }
    }


    /*public List<Variabile> getOrderedListBestFamilyVariables(String nameOfFamily){
        for(Famiglia f : this.families){
            if(f.getName().equalsIgnoreCase(nameOfFamily)){
                return f.getVariablesOrdered();
            }
        }
        throw new IllegalArgumentException("Family does Not exists");
    }*/
    public void sortFamilies(){
        this.families.sort(
                (e1, e2) -> {
                    var valE1= e1.getXr();
                    var valE2 =e2.getXr();
                    if(valE1 < valE2)
                        return 1;
                    else if(Math.abs(valE1) < 1e-5 && Math.abs(valE2) < 1e-5) {
                        if(Math.abs(e1.getRC()) > Math.abs(e2.getRC()))
                            return 1;
                    }
                    return -1;
                });
    }
    private List<String> extractFromFile (Path file) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = Files.newBufferedReader(file);
        String line = br.readLine();
        while (line != null){
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        return lines;
    }

    public Famiglia getFamigliaById(String it) {
        for(Famiglia f : this.families){
            if(f.getVarName().equals(it))
                return f;
        }
        return null;
    }

    public void setFamigliaStats(String fam, double Xr, double RC) {
        Famiglia f = this.getFamigliaById(fam);
        f.setXr(Xr);
        f.setRc(RC);
    }

    public List<Famiglia> getFamilies() {
        return this.families;
    }
}
