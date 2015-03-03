package popularioty.api.start;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages={
		"popularioty.api.configuration"
		})
@EnableAutoConfiguration
public class ReputationApp {

    public static void main(String[] args) {
        SpringApplication.run(ReputationApp.class, args);
    }
}
