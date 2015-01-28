package popularioty.api.rest.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import popularioty.api.common.exception.ReputationAPIException;
import popularioty.api.services.ReputationQueryService;



@Controller
@RequestMapping("/")
public class ReputationDetailsController 
{
	private static Logger LOG = LoggerFactory.getLogger(ReputationDetailsController.class);
	
	
	@Autowired
    private ReputationQueryService reputationQuery;
	 
	
	
	
	
	
	@RequestMapping(value = "{entity_type}/{entity_id}/reputation/", method = RequestMethod.GET, produces = "application/json")
        public  @ResponseBody ResponseEntity<Object> getReputationData( 
        		@PathVariable(value="entity_id") String entity_id,
        		@PathVariable(value="entity_type") String entity_type,
        		HttpServletRequest req) {
		    	 
    	
    			HttpHeaders headers = new HttpHeaders();
		    	try{
    				 
    				 Map<String,Object> ret = reputationQuery.getAggregatedValueForEntity(entity_type, entity_id);
    				 if(ret==null)
    					 new ResponseEntity<>(HttpStatus.NOT_FOUND);
    				 return new ResponseEntity<Object>(ret, headers, HttpStatus.OK);
		    	 }
		    	 catch(ReputationAPIException rep){
		    		 //since the creation of the exception generated the log entries for the stacktrace, we don't do it again here
		    		 return new ResponseEntity<Object>(rep.getErrorAsMap(), headers, HttpStatus.valueOf(rep.getHTTPErrorCode()));
		    	 } 
		    	 catch(Exception e)
		    	 {
		    		 String s = ReputationAPIException.getStackTrace(e);
		    		 LOG.error(s);
		    		 return new ResponseEntity<Object>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);	 
		    	 }
    	 
    	 
        }
    
    	
}
