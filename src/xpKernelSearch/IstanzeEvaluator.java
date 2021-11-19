package xpKernelSearch;

import kernel.Configuration;
import kernel.ConfigurationReader;
import kernel.KernelSearch;
import kernel.Start;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IstanzeEvaluator {
    public IstanzeEvaluator () {

    }
    public static void main (String[] args) throws Exception {
        //List<File> files = new Explorer(".\\Istanze\\mps\\Class1Mps").retrieveFiles();
        List<File> files = new Explorer(".\\Istanze\\mps\\SCmps").retrieveFiles();
        String pathlog = ".\\log";
        String pathConfig = ".\\config.txt";
        Configuration config = ConfigurationReader.read(pathConfig);
        String resTxt = "";
        List<File> istanzeToSolve= new ArrayList<>();
        for (File f : files) {
            if (!f.getName().startsWith("test30x2500-SC(1)") && !f.getName().startsWith("test30x2500-SC(2)"))
                continue;
            istanzeToSolve.add(f);
            System.out.println(f.canRead());
        }
        String[] resLines = new String[istanzeToSolve.size()];
        for (File f: istanzeToSolve){
            config.setKernelSize(1);
            resLines[istanzeToSolve.indexOf(f)] = f.getName().subSequence(0, f.getName().indexOf(".mps"))+"   "+
                    new MipResolver(f).solve()+"    "; // Solved by Gurobi
            //resLines[istanzeToSolve.indexOf(f)] = f.getName()+"   "+
            //        new  KernelSearch(f.getPath(), pathlog, config).start().getObj()+"    "; // Solved by Gurobi
            config.setKernelSize(0.15);
            config.setBucketSize(0.025);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj()+"    "; // Solve by KerSerch with 2.5% dim buckets
            config.setBucketSize(0.1);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj()+"    "; // Solve by KerSerch with 10% dim buckets
            config.setBucketSize(0.25);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj()+"    "; // Solve by KerSerch with 25% dim buckets
            config.setBucketSize(0.4);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj()+"    ";   // Solve by KerSerch with 40% dim bucketsconfig.setBucketSize(0.4);
            config.setBucketSize(0.5);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj();   // Solve by KerSerch with 50% dim bucketsconfig.setBucketSize(0.4);

        }
        System.out.println("RESULTS");
        for(String r: resLines)
            System.out.println(r);
    }
}
