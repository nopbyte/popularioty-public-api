package popularioty.api.rest.client;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.exception.PopulariotyException.Level;


public class HTTPClient2<T> {

	private static Logger LOG = LoggerFactory.getLogger(HTTPClient2.class);	
    
	public static final String GET ="get";

	public static final String POST ="post";

	
	public ResponseEntity<T> getDataHTTPCall(String messageType, String url, MultiValueMap<String, String> postData,HttpHeaders headers, Class returnTypeImplementation) throws PopulariotyException
	{
		ResponseEntity<T> responseEntity = null;
		try
		{
			
			RestTemplate restTemplate = new RestTemplate();
		    if(messageType.equals(GET))
			{
				HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
				responseEntity= restTemplate.exchange(url, HttpMethod.GET, httpEntity,
		                returnTypeImplementation);
			}
			else if(messageType.equals(POST))
			{ 
				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
	                postData, headers);       
				responseEntity= restTemplate.exchange(url, HttpMethod.POST, request,
	        		returnTypeImplementation);
			}
		}
		catch(HttpClientErrorException clientError)
		{
			if(clientError.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
					throw new PopulariotyException("Authentication failed, wrong credentials ",clientError,LOG,"Unauthorized while attempting to get token to "+url+" with Headers: "+headers+" and postdata "+postData,Level.ERROR,401);
			throw new PopulariotyException("An error ocurred during HTTP communication",clientError,LOG,"HttClientError  while attempting "+messageType+" message to "+url+" with Headers: "+headers+" and postdata "+postData,Level.ERROR,500);
		}
		catch(RestClientException restE)
		{
			throw new PopulariotyException("An error ocurred during HTTP communication",restE,LOG,"RestException  while attempting "+messageType+" message to "+url+" with Headers: "+headers+" and postdata "+postData,Level.ERROR,500);
		}
		catch(Exception e)
		{
			throw new PopulariotyException("An error ocurred during HTTP communication",e,LOG,"Unknown exception while attempting "+messageType+" message to "+url+" with Headers: "+headers+" and postdata "+postData,Level.ERROR,500);	
		}
		return responseEntity;
		
	}

}
