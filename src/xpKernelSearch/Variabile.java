package xpKernelSearch;

public record Variabile(String id, int profitto, int peso) {
    public double getRapportoProfPeso() {
        return ((double) profitto) / ((double) peso);
    }

    @Override
    public String toString() {
        return "Id: "+id+"  profSuPeso:: " + this.getRapportoProfPeso();
    }
}
