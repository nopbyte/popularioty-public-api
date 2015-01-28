package popularioty.api.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import popularioty.api.common.exception.ReputationAPIException;
import popularioty.api.rest.client.HTTPClient2;
import popularioty.api.services.elasticsearch.QueryDocumentDatabase;


@Service
public class ReputationQueryService {
	
	private static Logger LOG = LoggerFactory.getLogger(ReputationQueryService .class);	
	
	@Autowired 
	QueryDocumentDatabase docService;
	
	private HTTPClient2<HashMap> http;
	/*private String esHost;
	private String RES_HITS = "hits";
	private String RES_TOTAL= "total";
	private String RES_SOURCE = "_source";*/

	public ReputationQueryService()
	{
		//http = new HTTPClient2<>();
		//esHost = "http://localhost:9200";
		
	}
	public Map<String,Object> getAggregatedValueForEntity(String entityType, String entityId) throws ReputationAPIException
	{
		return docService.getAggregatedReputation(entityId, entityType);
	}
	
	public List<Map<String,Object>> getFeedbackForEntity(String entityType, String entityId, int from, int size) throws ReputationAPIException
	{
		return docService.getFeedbackByEntity(entityId, entityType, from, size);
	}
	
	public List<Map<String,Object>> getFeedbackLevenshteinString(String text, int maxQuerySize, int levenshtein) throws ReputationAPIException
	{
		return docService.getFeedbackLevenshteinString(text, maxQuerySize, levenshtein);
	}
	
	public List<Map<String, Object>> getMetaFeedbackByFeedback(
			String feedbackId, int from, int size) throws ReputationAPIException{
		
		return docService.getMetaFeedbackByFeedback(feedbackId, from, size);
	}
	
	
	/*private Map<String, Object> httpQueryAggregatedReputation(
			String entityType, String entityId) throws ReputationAPIException {
		String url = esHost+"/reputation_aggregations/rep/_search?q=entity_type:replace_type AND entity_id:replace_id&sort=date:desc&size=replace_size";
		url = url.replaceAll("replace_type", entityType);
		url = url.replaceAll("replace_id", entityId);
		url = url.replaceAll("replace_size", "1");
		
		LOG.debug("getting latest aggregated reputation value for entity with id:"+entityId+" and type: "+entityType);
		//TODO add headers
		LOG.info("Attempting the GET request: "+url+" to elastic search");
		ResponseEntity<HashMap> response = http.getDataHTTPCall(http.GET, url, null, null, HashMap.class);
		HashMap<String,Object> res = response.getBody();
		for (String key: res.keySet()) {
		    LOG.info("Response from ES: ("+key.toString() + ": " + res.get(key)+")");
		}
		if(res.containsKey(RES_HITS))
		{
			res = (HashMap<String, Object>) res.get(RES_HITS);
			if(res.containsKey(RES_TOTAL)&&((Integer)res.get(RES_TOTAL)).intValue()>0)
			{ 
					if(res.containsKey(RES_HITS))
					{
					   List<Object> hits= (List<Object>) res.get(RES_HITS);
					   res = ((HashMap<String,Object>)hits.get(0));
					   if(res.containsKey(RES_SOURCE))
						   return   (Map<String, Object>) res.get(RES_SOURCE);
					}	
				
				 
			}
		}
		throw new ReputationAPIException("No content found",null,LOG,"Reputation aggregated value not found for entity with id: "+entityId+" and type: "+entityType ,Level.DEBUG,204);
	}
	 */
}
