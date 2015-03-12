package popularioty.api.services.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.exception.PopulariotyException.Level;
import popularioty.commons.services.searchengine.factory.SearchProvider;
import popularioty.commons.services.storageengine.factory.StorageFactory;
import popularioty.commons.services.storageengine.factory.StorageProvider;

import com.couchbase.client.core.BackpressureException;
import com.couchbase.client.core.RequestCancelledException;
import com.couchbase.client.deps.io.netty.handler.timeout.TimeoutException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

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
            store = StorageFactory.getSearchProvider(properties.getProperty("storage.engine"));
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
	
	@Override
	public void destroy() throws Exception 
	{
		store.close(null);
		
	}
	
}
