package popularioty.api.services;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import popularioty.api.common.exception.ReputationAPIException;
import popularioty.api.common.exception.ReputationAPIException.Level;

import com.couchbase.client.core.BackpressureException;
import com.couchbase.client.core.RequestCancelledException;
import com.couchbase.client.deps.io.netty.handler.timeout.TimeoutException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

@Service
public class PersistentStorage implements DisposableBean{

	private static Logger LOG = LoggerFactory.getLogger(PersistentStorage.class);	
	
	private CouchbaseCluster cluster;
	private String host;
	
	private void readProperties(Properties properties) {
		this.host= properties.getProperty("couchbase.host");
				
	}
	
	public  PersistentStorage() {
		// load properties file from classpath
        Properties properties = new Properties();
        ClassPathResource resource = new ClassPathResource("couchbase.properties");
        try {
            properties.load(resource.getInputStream());
            readProperties(properties);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.cluster = CouchbaseCluster.create(this.host);
	}
	
	private JsonObject convertMap(Map<String,Object> data)
	{
		JsonObject ret = JsonObject.create();
		for(String key: data.keySet())
		{
			Object o = data.get(key);
			if(o instanceof List)
			{
				JsonArray array = JsonArray.create();
				Object current = null;
				for( Iterator it = ((List) o).iterator(); it.hasNext(); )
				{
					current = it.next();
					array.add(current);
				}
				ret.put( key , array);
			}
			else
				ret.put(key, o);
		}
		return ret;
	}
	public Map<String, Object> storeData(String id, Map<String, Object> data, String set) throws ReputationAPIException
	{
		
		try{
			//set is the bucket name
			Bucket bucket = cluster.openBucket(set);
			JsonObject d = convertMap(data);
			JsonDocument inserted = bucket.insert(
					JsonDocument.create(id, d));
		
			/*This could be removed for performance reasons, however 
			 * it helps to make sure that data really made it into the bucket?
			*/
			JsonDocument found = bucket.get(id);
			return found.content().toMap();

		}catch(BackpressureException bex)
		{
			throw new ReputationAPIException("Couchbase exception. Contact the Administrator... :(",null,LOG,"From Couchbase Client. It seems The producer outpaces the SDK. Backpressure exception. "+bex.getMessage() ,Level.DEBUG,500);
		}
		catch(RequestCancelledException rex)
		{
			throw new ReputationAPIException("Couchbase exception. Contact the Administrator... :(",null,LOG,"From Couchbase Client. The operation had to be cancelled while \"in flight\" on the wire. RequestCancelledException."+rex.getMessage() ,Level.DEBUG,500);
		}
		catch(TimeoutException tex)
		{
			throw new ReputationAPIException("Couchbase exception. Contact the Administrator... :(",null,LOG,"From Couchbase Client. The operation takes longer than the specified timeout. TimeoutException. "+tex.getMessage() ,Level.DEBUG,500);
		}
	
	}
	
	@Override
	public void destroy() throws Exception 
	{
		cluster.disconnect();
		
	}
	
}
