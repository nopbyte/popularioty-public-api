package popularioty.api.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import popularioty.api.rest.client.HTTPClient2;
import popularioty.api.rest.messages.input.Attribute;
import popularioty.api.rest.messages.input.Entity;
import popularioty.api.rest.messages.out.ClassReputationResponse;
import popularioty.api.services.io.QueryDocumentDatabase;
import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.exception.PopulariotyException.Level;
import popularioty.commons.services.searchengine.factory.ElasticSearchNode;


@Service
public class FeedbackSearchService {
	
	private static Logger LOG = LoggerFactory.getLogger(FeedbackSearchService .class);	
	
	@Autowired 
	QueryDocumentDatabase docService;
	
	@Autowired 
	FinalReputationSearchService finalService;
	
	public FeedbackSearchService()
	{
		
	}
	
	public List<Map<String,Object>> getFeedbackForEntity(String entityType, String entityId, int from, int size) throws PopulariotyException
	{
		return docService.getFeedbackByEntity(entityId, entityType, from, size);
	}
	
	public List<Map<String,Object>> getFeedbackLevenshteinString(String text, int maxQuerySize, int levenshtein) throws PopulariotyException
	{
		return docService.getFeedbackLevenshteinString(text, maxQuerySize, levenshtein);
	}
	
	public List<Map<String, Object>> getMetaFeedbackByFeedback(
			String feedbackId, int from, int size) throws PopulariotyException{
		
		return docService.getMetaFeedbackByFeedback(feedbackId, from, size);
	}
	
	}
