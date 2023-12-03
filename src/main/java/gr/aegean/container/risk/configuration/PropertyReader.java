package gr.aegean.container.risk.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import gr.aegean.container.risk.compute.Function;

public final class PropertyReader {
	private static final String propertyFileName = "config.properties";
	
	public enum Mode{
		ALL,
		SCAN,
		ASSESS
	}
	
    private static String clairServerIp;
    private static String clairPath;
    private static String nvdUrl;
    private static String cveApiUrl;
    private static Function imageFunction;
    private static Function appFunction;
    private static List<String> images;
    private static Mode mode; 
    
    private PropertyReader() {}
	
    static {
    	getProperties();
    }
	 
    private static Properties loadPropertyFile()
    {
    	try{
    			InputStream input = null;
    			String path = System.getProperty("risk.config.dir");
    			if (path != null) input = new FileInputStream(path + File.separator + propertyFileName);
    			else input = PropertyReader.class.getClassLoader().getResourceAsStream(propertyFileName);
    			Properties prop = new Properties(); 
    			if (input == null) { 
    				System.out.println("Sorry, unable to find config.properties"); 	
    				return null; 
    			}
    			prop.load(input); 
    			return prop;
    	} 
    	catch (Exception ex) { 
    		ex.printStackTrace();
    	}
    	return null;
    }
    
    private static String getDefaultValueIfNull(final String s, final String defaultVal) {
    	if (s == null || s.trim().equals("")) return defaultVal;
    	else return s;
    }

	private static void getProperties(){
		Properties props = loadPropertyFile();
		if (props != null){
			clairServerIp = getDefaultValueIfNull(props.getProperty("clair.server.ip"),"127.0.0.1");
			clairPath = getDefaultValueIfNull(props.getProperty("clair.path"),"/home/ubuntu/clair-scanner");
			nvdUrl = getDefaultValueIfNull(props.getProperty("nvd.url"),"https://services.nvd.nist.gov/rest/json/cve/1.0/");
			cveApiUrl = getDefaultValueIfNull(props.getProperty("cveapi.url"),"https://v1.cveapi.com/");
			String iFunc = getDefaultValueIfNull(props.getProperty("image.function"),"sophisticated").toLowerCase();
			imageFunction = getFunction(iFunc);
			String aFunc = getDefaultValueIfNull(props.getProperty("app.function"),"sophisticated").toLowerCase();
			appFunction = getFunction(aFunc);
			String imgs = getDefaultValueIfNull(props.getProperty("images"),"ruby:latest,node:latest").toLowerCase();
			images = getImages(imgs);
			String m = getDefaultValueIfNull(props.getProperty("mode"),"all").toLowerCase();
			mode = getMode(m);
		}
	}
	
	private static Mode getMode(String m) {
		if (m.toLowerCase().equals("scan")) return Mode.SCAN;
		else if (m.toLowerCase().equals("assess")) return Mode.ASSESS;
		else return Mode.ALL;
	}
	
	private static Function getFunction(String func) {
		Function f = Function.SOPHISTICATED;
		if (func.equals("average")) f = Function.AVERAGE;
		else if (func.equals("max")) f = Function.MAX;
		else if (func.equals("pweighted_avg")) f = Function.PWEIGHTED_AVG;
		
		return f;
	}
	
	private static List<String> getImages(String imgs){
		String[] a = imgs.split(",");
		List<String> images = Arrays.asList(a);
		
		return images;
	}

	public static String getClairServerIp() {
		return clairServerIp;
	}

	public static String getClairPath() {
		return clairPath;
	}

	public static String getNvdUrl() {
		return nvdUrl;
	}

	public static String getCveApiUrl() {
		return cveApiUrl;
	}

	public static Function getImageFunction() {
		return imageFunction;
	}

	public static void setImageFunction(Function imageFunction) {
		PropertyReader.imageFunction = imageFunction;
	}

	public static Function getAppFunction() {
		return appFunction;
	}

	public static void setAppFunction(Function appFunction) {
		PropertyReader.appFunction = appFunction;
	}

	public static List<String> getImages() {
		return images;
	}

	public static Mode getMode() {
		return mode;
	}
}
