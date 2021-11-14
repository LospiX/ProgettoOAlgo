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

		//String pathmps = ".\\Istanze\\mps\\SCmps\\";
		String pathmps = ".\\Istanze\\mps\\Class1Mps\\prob1_050_040_060_005_015_01.txt.mps";
		String pathlog = ".\\log";
		String pathConfig = ".\\config.txt";

		Configuration config = ConfigurationReader.read(pathConfig);
		KernelSearch ks = new KernelSearch(pathmps, pathlog, config);
		ks.start();
		
		List<List<Double>> objValues = ks.getObjValues();	
	}
}