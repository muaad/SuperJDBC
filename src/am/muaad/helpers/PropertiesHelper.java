package am.muaad.helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
 
public class PropertiesHelper {
	public static void set(String url, String database, String user, String password) {
		Properties prop = new Properties();
		OutputStream output = null;
	 
		try {
	 
			output = new FileOutputStream("db.properties");
	 
			// set the properties value
			prop.setProperty("url", url);
			prop.setProperty("database", database);
			prop.setProperty("user", user);
			prop.setProperty("password", password);
	 
			// save properties to project root folder
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
	}
	
	public static Map<String, String> get(String fileName) {
		Properties prop = new Properties();
		InputStream input = null;
		Map<String, String> properties = null;
		try {
			 
			input = new FileInputStream(fileName);
	 
			prop.load(input);
			
			properties = WordsHelper.constructParamsMap("url", prop.getProperty("url"), 
					"database", prop.getProperty("database"),"user", prop.getProperty("user"), 
					"password", prop.getProperty("password"));
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
	
	public static void main(String[] args) {
		set("localhost:3306", "KPos", "muaad", "muaad");
		System.out.println(get("db.properties"));
	}
}
