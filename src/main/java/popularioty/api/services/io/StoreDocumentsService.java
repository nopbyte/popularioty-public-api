package popularioty.api.services.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.services.storageengine.factory.StorageFactory;
import popularioty.commons.services.storageengine.factory.StorageProvider;

@Service
public class StoreDocumentsService implements DisposableBean{

	private static Logger LOG = LoggerFactory.getLogger(StoreDocumentsService.class);	
	
	private StorageProvider store = null;
	
	public  StoreDocumentsService() {
		// load properties file from classpath
        Properties properties = new Properties();
        ClassPathResource resource = new ClassPathResource("storage.properties");
        try {
            properties.load(resource.getInputStream());
            Map<String, Object> map = new HashMap<String, Object>();
            for (String key : properties.stringPropertyNames()) {
                map.put(key, properties.getProperty(key));
            }
            store = StorageFactory.getStorageProvider(properties.getProperty("storage.engine"));
            store.init(map);
            
        }   catch (IOException e) {
        	
            LOG.error("Error loading properties file to create StorageEngine - IOException.. " + PopulariotyException.getStackTrace(e.getCause()));
        } catch (Exception e) {
        	
        	LOG.error("Error loading properties file to create Storagengine - Exception.. " +PopulariotyException.getStackTrace(e.getCause()));
		}
        
	}
	
	public Map<String, Object> storeData(String id, Map<String, Object> data, String set) throws PopulariotyException
	{
		return store.storeData(id, data, set);
	}
	/**
	 * 
	 * @param ids list of strings (ids)
	 * @param set set where the documents need to be looked for
	 * @param idLabel label used to add the id to the resulting map for each result. If null is provided no id is added
	 * @return List of HashMaps containing the documents in the database
	 * @throws PopulariotyException
	 */
	public List<Map<String, Object>> getData(List<String> ids, String set, String idLabel) throws PopulariotyException
	{
		List<Map<String, Object>>  ret = new LinkedList<>();
		Map<String, Object> curr = null;
		
		for(String id: ids)
		{
			try{
				curr = store.getData(id, set);
				if(idLabel !=null)
					curr.put(idLabel,id);
				ret.add(curr);
			}catch(PopulariotyException ex)
			{
				if(ex.getHTTPErrorCode()!=404)
					throw ex;
				//else ig means that no content was found... we can live with that..
			}
		}
		return ret;
		
	}
	
	@Override
	public void destroy() throws Exception 
	{
		store.close(null);
		
	}
	
}
