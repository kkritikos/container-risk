package gr.aegean.container.risk.utility;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gr.aegean.container.risk.data.RiskData;

public class ReportGenerator {
	private static final String resultsDirSuffix = "results";
	
	private String resultsDirName;
	
	private RiskData data;
	private Map<String,RiskData> toolData;

	public ReportGenerator(RiskData data, Map<String,RiskData> toolData) {
		this.data = data;
		this.toolData = toolData;
	}
	
	public void writeResults() {
		createDir();
		writeResult("global",data);
		for (String key: toolData.keySet()) {
			writeResult(key,toolData.get(key));
		}
	}
	
	private Set<String> getCVEs(String imageName, RiskData riskData){
		Set<String> cves = new HashSet<String>();
		Map<String,Set<String>> assets = riskData.getFinalAssets(imageName);
		for (String asset: assets.keySet()) {
			cves.addAll(assets.get(asset));
		}
		
		return cves;
	}
	
	private String getCVEString(Set<String> cves) {
		String result = "";
		Iterator<String> it = cves.iterator();
		if (it.hasNext()) {
			result += it.next();
		}
		while (it.hasNext()){
			result += ", " + it.next();
		}
		
		return result;
	}
	
	private void writeResult(String name, RiskData riskData) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("overallRisk", riskData.getOverallRisk());
		JSONArray imageArray = new JSONArray();
		
		for (String image: riskData.getImageWithRisk().keySet()) {
			JSONObject imgObj = new JSONObject();
			imgObj.put("imageName",image);
			imgObj.put("imageRisk", riskData.getImageWithRisk().get(image));
			imgObj.put("imageProb", riskData.getImageWithProb().get(image));
			
			Set<String> cves = getCVEs(image, riskData);
			String cveStr = getCVEString(cves);
			imgObj.put("imageCVENum", cves.size());
			imgObj.put("imageCVEs", cves);
			
			JSONArray assetArray = new JSONArray();
			for (String asset: riskData.getAssetWithRisk(image).keySet()) {
				JSONObject assetObj = new JSONObject();
				assetObj.put("assetName", asset);
				assetObj.put("assetRisk", riskData.getAssetWithRisk(image).get(asset));
				assetObj.put("assetProb", riskData.getAssetWithProb(image).get(asset));
				
				cves = riskData.getFinalAssets(image).get(asset);
				cveStr = getCVEString(cves);
				imgObj.put("assetCVENum", cves.size());
				assetObj.put("assetCVEs", cves);
				
				assetArray.add(assetObj);
			}
			
			imgObj.put("imageAssets", assetArray);
			
			imageArray.add(imgObj);
		}
		
		jsonObject.put("images", imageArray);
		try {
			FileWriter file = new FileWriter(resultsDirName + File.separator + name + ".json");
			file.write(jsonObject.toJSONString());
	        file.flush();
			file.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createDir(){
		LocalDateTime myDateObj = LocalDateTime.now();
	    System.out.println("Before formatting: " + myDateObj);
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");

	    String formattedDate = myDateObj.format(myFormatObj);
		
		resultsDirName = resultsDirSuffix + File.separator + formattedDate;
		File resultsDir = new File(resultsDirName); //create new folder under results directory
		if (!resultsDir.exists()) {
			resultsDir.mkdirs();
		}
	}
}
