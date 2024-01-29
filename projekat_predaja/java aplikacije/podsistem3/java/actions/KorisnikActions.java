package actions;

import entities.Korisnik;
import entities.Pretplata;
import java.util.List;
import javax.persistence.EntityManager;
import messages.JMSResponse;
import podsistem3.Podsistem3Main;
import util.XMLConverter;

public class KorisnikActions {

    public static JMSResponse napraviKorisnika(String idKor, String ime, String email, String godiste, String pol) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
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
            em.getTransaction().commit();
            response = new JMSResponse(true, "Korisnik napravljen:\n" + XMLConverter.convertToXML(noviKorisnik), 200);
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

    public static JMSResponse promeniEmailKorisnika(String stariEmail, String noviEmail) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
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
            em.getTransaction().commit();
            response = new JMSResponse(true, "Email promenjen:\n" + XMLConverter.convertToXML(korisnik), 200);
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

    public static JMSResponse dohvatiPretplateZaKorisnika(String email) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", email).getResultList();
            if(korisnici.isEmpty()){
                return new JMSResponse(false, "Korisnik sa tim email-om ne postoji", 409);
            }
            Korisnik korisnik = korisnici.get(0);
            em.refresh(korisnik);
            StringBuilder sb = new StringBuilder();
            for(Pretplata p:korisnik.getPretplataList()){
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
