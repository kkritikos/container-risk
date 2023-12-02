package gr.aegean.container.risk.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gr.aegean.container.risk.configuration.PropertyReader;


public class ClairScannerRunner implements ToolRunner {
	private ArrayList<String> clairFiles;
	private static final String imagesDir = "images";
	private static final String filePostfix = "_res_from_clairScanner.json";
	
	public ClairScannerRunner() {
	}
	
	@Override
	public void runTool(List<String> imageNames) {
		clairFiles = new ArrayList<String>();
		
		for (int i = 0; i < imageNames.size(); i++) {
			System.out.println("Auto file to save clair-scanner output for image: " + imageNames.get(i));
			
			String clairFile = imagesDir + File.separator + "clair_" + imageNames.get(i).replace(':', '_') + filePostfix;
			clairFiles.add(clairFile);
			File file = new File(clairFile);
			
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("bash", "-c", " " + PropertyReader.getClairPath() + File.separator + "./clair-scanner --ip " + PropertyReader.getClairApi() + " -r " + file.toString() + " " + imageNames.get(i));
			//processBuilder.command("bash", "-c","/home/manolis/clair-scanner/./clair-scanner --ip 172.18.0.1 -r lal.json mongo");
			processBuilder.redirectOutput(file);
			try {
				Process process = processBuilder.start();
				int exitVal = process.waitFor();
				//System.out.println("exit value: "+exitVal);
				if (exitVal == 1) {
					System.out.println("Success!");
				} 
				else {
					System.out.println("failed");
				}
			} 
			catch (Exception e) {
				System.out.println("e: " + e);
			}
		}
	}

	@Override
	public List<String> getResultFiles() {
		return clairFiles;
	}
}
