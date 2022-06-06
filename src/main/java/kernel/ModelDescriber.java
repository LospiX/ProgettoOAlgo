package kernel;

import gurobi.*;

public class ModelDescriber {
    private GRBModel model;

    public ModelDescriber(GRBModel model) throws GRBException {
            this.model= model;
    }

    public void printModel () throws GRBException {
        this.printGeneralInfos();
        this.printObjFun();
        this.printConstraints();
    }

    private void printConstraints() throws GRBException {
        for (GRBConstr con: model.getConstrs()) {
            printConstr(con, model);
        }
    }

    public void printGeneralInfos () throws GRBException {
        System.out.println((model.get(GRB.IntAttr.IsMIP)==1 ?"MIP": "Not MIP")+ " problem");
        System.out.println((model.get(GRB.IntAttr.ModelSense)>0? "MINIMIZATION": "MAXIMIZATION")+" problem.");
    }

    public void printObjFun() throws GRBException {
        String varName;
        System.out.println("Objective Function: ");
        GRBLinExpr objectiveFun = (GRBLinExpr) model.getObjective();
        for (int i=0; i< objectiveFun.size(); i++){
            varName = objectiveFun.getVar(i).get(GRB.StringAttr.VarName);
            System.out.print(objectiveFun.getCoeff(i)+"*"+varName+"  ");
        }
        System.out.println("\nEND of Objective Function");
    }

    public void printSol() throws GRBException {
        String varName;
        for (GRBVar v: model.getVars())
            System.out.println(v.get(GRB.StringAttr.VarName)+ " "+ v.get(GRB.DoubleAttr.X));
    }

    public void printSolTable(int elemForRow) throws GRBException {
        String varName;
        int size = model.getVars().length;
        for (int i=0; i< size; i++){
            System.out.print(model.getVars()[i].get(GRB.StringAttr.VarName)+ " "+ model.getVars()[i].get(GRB.DoubleAttr.X)+ " ");
            if (i%elemForRow==0)
                System.out.print("\n");
        }
    }

    private void printConstr( GRBConstr g, GRBModel model) throws GRBException {
        GRBLinExpr lin =  model.getRow(g);
        System.out.print(g.get(GRB.StringAttr.ConstrName)+":  ");
        String varName;
        for (int i=0; i< lin.size(); i++){
            varName = lin.getVar(i).get(GRB.StringAttr.VarName);
            System.out.print(lin.getCoeff(i)+"*"+varName+"  ");
        }
        System.out.print(" "+g.get(GRB.CharAttr.Sense)+" ");
        System.out.print(g.get(GRB.DoubleAttr.RHS)+"\n");
    }
}
