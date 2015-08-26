package popularioty.api.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import popularioty.commons.exception.PopulariotyException;

public class PropertiesLoader {


	private static Logger LOG = LoggerFactory.getLogger(PropertiesLoader.class);	
	private static Map<String,Map<String,Object>> properties;
	

	public static Map<String,Object> loadSearchConfiuration(String propfile)
	{
		if (properties ==null)
		{	
			properties = new HashMap<String,Map<String,Object>>();
		}
		if(properties.containsKey(propfile))
			return properties.get(propfile);
		Map<String,Object> curr_props = new HashMap<String, Object>();
		Properties props = new Properties();
		ClassPathResource resource = new ClassPathResource(propfile);
		 try {
	            props.load(resource.getInputStream());
	            for (String key : props.stringPropertyNames()) {
	            	curr_props.put(key, props.getProperty(key).trim());
	            }
		 } catch (IOException e) {
	        	
	            LOG.error("Error loading properties file to create SearchEngine - IOException.. " + PopulariotyException.getStackTrace(e.getCause()));
	     } catch (Exception e) {
	        	
	        	LOG.error("Error loading properties file to create SearchEngine - Exception.. " +PopulariotyException.getStackTrace(e.getCause()));
	     }
		 properties.put(propfile,curr_props);
		 return curr_props;
			
	}
}
