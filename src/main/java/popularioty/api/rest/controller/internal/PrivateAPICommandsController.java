package popularioty.api.rest.controller.internal;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import popularioty.api.rest.messages.input.FeedbackCreate;
import popularioty.api.services.FeedbackStorageService;
import popularioty.api.services.PropertiesLoader;
import popularioty.api.services.io.DocumentDatabase;
import popularioty.commons.exception.PopulariotyException;



@Controller
@RequestMapping("/internal")
public class PrivateAPICommandsController 
{
	private static Logger LOG = LoggerFactory.getLogger(PrivateAPICommandsController.class);
	
	@Autowired
	DocumentDatabase storeService;

	private List<String> allowedSets = new LinkedList<>();
	
	//TODO fix this for the future with some authentication technique...
	private String fixedToken = "IYao9AeJcaUzhLPB0P1B";
	
	private boolean inSet(String x, List<String> s)
	{
		for(String st: s)
		{
			if(x.equals(st))
				return true;
			else{
				char []one = x.toCharArray();
				char []two = st.toCharArray();
				System.out.println(new String(one));
				System.out.println(new String(two));
			}
		}
		return false;
	}
	public PrivateAPICommandsController()
	{
		//to provide feedback massively for demo.
		Map<String,Object> properties = PropertiesLoader.loadSearchConfiuration("search.properties");
		allowedSets.add((String) properties.get("index.feedback"));
		properties = PropertiesLoader.loadSearchConfiuration("storage.properties");
		//for the private API
		allowedSets.add((String) properties.get("storage.application_popularity"));
		allowedSets.add((String) properties.get("storage.application_activity"));
		
	}
	
	@RequestMapping(value = "{set}/", method = RequestMethod.POST, produces = "application/json")
        public  @ResponseBody ResponseEntity<Object> getReputationData( 
        		@RequestHeader("Authorization") String token,
        		@PathVariable(value="set") String set,
        		@RequestParam(required=false,value="id") String idField,
        		@Valid  @RequestBody List<Map<String,Object>> message,
        		HttpServletRequest req) 
        {
		    if(!inSet(set,allowedSets) || !token.equals(fixedToken))	
		    	return new ResponseEntity<Object>( HttpStatus.FORBIDDEN);
			String id = "";
			HttpHeaders headers = new HttpHeaders();
	    	try{
				for(Map<String,Object> doc: message)
				{
					if(idField != null && !idField.equals(""))
					{
						if(doc.containsKey(idField))
							id = (String) doc.get(idField);
					}
					if("".equals(id))
						id = System.currentTimeMillis()+UUID.randomUUID().toString().replace("-", "");
					storeService.storeData(id, doc, set);
				}
				return new ResponseEntity<Object>(null, headers, HttpStatus.OK);
	    	 }
	    	 catch(PopulariotyException rep){
	    		 //since the creation of the exception generated the log entries for the stacktrace, we don't do it again here
	    		 return new ResponseEntity<Object>(rep.getErrorAsMap(), headers, HttpStatus.valueOf(rep.getHTTPErrorCode()));
	    	 } 
	    	 catch(Exception e)
	    	 {
	    		 String s = PopulariotyException.getStackTrace(e);
	    		 LOG.error(s);
	    		 return new ResponseEntity<Object>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);	 
	    	 }
	 
    	 
        }
    	
}
