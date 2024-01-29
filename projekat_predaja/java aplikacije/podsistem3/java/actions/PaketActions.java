package actions;

import entities.Paket;
import java.util.List;
import javax.persistence.EntityManager;
import messages.JMSResponse;
import podsistem3.Podsistem3Main;
import util.XMLConverter;

public class PaketActions {

    public static JMSResponse kreirajNoviPaket(String naziv, String mesecnaCena) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            if (!em.createNamedQuery("Paket.findByNaziv", Paket.class).
                    setParameter("naziv", naziv).getResultList().isEmpty()) {
                System.out.println("Korisnik sa tim email-om vec postoji");
                response = new JMSResponse(false, "Paket sa tim imenom vec postoji: " + naziv, 409);
                return response;
            }
            double cena = Double.parseDouble(mesecnaCena);
            em.getTransaction().begin();
            Paket noviPaket = new Paket();
            noviPaket.setNaziv(naziv);
            noviPaket.setCena(cena);
            em.persist(noviPaket);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Paket napravljen:\n" + XMLConverter.convertToXML(noviPaket), 200);
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

    public static JMSResponse promeniCenuPaketa(String naziv, String novaCena) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Paket> paketi = em.createNamedQuery("Paket.findByNaziv", Paket.class).
                    setParameter("naziv", naziv).getResultList();
            if (paketi.isEmpty()) {
                return new JMSResponse(false, "Paket sa tim imenom ne postoji", 409);
            }
            Paket paket = paketi.get(0);
            double cena = Double.parseDouble(novaCena);
            em.getTransaction().begin();
            paket.setCena(cena);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Cena paketa promenjena:\n" + XMLConverter.convertToXML(paket), 200);
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

    public static JMSResponse dohvatiSvePakete() {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Paket> paketi = em.createNamedQuery("Paket.findAll", Paket.class).getResultList();

            StringBuilder sb = new StringBuilder();
            for (Paket p : paketi) {
                sb.append(XMLConverter.convertToXML(p)).append("\n");
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

}
