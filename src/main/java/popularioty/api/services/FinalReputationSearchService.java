package popularioty.api.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import popularioty.api.services.io.DocumentDatabase;
import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.services.searchengine.criteria.search.SearchCriteria;
import popularioty.commons.services.searchengine.criteria.search.SearchCriteriaType;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteria;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteriaConstants;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteriaType;
import popularioty.commons.services.searchengine.queries.Query;
import popularioty.commons.services.searchengine.queries.QueryResponse;
import popularioty.commons.services.searchengine.queries.QueryType;


@Service
public class FinalReputationSearchService {
	
	private static Logger LOG = LoggerFactory.getLogger(FinalReputationSearchService .class);	
	
	@Autowired 
	DocumentDatabase docService;
	Map<String,Object> properties;

	private String prop_index_aggregated_rep;

	public FinalReputationSearchService()
	{
		properties = PropertiesLoader.loadSearchConfiuration("search.properties");
		this.prop_index_aggregated_rep = (String) properties.get("index.aggregated");
		
	}
	public Map<String,Object> getFinalReputationValueForEntity(String entityType, String entityId) throws PopulariotyException
	{
		Query q = new Query(QueryType.SELECT);
			
		if(entityType!=null&& !entityType.equals(""))
			q.addCriteria(new SearchCriteria<String>("entity_type", entityType, SearchCriteriaType.MUST_MATCH));
		q.addCriteria(new SearchCriteria<String>("entity_id", entityId, SearchCriteriaType.MUST_MATCH));
		
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_FROM, 0, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_SIZE, 1, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<String>("date", SortCriteriaConstants.VALUE_DESC, SortCriteriaType.SORT));
		
		QueryResponse res = docService.executeQuery(q,this.prop_index_aggregated_rep);
		List<Map<String,Object>> r = res.getListofMapsResult();
		//if it was empty, we would have gotten an exception...
		return r.get(0);

	}
}
