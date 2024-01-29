package actions;

import entities.Kategorija;
import entities.Korisnik;
import entities.Videosnimak;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class VideoActions {

    private static JMSResources resources = JMSResources.getInstance();
    private static final int waitForResponse = 10000;

    public static JMSResponse kreirajVideo(String naziv, String traj, String emailVlasnika) {
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        JMSContext jmsContext = resources.getConnectionFactory().createContext();
        JMSConsumer consumer = jmsContext.createConsumer(resources.getResponseQueue(), "target='ps2'");
        try {
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", emailVlasnika).getResultList();
            if (korisnici.isEmpty()) {
                return new JMSResponse(false, "Korisnik ne postoji", 409);
            }
            int trajanje = Integer.parseInt(traj);
            Korisnik vlasnik = korisnici.get(0);
            em.getTransaction().begin();

            Videosnimak noviVideo = new Videosnimak();
            noviVideo.setNaziv(naziv);
            noviVideo.setTrajanje(trajanje);
            noviVideo.setVlasnik(vlasnik);
            Date date = new Date();
            System.out.println(date.toString());
            noviVideo.setDatumVremePostavljanja(date);
            String dateString = dateToString(date);
            System.out.println(date.toString());
            System.out.println(dateString);
            em.persist(noviVideo);
            em.flush();

            //slanje novog videa u ps3 da se i tamo napravi
            Request request = new Request();
            request.addParameter("Naziv video snimka", naziv);
            request.addParameter("Trajanje", traj);
            request.addParameter("Email korisnika", emailVlasnika);
            request.addParameter("idVid", noviVideo.getIdVid() + "");
            request.addParameter("Datum", dateString);
            request.setRequestNumber(6);
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
            response = new JMSResponse(true, "Video snimak napravljen:\n" + XMLConverter.convertToXML(noviVideo), 200);
            System.out.println(noviVideo.getDatumVremePostavljanja().toString());
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

    public static JMSResponse promeniNazivVidea(String idVid, String noviNaziv) {
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        JMSContext jmsContext = resources.getConnectionFactory().createContext();
        JMSConsumer consumer = jmsContext.createConsumer(resources.getResponseQueue(), "target='ps2'");
        try {
            int id = Integer.parseInt(idVid);
            List<Videosnimak> videi = em.createNamedQuery("Videosnimak.findByIdVid", Videosnimak.class).
                    setParameter("idVid", id).getResultList();
            if (videi.isEmpty()) {
                return new JMSResponse(false, "Video ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            em.getTransaction().begin();

            video.setNaziv(noviNaziv);
            em.flush();

            //slanje novog videa u ps3 da se i tamo napravi
            Request request = new Request();
            request.addParameter("ID video snimka", idVid);
            request.addParameter("Novi naziv video snimka", noviNaziv);
            request.setRequestNumber(7);
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
            response = new JMSResponse(true, "Naziv snimka promenjen:\n" + XMLConverter.convertToXML(video), 200);
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

    public static JMSResponse dodajKategorijuVideu(String idVid, String nazivKategorije) {
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            int id = Integer.parseInt(idVid);
            List<Videosnimak> videi = em.createNamedQuery("Videosnimak.findByIdVid", Videosnimak.class).
                    setParameter("idVid", id).getResultList();
            if (videi.isEmpty()) {
                return new JMSResponse(false, "Video ne postoji", 409);
            }
            List<Kategorija> kategorije = em.createNamedQuery("Kategorija.findByNaziv", Kategorija.class).
                    setParameter("naziv", nazivKategorije).getResultList();
            if (kategorije.isEmpty()) {
                return new JMSResponse(false, "Kategorija ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            Kategorija kat = kategorije.get(0);
            em.refresh(video);
            em.refresh(kat);
            if(video.getKategorijaList().contains(kat)){
                return new JMSResponse(false, "Kategorija je vec dodata snimku", 409);
            }

            em.getTransaction().begin();

            video.getKategorijaList().add(kat);
            kat.getVideosnimakList().add(video);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Kategorija dodata snimku:\n" + XMLConverter.convertToXML(video), 200);
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

    public static JMSResponse obrisiVideo(String idVid, String emailKorisnika) {
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        JMSContext jmsContext = resources.getConnectionFactory().createContext();
        JMSConsumer consumer = jmsContext.createConsumer(resources.getResponseQueue(), "target='ps2'");
        try {
            int id = Integer.parseInt(idVid);
            List<Videosnimak> videi = em.createNamedQuery("Videosnimak.findByIdVid", Videosnimak.class).
                    setParameter("idVid", id).getResultList();
            if (videi.isEmpty()) {
                return new JMSResponse(false, "Video ne postoji", 409);
            }
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", emailKorisnika).getResultList();
            if (korisnici.isEmpty()) {
                return new JMSResponse(false, "Korisnik ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            Korisnik korisnik = korisnici.get(0);
            if (!video.getVlasnik().getEmail().equals(emailKorisnika)) {
                return new JMSResponse(false, "Korisnik nije autor videa", 409);
            }
            em.getTransaction().begin();

            em.remove(video);
            em.flush();

            //slanje novog videa u ps3 da se i tamo napravi
            Request request = new Request();
            request.addParameter("ID video snimka", idVid);
            request.addParameter("Email korisnika", emailKorisnika);
            request.setRequestNumber(16);
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
            response = new JMSResponse(true, "Video snimak obrisan", 200);
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

    public static JMSResponse dohvatiSveVideoSnimke() {
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Videosnimak> videi = em.createNamedQuery("Videosnimak.findAll", Videosnimak.class).getResultList();
            StringBuilder sb = new StringBuilder();
            for (Videosnimak v : videi) {
                sb.append(XMLConverter.convertToXML(v)).append("\n");
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

    public static JMSResponse dohvatiKategorijeZaVideo(String idVid) {
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            int id = Integer.parseInt(idVid);
            List<Videosnimak> videi = em.createNamedQuery("Videosnimak.findByIdVid", Videosnimak.class).
                    setParameter("idVid", id).getResultList();
            if (videi.isEmpty()) {
                return new JMSResponse(false, "Video ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            StringBuilder sb = new StringBuilder();
            for (Kategorija k : video.getKategorijaList()) {
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
    
    private static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
        return formatter.format(date);
    }

//    private static Date stringToDate(String dateString) {
//        try {
//            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
//            return formatter.parse(dateString);
//        } catch (ParseException ex) {
//            Logger.getLogger(VideoActions.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
}
