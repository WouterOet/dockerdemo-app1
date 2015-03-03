package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = App.class)
@IntegrationComponentScan(basePackageClasses = App.class)
@EnableIntegration
@ImportResource("classpath:application-*.xml")
@EnableJpaRepositories                                // Create JPA Repositories
//@Import(RepositoryRestMvcAutoConfiguration.class)     // Expose REST API automatically
public class App {

    private static Class app = App.class;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(app, args);
    }

}
