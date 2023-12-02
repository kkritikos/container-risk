package gr.aegean.container.risk.analyzer;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import gr.aegean.container.risk.data.RiskData;
import gr.aegean.container.risk.utility.CVSSPropertyCalculator;
import gr.aegean.container.risk.utility.FinalAssetsCalculator;

public class ClairAnalyzer implements ToolAnalyzer {
	private Map<String, String> assets = new HashMap<String, String>();
	
	private Map<String, Set<String>> finalAssets;
	private Map<String, Double> isFixed;
	private Map<String, Set<String>> localFinalAssets;
	private Map<String, Double> localIsFixed;
    
    private RiskData globalRiskData, localRiskData;
    private String imageName;
	
    public ClairAnalyzer(RiskData globalRiskData, RiskData localRiskData, String imageName) {
    	this.globalRiskData = globalRiskData;
    	this.localRiskData = localRiskData;
    	this.imageName = imageName;
    	
    	finalAssets = globalRiskData.getFinalAssets(imageName);
    	isFixed = globalRiskData.getIsFixed(imageName);
    	
    	localFinalAssets = localRiskData.getFinalAssets(imageName);
    	localIsFixed = localRiskData.getIsFixed(imageName);
	}

	@Override
	public void analyze(String filename) {
		JSONParser parser = new JSONParser();
		try {
			System.out.println("---------------- START THE PROCCESS ON CLAIR ----------------------");
			Object obj = parser.parse(new FileReader(filename));
			JSONObject clairFileData = (JSONObject) obj;
			JSONArray j1 = (JSONArray) clairFileData.get("vulnerabilities");
			
			/* Populating assets and isFixed */
			for (int i = 0; i < j1.size(); i++) {
				JSONObject j2 = (JSONObject) j1.get(i);
				
				String asset = (String) j2.get("featurename");
				assets.put(j2.get("vulnerability").toString(), asset);
				
				String j3 = (String) j2.get("fixedby");
				double rl = CVSSPropertyCalculator.calcRL(j3);
				String vulnId = j2.get("vulnerability").toString();
				
				Double existingRl = isFixed.get(vulnId);
				//Trying to increase RL if it exists in global isFixed
				if (existingRl != null) {
					if (existingRl < rl) isFixed.put(vulnId, rl);
				}
				else isFixed.put(vulnId, rl);
				localIsFixed.put(vulnId, rl);
			}
			
			FinalAssetsCalculator.calculate(imageName, assets, localFinalAssets, finalAssets);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("---------------- END THE PROCCESS ON CLAIR ----------------");
	}
}
