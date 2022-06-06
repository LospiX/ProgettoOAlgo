package kernel;

import xpKernelSearch.Variabile;

import java.util.Comparator;
import java.util.List;

public class ItemSorterByProfitOverCost implements ItemSorter{
    @Override
    public List<Variabile> sort(List<Variabile> items) {
        items.sort(this.comparator);
        return items;
    }

    private Comparator<Item> comparator = (v1, v2) -> {
        if(v1.getRapportoProfPeso()>v2.getRapportoProfPeso())
            return  -1;
        else if(v1.getRapportoProfPeso()<v2.getRapportoProfPeso())
            return 1;
        return 0;
    };
}
