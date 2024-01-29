package actions;

import entities.Mesto;
import java.util.List;
import javax.persistence.EntityManager;
import messages.JMSResponse;
import podsistem1_aplikacija.Podsistem1Main;
import util.XMLConverter;

public class MestoActions {

    public static JMSResponse napraviMesto(String naziv) {
        EntityManager em = Podsistem1Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            if (!em.createNamedQuery("Mesto.findByNaziv", Mesto.class).
                    setParameter("naziv", naziv).getResultList().isEmpty()) {
                System.out.println("Mesto sa tim imenom vec postoji: " + naziv);
                response = new JMSResponse(false, "Mesto sa tim imenom vec postoji: " + naziv, 409);
                return response;
            }
            Mesto mesto = new Mesto();
            mesto.setNaziv(naziv);
            em.getTransaction().begin();
            em.persist(mesto);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Mesto napravljeno:\n" + XMLConverter.convertToXML(mesto), 200);
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

    public static JMSResponse dohvatiSvaMesta() {
        EntityManager em = Podsistem1Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Mesto> mesta = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
            StringBuilder sb = new StringBuilder();
            for (Mesto m : mesta) {
                sb.append(XMLConverter.convertToXML(m)).append("\n");
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
