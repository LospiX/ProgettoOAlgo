package kernel;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Start
{
	public static void main(String[] args) throws IOException {

		/*String pathmps = args[0];
		String pathlog = args[1];
		String pathConfig = args[2];*/

		//String pathmps = ".\\Istanze\\mps\\SCmps\\test5x500-SC(3).mps";
		String pathInstance = ".\\Istanze\\InstancesCorrect\\test10x10000-SC(1).txt";
		String pathInstancemps = ".\\Istanze\\mps\\SCmps\\test10x10000-SC(1).mps";
		//String pathmps = ".\\Istanze\\mps\\Class2Mps\\prob2_100_090_110_035_045_10.mps";
		String pathlog = ".\\log";
		String pathConfig = ".\\config.txt";

		Configuration config = ConfigurationReader.read(pathConfig);
		KernelSearch ks = new KernelSearch(pathInstance, pathInstancemps, pathlog, config);
		System.out.println("BEST SOLUTION:: "+ks.start().getObj());
		
		List<List<Double>> objValues = ks.getObjValues();	
	}
}