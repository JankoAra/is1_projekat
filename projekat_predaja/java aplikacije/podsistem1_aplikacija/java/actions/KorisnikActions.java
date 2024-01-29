package actions;

import entities.Korisnik;
import entities.Mesto;
import java.util.List;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import messages.JMSResources;
import messages.JMSResponse;
import podsistem1_aplikacija.Podsistem1Main;
import requests.Request;
import util.XMLConverter;

public class KorisnikActions {

    private static JMSResources resources = JMSResources.getInstance();
    private static final int waitForResponse = 25000;

    public static JMSResponse napraviKorisnika(String ime, String email, String godiste, String pol, String nazivMesta) {
        EntityManager em = Podsistem1Main.getEmf().createEntityManager();
        JMSResponse response = null;
        JMSContext jmsContext = resources.getConnectionFactory().createContext();
        JMSConsumer consumer = jmsContext.createConsumer(resources.getResponseQueue(), "target='ps1'");
        try {
            if (!em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", email).getResultList().isEmpty()) {
                System.out.println("Korisnik sa tim email-om vec postoji");
                response = new JMSResponse(false, "Korisnik sa tim email-om vec postoji: " + email, 409);
                return response;
            }
            int god = Integer.parseInt(godiste);
            List<Mesto> mesta = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", nazivMesta).getResultList();
            em.getTransaction().begin();
            Mesto mesto = null;
            if (mesta.isEmpty()) {
                mesto = new Mesto();
                mesto.setNaziv(nazivMesta);
                em.persist(mesto);
                em.flush();
            } else {
                mesto = mesta.get(0);
            }
            Korisnik noviKorisnik = new Korisnik();
            noviKorisnik.setIme(ime);
            noviKorisnik.setEmail(email);
            noviKorisnik.setGodiste(god);
            noviKorisnik.setPol(pol);
            noviKorisnik.setMesto(mesto);
            em.persist(noviKorisnik);
            em.flush();

            //slanje novog korisnika u ps2 da se i tamo napravi
            Request request = new Request();
            request.addParameter("Ime", ime);
            request.addParameter("Email", email);
            request.addParameter("Godiste", godiste);
            request.addParameter("Pol", pol);
            request.addParameter("idKor", noviKorisnik.getIdKor() + "");
            request.setRequestNumber(2);
            request.setOrigin("ps1");

            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(10000);
            ObjectMessage msg = jmsContext.createObjectMessage(request);
            msg.setStringProperty("target", "ps2");
            producer.send(resources.getRequestQueue(), msg);
            Message message = consumer.receive(waitForResponse);
            if (message == null) {
                throw new Exception("Podsistem 2 nije odgovorio na zahtev");
            }
            response = (JMSResponse) (((ObjectMessage) message).getObject());
            if (!response.isSuccess()) {
                throw new Exception("Neuspesna obrada zahteva u ps2");
            }
            em.getTransaction().commit();
            response = new JMSResponse(true, "Korisnik napravljen:\n" + XMLConverter.convertToXML(noviKorisnik), 200);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            response = new JMSResponse(false, ex.getMessage(), 500);
        } finally {
            if (consumer != null) {
                consumer.close();
            }
            if (jmsContext != null) {
                jmsContext.close();
            }
            em.close();
        }
        return response;
    }

    public static JMSResponse promeniEmailKorisnika(String stariEmail, String noviEmail) {
        EntityManager em = Podsistem1Main.getEmf().createEntityManager();
        JMSResponse response = null;
        JMSContext jmsContext = resources.getConnectionFactory().createContext();
        JMSConsumer consumer = jmsContext.createConsumer(resources.getResponseQueue(), "target='ps1'");
        try {
            if (em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", stariEmail).getResultList().isEmpty()) {
                System.out.println("Korisnik sa tim email-om ne postoji");
                response = new JMSResponse(false, "Korisnik sa tim email-om ne postoji: " + stariEmail, 409);
                return response;
            }
            if (!em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", noviEmail).getResultList().isEmpty()) {
                System.out.println("Korisnik sa tim email-om vec postoji");
                response = new JMSResponse(false, "Korisnik sa tim email-om vec postoji: " + noviEmail, 409);
                return response;
            }
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", stariEmail).getResultList();
            Korisnik korisnik = korisnici.get(0);

            em.getTransaction().begin();
            korisnik.setEmail(noviEmail);

            //slanje novog korisnika u ps2 da se i tamo napravi
            Request request = new Request();
            request.addParameter("Email korisnika", stariEmail);
            request.addParameter("Novi email korisnika", noviEmail);
            request.setRequestNumber(3);
            request.setOrigin("ps1");

            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(10000);
            ObjectMessage msg = jmsContext.createObjectMessage(request);
            msg.setStringProperty("target", "ps2");
            producer.send(resources.getRequestQueue(), msg);
            Message message = consumer.receive(waitForResponse);
            if (message == null) {
                throw new Exception("Podsistem 2 nije odgovorio na zahtev");
            }
            response = (JMSResponse) (((ObjectMessage) message).getObject());
            if (!response.isSuccess()) {
                throw new Exception("Neuspesna obrada zahteva u ps2");
            }
            em.getTransaction().commit();
            response = new JMSResponse(true, "Email promenjen:\n" + XMLConverter.convertToXML(korisnik), 200);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            response = new JMSResponse(false, ex.getMessage(), 500);
        } finally {
            if (consumer != null) {
                consumer.close();
            }
            if (jmsContext != null) {
                jmsContext.close();
            }
            em.close();
        }
        return response;
    }

    public static JMSResponse promeniMestoKorisnika(String email, String nazivNovogMesta) {
        EntityManager em = Podsistem1Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", email).getResultList();
            if (korisnici.isEmpty()) {
                response = new JMSResponse(false, "Korisnik sa tim email-om ne postoji: " + email, 409);
                return response;
            }
            Korisnik korisnik = korisnici.get(0);
            List<Mesto> mesta = em.createNamedQuery("Mesto.findByNaziv", Mesto.class).setParameter("naziv", nazivNovogMesta).getResultList();
            em.getTransaction().begin();
            Mesto mesto = null;
            if (mesta.isEmpty()) {
                mesto = new Mesto();
                mesto.setNaziv(nazivNovogMesta);
                em.persist(mesto);
                em.flush();
            } else {
                mesto = mesta.get(0);
            }
            korisnik.setMesto(mesto);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Mesto promenjeno:\n" + XMLConverter.convertToXML(korisnik), 200);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            response = new JMSResponse(false, ex.getMessage(), 500);
        } finally {
            em.close();
        }
        return response;
    }

    public static JMSResponse dohvatiSveKorisnike() {
        EntityManager em = Podsistem1Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList();
            StringBuilder sb = new StringBuilder();
            for (Korisnik k : korisnici) {
                sb.append(XMLConverter.convertToXML(k)).append("\n");
            }
            System.out.println(sb.toString());
            response = new JMSResponse(true, sb.toString(), 200);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            response = new JMSResponse(false, ex.getMessage(), 500);
        } finally {
            em.close();
        }
        return response;
    }
}
