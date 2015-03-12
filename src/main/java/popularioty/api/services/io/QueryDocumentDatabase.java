package popularioty.api.services.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.services.search.FeedbackReputationSearch;
import popularioty.commons.services.search.FinalReputationSearch;
import popularioty.commons.services.search.MultiTypeReputationSearch;
import popularioty.commons.services.searchengine.factory.SearchEngineFactory;
import popularioty.commons.services.searchengine.factory.SearchProvider;
@Service
public class QueryDocumentDatabase implements DisposableBean{

	private static Logger LOG = LoggerFactory.getLogger(QueryDocumentDatabase.class);	
	

	private SearchProvider provider;
	private FeedbackReputationSearch feedbackSearch;
	private FinalReputationSearch finalSearch;
	private MultiTypeReputationSearch multiSearch;
	
	public QueryDocumentDatabase()
	{
		// load properties file from classpath
        Properties properties = new Properties();
        ClassPathResource resource = new ClassPathResource("search.properties");
        try {
            properties.load(resource.getInputStream());
            Map<String, Object> map = new HashMap<String, Object>();
            for (String key : properties.stringPropertyNames()) {
                map.put(key, properties.getProperty(key));
            }
            //in this way the api is independent of the class produced by the factory
            provider = SearchEngineFactory.getSearchProvider(properties.getProperty("search.engine"));
            provider.init(map);
            feedbackSearch = new FeedbackReputationSearch(map,provider );
            finalSearch = new FinalReputationSearch(map,provider );
            multiSearch = new MultiTypeReputationSearch(map,provider );
         
        	
        } catch (IOException e) {
        	
            LOG.error("Error loading properties file to create SearchEngine - IOException.. " + PopulariotyException.getStackTrace(e.getCause()));
        } catch (Exception e) {
        	
        	LOG.error("Error loading properties file to create SearchEngine - Exception.. " +PopulariotyException.getStackTrace(e.getCause()));
		}
     
    }


	public Map<String, Object> getFinalReputation(String entityId, String entityType) throws PopulariotyException
	{
			return finalSearch.getFinalReputation(entityId, entityType);
	}
	
	public Map<String, Object> getSingleClassReputation(String entityId, String entityType, String reputationType) throws PopulariotyException
	{
			return multiSearch.getSingleClassReputation(entityId, entityType, reputationType);
	}

	public List<Map<String, Object>> getFeedbackByEntity(String entityId, String entityType, int from, int size) throws PopulariotyException
	{
		return feedbackSearch.getFeedbackByEntity(entityId, entityType, from, size);
	}

	public List<Map<String, Object>> getMetaFeedbackByFeedback(String feedbackId, int from, int size) throws PopulariotyException
	{
		return feedbackSearch.getMetaFeedbackByFeedback(feedbackId, from, size);
	}
	
	public List<Map<String, Object>> getFeedbackLevenshteinString(String text, int maxQuerySize, int levenshtein) throws PopulariotyException
	{
		return feedbackSearch.getFeedbackLevenshteinString(text, maxQuerySize, levenshtein);		
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
