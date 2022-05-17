package xpKernelSearch;

import java.io.File;
import java.io.IOException;

public class TestingClass {
    public static void main (String[] args) throws IOException {
        ProblemaKnapSackSetup prob = new ProblemaKnapSackSetup (new File(".\\Istanze\\InstancesCorrect\\test5x500-SC(1).txt"));
        prob.printFamily(0);
    }
}
