package gr.aegean.container.risk.data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RetrieveDataFromOnlineDB {
	public String Read(Reader re) throws IOException {
		StringBuilder str = new StringBuilder(); // To Store Url Data In String.
		int temp;
		do {
			temp = re.read(); // reading Charcter By Chracter.
			str.append((char) temp);
		} while (temp != -1);
		// re.read() return -1 when there is end of buffer , data or end of file.
		return str.toString();
	}

	public JSONObject readFromNistDB(String url) throws IOException, JSONException {

		URLConnection input = new URL(url).openConnection();
		input.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		input.connect();
		BufferedReader re = new BufferedReader(new InputStreamReader(input.getInputStream(), Charset.forName("UTF-8")));
		// Buffer Reading In UTF-8
		String text = Read(re); // Handy Method To Read Data From BufferReader
		//System.out.println("Vulnerability text for url: " + url + " " + text);
		JSONObject json = new JSONObject(text); // Creating A JSON
		JSONObject j1 = (JSONObject) json.get("result");
		JSONArray j2 = (JSONArray) j1.get("CVE_Items");
		JSONObject j3 = (JSONObject) j2.get(0);
		JSONObject j4 = (JSONObject) j3.get("impact");
		JSONObject j5 = (JSONObject) j4.get("baseMetricV2");
		JSONObject j6 = null;
		if (j5 == null) {
			j5 = (JSONObject) j4.get("baseMetricV3");
			j6 = (JSONObject) j5.get("cvssV3");
		}
		else j6 = (JSONObject) j5.get("cvssV2");
		
		/*
		 * Testing System.out.println("j1 (result): "+j1);
		 * System.out.println("j2 (CVE_Items): "+j2); System.out.println("j3 (0): "+j3);
		 * System.out.println("j6 (cvssV2): " + j6);
		 */
		return j6; // Returning JSON
	}

	public JSONObject readFromCveApi(String url) throws IOException, JSONException {

		URLConnection input = new URL(url).openConnection();
		input.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		input.connect();

		BufferedReader re = new BufferedReader(new InputStreamReader(input.getInputStream(), Charset.forName("UTF-8")));
		// Buffer Reading In UTF-8
		String Text = Read(re); // Handy Method To Read Data From BufferReader
		JSONObject json = new JSONObject(Text); // Creating A JSON
		JSONObject j4 = (JSONObject) json.get("impact");
		JSONObject j5 = (JSONObject) j4.get("baseMetricV2");
		JSONObject j6 = null;
		if (j5 == null) {
			j5 = (JSONObject) j4.get("baseMetricV3");
			j6 = (JSONObject) j5.get("cvssV3");
		}
		else j6 = (JSONObject) j5.get("cvssV2");
		// System.out.println("j6 (cvssV2): " + j6);
		return j6; // Returning JSON
	}

}
