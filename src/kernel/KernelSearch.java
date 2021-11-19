package kernel;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import gurobi.GRB;
import gurobi.GRBCallback;
import gurobi.GRBException;

public class KernelSearch {
	private String instPath;
	private String logPath;
	private Configuration config;
	private List<Item> items;
	private ItemSorter sorter;
	private BucketBuilder bucketBuilder;
	private KernelBuilder kernelBuilder;
	private int tlim;
	private Solution bestSolution;
	private List<Bucket> buckets;
	private Kernel kernel;
	private int tlimKernel;
	private int tlimBucket;
	private int numIterations;
	private GRBCallback callback;
	private int timeThreshold = 5;
	private List<List<Double>> objValues;
	
	private Instant startTime;
	
	public KernelSearch(String instPath, String logPath, Configuration config) {
		this.instPath = instPath;
		this.logPath = logPath;
		this.config = config;
		bestSolution = new Solution();
		objValues = new ArrayList<>();
		configure(config);
	}
	
	private void configure(Configuration configuration) {
		sorter = config.getItemSorter();
		tlim = config.getTimeLimit();
		bucketBuilder = config.getBucketBuilder();
		kernelBuilder = config.getKernelBuilder();
		tlimKernel = config.getTimeLimitKernel();
		numIterations = config.getNumIterations();
		tlimBucket = config.getTimeLimitBucket();
	}
	
	public Solution start() {
		startTime = Instant.now();
		callback = new CustomCallback(logPath, startTime);
		items = buildItems();
		sorter.sort(items);	
		kernel = kernelBuilder.build(items, config);
		buckets = bucketBuilder.build(items.stream().filter(it -> !kernel.contains(it)).collect(Collectors.toList()), config);
		solveKernel();
		iterateBuckets();
		return bestSolution;
	}

	private List<Item> buildItems() {
		Model model = new Model(instPath, logPath, config.getTimeLimit(), config, true); // time limit equal to the global time limit
		model.buildModel();
		model.solve();
		List<Item> items = new ArrayList<>();
		List<String> varNames = model.getVarNames();
		for(String v : varNames) {
			double value = model.getVarValue(v);
			double rc = model.getVarRC(v); // can be called only after solving the LP relaxation
			Item it = new Item(v, value, rc);
			items.add(it);
		}
		return items;
	}
	
	private void solveKernel() {
		Model model = new Model(instPath, logPath, Math.min(tlimKernel, getRemainingTime()), config, false);	
		model.buildModel();
		objValues.add(new ArrayList<>());

		if(!bestSolution.isEmpty()) {
			model.addObjConstraint(bestSolution.getObj());		
			model.readSolution(bestSolution);
		}
		
		List<Item> toDisable = items.stream().filter(it -> !kernel.contains(it)).collect(Collectors.toList());
		model.disableItems(toDisable);
		model.setCallback(callback);
		model.solve();
		if(model.hasSolution()) {
			bestSolution = model.getSolution();
			model.exportSolution();
			
			objValues.get(objValues.size()-1).add(bestSolution.getObj());
		}
		else {
			objValues.get(objValues.size()-1).add(0.0);
		}
	}
	
	private void iterateBuckets() {
		for (int i = 0; i < numIterations; i++) {
			if(getRemainingTime() <= timeThreshold)
				return;
			if(i != 0)
				objValues.add(new ArrayList<>());
			
			System.out.println("\n\n\n\t\t******** Iteration "+i+" ********\n\n\n");
			solveBuckets();			
		}		
	}

	private void solveBuckets() {
		int count = 0;
		for(Bucket b : buckets) {
			System.out.println("\n\n\n\n\t\t** Solving bucket "+count++ +" **\n");
			// SE il kernel non contiene l'item ED il bucket nemmeno => disabilita le variabili;
			List<Item> toDisable = items.stream().filter(it -> !kernel.contains(it) && !b.contains(it)).collect(Collectors.toList());

			Model model = new Model(instPath, logPath, Math.min(tlimBucket, getRemainingTime()), config, false);	
			// Ad ogni iterazione ricostruisci il Model da capo e disabilita
			model.buildModel();
			model.disableItems(toDisable);

			// PRINT BUCKET ITEMS
			// for (Item it: b.getItems())
				// System.out.print(it.getName()+" ");

			// La bucketConstraint impone di scegliere almeno una variabile del Bucket
			model.addBucketConstraint(b.getItems()); // can we use this constraint regardless of the type of variables chosen as items?
			if(!bestSolution.isEmpty()) { // Se esiste una soluzione
				// ***** //
				model.addObjConstraint(bestSolution.getObj()); // -> model.getEnv().set(GRB.DoubleParam.Cutoff, obj);
				model.readSolution(bestSolution);
			}
			
			model.setCallback(callback);
			model.solve();
			
			if(model.hasSolution()) {
				bestSolution = model.getSolution();
				List<Item> selected = model.getSelectedItems(b.getItems());
				selected.forEach(it -> kernel.addItem(it));
				selected.forEach(it -> b.removeItem(it)); // ***** ?????
				model.exportSolution();
			}
			if(!bestSolution.isEmpty())
				objValues.get(objValues.size()-1).add(bestSolution.getObj());
			else
				objValues.get(objValues.size()-1).add(0.0);

			if(getRemainingTime() <= timeThreshold) {
				System.out.println("Treshold superata?");
				return;
			}
		}
		//System.out.println("BEST SOLUTION of Iteration: "+this.bestSolution.getObj());
	}

	private int getRemainingTime()
	{
		return (int) (tlim - Duration.between(startTime, Instant.now()).getSeconds());
	}

	public List<List<Double>> getObjValues()
	{
		return objValues;
	}
}