package xpKernelSearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MpsBuilder {
    int[] dimOfFamilies;
    int[] costsSetupOfFamilies;
    int[] costsOfFamilies;
    int numbOfFamilies;
    int numbOfItems;
    int capacity;
    int[] profits;
    int[] costs;
    String[] varX;
    String[] varY;
    String[] familyConstrNames;

    MpsBuilder(int capacity, int[] dimOfFamilies, int[] costsSetupOfFamilies, int[] costsOfFamilies, int[] profits, int[] costs) {
        this.capacity = capacity;
        this.dimOfFamilies = dimOfFamilies;
        this.numbOfFamilies = dimOfFamilies.length;
        this.costsSetupOfFamilies = costsSetupOfFamilies;
        this.costsOfFamilies = costsOfFamilies;
        this.profits = profits;
        this.costs = costs;
        this.numbOfItems = costs.length;
    }
    public void build (Path destination) throws IOException {
        buildVariables();
        String mps = "";
        mps = mps.concat("NAME"+this.getSpaces(11)+"HELLO"+"\n");
        mps = mps.concat("OBJSENSE"+"\n");
        mps = mps.concat(this.getSpaces(2)+"MAX"+"\n");
        mps = mps.concat("ROWS\n");
        mps = mps.concat(this.buildFunObj());
        mps = mps.concat(this.buildCapacityConstr());
        mps = mps.concat(this.buildFamilyConstr());
        mps = mps.concat("COLUMNS\n");
        mps = mps.concat(this.buildColumnsX());
        mps = mps.concat(this.buildColumnsY());
        mps = mps.concat("RHS\n");
        mps = mps.concat(this.buildRHS());
        mps = mps.concat("BOUNDS\n");
        mps = mps.concat(this.buildBounds());
        mps = mps.concat("ENDATA");
        Files.writeString(destination, mps);

    }

    private void buildVariables() {
        int dimX = this.costs.length;
        //String[] names = new String[dimX];
        this.varX =new String[dimX];
        String name="";
        for (int i=0; i<dimX; i++){
            for (int j=0; j<(8-String.valueOf(i).length()); j++){
                name+="X";
            }
            this.varX[i]=name+String.valueOf(i);
            name="";
        }
        int dimY = this.numbOfFamilies;
        this.varY= new String[dimY];
        for (int i=0; i<dimY; i++) {
            for (int j=0; j<(8-String.valueOf(i).length()); j++){
                name+="Y";
            }
            this.varY[i]=name+String.valueOf(i);
            name="";
        }

    }

    private String buildFamilyConstr() {
        String res ="";
        String name ="";
        this.familyConstrNames= new String[this.numbOfItems];
        for(int i=0; i< this.numbOfItems; i++){
            name="LIM"+String.valueOf(i);
            int mancanti = 8-name.length();
            for(int j=0; j<mancanti;j++)
                name="L"+name;
            res+=this.getSpaces(1)+"L"+this.getSpaces(2)+name+"\n";
            this.familyConstrNames[i]=name;
        }
        return res;
    }
    private String buildCapacityConstr(){
        String res="";
        res+=this.getSpaces(1)+"L"+this.getSpaces(2)+"CAPCNSTR"+"\n";
        return res;
    }
    private String buildFunObj(){
        String res="";
        res+=this.getSpaces(1)+"N"+this.getSpaces(2)+"OBJFUNCT"+"\n";
        return res;
    }

    private String getSpaces (int n){
        String res="";
        for (int i=0; i<n; i++)
            res+=" ";
        return res;
    }
    private String buildColumnsX(){
        String res="";
        String line="";
        int reachedIdx=0;
        int idxNamFam=0;
        for (int famDim: this.dimOfFamilies) { // PER OGNI FAMIGLIA, famDim è la sua dimensione
            for (int i = reachedIdx; i<reachedIdx+famDim; i++) {
                // INSERISCO LA LINEA CONTENENTE IL NOME DELLA VARIABILE
                line = this.getSpaces(4) +varX[i]+this.getSpaces(2); // Reached 15
                line+="OBJFUNCT"+this.getSpaces(2); // Reached 25
                line+=this.profits[i]+this.getSpaces(15-String.valueOf(this.profits[i]).length());
                line+=this.familyConstrNames[i] + this.getSpaces(2)+String.valueOf(1);
                line+="\n";
                //SecondLineConstr
                line+= this.getSpaces(4) +varX[i]+this.getSpaces(2); // Reached 15
                line+= "CAPCNSTR"+this.getSpaces(2)+this.costs[i];
                line+= "\n";
                res+=line;
            }
            reachedIdx+=famDim;
            idxNamFam+=1;
        }
        return res;
    }

    private String buildColumnsY (){
        String res="";
        int reachedIdx = 0;
        int idxFam = 0;
        String line = "";
        for (int famDim: this.dimOfFamilies) {
            line = this.getSpaces(4) +varY[idxFam]+this.getSpaces(2); // Reached 15
            line+="OBJFUNCT"+this.getSpaces(2); // Reached 25
            line+=this.costsSetupOfFamilies[idxFam]+this.getSpaces(15-String.valueOf(this.costsSetupOfFamilies[idxFam]).length()); // Reached 40
            line+= "CAPCNSTR"+this.getSpaces(2)+this.costsOfFamilies[idxFam]+"\n";
            if (famDim%2!=0){
                line += this.getSpaces(4) +varY[idxFam]+this.getSpaces(2); // Reached 15
                line += this.familyConstrNames[reachedIdx]+this.getSpaces(1)+"-"+String.valueOf(1)+"\n";
                reachedIdx+=1;
            }
            System.out.println("Reached: "+reachedIdx+"     famDim: "+famDim+"    expr: "+(reachedIdx+famDim-(famDim%2)));
            System.out.println(this.varY.length);
            for (int i = reachedIdx; i < reachedIdx+famDim-(famDim%2); i+=2) {
                line += this.getSpaces(4) +varY[idxFam]+this.getSpaces(2); // Reached 15
                line += this.familyConstrNames[i]+this.getSpaces(1)+"-"+String.valueOf(1)+this.getSpaces(14);
                line += this.familyConstrNames[i+1]+this.getSpaces(1)+"-"+String.valueOf(1)+"\n";
            }
            res+=line;
            reachedIdx+= famDim-famDim%2;
            idxFam+=1;
        }
        return res;
    }
    private String buildRHS () {
     String res ="";
     res+= this.getSpaces(4) +"RHSRHSRH"+this.getSpaces(2)+"CAPCNSTR"+this.getSpaces(2)+String.valueOf(this.capacity)+"\n";
     return res;
    }
    private String buildBounds() {
        String res="";
        String line="";
        for (int i=0; i<this.varX.length; i++){
            line= this.getSpaces(1) + "BV" + this.getSpaces(11);
            line+= this.varX[i]+"\n";
            res+=line;
        }
        for (int i=0; i<this.numbOfFamilies; i++){
            line= this.getSpaces(1) + "BV" + this.getSpaces(11);
            line+= this.varY[i]+"\n";
            res+=line;
        }
        return res;
    }

    public String[] getNameOfItems() {
        return this.varX;
    }
    public String[] getNameOfFamilies() {
        return this.varY;
    }
}
