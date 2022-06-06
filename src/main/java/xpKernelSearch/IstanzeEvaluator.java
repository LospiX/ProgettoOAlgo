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
    String[] benchmark = {
        "test5x500-SC(3).mps",
        "test5x500-SC(6).mps",
        "test30x2500-SC(1).mps",
        "test30x2500-SC(2).mps",
        "test20x5000-SC(1).mps",
        "test20x5000-SC(2).mps",
        "prob1_050_090_110_025_035_01.mps",
        "prob1_050_090_110_025_035_02.mps",
        "test10x10000-SC(1).mps",
        "test10x10000-SC(2).mps",
        "prob1_100_090_110_015_025_01.mps",
        "prob1_100_090_110_015_025_02.mps",
        "prob5_010_020000_01.mps",
        "prob5_010_020000_02.mps",
        "prob5_005_050000_01.mps",
        "prob5_005_050000_02.mps"
        };
        String pathlog = ".\\log";
        List<IstanzaMIP> istanzeToSolve= new ArrayList<>();
        IstanzeEvaluator evaluator = new IstanzeEvaluator();
        for (File f : new Explorer(".\\Istanze\\mps").retrieveFiles(benchmark)) {
            File mpsFile = new File(evaluator.getRelativePath(f.getAbsolutePath()));
            istanzeToSolve.add(new IstanzaMIP(mpsFile, pathlog));
        }
        String pathConfig = ".\\config.txt";
        Configuration config = ConfigurationReader.read(pathConfig);

        evaluator.solveIstanzeRelaxed(istanzeToSolve);
        /*for (IstanzaMIP ist: istanzeToSolve){

            resLines[istanzeToSolve.indexOf(f)] = f.getName().subSequence(0, f.getName().indexOf(".mps"))+" ;  ";

            //resLines[istanzeToSolve.indexOf(f)] = new MipResolver(f).solve()+"    "; // Solved by Gurobi
            //config.setKernelSize(1);
            //resLines[istanzeToSolve.indexOf(f)] = new  KernelSearch(f.getPath(), pathlog, config).start().getObj()+"    "; // Solved by Gurobi

            config.setKernelSize(0.15);
            config.setBucketSize(0.025);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj()+" ;   "; // Solve by KerSerch with 2.5% dim buckets
            config.setBucketSize(0.1);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj()+" ;   "; // Solve by KerSerch with 10% dim buckets
            config.setBucketSize(0.25);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj()+" ;   "; // Solve by KerSerch with 25% dim buckets

            config.setBucketSize(0.33);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj()+" ;   ";   // Solve by KerSerch with 40% dim bucketsconfig.setBucketSize(0.4);
            config.setBucketSize(0.5);
            resLines[istanzeToSolve.indexOf(f)] += new KernelSearch(f.getPath(), pathlog, config).start().getObj();   // Solve by KerSerch with 50% dim bucketsconfig.setBucketSize(0.4);

        }
        System.out.println("RESULTS");
        for(String r: resLines)
            System.out.println(r);*/
        for (IstanzaMIP ist: istanzeToSolve)
            System.out.println(ist.getName()+" relaxed Sol "+ist.getRelaxedSolution());
    }

    public void solveIstanze (List<IstanzaMIP> istanze) {
        for (IstanzaMIP ist: istanze){
        }
    }
    public void solveIstanzeRelaxed (List<IstanzaMIP> istanze) {
        for (IstanzaMIP ist: istanze){
            ist.solveRelaxed();
        }
    }

    private String getRelativePath(String absPaths) {
        String dirPath = System.getProperty("user.dir");
        return absPaths.substring(dirPath.length()+1, absPaths.length());
    }
}
