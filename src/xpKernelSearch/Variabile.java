package xpKernelSearch;

import kernel.Item;

public class Variabile implements Item {
    private final String id;
    private final int profitto;
    private final int peso;
    private boolean selected;
    public Variabile(String id, int profitto, int peso){
        this.id = id;
        this.profitto = profitto;
        this.peso = peso;
        this.selected = false;
    }
    public double getRapportoProfPeso() {
        return ((double) profitto) / ((double) peso);
    }

    @Override
    public String toString() {
        return "Id: "+id+"  profSuPeso:: " + this.getRapportoProfPeso();
    }

    @Override
    public String getVarName() {
        return this.id;
    }

    @Override
    public double getXr() {
        return 0;
    }

    @Override
    public double getRc() {
        return 0;
    }

    public int getProfitto() {
        return this.profitto;
    }

    public int getPeso() {
        return this.peso;
    }
    public boolean gotSelected() {
        return selected;
    }
    public void setSelected() {
        this.selected = true;
    }
}
