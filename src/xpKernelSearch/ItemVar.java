package xpKernelSearch;

public record ItemVar(String varName, FamilyVar family) implements Itm {
    @Override
    public String getVarName() {
        return this.varName;
    }
}
