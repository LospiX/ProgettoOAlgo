package xpKernelSearch;

import java.util.ArrayList;
import java.util.List;

public class TestSorting {
    public static void main(String[] args){
        List<Variabile> vars = new ArrayList<>();
        int max = 10;
        int min= 1;
        for(int i=0; i<100; i++){
            vars.add(new Variabile(String.valueOf(i), (int)Math.floor(Math.random()*(max-min+1)+min), (int)Math.floor(Math.random()*(max-min+1)+min)));
        }
        Famiglia fam = new Famiglia("fam", vars, 1, 1);
        fam.getVariablesOrdered().forEach((e) -> System.out.println(e));

    }
}
