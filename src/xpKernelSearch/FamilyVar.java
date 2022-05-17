package xpKernelSearch;

import kernel.Item;

import java.util.List;

//, List<ItemVar> items)
public record FamilyVar(String varName, double Xr) implements Item {
    @Override
    public String getVarName() {
        return this.varName;
    }

    @Override
    public double getXr() {
        return this.Xr;
    }
}
