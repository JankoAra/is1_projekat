package actions;

import entities.Kategorija;
import java.util.List;
import javax.persistence.EntityManager;
import messages.JMSResponse;
import podsistem2.Podsistem2Main;
import util.XMLConverter;

public class KategorijaActions {

    public static JMSResponse kreirajKategoriju(String naziv){
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            if (!em.createNamedQuery("Kategorija.findByNaziv", Kategorija.class).
                    setParameter("naziv", naziv).getResultList().isEmpty()) {
                System.out.println("Kategorija sa tim imenom vec postoji: " + naziv);
                response = new JMSResponse(false, "Kategorija sa tim imenom vec postoji: " + naziv, 409);
                return response;
            }
            Kategorija kat = new Kategorija();
            kat.setNaziv(naziv);
            em.getTransaction().begin();
            em.persist(kat);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Kategorija kreirana:\n" + XMLConverter.convertToXML(kat), 200);
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
    
    public static JMSResponse dohvatiSveKategorije(){
        EntityManager em = Podsistem2Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Kategorija> kategorije = em.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList();
            StringBuilder sb = new StringBuilder();
            for (Kategorija k : kategorije) {
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
