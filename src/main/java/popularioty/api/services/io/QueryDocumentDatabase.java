package popularioty.api.services.io;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.services.search.FeedbackReputationSearch;
import popularioty.commons.services.search.FinalReputationSearch;
import popularioty.commons.services.search.MultiTypeReputationSearch;
import popularioty.commons.services.searchengine.factory.ElasticSearchNode;
import popularioty.commons.services.searchengine.factory.SearchEngineFactory;
import popularioty.commons.services.searchengine.factory.SearchProvider;
import popularioty.commons.services.storageengine.factory.StorageFactory;
import popularioty.commons.services.storageengine.factory.StorageProvider;
@Service
public class QueryDocumentDatabase implements DisposableBean{

	private static Logger LOG = LoggerFactory.getLogger(QueryDocumentDatabase.class);	
	
	@Autowired
	private StoreDocumentsService storageService;
	
	private SearchProvider provider;
	
	private Map<String, Object> properties; 
	
	private FeedbackReputationSearch feedbackSearch;
	
	private FinalReputationSearch finalSearch;
	
	private MultiTypeReputationSearch multiSearch;
	
	public QueryDocumentDatabase()
	{
		// load properties file from classpath
        Properties props = new Properties();
        ClassPathResource resource = new ClassPathResource("search.properties");
        try {
            props.load(resource.getInputStream());
            properties = new HashMap<String, Object>();
            for (String key : props.stringPropertyNames()) {
                properties.put(key, props.getProperty(key));
            }
            //in this way the api is independent of the class produced by the factory
            provider = SearchEngineFactory.getSearchProvider(props.getProperty("search.engine"));
            provider.init(properties);
            feedbackSearch = new FeedbackReputationSearch(properties,provider );
            finalSearch = new FinalReputationSearch(properties,provider );
            multiSearch = new MultiTypeReputationSearch(properties,provider );
         
        	
        } catch (IOException e) {
        	
            LOG.error("Error loading properties file to create SearchEngine - IOException.. " + PopulariotyException.getStackTrace(e.getCause()));
        } catch (Exception e) {
        	
        	LOG.error("Error loading properties file to create SearchEngine - Exception.. " +PopulariotyException.getStackTrace(e.getCause()));
		}
     
    }


	public Map<String, Object> getFinalReputation(String entityId, String entityType) throws PopulariotyException
	{
		String id = finalSearch.getFinalReputation(entityId, entityType);	
		List<String>list = Collections.singletonList(id);
		return storageService.getData(list,(String) properties.get("index.aggregated"),null).get(0);
		
	}
	
	public Map<String, Object> getSingleClassReputation(String entityId, String entityType, String reputationType) throws PopulariotyException
	{
		String id = multiSearch.getSingleClassReputation(entityId, entityType, reputationType);
		List<String>list = Collections.singletonList(id);
		return storageService.getData(list,(String) properties.get("index.subreputation"),null).get(0);
	}

	public List<Map<String, Object>> getFeedbackByEntity(String entityId, String entityType, String groupId, int from, int size) throws PopulariotyException
	{
		return storageService.getData(feedbackSearch.getFeedbackByEntity(entityId, entityType, groupId, from, size),(String) properties.get("index.feedback"),"feedback_id");
	}

	public List<Map<String, Object>> getMetaFeedbackByFeedback(String feedbackId, int from, int size) throws PopulariotyException
	{
		return  storageService.getData(feedbackSearch.getMetaFeedbackByFeedback(feedbackId, from, size),((String) properties.get("index.metafeedback")),"meta_feedback_id");
		
	}
	
	public List<Map<String, Object>> getFeedbackLevenshteinString(String text, int maxQuerySize, int levenshtein) throws PopulariotyException
	{
		return storageService.getData(feedbackSearch.getFeedbackLevenshteinString(text, maxQuerySize, levenshtein),(String) properties.get("index.feedback"),"feedback_id");
	}
	/**
	 * This method will ensure that when the web applications goes down, the search node is acutally closed.
	 */
	@Override
	public void destroy() throws Exception {
			//close connection to the searchprovider
		    provider.close(null);		
	}
}
