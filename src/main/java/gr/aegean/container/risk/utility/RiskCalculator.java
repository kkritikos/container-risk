package gr.aegean.container.risk.utility;

import java.util.Map;

public class RiskCalculator {
	public static double calculateAverage(Map<String, Double> risks) {
		double risk = 0.0;
		int size = risks.size();
		for (Map.Entry<String, Double> entry : risks.entrySet()) {
			risk = risk + entry.getValue();
		}
		risk = risk / size;
		risk = Math.round(risk * 100.0) / 100.0;
		
		return risk;
	}
	
	public static double calculateMax(Map<String, Double> risks) { // function tp calculate the maximum value from Map
		double risk = 0.0;
		int counter = 0;

		for (Map.Entry<String, Double> entry : risks.entrySet()) {
			if (counter == 0) {// θέτω ως max την π�?�?τη τιμή που β�?ίσκω
				risk = entry.getValue();
				counter = 1;
			}
			if (risk < entry.getValue()) {
				risk = entry.getValue();
			}
		}
		
		return risk;
	}
	
	public static double calculatePWeightedAverage(Map<String,Double> risks, Map<String,Double> probs) {
		double risk = 0.0, pSum = 0.0, multiSum = 0.0;
		for (Map.Entry<String, Double> entry : risks.entrySet()) {
			double Pk = probs.get(entry.getKey()); 
			pSum += Pk;
			double Rk = entry.getValue();
			multiSum = multiSum + (Pk * Rk);
		}
		
		if (pSum == 0) risk = 0.0;
		else risk = multiSum / pSum;
		
		risk = Math.round(risk * 100.0) / 100.0;
		
		return risk;
	}
	
	public static double calculateSophisticated(Map<String, Double> risks, Map<String,Double> probs){
		double risk = 0.0;
		double prob = 1.0;
		double pSum = 0.0, rSum = 0.0;
		for (Map.Entry<String, Double> entry : probs.entrySet()) {
			prob = prob * (1 - entry.getValue());
			pSum += entry.getValue();
			rSum += risks.get(entry.getKey());
		}
		prob = 1 - prob;
		
		if (pSum == 0) risk = 0;
		else risk = prob * rSum / pSum;
		
		risk = Math.round(risk * 100.0) / 100.0;
		
		return risk;
	}
}
