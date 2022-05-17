package kernel;

import xpKernelSearch.Famiglia;

import java.util.List;

public class KernelBuilderCooler implements KernelBuilder {

    @Override
    public Kernel build(List<? extends Item> families, Configuration config) {
        Kernel ker= new Kernel();
        boolean zainoRiempito= false;
        while(!zainoRiempito) {
            for (Famiglia f : (List<Famiglia>) families) {
                if (f.getXr() > 1e-5)
                    f.getNextSubset().forEach(it -> ker.addItem(it));
            }
            zainoRiempito= true;
        }
        return ker;
    }
}
