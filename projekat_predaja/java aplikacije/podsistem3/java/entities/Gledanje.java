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
@Table(name = "gledanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gledanje.findAll", query = "SELECT g FROM Gledanje g"),
    @NamedQuery(name = "Gledanje.findByIdGle", query = "SELECT g FROM Gledanje g WHERE g.idGle = :idGle"),
    @NamedQuery(name = "Gledanje.findByDatumVremePocetka", query = "SELECT g FROM Gledanje g WHERE g.datumVremePocetka = :datumVremePocetka"),
    @NamedQuery(name = "Gledanje.findBySekundPocetka", query = "SELECT g FROM Gledanje g WHERE g.sekundPocetka = :sekundPocetka"),
    @NamedQuery(name = "Gledanje.findByTrajanjeSekundi", query = "SELECT g FROM Gledanje g WHERE g.trajanjeSekundi = :trajanjeSekundi")})
public class Gledanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idGle")
    private Integer idGle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datumVremePocetka")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVremePocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sekundPocetka")
    private int sekundPocetka;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trajanjeSekundi")
    private int trajanjeSekundi;
    @JoinColumn(name = "korisnik", referencedColumnName = "idKor")
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "videosnimak", referencedColumnName = "idVid")
    @ManyToOne(optional = false)
    private Videosnimak videosnimak;

    public Gledanje() {
    }

    public Gledanje(Integer idGle) {
        this.idGle = idGle;
    }

    public Gledanje(Integer idGle, Date datumVremePocetka, int sekundPocetka, int trajanjeSekundi) {
        this.idGle = idGle;
        this.datumVremePocetka = datumVremePocetka;
        this.sekundPocetka = sekundPocetka;
        this.trajanjeSekundi = trajanjeSekundi;
    }

    public Integer getIdGle() {
        return idGle;
    }

    public void setIdGle(Integer idGle) {
        this.idGle = idGle;
    }

    public Date getDatumVremePocetka() {
        return datumVremePocetka;
    }

    public void setDatumVremePocetka(Date datumVremePocetka) {
        this.datumVremePocetka = datumVremePocetka;
    }

    public int getSekundPocetka() {
        return sekundPocetka;
    }

    public void setSekundPocetka(int sekundPocetka) {
        this.sekundPocetka = sekundPocetka;
    }

    public int getTrajanjeSekundi() {
        return trajanjeSekundi;
    }

    public void setTrajanjeSekundi(int trajanjeSekundi) {
        this.trajanjeSekundi = trajanjeSekundi;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    public Videosnimak getVideosnimak() {
        return videosnimak;
    }

    public void setVideosnimak(Videosnimak videosnimak) {
        this.videosnimak = videosnimak;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGle != null ? idGle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gledanje)) {
            return false;
        }
        Gledanje other = (Gledanje) object;
        if ((this.idGle == null && other.idGle != null) || (this.idGle != null && !this.idGle.equals(other.idGle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Gledanje[ idGle=" + idGle + " ]";
    }

}
