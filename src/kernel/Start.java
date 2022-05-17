package kernel;

import java.nio.file.Path;
import java.util.List;

public class Start
{
	public static void main(String[] args)
	{

		/*String pathmps = args[0];
		String pathlog = args[1];
		String pathConfig = args[2];*/

		//String pathmps = ".\\Istanze\\mps\\SCmps\\test5x500-SC(3).mps";
		String pathmps = ".\\Istanze\\mps\\SCmps\\test10x1000-SC(1).mps";
		//String pathmps = ".\\Istanze\\mps\\Class2Mps\\prob2_100_090_110_035_045_10.mps";
		String pathlog = ".\\log";
		String pathConfig = ".\\config.txt";

		Configuration config = ConfigurationReader.read(pathConfig);
		KernelSearch ks = new KernelSearch(pathmps, pathlog, config);
		System.out.println("BEST SOLUTION:: "+ks.start());
		
		List<List<Double>> objValues = ks.getObjValues();	
	}
}