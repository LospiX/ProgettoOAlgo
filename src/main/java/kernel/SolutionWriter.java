package kernel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SolutionWriter {

    private String path_out;
    private String conf_path;

    private String risultati = ".\\risultati";


    public SolutionWriter(String conf_path) {
        File dir = new File(Path.of(".\\risultati").toUri());
        if(!dir.exists())
            dir.mkdir();
        this.path_out = risultati.concat("\\"+generate_path_out(conf_path)+".txt") ;
        this.conf_path = conf_path;
    }

    private String generate_path_out(String conf_path) {
        ArrayList<String> parameters = ConfigurationReader.get_general_conf(conf_path);
        StringBuffer out = new StringBuffer();
        for (String s : parameters) {
            out.append(s.replace(".", "")+"_");
        }
        return out.toString();
    }

    public void write_solution(OutputSolution sol) throws IOException {
        File out = new File(path_out);
        if(out.exists()){
            FileWriter fl = new FileWriter(out, true);
            BufferedWriter bf = new BufferedWriter(fl);
            bf.append(sol.getCsvRes()+"\n").flush();
            bf.close();
        }
        else {
            Files.createFile(out.toPath());
            FileWriter fl = new FileWriter(out, true);
            BufferedWriter bf = new BufferedWriter(fl);
            bf.append(OutputSolution.getHeader(conf_path)+"\n");
            bf.append(OutputSolution.getCsvSolutionHeader()+"\n");
            bf.append(sol.getCsvRes()+"\n").flush();
            bf.close();

        }
    }
}
