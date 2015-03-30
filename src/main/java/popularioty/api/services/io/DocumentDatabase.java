package popularioty.api.services.io;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import popularioty.api.services.PropertiesLoader;
import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.services.searchengine.factory.SearchEngineFactory;
import popularioty.commons.services.searchengine.factory.SearchProvider;
import popularioty.commons.services.searchengine.queries.Query;
import popularioty.commons.services.searchengine.queries.QueryResponse;
import popularioty.commons.services.storageengine.factory.StorageFactory;
import popularioty.commons.services.storageengine.factory.StorageProvider;
@Service
public class DocumentDatabase implements DisposableBean{

	private static Logger LOG = LoggerFactory.getLogger(DocumentDatabase.class);	
	
	private SearchProvider provider = null;;
	
	private StorageProvider store = null;
	
	private Map<String, Object> searchProperties;
	
	private Map<String, Object> storeProperties;
	
	
	
	public DocumentDatabase() throws Exception
	{
			storeProperties = PropertiesLoader.loadSearchConfiuration("storage.properties");
		 	store = StorageFactory.getStorageProvider((String) storeProperties .get("storage.engine"));
		 	store.init(storeProperties);
        
		 	searchProperties = PropertiesLoader.loadSearchConfiuration("search.properties");
            provider = SearchEngineFactory.getSearchProvider((String) searchProperties.get("search.engine"));
            searchProperties.put("storage.provider.object", store);            
            provider.init(searchProperties);
			//TODO remove this after refactoring

            
     
    }


	/**
	 * This method will ensure that when the web applications goes down, the search node is acutally closed.
	 */
	@Override
	public void destroy() throws Exception {
			//close connection to the providers
		    provider.close(null);	
			store.close(null);
	}


	public Map<String, Object> getFeedbackById(String id) throws PopulariotyException {
		
		return null;// storageService.getData(id, ((String)properties.get("index.feedback")),"feedback_id");
	}
	
	
	/////// AFTER
	public QueryResponse executeQuery(Query query, String index) throws PopulariotyException
	{
		return provider.execute(query, index);
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
	

	public Map<String, Object> getData(String id, String set, String idLabel) throws PopulariotyException{
		Map<String,Object> curr = store.getData(id, set);
		if(idLabel !=null)
			curr.put(idLabel,id);
		return curr;
	}
	
	

}
