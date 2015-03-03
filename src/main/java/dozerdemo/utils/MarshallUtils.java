package dozerdemo.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by marc on 15/01/15.
 */
public class MarshallUtils {

    public static String mashalObjectToString(Object bericht)
            throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(bericht.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(bericht, outputStream);

        return outputStream.toString();
    }

    public static Object unmarshalStringToObject(String bericht, Class clazz)
            throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bericht.getBytes());
        return jaxbUnmarshaller.unmarshal(inputStream);
    }

}
