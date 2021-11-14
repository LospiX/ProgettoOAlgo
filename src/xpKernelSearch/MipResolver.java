package xpKernelSearch;


import gurobi.*;
import kernel.ModelDescriber;


public class MipResolver {

    public static void main(String[] args) throws GRBException {
        //String pth = "C:\\Users\\Xavier\\Documents\\XP - Asus\\Università\\Magistrale 1 anno\\Optimization Algorithms\\Project\\Istanze\\mps\\test5x500.mps";
        //String pth ="C:\\Users\\Xavier\\Documents\\t.mps";
        String pth ="C:\\Users\\Xavier\\Documents\\testMps3.mps";
        //String pth ="C:\\Users\\Xavier\\Documents\\output.mps";
        /*Path file = Path.of(pth);
        System.out.println(file.getName(2));
        System.out.println(file.getName(4));*/
        GRBEnv env = new GRBEnv("logMIP");
        //String pth2 = "C:\\Users\\Xavier\\Documents\\XP - Asus\\Università\\Magistrale 1 anno\\Optimization Algorithms\\Project\\DataSet\\text5x500.mps";

        GRBModel model = new GRBModel(env,  pth);
        env.set(GRB.IntParam.Presolve, 2 );
        env.set(GRB.IntParam.Threads, 8);
        env.set(GRB.IntParam.MIPFocus, 2);
        env.set(GRB.DoubleParam.TimeLimit, 200);
        //env.set(GRB.DoubleParam.BestObjStop, 1e5);
        //env.set(GRB.DoubleParam.FuncMaxVal, 12000);
       // env.set(GRB.DoubleParam.Cutoff, 10000);
        env.set(GRB.IntParam.Cuts, 2);
        env.set(GRB.DoubleParam.MIPGap, 1e-3);


        GRBLinExpr objectiveFun =(GRBLinExpr) model.getObjective();
        String varName;
        GRBLinExpr lexpr= new GRBLinExpr();
        for (int i=0; i< objectiveFun.size(); i++){
            lexpr.addTerm(objectiveFun.getCoeff(i), objectiveFun.getVar(i));
        }
        GRBConstr c0 = model.addConstr(lexpr, GRB.LESS_EQUAL, 11500, "c0");
        model.addConstr(model.getVarByName("XXXXXXX6"), GRB.EQUAL, 0, "Test");
        model.addConstr(model.getVarByName("XXXXXXX7"), GRB.EQUAL, 0, "Test2");
        GRBLinExpr ge = new GRBLinExpr();
        ge.addTerm(2.0, model.getVarByName("XXXXX111"));
        model.addConstr(ge, GRB.LESS_EQUAL, 4.0, "Hii");
        GRBVar x = model.addVar(0.0, 1.0, 0.0, GRB.BINARY , "x" ) ;
        ge = new GRBLinExpr();
        ge.addTerm(3, x);
        model.addConstr(ge, GRB.LESS_EQUAL, 4.0, "Hello");
        model.optimize();



        ModelDescriber desc = new ModelDescriber(model);
        desc.printModel();
        System.out.println(model.get(GRB.IntAttr.NumConstrs));
        System.out.println(model.getConstrs().length);
    }
}
