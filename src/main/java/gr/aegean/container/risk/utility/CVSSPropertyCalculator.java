package gr.aegean.container.risk.utility;

public class CVSSPropertyCalculator {
	public static double calcRL(String prop) {
		if (prop.equals("")) return 0.0;
		else return 0.15;
	}
	
	public static double calcAV(String av) {
		double avValue = 0.0;
		
		if (av.equals("LOCAL")) {
			avValue = 0.4;
		} 
		else if (av.equals("ADJACENT")) {
			avValue = 0.6;
		} 
		else if (av.equals("NETWORK")) {
			avValue = 1.0;
		} 
		else if (av.equals("PHYSICAL")) {
			avValue = 0.3;
		} 
		else if (av.equals("ADJACENT_NETWORK")) {
			avValue = 1.0;
		} 
		else {
			avValue = 1.0;
			System.out.println("error with accessVector");
		}
		
		return avValue;
	}
	
	public static double calcAC(String ac) {
		double acValue = 0.0;
		
		if (ac.equals("HIGH")) {
			acValue = 0.5;
		} 
		else if (ac.equals("MEDIUM")) {
			acValue = 0.75;
		} 
		else if (ac.equals("LOW")) {
			acValue = 1.0;
		} 
		else {
			acValue = 1.0;
			System.out.println("error with accessComplexity");
		}
		
		return acValue;
	}
	
	public static double calcAU(String au) {
		double auValue = 0.0;
		
		if (au.equals("MULTIPLE") || au.equals("HIGH")) {
			auValue = 0.5;
		} 
		else if (au.equals("SINGLE") || au.equals("LOW")) {
			auValue = 0.55;
		} 
		else if (au.equals("NONE")) {
			auValue = 1.0;
		} 
		else {
			auValue = 1.0;
			System.out.println("error with authentication");
		}
		
		return auValue;
	}
	
	public static double calcCR(String cr) {
		double crValue = 0.0;
		
		if (cr.equals("L")) {
			crValue = 30.0;
		} 
		else if (cr.equals("M")) {
			crValue = 60.0;
		} 
		else if (cr.equals("H")) {
			crValue = 100.0;
		} 
		else if (cr.equals("N")) {
			crValue = 100.0;
		} 
		else {
			crValue = 100.0;
			System.out.println("error with confidentiality requirement");
		}
		
		return crValue;
	}
	
	public static double calcIR(String ir) {
		double irValue = 0.0;
		
		if (ir.equals("L")) {
			irValue = 30.0;
		} 
		else if (ir.equals("M")) {
			irValue = 60.0;
		} 
		else if (ir.equals("H")) {
			irValue = 100.0;
		} 
		else if (ir.equals("N")) {
			irValue = 100.0;
		} 
		else {
			irValue = 100.0;
			System.out.println("error with integrity requirement");
		}
		
		return irValue;
	}
	
	public static double calcAR(String ar) {
		double arValue = 0.0;
		
		if (ar.equals("L")) {
			arValue = 30.0;
		} 
		else if (ar.equals("M")) {
			arValue = 60.0;
		} 
		else if (ar.equals("H")) {
			arValue = 100.0;
		} 
		else if (ar.equals("N")) {
			arValue = 100.0;
		} 
		else {
			arValue = 100.0;
			System.out.println("error with Availability requirement");
		}
		
		return arValue;
	}
	
	public static double calcConfImpact(String confidentiality) {
		double confidentialityValue = 0.0;
		
		if (confidentiality.equals("HIGH") || confidentiality.equals("COMPLETE")) {
			confidentialityValue = 1.0;
		} 
		else if (confidentiality.equals("LOW") || confidentiality.equals("PARTIAL")) {
			confidentialityValue = 0.5;
		} 
		else if (confidentiality.equals("NONE")) {
			confidentialityValue = 0.0;
		} 
		else {
			confidentialityValue = 1.0;
			System.out.println("error with confidentiality");
		}
		
		return confidentialityValue;
	}
	
	public static double calcIntImpact(String integrity) {
		double integrityValue = 0.0;
		
		if (integrity.equals("HIGH") || integrity.equals("COMPLETE")) {
			integrityValue = 1.0;
		} else if (integrity.equals("LOW") || integrity.equals("PARTIAL")) {
			integrityValue = 0.5;
		} else if (integrity.equals("NONE")) {
			integrityValue = 0.0;
		} else {
			integrityValue = 1.0;
			System.out.println("error with integrity");
		}
		
		return integrityValue;
	}
	
	public static double calcAvImpact(String availability) {
		double availabilityValue = 0.0;
		
		if (availability.equals("HIGH") || availability.equals("COMPLETE")) {
			availabilityValue = 1.0;
		} else if (availability.equals("LOW") || availability.equals("PARTIAL")) {
			availabilityValue = 0.5;
		} else if (availability.equals("NONE")) {
			availabilityValue = 0.0;
		} else {
			availabilityValue = 1.0;
			System.out.println("error with availability");
		}
		
		return availabilityValue;
	}
}
