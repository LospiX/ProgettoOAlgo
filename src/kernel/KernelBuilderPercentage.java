package kernel;
import java.util.List;

public class KernelBuilderPercentage implements KernelBuilder
{
	@Override
	public Kernel build(List<Item> items, Configuration config)
	{
		Kernel kernel = new Kernel();
		//ADD FAMILIES VARS TO KERNEL:
		for (Item it : items){
			if(it.getName().startsWith("Y") && (kernel.size() < Math.round(config.getKernelSize()*items.size())))
				kernel.addItem(it);
		}
		for(Item it : items)
		{
			if(kernel.size() < Math.round(config.getKernelSize()*items.size()))
			{
				kernel.addItem(it);
			}
		}	
		return kernel;
	}
}