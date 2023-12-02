package gr.aegean.container.risk.scanner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrypeRunner implements ToolRunner {
	private ArrayList<String> grypeFiles;
	private static final String imagesDir = "images";
	private static final String filePostfix = "_res_from_grype.json";
	
	public GrypeRunner() {
	}
	
	public ArrayList<String> getGrypeFiles(){
		return grypeFiles;
	}
		
	@Override
	public void runTool(List<String> imageNames) {
		grypeFiles = new ArrayList<String>();

		for (int i = 0; i < imageNames.size(); i++) {
			System.out.println("Auto created file to save Grype output for image: " + imageNames.get(i));
			
			String grypeFile = imagesDir + File.separator + "grype_" + imageNames.get(i).replace(':','_') + filePostfix;
			grypeFiles.add(grypeFile);
			File file = new File(grypeFile);
			
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("sh", "-c", "grype " + imageNames.get(i) + " -o json");
			processBuilder.redirectOutput(file);
			try {
				Process process = processBuilder.start();
				
				int exitVal = process.waitFor();
				if (exitVal == 0) {
					System.out.println("Success!");
				} 
				else {
					System.out.println("Error");
				}
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<String> getResultFiles() {
		return grypeFiles;
	}
}
