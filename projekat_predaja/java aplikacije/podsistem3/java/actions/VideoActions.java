package actions;

import entities.Gledanje;
import entities.Korisnik;
import entities.Ocenjuje;
import entities.Videosnimak;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import messages.JMSResponse;
import podsistem3.Podsistem3Main;
import util.XMLConverter;

public class VideoActions {

    public static JMSResponse kreirajVideo(String idVid, String naziv, String traj, String datumString, String emailVlasnika) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", emailVlasnika).getResultList();
            if (korisnici.isEmpty()) {
                return new JMSResponse(false, "Korisnik ne postoji", 409);
            }
            int trajanje = Integer.parseInt(traj);
            Date datum = stringToDate(datumString);
            System.out.println(datumString);
            System.out.println(datum.toString());
            int id = Integer.parseInt(idVid);
            Korisnik vlasnik = korisnici.get(0);

            em.getTransaction().begin();
            Videosnimak noviVideo = new Videosnimak();
            noviVideo.setNaziv(naziv);
            noviVideo.setTrajanje(trajanje);
            noviVideo.setVlasnik(vlasnik);
            noviVideo.setIdVid(id);
            noviVideo.setDatumVremePostavljanja(datum);
            em.persist(noviVideo);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Video snimak napravljen:\n" + XMLConverter.convertToXML(noviVideo), 200);
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

    public static JMSResponse promeniNazivVidea(String idVid, String noviNaziv) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
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
            em.getTransaction().commit();
            response = new JMSResponse(true, "Naziv snimka promenjen:\n" + XMLConverter.convertToXML(video), 200);
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
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
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
            em.getTransaction().commit();
            response = new JMSResponse(true, "Video snimak obrisan", 200);
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

    public static JMSResponse dohvatiGledanjaZaSnimak(String idVid) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            int id = Integer.parseInt(idVid);
            List<Videosnimak> videi = em.createNamedQuery("Videosnimak.findByIdVid", Videosnimak.class).
                    setParameter("idVid", id).getResultList();
            if (videi.isEmpty()) {
                return new JMSResponse(false, "Video ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            em.refresh(video);
            StringBuilder sb = new StringBuilder();
            for (Gledanje g : video.getGledanjeList()) {
                sb.append(XMLConverter.convertToXML(g)).append("\n");
            }
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

    public static JMSResponse dohvatiSveOceneZaVideo(String idVid) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            int id = Integer.parseInt(idVid);
            List<Videosnimak> videi = em.createNamedQuery("Videosnimak.findByIdVid", Videosnimak.class).
                    setParameter("idVid", id).getResultList();
            if (videi.isEmpty()) {
                return new JMSResponse(false, "Video ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            em.refresh(video);
            StringBuilder sb = new StringBuilder();
            for (Ocenjuje o : video.getOcenjujeList()) {
                sb.append(XMLConverter.convertToXML(o)).append("\n");
            }
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

    private static Date stringToDate(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
            return formatter.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(VideoActions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
