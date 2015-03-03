package app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Test repository
 */

//https://github.com/spring-projects/spring-data-rest/wiki/Configuring-the-REST-URL-path
@RestResource(exported = false)
public interface MeldingRepository extends CrudRepository<Melding, Long> {

}
