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
import popularioty.api.services.idm.FindEntityAttributes;
import popularioty.api.services.io.DocumentDatabase;
import popularioty.commons.exception.PopulariotyException;

@Service
public class FeedbackStorageService{

	private static Logger LOG = LoggerFactory.getLogger(FeedbackStorageService.class);	
	private String feedbackBucketName;
	
	@Autowired
	private DocumentDatabase storage;
	
	@Autowired
	private FinalReputationSearchService repService;

	@Autowired
	private AuthenticateUser auth;
	
	@Autowired
	private FindEntityAttributes attr;
	
	@Autowired
	private DefaultReputationService defRep;
	
	private void readProperties(Properties properties) {
		this.feedbackBucketName= (String) properties.get("index.feedback");
	}
	
	private Object getGroupsFromMemberships(Object object) {
		if(object == null)
			return new LinkedList<>();
		List list = (List) object;
		List<String> ret = new LinkedList<String>();
		Map<String,Object> map = null;
		Iterator<Map> it = list.iterator();
		while( it.hasNext() )
		{
			 map=(Map<String, Object>) it.next();
			 ret.add((String) map.get("group_id"));
		}
		return ret;
	}	
	public FeedbackStorageService(){
		// load properties file from classpath
        Properties properties = new Properties();
        ClassPathResource resource = new ClassPathResource("search.properties");
        try {
            properties.load(resource.getInputStream());
            readProperties(properties);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.cluster = CouchbaseCluster.create(this.host);
	}
	
	public Map createFeedbackEntry(String entity_id, String entity_type, String token, String title, String text, int rating) throws PopulariotyException
	{
        
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		Map<String,Object> attributes = auth.attributesFromUser(token);
		double repValue  = 0;
		try{
			Map<String,Object> reputation = repService.getFinalReputationValueForEntity( "user", (String) attributes.get("id"));
			if(reputation.containsKey("end_user_reputation"))
				repValue= Double.parseDouble((reputation.get("end_user_reputation")).toString());
			else if(attributes.containsKey("reputation"))
				repValue = Integer.parseInt(attributes.get("reputation").toString());
		}catch(PopulariotyException e)
		{
			//likely that this is due to an empty index... the analytics has not run yet...
			if(e.getHTTPErrorCode()==500 && e.getMessage().toLowerCase().contains("search error"))
			{
				if(attributes.containsKey("reputation"))
					repValue = Integer.parseInt(attributes.get("reputation").toString());
				else
					repValue = defRep.defaultReputationValueForEntity(entity_id);
			}
			if(attributes.containsKey("reputation"))
				repValue = Integer.parseInt(attributes.get("reputation").toString());
				
		}
		String owner = "none";
		Map<String, Object> entityInfo = attr.attributesFromEntity(token, entity_id);
		if(entityInfo == null || entityInfo.keySet().isEmpty()){
			//throw new PopulariotyException("Non existing entity with id: "+entity_id,null,LOG,"Non existent entity with id : "+entity_id+"in IDM.",PopulariotyException.Level.DEBUG,404);
		}
		else{
			String key = entityInfo.keySet().iterator().next();
			Map<String, Object> entityData= (Map<String, Object>) entityInfo.get(key);
			 owner = (String) entityData.get("owner_id");
		}		
		
	
		System.out.println(attributes.toString());
		Map<String, Object> document = new HashMap<String, Object>();
		document.put("feedback_id", id);
		document.put("entity_id", entity_id);
		document.put("entity_type", entity_type);
		document.put("entity_owner_id", owner);
		document.put("title", title);
		document.put("text", text);
		document.put("rating", rating);
		document.put("date", System.currentTimeMillis());
		document.put("user_id", attributes.get("id"));
		document.put("user_reputation", repValue);
		document.put("user_name", attributes.get("username"));
		document.put("user_groups", getGroupsFromMemberships(attributes.get("approvedMemberships")));
		
		LOG.info("Feedback stored for entity_id "+entity_id);
		return storage.storeData(id, document,feedbackBucketName);
		
	}



	

}
