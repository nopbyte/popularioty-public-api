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
import popularioty.api.services.io.StoreDocumentsService;
import popularioty.commons.exception.PopulariotyException;

@Service
public class FeedbackStorageService{

	private static Logger LOG = LoggerFactory.getLogger(FeedbackStorageService.class);	
	private String feedbackBucketName;
	
	@Autowired
	private StoreDocumentsService storage;

	@Autowired
	private AuthenticateUser auth;
	
	private void readProperties(Properties properties) {
		this.feedbackBucketName= (String) properties.get("index.feedback");
	}
	
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
		//TODO include verification that the entity has been used indeed before by this user.
		
		System.out.println(attributes.toString());
		Map<String, Object> document = new HashMap<String, Object>();
		document.put("feedback_id", id);
		document.put("entity_id", entity_id);
		document.put("entity_type", entity_type);
		document.put("title", title);
		document.put("text", text);
		document.put("rating", rating);
		document.put("date", System.currentTimeMillis());
		document.put("user_id", attributes.get("id"));
		document.put("user_name", attributes.get("username"));
		document.put("user_groups", getGroupsFromMemberships(attributes.get("approvedMemberships")));
		
		LOG.info("Feedback stored for entity_id "+entity_id);
		return storage.storeData(id, document,feedbackBucketName);
		
	}



	

}
