package app;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.xml.bind.JAXBException;

import nl.marcenschede.dozerdemo.melding.v1_0.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private MeldingRepository meldingRepository;
    
    @Autowired
    private BerichtVerzender berichtVerzender;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/triggerMessage")
    @ResponseBody
    public Melding greeting()
            throws JAXBException {

        nl.marcenschede.dozerdemo.melding.v1_0.Melding melding = new nl.marcenschede.dozerdemo.melding.v1_0.Melding();
        melding.setResult(Result.OVERDRACHT_OK);

        berichtVerzender.verzendBericht(melding);

        Melding appMelding = new Melding();
        appMelding.setTimestamp(new Date(System.currentTimeMillis()));
        appMelding.setResult("Verzonden bericht: " + melding.getResult());
        appMelding.setHostprocessed(getHostname());
        meldingRepository.save(appMelding);
        
        return appMelding;
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Hostname not retrieved";
        }
    }

    @RequestMapping("/env")
    public String env()
            throws JAXBException {

        StringBuilder sb = new StringBuilder();
        for (String item : applicationContext.getBeanDefinitionNames())
            sb.append(item + "<br>");
        return sb.toString();
    }

    @RequestMapping("/meldingen")
    public Iterable<Melding> getMeldingen(){
        return meldingRepository.findAll();
    }

}
