package popularioty.api.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import popularioty.api.rest.messages.input.Attribute;
import popularioty.api.rest.messages.input.Entity;
import popularioty.api.rest.messages.out.ClassReputationResponse;
import popularioty.api.services.io.DocumentDatabase;
import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.exception.PopulariotyException.Level;
import popularioty.commons.services.searchengine.criteria.search.SearchCriteria;
import popularioty.commons.services.searchengine.criteria.search.SearchCriteriaType;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteria;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteriaConstants;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteriaType;

import popularioty.commons.services.searchengine.queries.Query;
import popularioty.commons.services.searchengine.queries.QueryResponse;
import popularioty.commons.services.searchengine.queries.QueryType;


@Service
public class AggregatedReputationSearchService {
	
	private static Logger LOG = LoggerFactory.getLogger(AggregatedReputationSearchService .class);	
	
	@Autowired 
	DocumentDatabase docService;
	
	@Autowired 
	FinalReputationSearchService finalService;
	
	private String prop_index_subreputation;
	
	public AggregatedReputationSearchService()
	{
		prop_index_subreputation = (String) PropertiesLoader.loadSearchConfiuration("search.properties").get("index.subreputation");
		
	}
		
	public Map<String, Object> getSubReputationSearch(
			String entityId, String entityType, String classReputationType) throws PopulariotyException{
		
		Query q = new Query(QueryType.SELECT_ID);
		if(entityType!=null&& !entityType.equals(""))
			q.addCriteria(new SearchCriteria<String>("entity_type", entityType, SearchCriteriaType.MUST_MATCH));
		q.addCriteria(new SearchCriteria<String>("sub_reputation_type", classReputationType, SearchCriteriaType.MUST_MATCH));
		q.addCriteria(new SearchCriteria<String>("entity_id", entityId, SearchCriteriaType.MUST_MATCH));
		
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_FROM, 0, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_SIZE, 1, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<String>("date", SortCriteriaConstants.VALUE_DESC, SortCriteriaType.SORT));
		
		QueryResponse res = docService.executeQuery(q,this.prop_index_subreputation);
		return res.getMapResult();
		
	}
	
		public List<ClassReputationResponse> getSubReputationBatchSearch(		
			List<Entity> entities, List<Attribute> attributes) throws PopulariotyException{
		
			List<ClassReputationResponse> res = new LinkedList<>();
			if(attributes.size()<=0 || entities.size()<=0)
				throw new PopulariotyException("No attributes requested or entities",null,LOG,"No attributes requested",Level.DEBUG,400);
			boolean rep = false;
			for(Entity ent: entities)
			{
				ClassReputationResponse data = new ClassReputationResponse();
				data.setEntity_id(ent.getEntity_id());
				for(Attribute att: attributes)
				{
					  Map<String,Object> external= new HashMap<>();
					  Map<String,Object> internal= new HashMap<>();
					  Map<String, Object> row = null;
					  try{
						   if(att.getReputation_type().toLowerCase().equals("final"))
						    row = finalService.getFinalReputationValueForEntity(ent.getEntity_type(), ent.getEntity_id());
						   else 
							  row = getSubReputationSearch( ent.getEntity_id(),  ent.getEntity_type(),  att.getReputation_type()) ;
					   }catch (PopulariotyException ex)
					   {
						  if(ex.getHTTPErrorCode()==204)
							  LOG.debug("information not found for");
						}
					    if(row == null)
					    	for(String value: att.getValues())
					    		internal.put(value,  -1);
					    else
					    	for(String value: att.getValues())
					    		if(row.containsKey(value))
					    			internal.put(value,  row.get(value));
						   
						   external.put(att.getReputation_type(),internal);
						   data.setEntity_type( (row!=null)?(String) row.get("entity_type"):"");
						   data.addAttribute(external);
				
					    
					  
				}
				res.add(data);
			}
		return res;
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
