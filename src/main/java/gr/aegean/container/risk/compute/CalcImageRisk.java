package gr.aegean.container.risk.compute;

import java.util.Map;

import gr.aegean.container.risk.data.RiskData;
import gr.aegean.container.risk.utility.RiskCalculator;

public class CalcImageRisk {
	private RiskData riskData;
	private Function function;
	private String imageName;
	
	public CalcImageRisk(RiskData riskData, String imageName, Function function) {
		this.riskData = riskData;
		this.function = function;
		this.imageName = imageName;
	}
	
	private void printMap(Map<String, Double> map, String msg) {
		System.out.println(msg);
		for (String asset: map.keySet()) {
			System.out.println("Asset: " + asset + " with value: " + map.get(asset));
		}
	}
	
	public void calculateRisk() {
		Map<String,Double> assetWithRisk = riskData.getAssetWithRisk(imageName);
		printMap(assetWithRisk, "Printing image's assets risks");
		Map<String,Double> assetWithProb = riskData.getAssetWithProb(imageName);
		printMap(assetWithProb, "Printing image's assets probs");
		if (!assetWithRisk.isEmpty()) {
			double imageRisk = 0.0;
			calculateProb();
			switch(function) {
				case AVERAGE: imageRisk = RiskCalculator.calculateAverage(assetWithRisk); break;
				case MAX: imageRisk = RiskCalculator.calculateMax(assetWithRisk); break;
				case PWEIGHTED_AVG: imageRisk = RiskCalculator.calculatePWeightedAverage(assetWithRisk, assetWithProb); break;
				default: imageRisk = RiskCalculator.calculateSophisticated(assetWithRisk, assetWithProb); break; 
			}
			riskData.getImageWithRisk().put(imageName, imageRisk);
		}
		else {
			riskData.getImageWithRisk().put(imageName, 0.0);
			riskData.getImageWithProb().put(imageName, 0.0);
		}
	}
	
	private void calculateProb() {
		double imageProb = 1.0;
		for (Map.Entry<String, Double> entry : riskData.getAssetWithProb(imageName).entrySet()) {
			imageProb = imageProb * (1 - entry.getValue());
		}
		imageProb = 1 - imageProb;
		
		riskData.getImageWithProb().put(imageName, imageProb);
	}
}
