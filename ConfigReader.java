package media;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/***
 * CSC 478
 * Team2
 * ConfigReader.java
 * Purpose: Provide API for accessing config file values
 * 
 * @author Karissa (Nash) Stisser, Jeremy Egner, Yuji Tsuzuki
 * @version 0.2.0 4/20/2015
 */
public class ConfigReader {
	private final String path = "config.cfg";
	private HashMap<String, String> configMap;
	
	/**
	 * Constructor
	 * 
	 * @throws IOException	if the configuration file is missing
	 */
	public ConfigReader() {
		try {
			makeConfigMap();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a HashMap to cache configuration values.
	 * 
	 * @throws IOException	if the file path is unable to be found
	 */
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

	/**
	 * 
	 * @param key	the configuration file key to look up
	 * @return		the configuration value associated with the key
	 */
	public String getValue(String key) {
		return configMap.get(key);
	}
}
