package podsistem2;

import actions.KategorijaActions;
import actions.KorisnikActions;
import actions.VideoActions;
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

public class Podsistem2Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("podsistem2PU");
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
            consumer = context.createConsumer(resources.getRequestQueue(), "target='ps2'");
            producer = context.createProducer();
            producer.setTimeToLive(10000);
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
                    case 2:
                        //kreiranje novog korisnika
                        String imeKorisnika = req.getParameters().get("Ime");
                        String emailKorisnika = req.getParameters().get("Email");
                        String godisteKorisnika = req.getParameters().get("Godiste");
                        String polKorisnika = req.getParameters().get("Pol");
                        String idKor = req.getParameters().get("idKor");
                        response = KorisnikActions.napraviKorisnika(idKor, imeKorisnika, emailKorisnika, godisteKorisnika, polKorisnika);
                        break;
                    case 3:
                        //promena email adrese
                        String stariEmail = req.getParameters().get("Email korisnika");
                        String noviEmail = req.getParameters().get("Novi email korisnika");
                        response = KorisnikActions.promeniEmailKorisnika(stariEmail, noviEmail);
                        break;
                    case 5:
                        //kreiranje kategorije
                        String nazivNoveKategorije = req.getParameters().get("Naziv kategorije");
                        response = KategorijaActions.kreirajKategoriju(nazivNoveKategorije);
                        break;
                    case 6:
                        //kreiranje videa
                        String nazivNovogVidea= req.getParameters().get("Naziv video snimka");
                        String trajanje = req.getParameters().get("Trajanje");
                        String emailVlasnikaNovogSnimka = req.getParameters().get("Email korisnika");
                        response= VideoActions.kreirajVideo(nazivNovogVidea, trajanje, emailVlasnikaNovogSnimka);
                        break;
                    case 7:
                        //promena naziva videa
                        String idVidPromenaNaziva = req.getParameters().get("ID video snimka");
                        String noviNaziv=req.getParameters().get("Novi naziv video snimka");
                        response = VideoActions.promeniNazivVidea(idVidPromenaNaziva, noviNaziv);
                        break;
                    case 8:
                        //dodavanje kategorije videu
                        String idVidDodajKategoriju = req.getParameters().get("ID video snimka");
                        String nazivKategorijeDodavanje = req.getParameters().get("Naziv kategorije");
                        response = VideoActions.dodajKategorijuVideu(idVidDodajKategoriju, nazivKategorijeDodavanje);
                        break;
                    case 16:
                        //brisanje videa od autora
                        String idVidBrisanje = req.getParameters().get("ID video snimka");
                        String emailKorisnikaBrisanje = req.getParameters().get("Email korisnika");
                        response = VideoActions.obrisiVideo(idVidBrisanje, emailKorisnikaBrisanje);
                        break;
                    case 19:
                        //dohvatanje svih kategorija
                        response = KategorijaActions.dohvatiSveKategorije();
                        break;
                    case 20:
                        //dohvatanje svih videa
                        response = VideoActions.dohvatiSveVideoSnimke();
                        break;
                    case 21:
                        //dohvatanje kategorija za video
                        String idVidSveKategorije = req.getParameters().get("ID video snimka");
                        response = VideoActions.dohvatiKategorijeZaVideo(idVidSveKategorije);
                        break;
                    default:
                        response = new JMSResponse(false, "Nepostojeci zahtev za podsistem 2", 400);
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
