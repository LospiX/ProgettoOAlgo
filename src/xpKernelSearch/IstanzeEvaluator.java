package xpKernelSearch;

import kernel.Configuration;
import kernel.ConfigurationReader;
import kernel.KernelSearch;
import kernel.Start;

import java.io.File;
import java.util.List;

public class IstanzeEvaluator {
    public IstanzeEvaluator () {

    }
    public static void main (String[] args) throws Exception {
        List<File> files = new Explorer(".\\Istanze\\mps\\Class1Mps").retrieveFiles();
        String pathlog = ".\\log";
        String pathConfig = ".\\config.txt";
        Configuration config = ConfigurationReader.read(pathConfig);
        for (File f : files) {
            System.out.println(f.getName()+"  Soluzione:  "+ new KernelSearch(f.getPath(), pathlog, config ).start().getObj());
        }
    }
}
