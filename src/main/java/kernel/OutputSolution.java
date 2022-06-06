package kernel;

public class OutputSolution {
    private String nomeIstanza;
    private int numeroFamiglie;
    private int numeroVariabili;
    private double ottimoRilassato;
    private int ottimoAtteso;
    private int ottimoRaggiunto;
    private double scarto;
    private int tempoImpiegato;
    private double risorseUtilizzate;
    private int mod;

    public OutputSolution(){
    }
    public void setNomeIstanza(String nomeIstanza) {
        this.nomeIstanza = nomeIstanza;
    }

    public void setNumeroFamiglie(int numeroFamiglie) {
        this.numeroFamiglie = numeroFamiglie;
    }

    public void setNumeroVariabili(int numeroVariabili) {
        this.numeroVariabili = numeroVariabili;
    }

    public void setOttimoRilassato(double ottimoRilassato) {
        this.ottimoRilassato = ottimoRilassato;
    }

    public void setOttimoAtteso(int ottimoAtteso) {
        this.ottimoAtteso = ottimoAtteso;
    }

    public void setOttimoRaggiunto(int ottimoRaggiunto) {
        this.ottimoRaggiunto = ottimoRaggiunto;
    }

    public void setTempoImpiegato(int tempoImpiegato) {
        this.tempoImpiegato = tempoImpiegato;
    }

    public void setRisorseUtilizzate(double risorseUtilizzate) {
        this.risorseUtilizzate = risorseUtilizzate;
    }

    public void setMod(int mod) {
        this.mod = mod;
    }
    private double calcScarto(){
       return ((double) this.ottimoAtteso-(double) this.ottimoRaggiunto)/(double) this.ottimoAtteso*100.0;
    }

    public String getCsvRes() {
        String res ="";
        res+= this.nomeIstanza+";";
        res+= String.valueOf(this.numeroFamiglie)+";";
        res+= String.valueOf(this.numeroVariabili)+";";
        res+= String.valueOf(this.ottimoRilassato)+";";
        res+= String.valueOf(this.ottimoAtteso)+";";
        res+= String.valueOf(this.ottimoRaggiunto)+";";
        res+= String.valueOf(this.calcScarto())+";";
        /*res+= String.valueOf(this.tempoImpiegato)+";";
        res+= String.valueOf(this.risorseUtilizzate)+";";*/
        return res;
    }
}
