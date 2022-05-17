package xpKernelSearch;

import kernel.Item;

import java.util.List;

//, List<ItemVar> items)
public record FamilyVar(String varName) implements Item {
    @Override
    public String getVarName() {
        return this.varName;
    }
}
