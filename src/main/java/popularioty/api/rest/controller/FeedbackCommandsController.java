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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import popularioty.api.rest.messages.input.FeedbackCreate;
import popularioty.api.rest.messages.input.LevenshteinSearch;
import popularioty.api.rest.messages.input.SubReputationSearch;
import popularioty.api.services.FeedbackStorageService;
import popularioty.api.services.FeedbackSearchService;
import popularioty.commons.exception.PopulariotyException;



@Controller
@RequestMapping("/")
public class FeedbackCommandsController 
{
	private static Logger LOG = LoggerFactory.getLogger(FeedbackCommandsController.class);
	
	
	@Autowired
    private FeedbackStorageService feedbackService;	
	
	
	@RequestMapping(value = "{entity_type}/{entity_id}/feedback/", method = RequestMethod.POST, produces = "application/json")
        public  @ResponseBody ResponseEntity<Object> getReputationData( 
        		@RequestHeader("Authorization") String token,
        		@PathVariable(value="entity_id") String entity_id,
        		@PathVariable(value="entity_type") String entity_type,
        		@Valid  @RequestBody FeedbackCreate message,
        		HttpServletRequest req) {
		    	     	
    			HttpHeaders headers = new HttpHeaders();
		    	try{
    				 
		    		Map ret = null;
		    		ret = feedbackService.createFeedbackEntry(entity_id, entity_type, token,
		    										message.getTitle(), message.getText(), 
		    										message.getRating());
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
