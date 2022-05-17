package xpKernelSearch;

import gurobi.GRBException;
import kernel.Configuration;
import kernel.KernelSearch;

import java.io.File;

class IstanzaMIP {
    private File istanzaFile;
    private String PATHLOG;
    private double relaxedSolution;
    private boolean hasBeenSolvedRelaxation = false;
    IstanzaMIP (File f, String pathLog) {
        this.istanzaFile = f;
        this.PATHLOG = pathLog;
    }

    public double solveKernelSearch (Configuration config) {
        try {
           // return new KernelSearch(this.istanzaFile.getPath(), this.PATHLOG, config).start().getObj();
        } catch (Exception e) {
            System.out.println("Unable to solve the relaxation of the instance: "+this.istanzaFile.getName());
            e.printStackTrace();
        }
        return -1;
    }

    public double solveRelaxed () {
        try {
            this.relaxedSolution = new MipResolver(this.istanzaFile).solve();
            this.hasBeenSolvedRelaxation = true;
            return this.getRelaxedSolution();
        } catch (Exception e) {
            System.out.println("Unable to solve the relaxation of the instance: "+this.istanzaFile.getName());
            e.printStackTrace();
        }
        return -1;
    }

    public String getName() {
        return this.istanzaFile.getName();
    }
    public double getRelaxedSolution() {
       if(this.hasBeenSolvedRelaxation)
           return this.relaxedSolution;
       throw new ArithmeticException("The relaxation of the instance \""+this.getName()+"\" has not been resolved yet or has not been able to complete the resolution.");
    }

}
