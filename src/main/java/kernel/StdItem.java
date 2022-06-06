package kernel;

public class StdItem implements Item {
    private String name;
    private double rc;
    private double xr;
    public StdItem(String name, double xr, double rc)
    {
        this.name = name;
        this.xr = xr;
        this.rc = rc;
    }

    public double  getRc() {
        return rc;
    }

    @Override
    public double getRapportoProfPeso() {
        return 0;
    }

    public double getXr() {
        return xr;
    }

    public double getAbsoluteRC() {
        return Math.abs(rc);
    }

    @Override
    public String getVarName() {
        return this.name;
    }
}
