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
public class FinalReputationSearchService {
	
	private static Logger LOG = LoggerFactory.getLogger(FinalReputationSearchService .class);	
	
	@Autowired 
	QueryDocumentDatabase docService;
	

	public FinalReputationSearchService()
	{

		
	}
	public Map<String,Object> getFinalReputationValueForEntity(String entityType, String entityId) throws PopulariotyException
	{
		return docService.getFinalReputation(entityId, entityType);
	}
	}
