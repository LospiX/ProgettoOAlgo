package kernel;
import java.util.List;

public interface KernelBuilder
{
	public Kernel build(List<? extends Item> items, Configuration config);
}