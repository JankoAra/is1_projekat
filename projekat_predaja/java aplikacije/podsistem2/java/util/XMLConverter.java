package util;

import entities.Kategorija;
import entities.Korisnik;
import entities.Videosnimak;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XMLConverter {

    private static JAXBContext context;

    static {
        try {
            context = JAXBContext.newInstance(Korisnik.class, Kategorija.class,
                    Videosnimak.class);
        } catch (JAXBException ex) {
            Logger.getLogger(XMLConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String convertToXML(Object entity) {
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(entity, writer);
            return writer.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(XMLConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
