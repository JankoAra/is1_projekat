package actions;

import entities.Korisnik;
import java.util.List;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import messages.JMSResources;
import messages.JMSResponse;
import podsistem2.Podsistem2Main;
import requests.Request;
import util.XMLConverter;

public class KorisnikActions {

    private static JMSResources resources = JMSResources.getInstance();
    
    private static final int waitForResponse = 10000;

    public static JMSResponse napraviKorisnika(String idKor, String ime, String email, String godiste, String pol) {
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        JMSContext jmsContext = resources.getConnectionFactory().createContext();
        JMSConsumer consumer = jmsContext.createConsumer(resources.getResponseQueue(), "target='ps2'");
        try {
            if (!em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", email).getResultList().isEmpty()) {
                System.out.println("Korisnik sa tim email-om vec postoji");
                response = new JMSResponse(false, "Korisnik sa tim email-om vec postoji: " + email, 409);
                return response;
            }
            int god = Integer.parseInt(godiste);
            int id = Integer.parseInt(idKor);
            em.getTransaction().begin();
            Korisnik noviKorisnik = new Korisnik();
            noviKorisnik.setIme(ime);
            noviKorisnik.setEmail(email);
            noviKorisnik.setGodiste(god);
            noviKorisnik.setPol(pol);
            noviKorisnik.setIdKor(id);
            em.persist(noviKorisnik);
            em.flush();

            //slanje novog korisnika u ps3 da se i tamo napravi
            Request request = new Request();
            request.addParameter("Ime", ime);
            request.addParameter("Email", email);
            request.addParameter("Godiste", godiste);
            request.addParameter("Pol", pol);
            request.addParameter("idKor", idKor);
            request.setRequestNumber(2);
            request.setOrigin("ps2");

            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(10000);
            ObjectMessage msg = jmsContext.createObjectMessage(request);
            msg.setStringProperty("target", "ps3");
            producer.send(resources.getRequestQueue(), msg);
            Message message = consumer.receive(waitForResponse);
            if (message == null) {
                throw new Exception("Podsistem 3 nije odgovorio na zahtev");
            }
            response = (JMSResponse) (((ObjectMessage) message).getObject());
            if (!response.isSuccess()) {
                throw new Exception("Neuspesna obrada zahteva u ps3");
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
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        JMSContext jmsContext = resources.getConnectionFactory().createContext();
        JMSConsumer consumer = jmsContext.createConsumer(resources.getResponseQueue(), "target='ps2'");
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
            request.setOrigin("ps2");

            JMSProducer producer = jmsContext.createProducer();
            producer.setTimeToLive(10000);
            ObjectMessage msg = jmsContext.createObjectMessage(request);
            msg.setStringProperty("target", "ps3");
            producer.send(resources.getRequestQueue(), msg);
            Message message = consumer.receive(waitForResponse);
            if (message == null) {
                throw new Exception("Podsistem 3 nije odgovorio na zahtev");
            }
            response = (JMSResponse) (((ObjectMessage) message).getObject());
            if (!response.isSuccess()) {
                throw new Exception("Neuspesna obrada zahteva u ps3");
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
}
