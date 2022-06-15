package xpKernelSearch;

import kernel.Bucket;
import kernel.Configuration;
import kernel.ItemSorter;
import kernel.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ProblemaKnapSackSetup {
    private int numeroDiIterazioni = 0;
    private final List<Famiglia> families = new ArrayList<>();;
    private final int capacity;
    private final int numOfFamilies;
    private final int[] dimOfFamilies;
    private final int[] costsSetupOfFamilies;
    private final int[] profits;
    private final int[] costs;
    private final int[] costsOfFamilies;
    private final double subsetFactor;
    private final int numberOfTentativi;
    private String[] varX;
    private String[] varY;
    private int numOfItems;

    private ItemSorter itemFamSorter;
    //private KernelSetState kerSetState;

    private List<Candidato> lastSubmittedCandidati = new  ArrayList<>();
    private final LinkedList<Candidato> annaList = new LinkedList<>();
    private final double bucketDimFactor;

    private final double decreasing_percentage;

    private final double min_percentage;

    public ProblemaKnapSackSetup(File f, Configuration config) throws IOException {
        List<String> lines = this.extractFromFile(f.toPath());
        this.numOfItems = Integer.parseInt(lines.get(0));
        this.numOfFamilies = Integer.parseInt(lines.get(1));
        this.capacity = Integer.parseInt(lines.get(2));
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
        this.buildVariablesNames();
        this.itemFamSorter = config.getItemSorter();
        this.subsetFactor = config.getSubsetFactor();
        this.numberOfTentativi = config.getNumberOfTries();
        this.bucketDimFactor = config.getBucketDimension();
        this.decreasing_percentage = config.getDecreasingPercentage();
        this.min_percentage = config.getMInPercentageDim();
        this.buildFamilies();
    }
    public void buildFamilies() {
        int idxLastRowOfItems= 0;
        int j= 0;
        List<Variabile> variables;
        for(int i = 0; i < numOfFamilies; i++) {
            variables = new ArrayList<>();
            for(; j<idxLastRowOfItems+dimOfFamilies[i]; j++){
                variables.add(new Variabile(this.varX[j], profits[j], costs[j])); // Create a variable
            }
            idxLastRowOfItems= j;
            this.families.add(new Famiglia(this.varY[i], variables, costsSetupOfFamilies[i], costsOfFamilies[i], itemFamSorter));
        }
    }

    public void updateXrRc(Model model) {
        for (Famiglia family : families) {
            family.setXr(model.getVarValue(family.getVarName()));
            family.setRc(model.getVarRC(family.getVarName()));
            family.getVariables().forEach(
                v -> {
                    v.setXr(model.getVarValue(v.getVarName()));
                    v.setRc(model.getVarRC(v.getVarName()));
                }
            );
        }
    }
    public void printFamily(int i) {
        System.out.println("Hello");
        //System.out.println(this.families.get(i).getVariablesOrdered().toString());
    }


    private void buildVariablesNames() {
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
                return 0;
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

    public int getCapZaino() {
        return this.capacity;
    }

    public Bucket genNextBucket(Model model, boolean firstTime, int dimKerSet){
        this.numeroDiIterazioni++;
        if(firstTime == false ) this.updateMarketList(model);// Update della Market List
        List<Candidato> newCandidati = new ArrayList<>();
        int dim= 0;
        Candidato c;
        System.out.println("\n::: GENERATING NEW BUCKET :::");
        while (dim < Math.floor(this.bucketDimFactor*dimKerSet) && this.annaList.size()>0) {
            c= this.annaList.pollFirst();
            System.out.println("\tAdding a subset");
            c.printCandidatoStatus();
            dim+= c.getDim();
            newCandidati.add(c);
        }
        System.out.println("Max bucket dim: "+bucketDimFactor*dimKerSet);
        this.lastSubmittedCandidati = newCandidati;
        if(newCandidati.size()> 0)
            return this.fromCandidatiToBucket(newCandidati);
        else
            return null;

    }


    private Bucket fromCandidatiToBucket(List<Candidato> lisCandidati) {
        Bucket bckt = new Bucket();
        for(Candidato c: lisCandidati) {
            c.getSubsets().forEach(sub -> sub.getSet().forEach(v -> bckt.addItem(v)));
        }
        return bckt;
    }

    public void buildFirstMarketList () {
        Candidato c;
        System.out.println("BUILDING FIRST MARKETLIST");
        for(Famiglia f: this.families) {
            System.out.println("Family: "+f.getVarName());
            c = new Candidato(f);
            c.addSubSet();
            System.out.println("\tSize of subset added:  "+c.getDim());
            this.annaList.add(c);
        }
    }

    private void updateMarketList (Model model) {
        List<Variabile> selectedVars;
        boolean failed = true;
        for (Candidato c : this.lastSubmittedCandidati) {
            for(SubSet s: c.getSubsets()) {
                selectedVars = s.getSet().stream()
                    .filter(v -> model.getVarValue(v.getVarName())>0)
                    .peek(v -> v.setSelected())
                    .collect(Collectors.toList());
                if(selectedVars.size() > 0)
                    failed= false;
                selectedVars.forEach(varToRmv -> s.getSet().remove(varToRmv));
            }
            if(failed == true ) { // If now has failed -> no vars of the candidate has been selected
                System.out.println("Candidato ha failed");
                c.setFailed();
                if(c.hasFailed() == true){ // if the last time failed
                    if(c.getNumOfFails() < this.numberOfTentativi) {
                        c.addSubSet();
                        this.annaList.addLast(c);
                    }
                    System.out.println("\tCandidato Second time faild add to market List");
                } else {
                    System.out.println("\tCandidato first time faild add to market List");
                    c.setFailed();
                    c.addSubSet();
                    this.annaList.addLast(c);
                    // Aggiungi il candidato con il subset successivo IN FONDO alla marketlist
                }
            } else {  // If now has succeeded -> some vars of the candidate has been selected
                System.out.println("Candidato Not Failed time add to market List");
                // Aggiungi il candidato con il subset successivo All'inizio della marketlist
                c.addSubSet();
                this.annaList.addFirst(c);
                if(c.hasFailed() == false){  // Reset if the last time failed
                    c.resetFailed();
                }
            }
            failed = true;
        }
    }


    public int getNumOfVariables() {
        return this.numOfItems;
    }

    public void setUp(Model model) {
        this.updateXrRc(model); // Assign Xr and Rc to all variables
        this.sortFamilies();
        families.forEach(f -> {
            f.sortVariables();
            f.generateSubsets(this.subsetFactor, this.decreasing_percentage, this.min_percentage);
        });
    }

    public int getNumeroDiIterazioni(){
        return numeroDiIterazioni;
    }
}
