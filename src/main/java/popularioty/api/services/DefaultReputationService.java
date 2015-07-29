package popularioty.api.services;

import org.springframework.stereotype.Service;

@Service
public class DefaultReputationService
{

	public  int defaultReputationValueForEntity(String id)
	{
		return 4;
	}
}
