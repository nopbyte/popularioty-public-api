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

import popularioty.api.common.exception.ReputationAPIException;
import popularioty.api.rest.messages.input.LevenshteinSearch;
import popularioty.api.services.QueryService;



@Controller
@RequestMapping("/")
public class MetaFeedbackDetailsController 
{
	private static Logger LOG = LoggerFactory.getLogger(MetaFeedbackDetailsController.class);
	
	
	@Autowired
    private QueryService reputationQuery;	
	
	
	@RequestMapping(value = "feedback/{feedback_id}/meta/", method = RequestMethod.GET, produces = "application/json")
        public  @ResponseBody ResponseEntity<Object> getReputationData( 
        		@PathVariable(value="feedback_id") String feedbackId,        		
        		@RequestParam("from") int from,
        		@RequestParam("size") int size,
        		HttpServletRequest req) {
		    	 
    	
    			HttpHeaders headers = new HttpHeaders();
		    	try{
    				 
    				 List<Map<String,Object>> ret = reputationQuery.getMetaFeedbackByFeedback(feedbackId, from, size);
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
