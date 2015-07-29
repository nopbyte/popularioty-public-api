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
public class FindEntityAttributes {

	private static Logger LOG = LoggerFactory.getLogger(FindEntityAttributes .class);	
	private String idm_host = null;
	private String entity_uri_preffix = null;
	public FindEntityAttributes()
	{
		 Properties properties = new Properties();
	        ClassPathResource resource = new ClassPathResource("idm.properties");
	        try {
	            properties.load(resource.getInputStream());
	            idm_host = properties.getProperty("idm.host");
	            entity_uri_preffix = properties.getProperty("uri.entity");
	        	
	        } catch (IOException e) {
	        	
	            LOG.error("Error loading properties file to connect to IDM- IOException.. " + PopulariotyException.getStackTrace(e.getCause()));
	        } catch (Exception e) {
	        	
	        	LOG.error("Error loading properties file to connect to IDM- Exception.. " +PopulariotyException.getStackTrace(e.getCause()));
			}
	}
	public Map<String, Object> attributesFromEntity(String token, String id) throws PopulariotyException
	{
		
		String url = idm_host+entity_uri_preffix;
		
		try{
			ResponseEntity<HashMap> responseEntity = null;
			HttpHeaders headers = new HttpHeaders();
			RestTemplate restTemplate = new RestTemplate();
			headers.add("Accept","application/json;charset=utf-8");
			headers.add("Authorization",token);
			HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
			LOG.debug("Getting entity information from IDM");
			responseEntity= restTemplate.exchange(url+"/"+id+"/", HttpMethod.GET, httpEntity,HashMap.class);
			return  responseEntity.getBody();
			
		}catch(HttpClientErrorException clientError)
		{
			if(clientError.getStatusCode().equals(HttpStatus.NOT_FOUND))
				throw new PopulariotyException("Non existing entity for uri: "+url,clientError,LOG,"Non existent entity for URI: "+url,Level.DEBUG,404);
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
