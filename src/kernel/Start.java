package kernel;

import xpKernelSearch.Explorer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Start
{
	public static void main(String[] args) throws Exception {
		System.out.println("Working directory ::" +System.getProperty("user.dir"));
		String pathlog = ".\\log";
		String pathConfig = ".\\config.txt";
		Configuration config = ConfigurationReader.read(pathConfig);
		List<OutputSolution> solutions = new ArrayList<>();
		Explorer exp = new Explorer("Istanze");

		List<String[]> istanze = Arrays.stream(args)
			.map(ist -> new String[]{ist + ".txt", ist + ".mps"})
			.toList();
		OutputSolution solution;
		KernelSearch ks;
		for (String[] ist : istanze) {
			solution = new OutputSolution();
			String nomeIstanza = ist[0].split("\\.")[0];
			solution.setNomeIstanza(nomeIstanza);
			solution.setOttimoAtteso((int) getOptimum(new File("KPS_results.CSV"), nomeIstanza));
			var pth = exp.retrieveFiles(ist);
			ks = new KernelSearch(pth.get(0).getPath(), pth.get(1).getPath(), pathlog, config);
			System.out.println("Inizio ad eseguire il KernelSearch sull'istanza chiamata: "+ist[0]+"  "+ist[1]);
			runKerSearch(ks, solution);
			solutions.add(solution);
		}
		solutions.forEach(s -> {
			System.out.println("Soluzione istanza "+ "\n\t" + s.getCsvRes());
		});

		/*
		//String pathmps = ".\\Istanze\\mps\\SCmps\\test5x500-SC(3).mps";
		String pathInstance = ".\\Istanze\\KPS_Class3\\Class3\\prob3_050_090_110_005_015_01.txt";
//		String pathInstance = ".\\Istanze\\InstancesCorrect\\test20x10000-SC(1).txt";
		String pathInstancemps = ".\\Istanze\\mps\\Class3Mps\\prob3_050_090_110_005_015_01.mps";
//		String pathInstancemps = ".\\Istanze\\mps\\SCmps\\test20x10000-SC(1).mps";
		//String pathmps = ".\\Istanze\\mps\\Class2Mps\\prob2_100_090_110_035_045_10.mps";*/
		// TODO: Guarda perchè prob3_050_090_110_005_015_01 è cursata



	}

	private static void runKerSearch(KernelSearch ks, OutputSolution sol) {
//		System.out.println("BEST SOLUTION:: "+ks.start().getObj());
		Runtime rtime = Runtime.getRuntime();
		//rtime.gc();
		long memory = rtime.totalMemory()-rtime.freeMemory();

		sol.setOttimoRaggiunto((int) ks.start().getObj());
		sol.setOttimoRilassato(ks.getOttimoRilassato());
		sol.setNumeroFamiglie(ks.getNumOfFamilies());
		sol.setNumeroVariabili(ks.getNumOfVariables());
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