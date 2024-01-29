package actions;

import entities.Korisnik;
import entities.Paket;
import entities.Pretplata;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import messages.JMSResponse;
import podsistem3.Podsistem3Main;
import util.XMLConverter;

public class PretplataActions {

    public static JMSResponse kreirajPretplatu(String email, String nazivPaketa) {
        EntityManager em = Podsistem3Main.getEmf().createEntityManager();
        JMSResponse response = null;
        try {
            List<Paket> paketi = em.createNamedQuery("Paket.findByNaziv", Paket.class).
                    setParameter("naziv", nazivPaketa).getResultList();
            if (paketi.isEmpty()) {
                return new JMSResponse(false, "Paket sa tim imenom ne postoji", 409);
            }
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findByEmail", Korisnik.class).
                    setParameter("email", email).getResultList();
            if (korisnici.isEmpty()) {
                return new JMSResponse(false, "Korisnik ne postoji", 409);
            }
            Paket paket = paketi.get(0);
            Korisnik korisnik = korisnici.get(0);
            em.refresh(paket);
            em.refresh(korisnik);
            List<Pretplata> pretplateKorisnika = korisnik.getPretplataList();
            Pretplata poslednjaPretplata = null;
            Date datumVreme = new Date();
            for (Pretplata p : pretplateKorisnika) {
                if (poslednjaPretplata == null) {
                    poslednjaPretplata = p;
                } else if (p.getDatumVremePocetka().after(poslednjaPretplata.getDatumVremePocetka())) {
                    poslednjaPretplata = p;
                }
            }

            if (poslednjaPretplata != null) {
                System.out.println(poslednjaPretplata.toString());
                System.out.println(poslednjaPretplata.getDatumVremePocetka().toString());
                long thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000;
                Date pretplataVaziDo = new Date(poslednjaPretplata.getDatumVremePocetka().getTime() + thirtyDaysMillis);
                System.out.println(pretplataVaziDo.toString());
                if (pretplataVaziDo.after(datumVreme)) {
                    return new JMSResponse(false, "Korisnik vec ima vazecu pretplatu", 409);
                }
            }
            em.getTransaction().begin();
            Pretplata novaPretplata = new Pretplata();
            novaPretplata.setKorisnik(korisnik);
            novaPretplata.setPaket(paket);
            novaPretplata.setCenaPlacena(paket.getCena());
            novaPretplata.setDatumVremePocetka(datumVreme);
            em.persist(novaPretplata);
            em.getTransaction().commit();
            response = new JMSResponse(true, "Pretplata kreirana:\n" + XMLConverter.convertToXML(novaPretplata), 200);
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
