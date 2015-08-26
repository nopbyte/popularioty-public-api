package popularioty.api.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import popularioty.api.services.idm.AuthenticateUser;
import popularioty.api.services.io.DocumentDatabase;
import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.exception.PopulariotyException.Level;

@Service
public class MetaFeedbackStorageService{

	private static Logger LOG = LoggerFactory.getLogger(MetaFeedbackStorageService.class);	
	private String metaFeedbackSetName;

	@Autowired
	private DocumentDatabase storage;
	
	@Autowired
	private DocumentDatabase search;

	@Autowired
	private AuthenticateUser auth;
	
	@Autowired
	FeedbackSearchService fSearch;
	
	
	private Object getGroupsFromMemberships(Object object) {
		List list = (List) object;
		List<String> ret = new LinkedList<String>();
		Map<String,Object> map = null;
		for(Iterator<Map> it = list.iterator(); it.hasNext();)
		{
			 map=(Map<String, Object>) it.next();
			 ret.add((String) map.get("group_id"));
		}
		return ret;
	}	
	public MetaFeedbackStorageService(){
		// load properties file from classpath
        Map<String, Object> properties = properties = PropertiesLoader.loadSearchConfiuration("search.properties");
		this.metaFeedbackSetName= (String) properties.get("index.metafeedback");
	}
	
	public Map createMetaFeedbackEntry(String feedbackId, String token, String title, String text, boolean rating) throws Exception
	{
        
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		
		Map<String,Object> attributes = auth.attributesFromUser(token);
		Map<String,Object> feedback = fSearch.getFeedbackById(feedbackId);
		int repValue = 4;

		//TODO include verification that the entity has been used indeed before by this user.
		
		
		System.out.println(attributes.toString());
		Map<String, Object> document = new HashMap<String, Object>();
		document.put("feedback", feedback);
		document.put("meta_feedback", id);
		if(title != null)
			document.put("title", title);
		if(text != null)
			document.put("text", text);
		document.put("rating", rating);
		document.put("date", System.currentTimeMillis());
		document.put("user_id", attributes.get("id"));
		document.put("user_reputation", repValue);
		document.put("user_name", attributes.get("username"));
		if(attributes != null)
			document.put("user_groups", getGroupsFromMemberships(attributes.get("approvedMemberships")));
		
		LOG.info("MetaFeedback stored for feedback with id"+feedbackId);
		return storage.storeData(id, document,metaFeedbackSetName);
		
	}



	

}
