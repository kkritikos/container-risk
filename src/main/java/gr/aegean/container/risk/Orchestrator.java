package gr.aegean.container.risk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.aegean.container.risk.analyzer.ClairAnalyzer;
import gr.aegean.container.risk.analyzer.GrypeAnalyzer;
import gr.aegean.container.risk.compute.CalcAssetRisk;
import gr.aegean.container.risk.compute.CalcImageRisk;
import gr.aegean.container.risk.compute.CalcOverallRisk;
import gr.aegean.container.risk.configuration.PropertyReader;
import gr.aegean.container.risk.data.RiskData;
import gr.aegean.container.risk.data.VulnerabilityRepository;
import gr.aegean.container.risk.scanner.ClairScannerRunner;
import gr.aegean.container.risk.scanner.GrypeRunner;
import gr.aegean.container.risk.utility.ReportGenerator;

public class Orchestrator {
	
	public static void run() {
		List<String> images = PropertyReader.getImages();
		
		//Running the image vulnerability scanners
		ClairScannerRunner clairRunner = new ClairScannerRunner();
		clairRunner.runTool(images);
		GrypeRunner grypeRunner = new GrypeRunner();
		grypeRunner.runTool(images);
		
		//Creating the RiskData
		RiskData globalRiskData = new RiskData(images);
		Map<String, RiskData> localRiskData = new HashMap<String, RiskData>();
		localRiskData.put("clair", new RiskData(images));
		localRiskData.put("grype", new RiskData(images));
		
		//Running the result analyzers
		for (int i = 0; i < images.size(); i++) {
			String image = images.get(i);
			ClairAnalyzer ca = new ClairAnalyzer(globalRiskData,localRiskData.get("clair"),image);
			ca.analyze(clairRunner.getResultFiles().get(i));
			
			GrypeAnalyzer ga = new GrypeAnalyzer(globalRiskData,localRiskData.get("grype"),image);
			ga.analyze(grypeRunner.getResultFiles().get(i));
		}
		
		VulnerabilityRepository repo = new VulnerabilityRepository();
		//Calculating the risks for tool agglomeration & each tool
		calculateRiskData(globalRiskData,repo,images);
		/*calculateRiskData(localRiskData.get("clair"),repo,images);*/
		calculateRiskData(localRiskData.get("grype"),repo,images);

		//Writing results to results directory
		ReportGenerator rg = new ReportGenerator(globalRiskData, localRiskData);
		rg.writeResults();
	}
	
	private static void calculateRiskData(RiskData data, VulnerabilityRepository repo, List<String> images) {
		for (String image: images) {
			CalcAssetRisk car = new CalcAssetRisk("M", "M", "M", data, repo, image);
			car.calculateRisk();
			
			CalcImageRisk cir = new CalcImageRisk(data, image, PropertyReader.getImageFunction());
			cir.calculateRisk();
		}
		CalcOverallRisk cor = new CalcOverallRisk(data,PropertyReader.getAppFunction());
		cor.calculateRisk();
	}
	
	public static void main(String args[]) {
		run();
	}
}
