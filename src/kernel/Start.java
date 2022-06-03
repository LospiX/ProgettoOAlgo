package kernel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Start
{
	public static void main(String[] args) throws IOException {

		/*String pathmps = args[0];
		String pathlog = args[1];
		String pathConfig = args[2];*/


		var res = getOptimum(new File("KPS_results.CSV"), "prob7_200_100000_10");
		System.out.println("Res   "+ res);
		//String pathmps = ".\\Istanze\\mps\\SCmps\\test5x500-SC(3).mps";
		String pathInstance = ".\\Istanze\\KPS_Class3\\Class3\\prob3_050_090_110_005_015_01.txt";
//		String pathInstance = ".\\Istanze\\InstancesCorrect\\test20x10000-SC(1).txt";
		String pathInstancemps = ".\\Istanze\\mps\\Class3Mps\\prob3_050_090_110_005_015_01.mps";
//		String pathInstancemps = ".\\Istanze\\mps\\SCmps\\test20x10000-SC(1).mps";
		//String pathmps = ".\\Istanze\\mps\\Class2Mps\\prob2_100_090_110_035_045_10.mps";
		String pathlog = ".\\log";
		String pathConfig = ".\\config.txt";

		Configuration config = ConfigurationReader.read(pathConfig);
		KernelSearch ks = new KernelSearch(pathInstance, pathInstancemps, pathlog, config);
		System.out.println("BEST SOLUTION:: "+ks.start().getObj());
		Runtime rtime = Runtime.getRuntime();
		//rtime.gc();
		long memory = rtime.totalMemory()-rtime.freeMemory();

		System.out.println("USED MEMORY IN BYTES " +memory);
		System.out.println("USED MEMORY IN MEGABYTES " +memory/(1024L*1024L));

	}

	public static long getOptimum(File file, String nomeIstanza){
		try{
			var lines = Files.readAllLines(file.toPath());
			return Long.parseLong(lines.stream()
				.map(line -> new String[]{line.split(";")[0].trim(), line.split(";")[1].trim()})
				.filter(elem -> elem[0].equals(nomeIstanza))
				.findFirst()
				.orElseThrow()[1]);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}