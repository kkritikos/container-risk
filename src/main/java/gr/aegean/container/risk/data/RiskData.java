package gr.aegean.container.risk.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RiskData {
	private Map<String,Map<String, Set<String>>> finalAssets = new HashMap<String,Map<String, Set<String>>>(); // new add
	private Map<String, Map<String, Double>> isFixed = new HashMap<String, Map<String, Double>>();
	
	private Map<String, Map<String, Double>> assetWithRisk = new HashMap<String, Map<String, Double>>();
	private Map<String, Map<String, Double>> assetWithProb = new HashMap<String, Map<String, Double>>();
	private Map<String, Double> imageWithRisk = new HashMap<String, Double>();
	private Map<String, Double> imageWithProb = new HashMap<String, Double>();
	
	private double overallRisk = 0.0;
	
	public RiskData(List<String> images) {
		for (String image: images) {
			Map<String, Set<String>> fAssets = new HashMap<String, Set<String>>();
			setFinalAssets(image, fAssets);
			
			Map<String, Double> isF = new HashMap<String, Double>();
			setIsFixed(image, isF);
			
			Map<String, Double> awr = new HashMap<String, Double>();
			setAssetWithRisk(image, awr);
			
			Map<String, Double> awp = new HashMap<String, Double>();
			setAssetWithProb(image, awp);
		}
	}
	
	public Map<String, Set<String>> getFinalAssets(String imageName) {
		return finalAssets.get(imageName);
	}
	
	public void setFinalAssets(String imageName, Map<String, Set<String>> finalAssets) {
		this.finalAssets.put(imageName,finalAssets);
	}
	
	public Map<String, Double> getIsFixed(String imageName) {
		return isFixed.get(imageName);
	}
	
	public void setIsFixed(String imageName, Map<String, Double> isFixed) {
		this.isFixed.put(imageName, isFixed);
	}
	
	public Map<String, Double> getAssetWithRisk(String imageName) {
		return assetWithRisk.get(imageName);
	}
	
	public void setAssetWithRisk(String imageName, Map<String, Double> assetWithRisk) {
		this.assetWithRisk.put(imageName, assetWithRisk);
	}
	
	public Map<String, Double> getAssetWithProb(String imageName) {
		return assetWithProb.get(imageName);
	}
	
	public void setAssetWithProb(String imageName, Map<String, Double> assetWithProb) {
		this.assetWithProb.put(imageName, assetWithProb);
	}

	public Map<String, Double> getImageWithRisk() {
		return imageWithRisk;
	}

	public void setImageWithRisk(Map<String, Double> imageWithRisk) {
		this.imageWithRisk = imageWithRisk;
	}

	public double getOverallRisk() {
		return overallRisk;
	}

	public void setOverallRisk(double overallRisk) {
		this.overallRisk = overallRisk;
	}

	public Map<String, Double> getImageWithProb() {
		return imageWithProb;
	}

	public void setImageWithProb(Map<String, Double> imageWithProb) {
		this.imageWithProb = imageWithProb;
	}
}
