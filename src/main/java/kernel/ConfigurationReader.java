package kernel;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationReader {
	public static Configuration read(String path) {
		Configuration config = new Configuration();
		
		List<String> lines = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
        	lines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
			e.printStackTrace();
		}
        for(String line : lines) {
        	String[] splitLine = line.split("\\s+");
        	String valToParse = splitLine[1];
			switch(splitLine[0]) {
				case "THREADS" -> config.setNumThreads(Integer.parseInt(valToParse));
				case "MIPGAP" -> config.setMipGap(Double.parseDouble(valToParse));
				case "PRESOLVE" -> config.setPresolve(Integer.parseInt(valToParse));
				case "TIMELIMIT" -> config.setTimeLimit(Integer.parseInt(valToParse));
				case "SORTER" -> {
					switch (Integer.parseInt(valToParse)) {
						case 0 -> config.setItemSorter(new ItemSorterByValueAndAbsoluteRC());
						case 1 -> config.setItemSorter(new ItemSorterByProfitOverCost());
						default -> System.out.println("Unrecognized item sorter.");
					}
				}
				case "KERNELSIZE" -> config.setKernelSize(Double.parseDouble(valToParse));
				case "BUCKETSIZE" -> config.setBucketSize(Double.parseDouble(valToParse));
				case "BUCKETBUILDER" -> {
					switch (Integer.parseInt(valToParse)) {
						case 0 -> config.setBucketBuilder(new DefaultBucketBuilder());
						default -> System.out.println("Unrecognized bucket builder.");
					}
				}
				case "TIMELIMITKERNEL" -> config.setTimeLimitKernel(Integer.parseInt(valToParse));
				case "NUMITERATIONS" -> config.setNumIterations(Integer.parseInt(valToParse));
				case "TIMELIMITBUCKET" -> config.setTimeLimitBucket(Integer.parseInt(valToParse));
				case "KERNELBUILDER" -> {
					switch (Integer.parseInt(valToParse)) {
						case 0 -> config.setKernelBuilder(new KernelBuilderPositive());
						case 1 -> config.setKernelBuilder(new KernelBuilderPercentage());
						case 2 -> config.setKernelBuilder(new KernelBuilderCooler());
						default -> System.out.println("Unrecognized kernel builder.");
					}
				}
				case "BUCKETRESOLVER" -> config.setBucketResolver(Integer.parseInt(valToParse));
			}
        }
        return config;
	}
}