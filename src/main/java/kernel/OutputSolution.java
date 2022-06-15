package kernel;

import java.util.List;

public class OutputSolution {
    private String nomeIstanza;
    private int numeroFamiglie;
    private int numeroVariabili;
    private double ottimoRilassato;
    private int ottimoAtteso;
    private int ottimoRaggiunto;
    private double scarto;
    private long tempoImpiegato;
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

    public void setTempoImpiegato(long tempoImpiegato) {
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
        res+= String.valueOf(this.tempoImpiegato)+";";/*
        res+= String.valueOf(this.risorseUtilizzate)+";";*/
        return res;
    }


    public static String getCsvSolutionHeader() {
        return "nomeIstanza;numeroFamiglie;numeroVariabili;ottimoRilassato;ottimoAtteso;ottimoRaggiunto;scarto;tempoImpiegato;risorseUtilizzate;mod";
        /*String res = "";
        Arrays.stream(OutputSolution.class.getDeclaredFields()).forEach(f -> {
            System.out.print(f.getName()+";");
        });

        return res;*/
    }
    public static String getHeader(List<String> lines) {
        String header = "";
        for (String line : lines) {
            header += switch (line.split("\\s+")[0]) {
                case "SORTER" -> "SORTER "+ getIntegerParam(line);
                case "NUMITERATIONS" -> "NUMITERATIONS " + getIntegerParam(line);
                case "NUMBEROFTRIES" -> "NUMBEROFTRIES " + getIntegerParam(line);

                case "SUBSETFACTOR" -> "SUBSETFACTOR " + getDoubleParam(line);
                case "KERNELSETDIMENSION" -> "KERNELSETDIMENSION "+ getDoubleParam(line);
                case "BUCKETDIMENSION" -> "BUCKETDIMENSION "+ getDoubleParam(line);
                case "DECREASINGPERCENTAGE" -> "DECREASINGPERCENTAGE "+ getDoubleParam(line);
                case "MINSUBSETDIMPERCENTAGE" -> "MINSUBSETDIMPERCENTAGE " + getDoubleParam(line);
                default -> "";
            };
        }
        return header;
    }

    private static String getIntegerParam(String line) {
        return line.split("\\s")[1]+"\n";
    }
    private static String getDoubleParam(String line) {
        return line.split("\\s")[1]+"\n";
    }
}
