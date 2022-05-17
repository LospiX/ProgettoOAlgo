package xpKernelSearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestingClass {
    public static void main (String[] args) throws IOException {
        List<String> hi= new ArrayList<String>();
        hi.add("ciao");
        hi.add("miao");
        hi.add("xiao");
        hi.add("wiao");
        hi.add("yiao");
        int idx = 4;
        System.out.println(idx +"    "+ hi.get(idx++)+"   idx: "+ idx);
        ProblemaKnapSackSetup prob = new ProblemaKnapSackSetup (new File(".\\Istanze\\InstancesCorrect\\test5x500-SC(1).txt"));
        prob.printFamily(0);
    }
}
