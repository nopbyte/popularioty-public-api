package popularioty.api.configuration;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import popularioty.api.start.ReputationApp;

public class PopulariotyPublicAPIWebXml extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        //TODO changed return to application class
        return application.sources(ReputationApp.class);
    }

}