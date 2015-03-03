package popularioty.api.services.idm;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import popularioty.api.common.exception.ReputationAPIException;
import popularioty.api.common.exception.ReputationAPIException.Level;

@Service
public class AuthenticateUser {

	private static Logger LOG = LoggerFactory.getLogger(AuthenticateUser .class);	
	
	
	public Map<String, Object> attributesFromUser(String token) throws ReputationAPIException
	{
		String idmhost = "http://132.231.11.217:8080/";
		String url = idmhost+"idm/user/info/";
		
		try{
			ResponseEntity<HashMap> responseEntity = null;
			HttpHeaders headers = new HttpHeaders();
			RestTemplate restTemplate = new RestTemplate();
			headers.add("Accept","application/json;charset=utf-8");
			headers.add("Authorization",token);
			HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
			LOG.debug("Getting user information from IDM");
			responseEntity= restTemplate.exchange(url, HttpMethod.GET, httpEntity,HashMap.class);
			return  responseEntity.getBody();
			
		}catch(HttpClientErrorException clientError)
		{
			if(clientError.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
					throw new ReputationAPIException("Authentication failed, wrong credentials ",clientError,LOG,"Unauthorized while attempting to get user's attributes from idm from token to "+url,Level.ERROR,401);
			throw new ReputationAPIException("An error ocurred during HTTP communication with IDM. Contact the administrator",clientError,LOG,"HttClientError  while attempting to get user's attributes from IDM.  message to "+url,Level.ERROR,500);
		}
		catch(RestClientException restE)
		{
			throw new ReputationAPIException("An error ocurred during HTTP communication with IDM. Contact the administrator",restE,LOG,"RestException  while attempting to get user's attributes from IDM.  message to "+url,Level.ERROR,500);
		}
		catch(Exception e)
		{
			throw new ReputationAPIException("An error ocurred during HTTP communication with IDM. Contact the administrator",e,LOG,"Unknown exception while attempting to get user's attributes from IDM.  message to "+url ,Level.ERROR,500);	
		}
		
	}
	

}
