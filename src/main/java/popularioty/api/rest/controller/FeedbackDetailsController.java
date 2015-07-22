package popularioty.api.rest.controller;


import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import popularioty.api.rest.messages.input.LevenshteinSearch;
import popularioty.api.services.FeedbackSearchService;
import popularioty.commons.exception.PopulariotyException;



@Controller
@RequestMapping("/")
public class FeedbackDetailsController 
{
	private static Logger LOG = LoggerFactory.getLogger(FeedbackDetailsController.class);
	
	
	@Autowired
    private FeedbackSearchService reputationQuery;	
	
	
	@RequestMapping(value = "{entity_type}/{entity_id}/feedback/", method = RequestMethod.GET, produces = "application/json")
        public  @ResponseBody ResponseEntity<Object> getReputationData( 
        		@PathVariable(value="entity_id") String entity_id,
        		@PathVariable(value="entity_type") String entity_type,
        		@RequestParam(required=false,value="group_id") String gropId,
        		@RequestParam("from") int from,
        		@RequestParam("size") int size,
        		HttpServletRequest req) {
		    	 
    	
    			HttpHeaders headers = new HttpHeaders();
		    	try{
    				 
    				 List<Map<String,Object>> ret = reputationQuery.getFeedbackForEntity(entity_type, entity_id, gropId, from, size);
    				 if(ret==null)
    					 new ResponseEntity<>(HttpStatus.NOT_FOUND);
    				 return new ResponseEntity<Object>(ret, headers, HttpStatus.OK);
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
	
	@RequestMapping(value = "search_levenshtein/feedback/", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json")
    public  @ResponseBody ResponseEntity<Object> getReputationData(
    		@Valid  @RequestBody LevenshteinSearch message,
    		HttpServletRequest req) {
	    	 
	
			HttpHeaders headers = new HttpHeaders();
	    	try{
	    		 List<Map<String,Object>> ret = reputationQuery.getFeedbackLevenshteinString(message.getText(), message.getMax_query_terms(), message.getLevenshtein());
				 if(ret==null)
					 new ResponseEntity<>(HttpStatus.NOT_FOUND);
				 return new ResponseEntity<Object>(ret, headers, HttpStatus.OK);
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
