package xpKernelSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converter {
    private Path path;
    private Path destination;
    private Path destinationFolder;

    public static void main(String[] args) throws Exception {
        List<File> files = new Explorer(".\\Istanze\\KPS_Class6\\Class6").retrieveFiles();
        for (File f: files) {
            //Converter conv = new Converter(".\\Istanze\\KPS_Class3\\Class3\\prob3_100_090_110_035_045_01.txt", ".\\Istanze\\mps\\Class1Mps");
            //Converter conv = new Converter(".\\Istanze\\InstancesCorrect\\test5x500-SC(5).txt", ".\\Istanze\\mps\\SCmps");
            Converter conv = new Converter(f.getAbsolutePath(), ".\\Istanze\\mps\\Class6Mps");
            conv.convert();
        }

    }

    Converter(String filename, String destinationFolder) {
        this.path = Path.of(filename);
        this.destinationFolder = Path.of(destinationFolder);
        this.destination = Path.of(destinationFolder+"\\"+this.path.getFileName().toString().subSequence(0, this.path.getFileName().toString().indexOf(".txt"))+".mps");
    }

    public void convert (){
        try {
            List<String> lines = this.extractFromFile(this.path);
            int numOfVars = Integer.parseInt(lines.get(0));
            int numbOfFamilies = Integer.parseInt(lines.get(1));
            int capacity = Integer.parseInt(lines.get(2));
            int[] dimOfFamilies =  Arrays.stream(lines.get(3).split("\s+")).mapToInt(Integer::parseInt).toArray();
            int[] costsSetupOfFamilies =  Arrays.stream(lines.get(4).split("\s+")).mapToInt(Integer::parseInt).toArray();
            int[] costsOfFamilies =  Arrays.stream(lines.get(5).split("\s+")).mapToInt(Integer::parseInt).toArray();
            int[] profits= new int[numOfVars];
            int[] costs= new int[numOfVars];
            for (int i=0; i<=5; i++)
                lines.remove(0);
            for (int i=0; i< numOfVars; i++){
                //ATTENZIONE SPACING Dipende da istanze ad istanze
                profits[i]= Integer.parseInt(lines.get(i).split("\s+")[0].trim());
                costs[i]= Integer.parseInt(lines.get(i).split("\s+")[1].trim());
            }

            MpsBuilder conv = new MpsBuilder(capacity, dimOfFamilies, costsSetupOfFamilies, costsOfFamilies, profits, costs);
            conv.build(destination);
        } catch (IOException e) {
            System.out.println(e.getMessage()+ "\nFile Path provided not correct");
        }
    }

    private List<String> extractFromFile (Path file) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = Files.newBufferedReader(file);
        String line = br.readLine();
        while (line != null){
            lines.add(line);
            line = br.readLine();
        }
        return lines;
    }
}
