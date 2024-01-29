package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "pretplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pretplata.findAll", query = "SELECT p FROM Pretplata p"),
    @NamedQuery(name = "Pretplata.findByIdPre", query = "SELECT p FROM Pretplata p WHERE p.idPre = :idPre"),
    @NamedQuery(name = "Pretplata.findByCenaPlacena", query = "SELECT p FROM Pretplata p WHERE p.cenaPlacena = :cenaPlacena"),
    @NamedQuery(name = "Pretplata.findByDatumVremePocetka", query = "SELECT p FROM Pretplata p WHERE p.datumVremePocetka = :datumVremePocetka")})
public class Pretplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPre")
    private Integer idPre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cenaPlacena")
    private double cenaPlacena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datumVremePocetka")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVremePocetka;
    @JoinColumn(name = "korisnik", referencedColumnName = "idKor")
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "paket", referencedColumnName = "idPak")
    @ManyToOne(optional = false)
    private Paket paket;

    public Pretplata() {
    }

    public Pretplata(Integer idPre) {
        this.idPre = idPre;
    }

    public Pretplata(Integer idPre, double cenaPlacena, Date datumVremePocetka) {
        this.idPre = idPre;
        this.cenaPlacena = cenaPlacena;
        this.datumVremePocetka = datumVremePocetka;
    }

    public Integer getIdPre() {
        return idPre;
    }

    public void setIdPre(Integer idPre) {
        this.idPre = idPre;
    }

    public double getCenaPlacena() {
        return cenaPlacena;
    }

    public void setCenaPlacena(double cenaPlacena) {
        this.cenaPlacena = cenaPlacena;
    }

    public Date getDatumVremePocetka() {
        return datumVremePocetka;
    }

    public void setDatumVremePocetka(Date datumVremePocetka) {
        this.datumVremePocetka = datumVremePocetka;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Paket getPaket() {
        return paket;
    }

    public void setPaket(Paket paket) {
        this.paket = paket;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPre != null ? idPre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pretplata)) {
            return false;
        }
        Pretplata other = (Pretplata) object;
        if ((this.idPre == null && other.idPre != null) || (this.idPre != null && !this.idPre.equals(other.idPre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Pretplata[ idPre=" + idPre + " ]";
    }

}
