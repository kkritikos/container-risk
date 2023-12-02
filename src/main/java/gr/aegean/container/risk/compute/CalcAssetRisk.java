package gr.aegean.container.risk.compute;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gr.aegean.container.risk.data.RiskData;
import gr.aegean.container.risk.data.Vulnerability;
import gr.aegean.container.risk.data.VulnerabilityRepository;
import gr.aegean.container.risk.utility.CVSSPropertyCalculator;

public class CalcAssetRisk {
	private String cr, ir, ar;
	private RiskData riskData;
	private VulnerabilityRepository repository;
	private String imageName;

	public CalcAssetRisk(String cr, String ir, String ar, RiskData riskData, VulnerabilityRepository repository, String imageName) {
		this.cr = cr;
		this.ir = ir;
		this.ar = ar;
		this.riskData = riskData;
		this.repository = repository;
		this.imageName = imageName;
	}
	
	public void calculateRisk() {
		for (Map.Entry<String, Set<String>> entry : riskData.getFinalAssets(imageName).entrySet()) {
			String asset = entry.getKey();
			List<Double> probabilities = new ArrayList<Double>();
			List<Double> impacts = new ArrayList<Double>();
			List<Double> risks = new ArrayList<Double>();
			for (String cveId: entry.getValue()) {
				//if (cveId.contains("2023")) continue;
				Vulnerability v = repository.getVulnerability(cveId);
				if (v == null) continue;
				
				double avValue, acValue, auValue, probability, ic, ii, ia, crValue, irValue, arValue;
				double availabilityValue, integrityValue, confidentialityValue, impact, risk, rl = 0.0;
				
				avValue = CVSSPropertyCalculator.calcAV(v.getAccessVector());
				acValue = CVSSPropertyCalculator.calcAC(v.getAccessComplexity());	
				auValue = CVSSPropertyCalculator.calcAU(v.getAuthentication());
				
				probability = avValue * acValue * auValue;
				//probability = Math.round(probability * 100.0) / 100.0;
				probabilities.add(probability);
				
				crValue = CVSSPropertyCalculator.calcCR(cr);
				irValue = CVSSPropertyCalculator.calcIR(ir);
				arValue = CVSSPropertyCalculator.calcIR(ar);
								
				confidentialityValue = CVSSPropertyCalculator.calcConfImpact(v.getConfidentialityImpact());
				integrityValue = CVSSPropertyCalculator.calcConfImpact(v.getIntegrityImpact());
				availabilityValue = CVSSPropertyCalculator.calcAvImpact(v.getAvailabilityImpact());
										
				ic = confidentialityValue * crValue;
				ii = integrityValue * irValue;
				ia = availabilityValue * arValue;
					
				boolean flag = false;
				for (String fixed: riskData.getIsFixed(imageName).keySet()) {
					if (fixed.equals(cveId)) {
						rl = riskData.getIsFixed(imageName).get(cveId);
						flag = true;
						break;
					}
				}
				if (flag == false) {
					rl = 0.0;			
				}

				Double impactPar = (ic + ii + ia) / 3;
				impact = impactPar * (1 - rl);
				//impact = Math.round(impact * 100.0) / 100.0;
				impacts.add(impact);
				
				risk = probability * impact;
				//risk = Math.round(risk * 100.0) / 100.0;
				risks.add(risk);
			} // end of for with cve-id's list

			if (probabilities.size() != 0) {
				Double finalRisk = 0.0, propabilitySum = 0.0, riskSum = 0.0;
				for (int x = 0; x < probabilities.size(); x++) {
					propabilitySum = propabilitySum + probabilities.get(x);
				}
				// calculate P_aone
				Double probForP_aone = 1.0;
				BigDecimal bd = new BigDecimal("1.0");
				for (int x = 0; x < probabilities.size(); x++) {
					// System.out.println("probabilties.get(x): "+probabilities.get(x));
					probForP_aone = probForP_aone * (1 - probabilities.get(x));
					bd = bd.multiply(new BigDecimal(1-probabilities.get(x)));
					// System.out.println("probForP_aone inside for loop: " + probForP_aone);
				} //for
				probForP_aone = 1 - probForP_aone;
				BigDecimal bd2 = new BigDecimal(1.0);
				bd2 = bd2.subtract(bd);
				System.out.println("BD is: " + bd.toPlainString());
				System.out.println("probForP_aone: " + probForP_aone);
				for (int x = 0; x < risks.size(); x++) {
					riskSum = riskSum + risks.get(x);
				}//for
				
				
				finalRisk = probForP_aone * (1 / propabilitySum) * riskSum;
				//finalRisk = Math.round(finalRisk * 100.0) / 100.0;
				System.out.println("risk for asset: " + asset + " is: " + finalRisk);
				riskData.getAssetWithRisk(imageName).put(asset, finalRisk);
				riskData.getAssetWithProb(imageName).put(asset, probForP_aone);
			}
		} // end of for with finalAssets hashMap

	} //end constructor 
} // end class
