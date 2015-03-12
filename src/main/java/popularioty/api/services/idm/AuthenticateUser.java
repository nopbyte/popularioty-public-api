package popularioty.api.services.idm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import popularioty.commons.exception.PopulariotyException;
import popularioty.commons.exception.PopulariotyException.Level;

@Service
public class AuthenticateUser {

	private static Logger LOG = LoggerFactory.getLogger(AuthenticateUser .class);	
	private String idm_host = null;
	private String auth_user_uri = null;
	public AuthenticateUser()
	{
		 Properties properties = new Properties();
	        ClassPathResource resource = new ClassPathResource("idm.properties");
	        try {
	            properties.load(resource.getInputStream());
	            idm_host = properties.getProperty("idm.host");
	            auth_user_uri = properties.getProperty("uri.auth");
	        	
	        } catch (IOException e) {
	        	
	            LOG.error("Error loading properties file to connect to IDM- IOException.. " + PopulariotyException.getStackTrace(e.getCause()));
	        } catch (Exception e) {
	        	
	        	LOG.error("Error loading properties file to connect to IDM- Exception.. " +PopulariotyException.getStackTrace(e.getCause()));
			}
	}
	public Map<String, Object> attributesFromUser(String token) throws PopulariotyException
	{
		
		String url = idm_host+auth_user_uri;
		
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
					throw new PopulariotyException("Authentication failed, wrong credentials ",clientError,LOG,"Unauthorized while attempting to get user's attributes from idm from token to "+url,Level.ERROR,401);
			throw new PopulariotyException("An error ocurred during HTTP communication with IDM. Contact the administrator",clientError,LOG,"HttClientError  while attempting to get user's attributes from IDM.  message to "+url,Level.ERROR,500);
		}
		catch(RestClientException restE)
		{
			throw new PopulariotyException("An error ocurred during HTTP communication with IDM. Contact the administrator",restE,LOG,"RestException  while attempting to get user's attributes from IDM.  message to "+url,Level.ERROR,500);
		}
		catch(Exception e)
		{
			throw new PopulariotyException("An error ocurred during HTTP communication with IDM. Contact the administrator",e,LOG,"Unknown exception while attempting to get user's attributes from IDM.  message to "+url ,Level.ERROR,500);	
		}
		
	}
	

}
