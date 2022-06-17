package kernel;

import xpKernelSearch.Explorer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Start
{
	private static String CLASSPATH;
	public static void main(String[] args) throws Exception {

//		System.out.println(OutputSolution.getHeader(Files.readAllLines(Path.of(".\\config.txt"))));
		System.out.println("Working directory ::" +System.getProperty("user.dir"));
		CLASSPATH = Arrays.stream(System.getProperty("java.class.path").split(";")).filter(e -> e.endsWith("classes")|| e.endsWith(".jar")).findFirst().orElseThrow();
		Path classPath = Path.of(CLASSPATH);
		Path jarDirHome = Path.of(System.getProperty("user.dir"));
		String pathlog  =  Path.of(jarDirHome.toAbsolutePath().toString(), "log").toString();
		String pathConfig  =  Path.of(jarDirHome.toAbsolutePath().toString(), "config.txt").toString();
		Configuration config = ConfigurationReader.read(pathConfig);
		SolutionWriter solwriter = new SolutionWriter(pathConfig);
		List<OutputSolution> solutions = new ArrayList<>();

		Explorer exp = new Explorer(".\\istanze");
		/*String pathOfIstanze;
		if(Start.class.getResource("Start.class").toString().startsWith("jar")) {
			System.out.println("The program is running as a jar");
			exp = new Explorer(Start.class.getResource("/Istanze").toURI());
		} else {
			System.out.println("The program is NOT running as a jar");
			pathOfIstanze = Start.class.getResource("/Istanze").toURI().getPath().substring(1); // substring to remove the leading a slash
			exp = new Explorer(pathOfIstanze);
		}*/
		/*System.out.println("Start.class.getResource(\"/Istanze\")= "+Start.class.getResource("/Istanze"));
		System.out.println("Start.class.getResource(\"/Istanze\toUri\")= "+Start.class.getResource("/Istanze").toURI());
		System.out.println("Start.class.getResource(\"/Istanze\"getPath)= "+Start.class.getResource("/Istanze").toURI().getPath());*/
		List<String[]> istanze = Arrays.stream(args)
			.map(ist -> new String[]{ist + ".txt", ist + ".mps"})
			.toList();
		OutputSolution solution;
		KernelSearch ks;
		for (String[] ist : istanze) {
			solution = new OutputSolution();
			String nomeIstanza = ist[0].split("\\.")[0];
			solution.setNomeIstanza(nomeIstanza);
			solution.setOttimoAtteso((int) getOptimum("/risultati/KPS_results.CSV", nomeIstanza));
			var pth = exp.retrieveFiles(ist);
			System.out.println("Inizio ad eseguire il KernelSearch sull'istanza chiamata: "+ist[0]+"  "+ist[1]);
			runKerSearch(pth, pathlog, config, solution);
			solutions.add(solution);
			solwriter.write_solution(solution);
		}
		solutions.forEach(s -> {
			System.out.println("Soluzione istanza "+ "\n\t" + s.getCsvRes());
		});
		// TODO: Guarda perchè prob3_050_090_110_005_015_01 è cursata
	}

	private static void runKerSearch(List<File> pth, String pathlog, Configuration config, OutputSolution sol) throws IOException {
		Runtime rtime = Runtime.getRuntime();
		//rtime.gc();
		long memory = rtime.totalMemory()-rtime.freeMemory();
		var bef = Instant.now();
		KernelSearch ks = new KernelSearch(pth.get(0).getPath(), pth.get(1).getPath(), pathlog, config);
		sol.setOttimoRaggiunto((int) ks.start().getObj());
		var timeBetween = Duration.between(bef, Instant.now()).toMillis();
		sol.setTempoImpiegato(timeBetween);
		sol.setOttimoRilassato(ks.getOttimoRilassato());
		sol.setNumeroFamiglie(ks.getNumOfFamilies());
		sol.setNumeroVariabili(ks.getNumOfVariables());
		sol.setNumeroIterazioni(ks.getNumeroIterazioni());
		/*System.out.println("USED MEMORY IN BYTES " +memory);
		System.out.println("USED MEMORY IN MEGABYTES " +memory/(1024L*1024L));*/
	}

	public static long getOptimum(String resPath, String nomeIstanza){
		try (var bReader = new BufferedReader(new InputStreamReader(
			Start.class.getResourceAsStream(resPath)))){
			var lines = bReader.lines().toList();
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