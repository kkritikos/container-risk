package gr.aegean.container.risk.utility;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FinalAssetsCalculator {
	public static void calculate(String imageName, Map<String, String> assets, Map<String, Set<String>> localFinalAssets, Map<String, Set<String>> finalAssets) {
		for (Map.Entry<String, String> entry : assets.entrySet()) {
			String cveId = entry.getKey();
			// System.out.println("cve-id inside assets Map for: "+cveId);
			String assetName = entry.getValue();
			// System.out.println("assetName inside assets Map for: "+assetName);
			
			boolean found = false;
			for (Map.Entry<String, Set<String>> entry2 : finalAssets.entrySet()) { // new add
				String key = entry2.getKey();
				if (key.equals(assetName)) {
					entry2.getValue().add(cveId);
					found = true;
					break;
				}
			}
			if (!found) {
				Set<String> set = new HashSet<String>();
				set.add(cveId);
				finalAssets.put(assetName, set);
			}
			
			found = false;
			for (Map.Entry<String, Set<String>> entry2 : localFinalAssets.entrySet()) { // new add
				String key = entry2.getKey();
				if (key.equals(assetName)) {
					entry2.getValue().add(cveId);
					found = true;
					break;
				}
			}
			if (!found) {
				Set<String> set = new HashSet<String>();
				set.add(cveId);
				localFinalAssets.put(assetName, set);
			}	
		}	
	}
}
