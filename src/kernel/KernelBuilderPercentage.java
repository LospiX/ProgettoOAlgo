package kernel;
import java.util.List;

public class KernelBuilderPercentage implements KernelBuilder {
	@Override
	public Kernel build(List<Item> items, Configuration config) {
		Kernel kernel = new Kernel();
		//ADD FAMILIES VARS TO KERNEL:
		//*
		for (Item it : items){
			if(it.getVarName().startsWith("Y") && (kernel.size() < Math.round(config.getKernelSize()*items.size()))) {
				kernel.addItem(it);
				//System.out.println("Var fami:: "+ it.getName()+ "  val:: " + it.getXr());
				//System.out.println("\tRC:: "+ it.getRc());
				//System.out.println("Var fami:: "+ it.getName()+"   Valore nel rilassato:: "+ it.getXr()+ "  Rc corrispondente:: " + it.getRc());
			}
		}
		for(Item it : items) {
			if((kernel.size() < Math.round(config.getKernelSize()*items.size())) && !it.getVarName().startsWith("Y")) {
				kernel.addItem(it);

			}
		}/*/
		for(Item it : items) {
			if((kernel.size() < Math.round(config.getKernelSize()*items.size())))
				kernel.addItem(it);
		}*/

		return kernel;
	}
}