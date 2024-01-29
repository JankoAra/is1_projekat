package actions;

import entities.Gledanje;
import entities.Korisnik;
import entities.Videosnimak;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import messages.JMSResponse;
import podsistem3.Podsistem3Main;
import util.XMLConverter;

public class GledanjeActions {
    
    public static JMSResponse kreirajGledanje(String idVid, String email, String sekundPocetka, String traj) {
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
            int trajanje = Integer.parseInt(traj);
            int pocetak = Integer.parseInt(sekundPocetka);
            Date datumVreme = new Date();
            em.getTransaction().begin();
            Gledanje novoGledanje = new Gledanje();
            novoGledanje.setKorisnik(korisnik);
            novoGledanje.setVideosnimak(video);
            novoGledanje.setDatumVremePocetka(datumVreme);
            novoGledanje.setTrajanjeSekundi(trajanje);
            novoGledanje.setSekundPocetka(pocetak);
            em.persist(novoGledanje);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Gledanje kreirano: " + XMLConverter.convertToXML(novoGledanje), 200);
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
