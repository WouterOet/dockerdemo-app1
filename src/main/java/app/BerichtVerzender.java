package app;

import nl.marcenschede.dozerdemo.melding.v1_0.Melding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

import static dozerdemo.utils.MarshallUtils.mashalObjectToString;

/**
 * Created by marc on 15/01/15.
 */
@Component
public class BerichtVerzender {

    @Autowired
    private BerichtGateway berichtGateway;
    
    public void verzendBericht(Melding melding)
            throws JAXBException {

        berichtGateway.zendBericht(mashalObjectToString(melding));
    }

}
