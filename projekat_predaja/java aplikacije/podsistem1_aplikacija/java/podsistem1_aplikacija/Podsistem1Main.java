package podsistem1_aplikacija;

import actions.KorisnikActions;
import actions.MestoActions;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import messages.JMSResources;
import messages.JMSResponse;
import requests.Request;

public class Podsistem1Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("podsistem1_aplikacijaPU");
    private static JMSResources resources = JMSResources.getInstance();

    public static EntityManagerFactory getEmf() {
        return emf;
    }

    public static JMSResources getResources() {
        return resources;
    }

    public static void main(String[] args) {
        JMSContext context = null;
        JMSProducer producer = null;
        JMSConsumer consumer = null;
        try {
            context = resources.getConnectionFactory().createContext();
            consumer = context.createConsumer(resources.getRequestQueue(), "target='ps1'");
            producer = context.createProducer();
            producer.setTimeToLive(5000);
            while (true) {
                //citanje poruke
                System.out.println("Waiting for requests:");
                Message message = consumer.receive();
                ObjectMessage msg = (ObjectMessage) message;
                System.out.println("Message received");
                Object obj = msg.getObject();
                if (!(obj instanceof Request)) {
                    System.out.println("Nije primljen request u poruci");
                    continue;
                }
                Request req = (Request) obj;

                //obrada poruke
                JMSResponse response = new JMSResponse(false, "Prazna poruka", 400);
                switch (req.getRequestNumber()) {
                    case 1:
                        //kreiranje novog mesta
                        String nazivNovogMesta = req.getParameters().get("Naziv grada");
                        response = MestoActions.napraviMesto(nazivNovogMesta);
                        break;
                    case 2:
                        //kreiranje novog korisnika
                        String imeKorisnika = req.getParameters().get("Ime");
                        String emailKorisnika = req.getParameters().get("Email");
                        String godisteKorisnika = req.getParameters().get("Godiste");
                        String polKorisnika = req.getParameters().get("Pol");
                        String imeMesta = req.getParameters().get("Naziv mesta");
                        response = KorisnikActions.napraviKorisnika(imeKorisnika, emailKorisnika,
                                godisteKorisnika, polKorisnika, imeMesta);
                        break;
                    case 3:
                        //promena email adrese
                        String stariEmail = req.getParameters().get("Email korisnika");
                        String noviEmail = req.getParameters().get("Novi email korisnika");
                        response = KorisnikActions.promeniEmailKorisnika(stariEmail, noviEmail);
                        break;
                    case 4:
                        //promena mesta korisnika
                        String emailKorisnikaZaMesto = req.getParameters().get("Email korisnika");
                        String nazivNovogMestaZaKorisnika = req.getParameters().get("Naziv novog mesta");
                        response = KorisnikActions.promeniMestoKorisnika(emailKorisnikaZaMesto, nazivNovogMestaZaKorisnika);
                        break;
                    case 17:
                        //dohvatanje svih mesta
                        response = MestoActions.dohvatiSvaMesta();
                        break;
                    case 18:
                        //dohvatanje svih korisnika
                        response = KorisnikActions.dohvatiSveKorisnike();
                        break;
                    default:
                        response = new JMSResponse(false, "Nepostojeci zahtev za podsistem 1", 400);
                        break;
                }

                //vracanje odgovora
                ObjectMessage objMsg = context.createObjectMessage(response);
                objMsg.setStringProperty("target", req.getOrigin());
                Destination dest = resources.getResponseQueue();
                System.out.println("Saljem odgovor");
                producer.send(dest, objMsg);
                System.out.println("Poslao odgovor");
            }
        } catch (JMSException ex) {
            ex.printStackTrace();
        } finally {
            if (consumer != null) {
                consumer.close();
            }
            if (context != null) {
                context.close();
            }
            if (emf != null) {
                emf.close();
            }
        }

    }

}
