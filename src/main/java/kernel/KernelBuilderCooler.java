package kernel;

import xpKernelSearch.Famiglia;
import xpKernelSearch.SubSet;
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
            for (Famiglia f : (List<Famiglia>) families) {
                if (cnt == 0) { // Solo per la prima iterazione
                    ker.addItem(f); // Aggiungo tutte le famiglie al KerSet
                }
                if (f.getXr() > 0.0) {
                    this.addSubset(f.getNextSubset());
                    if (cnt == 0)
                        consumoZaino += f.getPeso(); // Aggiungo tutti i pesi delle famiglie al KerSet
                }
            }
            if(consumoZaino > config.getCapZaino()*config.getKernelSetDimension()) {
                zainoRiempito = true;
            }
            cnt++;
            System.out.println("Iterazione :: " + cnt+"   Consumo Zaino :: "+consumoZaino+"/"+config.getCapZaino()*config.getKernelSetDimension());
        }
        return ker;
    }


    private void addSubset(SubSet subSet) {
        for(Variabile v: subSet.getSet()) {
            consumoZaino += v.getPeso();
            ker.addItem(v);
        }
    }
}
