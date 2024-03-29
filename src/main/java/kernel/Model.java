package kernel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gurobi.*;
import gurobi.GRB.DoubleAttr;
import gurobi.GRB.IntAttr;
import gurobi.GRB.StringAttr;

public class Model {
	private String mpsFilePath;
	private String logPath;
	private int timeLimit;
	private Configuration config;
	private boolean lpRelaxation;
	private GRBEnv env;
	private GRBModel model;
	private boolean hasSolution;
	private double positiveThreshold = 1e-5;

	private double ottimo;
	
	public Model(String mpsFilePath, String logPath, int timeLimit, Configuration config, boolean lpRelaxation) {
		this.mpsFilePath = mpsFilePath;
		this.logPath = logPath;
		this.timeLimit = timeLimit;	
		this.config = config;
		this.lpRelaxation = lpRelaxation;
		this.hasSolution = false;
	}

	public void buildModel() {
		try {
			env = new GRBEnv();
			setParameters();
			model = new GRBModel(env, mpsFilePath);
			if(lpRelaxation)
				model = model.relax();
		} catch (GRBException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void setParameters() throws GRBException {
		env.set(GRB.StringParam.LogFile, logPath+"log.txt");
		env.set(GRB.IntParam.Threads, config.getNumThreads());
		env.set(GRB.IntParam.Presolve, config.getPresolve());
		env.set(GRB.DoubleParam.MIPGap, config.getMipGap());
		if (timeLimit > 0)
			env.set(GRB.DoubleParam.TimeLimit, timeLimit);
		//env.set(GRB.IntParam.Method, 0);
		//env.set(GRB.IntParam.Presolve, 0);
		//env.set(IntParam.OutputFlag, 0);
		
	}
	
	public void solve() {
		try {
			model.update();
			ModelDescriber md = new ModelDescriber(model);
			//System.out.println("PRERESOLVE, PRINT MODEL:");
			//md.printModel();

			model.optimize(); // SOLVE!
			this.ottimo = model.get(DoubleAttr.ObjVal);
			System.out.println("***STATUS***: "+Status.values()[model.get(IntAttr.Status)]);

			if(model.get(IntAttr.SolCount) > 0) {
				hasSolution = true;
				//md.printSolTable(6);
				GRBLinExpr sol = (GRBLinExpr) model.getObjective();
				//System.out.println("SOL VAL:: " + sol.getValue());
				//System.out.println("***OBJ VAL: " + model.get(DoubleAttr.ObjVal));
			}
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getVarNames() {
		List<String> varNames = new ArrayList<>();
		for(GRBVar v : model.getVars()) {
			try {
				varNames.add(v.get(StringAttr.VarName));
			} catch (GRBException e) {
				e.printStackTrace();
			}
		}
		return varNames;
	}

	public double getVarValue(String v) {
		try {
			if(model.get(IntAttr.SolCount) > 0) {
				return model.getVarByName(v).get(DoubleAttr.X);
			}
		} catch (GRBException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public double getVarRC(String v) {
		try {
			if(model.get(IntAttr.SolCount) > 0)
				return model.getVarByName(v).get(DoubleAttr.RC);
		} catch (GRBException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public void disableItems(List<Item> items) {
		try {
			for(Item it : items) {
				// RIDONDANTE
				// if(it.getName().startsWith("Y")) // PREVENT FAMILIES VARIABLES DISABLING
				//	continue;

				String constrName = "FIX_VAR_" + it.getVarName();
				model.addConstr(model.getVarByName(it.getVarName()), GRB.EQUAL, 0, constrName);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void printNumberConstr ()  {
		System.out.println("constraints number= "+model.getConstrs().length);
	}
	public void exportSolution() {
		try {
			model.write("bestSolution.sol");
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}
	
	public void readSolution(String path) {
		try {
			model.read(path);
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}
	
	public void readSolution(Solution solution) {
		try {
			for(GRBVar var : model.getVars())
				var.set(DoubleAttr.Start, solution.getVarValue(var.get(StringAttr.VarName)));
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	public boolean hasSolution() {
		return hasSolution;
	}
	
	public Solution getSolution() {
		Solution sol = new Solution();
		try {
			sol.setObj(model.get(DoubleAttr.ObjVal));
			Map<String, Double> vars = new HashMap<>();
			for(GRBVar var : model.getVars())
				vars.put(var.get(StringAttr.VarName), var.get(DoubleAttr.X));
			sol.setVars(vars);
		} catch (GRBException e) {
			e.printStackTrace();
		}
		return sol;
	}
	
	public void addBucketConstraint(List<Item> items) {
		GRBLinExpr expr = new GRBLinExpr();
		try {
			for(Item it : items)
				expr.addTerm(1, model.getVarByName(it.getVarName()));
			model.addConstr(expr, GRB.GREATER_EQUAL, 1, "bucketConstraint");
		} catch (GRBException e) {
			e.printStackTrace();
		}	
	}

	public void addObjConstraint(double obj) {
		try {
			model.getEnv().set(GRB.DoubleParam.Cutoff, obj);
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}
	
	public List<Item> getSelectedItems(List<Item> items) {
		List<Item> selected = new ArrayList<>();
		for(Item it : items) {
			try {
				if(model.getVarByName(it.getVarName()).get(DoubleAttr.X)> positiveThreshold)
					selected.add(it);
			} catch (GRBException e) {
				e.printStackTrace();
			}
		}
		return selected;
	}
	
	public void setCallback(GRBCallback callback) {
		model.setCallback(callback);
	}

	public double getOttimo() {
		return this.ottimo;
	}

	/*public void getProfit(String v) {
		try {
			GRBLinExpr objectiveFun = (GRBLinExpr)model.getObjective();
			objectiveFun.
		} catch (GRBException e) {
			throw new RuntimeException(e);
		}
		model.getVarByName(v).
	}*/
}