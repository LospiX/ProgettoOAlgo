package xpKernelSearch;

import java.io.BufferedReader;
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

    public static void main(String[] args) {
        Converter conv = new Converter("C:\\Users\\Xavier\\Documents\\XP - Asus\\Università\\Magistrale 1 anno\\Optimization Algorithms\\Project\\Istanze\\KPS_Class1\\Class1\\prob1_050_040_060_005_015_01.txt", "C:\\Users\\Xavier\\Documents\\XP - Asus\\Università\\Magistrale 1 anno\\Optimization Algorithms\\Project\\Istanze\\mps\\Class1Mps");
        conv.convert();
    }

    Converter(String filename, String destinationFolder) {
        this.path = Path.of(filename);
        this.destinationFolder = Path.of(destinationFolder);
        this.destination = Path.of(destinationFolder+"\\"+this.path.getFileName()+".mps");
    }

    public void convert (){
        try {
            List<String> lines = this.extractFromFile(this.path);
            int numOfVars = Integer.parseInt(lines.get(0));
            int numbOfFamilies = Integer.parseInt(lines.get(1));
            int capacity = Integer.parseInt(lines.get(2));
            int[] dimOfFamilies =  Arrays.stream(lines.get(3).split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] costsSetupOfFamilies =  Arrays.stream(lines.get(4).split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] costsOfFamilies =  Arrays.stream(lines.get(5).split(" ")).mapToInt(Integer::parseInt).toArray();
            int[] profits= new int[numOfVars];
            int[] costs= new int[numOfVars];
            for (int i=0; i<=5; i++)
                lines.remove(0);
            for (int i=0; i< numOfVars; i++){
                //ATTENZIONE SPACING Dipende da istanze ad istanze
                profits[i]= Integer.parseInt(lines.get(i).split("\s*")[0].trim());
                costs[i]= Integer.parseInt(lines.get(i).split("\s*")[1].trim());
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
