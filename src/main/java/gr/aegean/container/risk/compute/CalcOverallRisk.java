package gr.aegean.container.risk.compute;

import gr.aegean.container.risk.data.RiskData;
import gr.aegean.container.risk.utility.RiskCalculator;

public class CalcOverallRisk {
	RiskData riskData;
	Function function;
	
	public CalcOverallRisk(RiskData riskData, Function function) {
		this.riskData = riskData;
		this.function = function;
	}
	
	public void calculateRisk() {
		double risk = 0;
		switch(function) {
			case AVERAGE: risk = RiskCalculator.calculateAverage(riskData.getImageWithRisk()); break;
			case MAX: risk = RiskCalculator.calculateMax(riskData.getImageWithRisk()); break;
			case PWEIGHTED_AVG: risk = RiskCalculator.calculatePWeightedAverage(riskData.getImageWithRisk(), riskData.getImageWithProb()); break;
			default: risk = RiskCalculator.calculateSophisticated(riskData.getImageWithRisk(), riskData.getImageWithProb()); break;
		}
		
		riskData.setOverallRisk(risk);
	}

}
