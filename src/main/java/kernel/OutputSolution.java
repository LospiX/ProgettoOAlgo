package kernel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OutputSolution {
    private String nomeIstanza;
    private int numeroFamiglie;
    private int numeroVariabili;
    private double ottimoRilassato;
    private int ottimoAtteso;
    private int ottimoRaggiunto;
    private long tempoImpiegato;
//    private double risorseUtilizzate;
    private int numeroIterazioni;

    private String header;

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

//    public void setRisorseUtilizzate(double risorseUtilizzate) {
//        this.risorseUtilizzate = risorseUtilizzate;
//    }

    public void setNumeroIterazioni(int mod) {
        this.numeroIterazioni = mod;
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
        res+= String.valueOf(this.tempoImpiegato)+";";
        res+= String.valueOf(this.numeroIterazioni)+";";/*
        res+= String.valueOf(this.risorseUtilizzate)+";";*/
        return res;
    }


    public static String getCsvSolutionHeader() {
        return "nomeIstanza;numeroFamiglie;numeroVariabili;ottimoRilassato;ottimoAtteso;ottimoRaggiunto;scarto;tempoImpiegato;numeroIterazioni";
        /*String res = "";
        Arrays.stream(OutputSolution.class.getDeclaredFields()).forEach(f -> {
            System.out.print(f.getName()+";");
        });

        return res;*/
    }
    public static String getHeader(String path) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            lines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
