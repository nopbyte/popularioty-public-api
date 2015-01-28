package popularioty.api.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={"popularioty.api.services"})
public class CoreConfiguration
{
	
	//@Bean
	/*public UsersAuthzAndAuthClient getUAA()
	{
		return new UAAClient();
	}*/


}
