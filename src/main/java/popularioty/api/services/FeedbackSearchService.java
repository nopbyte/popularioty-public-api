package popularioty.api.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import popularioty.api.services.io.DocumentDatabase;
import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.exception.PopulariotyException.Level;
import popularioty.commons.services.searchengine.criteria.search.SearchCriteria;
import popularioty.commons.services.searchengine.criteria.search.SearchCriteriaConstants;
import popularioty.commons.services.searchengine.criteria.search.SearchCriteriaType;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteria;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteriaConstants;
import popularioty.commons.services.searchengine.criteria.sort.SortCriteriaType;
import popularioty.commons.services.searchengine.queries.Query;
import popularioty.commons.services.searchengine.queries.QueryResponse;
import popularioty.commons.services.searchengine.queries.QueryType;


@Service
public class FeedbackSearchService {
	
	private static Logger LOG = LoggerFactory.getLogger(FeedbackSearchService .class);	
	
	@Autowired 
	DocumentDatabase docService;

	private String prop_index_feedback;

	private String prop_index_meta_feedback;


	public FeedbackSearchService()
	{
		Map<String,Object> properties = PropertiesLoader.loadSearchConfiuration("search.properties");
		this.prop_index_feedback= (String) properties.get("index.feedback");
		this.prop_index_meta_feedback= (String) properties.get("index.metafeedback");
	}
	public Map<String,Object> getFeedbackById(String id) throws Exception
	{
		return docService.getFeedbackById(id);
	}
	
	public List<Map<String,Object>> getFeedbackForEntity(String entityType, String entityId, String groupId, int from, int size) throws PopulariotyException
	{

		Query q = new Query(QueryType.SELECT);
		if(groupId != null && !"".equals(groupId))
			q.addCriteria(new SearchCriteria<String>("user_groups", groupId, SearchCriteriaType.MUST_MATCH));
		
		if(entityType!=null&& !entityType.equals(""))
			q.addCriteria(new SearchCriteria<String>("entity_type", entityType, SearchCriteriaType.MUST_MATCH));
		q.addCriteria(new SearchCriteria<String>("entity_id", entityId, SearchCriteriaType.MUST_MATCH));
		
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_FROM, from, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_SIZE, size, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<String>("date", SortCriteriaConstants.VALUE_DESC, SortCriteriaType.SORT));
		
		QueryResponse res = docService.executeQuery(q,this.prop_index_feedback);
		return res.getListofMapsResult();
	}
	
	public List<Map<String,Object>> getFeedbackLevenshteinString(String text, int maxQuerySize, int levenshtein) throws PopulariotyException
	{
		if(maxQuerySize>50)
			throw new PopulariotyException("too many results requested",null,LOG,"too many results requested for fuzzy search of feedback" ,Level.DEBUG,422);
		
		Query q = new Query(QueryType.FUZZY_TEXT_SEARCH);
		
		q.addCriteria(new SearchCriteria<String>("test", text , SearchCriteriaType.LIKE));
		q.addCriteria(new SearchCriteria<String>("title", text , SearchCriteriaType.LIKE));
		q.addCriteria(new SearchCriteria<Integer>( SearchCriteriaConstants.FIELD_LEVENSHTEIN,levenshtein, SearchCriteriaType.MUST_MATCH));
		
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_SIZE, maxQuerySize, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<String>("date", SortCriteriaConstants.VALUE_DESC, SortCriteriaType.SORT));
		
		QueryResponse res = docService.executeQuery(q,this.prop_index_feedback);
		return res.getListofMapsResult();

	}
	
	public List<Map<String, Object>> getMetaFeedbackByFeedback(
			String feedbackId, int from, int size) throws PopulariotyException
	{
		Query q = new Query(QueryType.SELECT);
		
		q.addCriteria(new SearchCriteria<String>("feedback_id", feedbackId, SearchCriteriaType.MUST_MATCH));
		
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_FROM, from, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<Integer>(SortCriteriaConstants.FIELD_SIZE, size, SortCriteriaType.RANGE));
		q.addCriteria(new SortCriteria<String>("date", SortCriteriaConstants.VALUE_DESC, SortCriteriaType.SORT));
		
		QueryResponse res = docService.executeQuery(q,this.prop_index_meta_feedback);
		return res.getListofMapsResult();
		//return docService.getMetaFeedbackByFeedback(feedbackId, from, size);
	}
	
}
