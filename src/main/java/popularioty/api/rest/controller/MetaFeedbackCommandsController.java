package popularioty.api.rest.controller;


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
import org.springframework.web.bind.annotation.ResponseBody;

import popularioty.api.rest.messages.input.FeedbackCreate;
import popularioty.api.rest.messages.input.MetaFeedbackCreate;
import popularioty.api.services.FeedbackStorageService;
import popularioty.api.services.MetaFeedbackStorageService;
import popularioty.commons.exception.PopulariotyException;



@Controller
@RequestMapping("/")
public class MetaFeedbackCommandsController 
{
	private static Logger LOG = LoggerFactory.getLogger(MetaFeedbackCommandsController.class);
	
	
	@Autowired
    private MetaFeedbackStorageService metafeedbackService;	
	
	
	@RequestMapping(value = "feedback/{entity_id}/meta_feedback/", method = RequestMethod.POST, produces = "application/json")
        public  @ResponseBody ResponseEntity<Object> getReputationData( 
        		@RequestHeader("Authorization") String token,
        		@PathVariable(value="entity_id") String entity_id,
        		@Valid  @RequestBody MetaFeedbackCreate message,
        		HttpServletRequest req) {
		    	     	
    			HttpHeaders headers = new HttpHeaders();
		    	try{
    				 
		    		Map ret = null;
		    		ret = metafeedbackService.createMetaFeedbackEntry(entity_id, token,
		    										message.getTitle(), message.getText(), 
		    										message.isRating());
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
