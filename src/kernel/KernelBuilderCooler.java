package kernel;

import xpKernelSearch.Famiglia;
import xpKernelSearch.Variabile;

import java.util.List;

public class KernelBuilderCooler implements KernelBuilder {
    private final Kernel ker = new Kernel();
    private int consumoZaino;
    @Override
    public Kernel build(List<? extends Item> families, Configuration config) {
        System.out.println("Families passate al ker builder size:: "+families.size());
        for (var f : families){
            System.out.println("\t var: "+f.getVarName()+ "   xr: "+f.getXr());
        }
        boolean zainoRiempito= false;
       this.consumoZaino = 0;
        int cnt = 0;
        while(!zainoRiempito) {
            /*if (cnt == 0) {
                for (Famiglia f : (List<Famiglia>) families) {
                    ker.addItem(f);
                    consumoZaino+= f.getPeso();
                    // System.out.println("Subset dim :: "+ f.getNextSubset().size());
                    this.addSubset(f.getNextSubset());
                }
            } else {*/
            for (Famiglia f : (List<Famiglia>) families) {
                if (cnt == 0) {
                    ker.addItem(f);
                    consumoZaino += f.getPeso();
                }
                if (f.getXr() > 1e-5) {
                    this.addSubset(f.getNextSubset());
                }
            }
//            }
            System.out.println("CCAAAP :: "+config.getCapZaino());
            if(consumoZaino > config.getCapZaino()) {
                zainoRiempito = true;
            }
            cnt++;
            System.out.println("Consumo Totale iterazione :: " + cnt+"   valore :: "+consumoZaino);
        }
        return ker;
    }


    private void addSubset(List<Variabile> varsToAdd) {
        for(Variabile v: varsToAdd) {
            consumoZaino += v.getPeso();
            ker.addItem(v);
        }
    }
}
