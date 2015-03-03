package app;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;


@MessagingGateway(name = "berichtGateway", defaultRequestChannel = "BerichtUit")
public interface BerichtGateway {
    
    @Gateway(replyTimeout = 2, requestTimeout = 200, replyChannel = "BerichtUit")
    public void zendBericht(String melding);
    
}
