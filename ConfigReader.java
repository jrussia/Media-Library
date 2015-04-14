package media;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ConfigReader {
	private final String path = "config.cfg";
	private HashMap<String, String> configMap;
	
	public ConfigReader() throws IOException {
		makeConfigMap();
	}
	
	// TODO: is this the right way to handle files?
	private void makeConfigMap() throws IOException {
		configMap = new HashMap<String, String>();
		FileReader reader = new FileReader(path);
		BufferedReader buffRead = new BufferedReader(reader);
		String line;
		String[] lineAry = new String[2];
		while ((line = buffRead.readLine()) != null) {
			lineAry = line.split(":");
			configMap.put(lineAry[0], lineAry[1]);
		}
		buffRead.close();
	}

	public String getValue(String key) {
		return configMap.get(key);
	}
}
