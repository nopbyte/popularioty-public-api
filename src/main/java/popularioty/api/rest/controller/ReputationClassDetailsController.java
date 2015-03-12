package popularioty.api.rest.controller;


import java.util.HashMap;
import java.util.LinkedList;
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

import popularioty.api.rest.messages.input.Attribute;
import popularioty.api.rest.messages.input.Entity;
import popularioty.api.rest.messages.input.LevenshteinSearch;
import popularioty.api.rest.messages.input.SubReputationBatchSearch;
import popularioty.api.rest.messages.input.SubReputationSearch;
import popularioty.api.rest.messages.out.ClassReputationResponse;
import popularioty.api.services.AggregatedReputationSearchService;
import popularioty.api.services.FeedbackSearchService;
import popularioty.commons.exception.PopulariotyException;



@Controller
@RequestMapping("/")
public class ReputationClassDetailsController 
{
	private static Logger LOG = LoggerFactory.getLogger(ReputationClassDetailsController.class);
	
	
	@Autowired
    private AggregatedReputationSearchService reputationQuery;	
	
	
	
	@RequestMapping(value = "class_reputation/", method = {RequestMethod.GET,RequestMethod.POST}, produces = "application/json")
    public  @ResponseBody ResponseEntity<Object> getReputationData(
    		@Valid  @RequestBody SubReputationSearch message,
    		HttpServletRequest req) {
	    	 
	
			HttpHeaders headers = new HttpHeaders();
	    	try{
	    		 Map<String,Object> ret = reputationQuery.getSubReputationSearch(message.getEntity_id(),message.getEntity_type(),message.getSub_reputation_type());
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
	
	@RequestMapping(value = "class_reputation/batch/", method = {RequestMethod.GET,RequestMethod.POST}, produces = "application/json")
    public  @ResponseBody ResponseEntity<Object> getOnDemandClassReputation(
    		@Valid  @RequestBody SubReputationBatchSearch message,
    		HttpServletRequest req) {
	    	 
	
			HttpHeaders headers = new HttpHeaders();
			List<ClassReputationResponse> res = new LinkedList<>();
	    	try{
	    		 List<ClassReputationResponse> ret =  reputationQuery.getSubReputationBatchSearch(message.getEntities(),message.getAttributes());
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
