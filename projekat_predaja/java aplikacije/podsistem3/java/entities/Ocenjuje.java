package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "ocenjuje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocenjuje.findAll", query = "SELECT o FROM Ocenjuje o"),
    @NamedQuery(name = "Ocenjuje.findByDatumVreme", query = "SELECT o FROM Ocenjuje o WHERE o.datumVreme = :datumVreme"),
    @NamedQuery(name = "Ocenjuje.findByOcena", query = "SELECT o FROM Ocenjuje o WHERE o.ocena = :ocena"),
    @NamedQuery(name = "Ocenjuje.findByIdVid", query = "SELECT o FROM Ocenjuje o WHERE o.ocenjujePK.idVid = :idVid"),
    @NamedQuery(name = "Ocenjuje.findByIdKor", query = "SELECT o FROM Ocenjuje o WHERE o.ocenjujePK.idKor = :idKor")})
public class Ocenjuje implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OcenjujePK ocenjujePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ocena")
    private int ocena;
    @JoinColumn(name = "idKor", referencedColumnName = "idKor", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Korisnik korisnik;
    @JoinColumn(name = "idVid", referencedColumnName = "idVid", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Videosnimak videosnimak;

    public Ocenjuje() {
    }

    public Ocenjuje(OcenjujePK ocenjujePK) {
        this.ocenjujePK = ocenjujePK;
    }

    public Ocenjuje(OcenjujePK ocenjujePK, Date datumVreme, int ocena) {
        this.ocenjujePK = ocenjujePK;
        this.datumVreme = datumVreme;
        this.ocena = ocena;
    }

    public Ocenjuje(int idVid, int idKor) {
        this.ocenjujePK = new OcenjujePK(idVid, idKor);
    }

    @XmlTransient
    public OcenjujePK getOcenjujePK() {
        return ocenjujePK;
    }

    public void setOcenjujePK(OcenjujePK ocenjujePK) {
        this.ocenjujePK = ocenjujePK;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public int getOcena() {
        return ocena;
    }

    public void setOcena(int ocena) {
        this.ocena = ocena;
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
        hash += (ocenjujePK != null ? ocenjujePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocenjuje)) {
            return false;
        }
        Ocenjuje other = (Ocenjuje) object;
        if ((this.ocenjujePK == null && other.ocenjujePK != null) || (this.ocenjujePK != null && !this.ocenjujePK.equals(other.ocenjujePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Ocenjuje[ ocenjujePK=" + ocenjujePK + " ]";
    }

}
