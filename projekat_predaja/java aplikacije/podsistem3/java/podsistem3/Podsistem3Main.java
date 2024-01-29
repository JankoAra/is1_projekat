package podsistem3;

import actions.GledanjeActions;
import actions.KorisnikActions;
import actions.OcenjujeActions;
import actions.PaketActions;
import actions.PretplataActions;
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

public class Podsistem3Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("podsistem3PU");
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
            consumer = context.createConsumer(resources.getRequestQueue(), "target='ps3'");
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
                        //promena email adrese korisnika
                        String stariEmail = req.getParameters().get("Email korisnika");
                        String noviEmail = req.getParameters().get("Novi email korisnika");
                        response = KorisnikActions.promeniEmailKorisnika(stariEmail, noviEmail);
                        break;
                    case 6:
                        //kreiranje videa
                        String nazivNovogVidea = req.getParameters().get("Naziv video snimka");
                        String trajanje = req.getParameters().get("Trajanje");
                        String emailVlasnikaNovogSnimka = req.getParameters().get("Email korisnika");
                        String idVidKreiranje = req.getParameters().get("idVid");
                        String datumKreiranjeVidea = req.getParameters().get("Datum");
                        response = VideoActions.kreirajVideo(idVidKreiranje, nazivNovogVidea, trajanje, datumKreiranjeVidea, emailVlasnikaNovogSnimka);
                        break;
                    case 7:
                        //promena naziva videa
                        String idVidPromenaNaziva = req.getParameters().get("ID video snimka");
                        String noviNaziv = req.getParameters().get("Novi naziv video snimka");
                        response = VideoActions.promeniNazivVidea(idVidPromenaNaziva, noviNaziv);
                        break;
                    case 9:
                        //kreiranje paketa
                        String nazivNovogPaketa = req.getParameters().get("Naziv paketa");
                        String mesecnaCenaNovogPaketa = req.getParameters().get("Mesecna cena");
                        response = PaketActions.kreirajNoviPaket(nazivNovogPaketa, mesecnaCenaNovogPaketa);
                        break;
                    case 10:
                        //promena cene paketa
                        String nazivPaketaPromenaCene = req.getParameters().get("Naziv paketa");
                        String novaCenaPaketa = req.getParameters().get("Nova mesecna cena");
                        response = PaketActions.promeniCenuPaketa(nazivPaketaPromenaCene, novaCenaPaketa);
                        break;
                    case 11:
                        //kreiranje pretplate na paket
                        String emailKorisnikaKreiranjePretplate = req.getParameters().get("Email korisnika");
                        String nazivPaketaKreiranjePretplate = req.getParameters().get("Naziv paketa");
                        response = PretplataActions.kreirajPretplatu(emailKorisnikaKreiranjePretplate, nazivPaketaKreiranjePretplate);
                        break;
                    case 12:
                        //kreiranje gledanja
                        String idVidNovoGledanje = req.getParameters().get("ID video snimka");
                        String emailNovoGledanje = req.getParameters().get("Email korisnika");
                        String sekundPocetkaGledanja = req.getParameters().get("Sekund snimka pocetka gledanja");
                        String trajanjeGledanja = req.getParameters().get("Trajanje gledanja u sekundama");
                        response = GledanjeActions.kreirajGledanje(idVidNovoGledanje, emailNovoGledanje, sekundPocetkaGledanja, trajanjeGledanja);
                        break;
                    case 13:
                        //kreiranje ocene
                        String idVidNovaOcena = req.getParameters().get("ID video snimka");
                        String emailNovaOcena = req.getParameters().get("Email korisnika");
                        String ocenaNovaOcena = req.getParameters().get("Ocena");
                        response = OcenjujeActions.kreirajNovuOcenu(idVidNovaOcena, emailNovaOcena, ocenaNovaOcena);
                        break;
                    case 14:
                        //menjanje ocene
                        String idVidMenjanjeOcene = req.getParameters().get("ID video snimka");
                        String emailMenjanjeOcene = req.getParameters().get("Email korisnika");
                        String ocenaMenjanjeOcene = req.getParameters().get("Ocena");
                        response = OcenjujeActions.promeniOcenu(idVidMenjanjeOcene, emailMenjanjeOcene, ocenaMenjanjeOcene);
                        break;
                    case 15:
                        //brisanje ocene
                        String idVidBrisanjeOcene = req.getParameters().get("ID video snimka");
                        String emailBrisanjeOcene = req.getParameters().get("Email korisnika");
                        response = OcenjujeActions.obrisiOcenu(idVidBrisanjeOcene, emailBrisanjeOcene);
                        break;
                    case 16:
                        //brisanje videa od autora
                        String idVidBrisanje = req.getParameters().get("ID video snimka");
                        String emailKorisnikaBrisanje = req.getParameters().get("Email korisnika");
                        response = VideoActions.obrisiVideo(idVidBrisanje, emailKorisnikaBrisanje);
                        break;
                    case 22:
                        //dohvatanje svih paketa
                        response = PaketActions.dohvatiSvePakete();
                        break;
                    case 23:
                        //dohvatanje svih pretplata za korisnika
                        String emailKorisnikaSvePretplate = req.getParameters().get("Email korisnika");
                        response = KorisnikActions.dohvatiPretplateZaKorisnika(emailKorisnikaSvePretplate);
                        break;
                    case 24:
                        //dohvatanje svih gledanja za snimak
                        String idVidSvaGledanja = req.getParameters().get("ID video snimka");
                        response = VideoActions.dohvatiGledanjaZaSnimak(idVidSvaGledanja);
                        break;
                    case 25:
                        //dohvatanje svih ocena za snimak
                        String idVidSveOcene = req.getParameters().get("ID video snimka");
                        response = VideoActions.dohvatiSveOceneZaVideo(idVidSveOcene);
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
