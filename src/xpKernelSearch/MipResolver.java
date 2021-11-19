package xpKernelSearch;


import gurobi.*;
import kernel.ModelDescriber;

import java.io.File;


public class MipResolver {
    private File file;
    MipResolver (File istFile) {
        this.file = istFile;
    }
    public double solve () throws GRBException {
        GRBEnv env = new GRBEnv("logMIP");

        GRBModel model = new GRBModel(env,  this.file.getPath());
        env.set(GRB.IntParam.Presolve, 2 );
        env.set(GRB.IntParam.Threads, 8);
        env.set(GRB.IntParam.MIPFocus, 2);
        env.set(GRB.DoubleParam.TimeLimit, 200);
        env.set(GRB.DoubleParam.MIPGap, 1e-12);


        GRBLinExpr objectiveFun =(GRBLinExpr) model.getObjective();
        model.update();
        model.optimize();
        return ((GRBLinExpr) model.getObjective()).getValue();
    }

    public static void main(String[] args) throws GRBException {
        String pth =".\\Istanze\\mps\\SCmps\\test5x500-SC(2).mps";
        //String pth =".\\Istanze\\mps\\Class1Mps\\prob1_050_040_060_005_015_01.mps";

        GRBEnv env = new GRBEnv("logMIP");

        GRBModel model = new GRBModel(env,  pth);
        env.set(GRB.IntParam.Presolve, 2 );
        env.set(GRB.IntParam.Threads, 8);
        env.set(GRB.IntParam.MIPFocus, 2);
        env.set(GRB.DoubleParam.TimeLimit, 200);
        //env.set(GRB.DoubleParam.BestObjStop, 1e5);
        //env.set(GRB.DoubleParam.FuncMaxVal, 12000);
        //env.set(GRB.DoubleParam.Cutoff, 10000);
        //env.set(GRB.IntParam.Cuts, 2);
        env.set(GRB.DoubleParam.MIPGap, 1e-12);


        GRBLinExpr objectiveFun =(GRBLinExpr) model.getObjective();

        GRBLinExpr lexpr= new GRBLinExpr();
        for (int i=0; i< objectiveFun.size(); i++){
            lexpr.addTerm(objectiveFun.getCoeff(i), objectiveFun.getVar(i));
        }
        //GRBConstr c0 = model.addConstr(lexpr, GRB.LESS_EQUAL, 11005, "c0");
        model.update();
        model.optimize();

        ModelDescriber desc = new ModelDescriber(model);
        desc.printModel();
        desc.printSolTable(7);
    }
}
