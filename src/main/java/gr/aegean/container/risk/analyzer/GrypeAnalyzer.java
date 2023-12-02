package gr.aegean.container.risk.analyzer;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import gr.aegean.container.risk.data.RiskData;
import gr.aegean.container.risk.utility.CVSSPropertyCalculator;
import gr.aegean.container.risk.utility.FinalAssetsCalculator;

public class GrypeAnalyzer implements ToolAnalyzer {
	private Map<String, String> assets = new HashMap<String, String>();
    
    private RiskData globalRiskData, localRiskData;
    private String imageName;
    
    private Map<String, Set<String>> finalAssets;
    private Map<String, Double> isFixed;
    private Map<String, Set<String>> localFinalAssets;
    private Map<String, Double> localIsFixed;
    
	public GrypeAnalyzer(RiskData globalRiskData, RiskData localRiskData, String imageName) {
		this.imageName = imageName;
		this.globalRiskData = globalRiskData;
		this.localRiskData = localRiskData;
		
		finalAssets = globalRiskData.getFinalAssets(imageName);
		isFixed = globalRiskData.getIsFixed(imageName);
		localFinalAssets = localRiskData.getFinalAssets(imageName);
		localIsFixed = localRiskData.getIsFixed(imageName);
	}

	@Override
	public void analyze(String filename) {
		JSONParser parser = new JSONParser();
		try {
			System.out.println("---------------- START THE PROCCESS ON GRYPE ----------------------");
			Object obj = parser.parse(new FileReader(filename));
			JSONObject grypeFileData = (JSONObject) obj;
			JSONArray j1 = (JSONArray) grypeFileData.get("matches");// Παί�?νω �?λα τα αποτελέσματα
			for (int i = 0; i < j1.size(); i++) {
				JSONObject j2 = (JSONObject) j1.get(i);
				JSONObject j3 = (JSONObject) j2.get("vulnerability");
				String j4 = (String) j3.get("id"); // get cve-id
				JSONArray j6 = (JSONArray) j2.get("matchDetails");
				JSONObject j7 = (JSONObject) j6.get(0);
				JSONObject j8 = (JSONObject) j7.get("searchedBy");
				
				String asset;
				if (!j8.containsKey("package")){
					JSONObject j10 = (JSONObject) j2.get("artifact");
					 asset = (String) j10.get("name");
				}
				else{
					JSONObject j9 = (JSONObject) j8.get("package");
					 asset = (String) j9.get("name");
				}

				assets.put(j4, asset);

				// Add fix state and cve-id in a hashMap (cve-id is the key)
				JSONObject jFix = (JSONObject) j3.get("fix");
				double rl = 0.0;
				if (!jFix.toString().contains("not-fixed")) {
					rl = 0.15;
				} 
				Double existingRl = isFixed.get(j4);
				//Trying to increase RL if it exists in global isFixed
				if (existingRl != null) {
					if (existingRl < rl) isFixed.put(j4, rl);
				}
				else isFixed.put(j4, rl);
				localIsFixed.put(j4, rl);
			}
			
			FinalAssetsCalculator.calculate(imageName, assets, localFinalAssets, finalAssets);
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("error: "+e);
		}
		
		System.out.println("---------------- END THE PROCCESS ON GRYPE ----------------------");
	}
}
