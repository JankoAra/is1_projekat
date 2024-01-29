package actions;

import entities.Korisnik;
import entities.Ocenjuje;
import entities.OcenjujePK;
import entities.Videosnimak;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import messages.JMSResponse;
import podsistem3.Podsistem3Main;
import util.XMLConverter;

public class OcenjujeActions {

    public static JMSResponse kreirajNovuOcenu(String idVid, String email, String ocena) {
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
                    setParameter("email", email).getResultList();
            if (korisnici.isEmpty()) {
                return new JMSResponse(false, "Korisnik ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            Korisnik korisnik = korisnici.get(0);
            OcenjujePK pk = new OcenjujePK(video.getIdVid(), korisnik.getIdKor());
            List<Ocenjuje> ocene = em.createQuery("select o from Ocenjuje o where o.korisnik.idKor=:idKor and o.videosnimak.idVid=:idVid", Ocenjuje.class).
                    setParameter("idKor", pk.getIdKor()).setParameter("idVid", pk.getIdVid()).getResultList();
            if (!ocene.isEmpty()) {
                return new JMSResponse(false, "Korisnik je vec ocenio ovaj video", 409);
            }
            int ocenaInt = Integer.parseInt(ocena);
            if(ocenaInt<1 || ocenaInt>5){
                return new JMSResponse(false, "Ocena mora biti u opsegu 1-5", 409);
            }
            Date datumVreme = new Date();
            em.getTransaction().begin();
            Ocenjuje novaOcena = new Ocenjuje(pk, datumVreme, ocenaInt);
            em.persist(novaOcena);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Ocena kreirana: " + XMLConverter.convertToXML(novaOcena), 200);
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

    public static JMSResponse promeniOcenu(String idVid, String email, String ocena) {
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
                    setParameter("email", email).getResultList();
            if (korisnici.isEmpty()) {
                return new JMSResponse(false, "Korisnik ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            Korisnik korisnik = korisnici.get(0);
            OcenjujePK pk = new OcenjujePK(video.getIdVid(), korisnik.getIdKor());
            List<Ocenjuje> ocene = em.createQuery("select o from Ocenjuje o where o.korisnik.idKor=:idKor and o.videosnimak.idVid=:idVid", Ocenjuje.class).
                    setParameter("idKor", pk.getIdKor()).setParameter("idVid", pk.getIdVid()).getResultList();
            if (ocene.isEmpty()) {
                return new JMSResponse(false, "Korisnik je nije ocenio ovaj video", 409);
            }
            Ocenjuje ocenaMenjanje = ocene.get(0);
            int ocenaInt = Integer.parseInt(ocena);
            Date datumVreme = new Date();
            em.getTransaction().begin();
            ocenaMenjanje.setDatumVreme(datumVreme);
            ocenaMenjanje.setOcena(ocenaInt);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Ocena promenjena: " + XMLConverter.convertToXML(ocenaMenjanje), 200);
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

    public static JMSResponse obrisiOcenu(String idVid, String email) {
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
                    setParameter("email", email).getResultList();
            if (korisnici.isEmpty()) {
                return new JMSResponse(false, "Korisnik ne postoji", 409);
            }
            Videosnimak video = videi.get(0);
            Korisnik korisnik = korisnici.get(0);
            OcenjujePK pk = new OcenjujePK(video.getIdVid(), korisnik.getIdKor());
            List<Ocenjuje> ocene = em.createQuery("select o from Ocenjuje o where o.korisnik.idKor=:idKor and o.videosnimak.idVid=:idVid", Ocenjuje.class).
                    setParameter("idKor", pk.getIdKor()).setParameter("idVid", pk.getIdVid()).getResultList();
            if (ocene.isEmpty()) {
                return new JMSResponse(false, "Korisnik je nije ocenio ovaj video", 409);
            }
            Ocenjuje ocenaBrisanje = ocene.get(0);
            em.getTransaction().begin();
            em.remove(ocenaBrisanje);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Ocena obrisana", 200);
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
