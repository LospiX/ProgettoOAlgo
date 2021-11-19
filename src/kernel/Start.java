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
		String pathmps = ".\\Istanze\\mps\\Class1Mps\\prob3_100_090_110_035_045_01.mps";
		String pathlog = ".\\log";
		String pathConfig = ".\\config.txt";

		Configuration config = ConfigurationReader.read(pathConfig);
		KernelSearch ks = new KernelSearch(pathmps, pathlog, config);
		ks.start();
		
		List<List<Double>> objValues = ks.getObjValues();	
	}
}