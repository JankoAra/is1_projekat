package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "videosnimak")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Videosnimak.findAll", query = "SELECT v FROM Videosnimak v"),
    @NamedQuery(name = "Videosnimak.findByIdVid", query = "SELECT v FROM Videosnimak v WHERE v.idVid = :idVid"),
    @NamedQuery(name = "Videosnimak.findByDatumVremePostavljanja", query = "SELECT v FROM Videosnimak v WHERE v.datumVremePostavljanja = :datumVremePostavljanja"),
    @NamedQuery(name = "Videosnimak.findByNaziv", query = "SELECT v FROM Videosnimak v WHERE v.naziv = :naziv"),
    @NamedQuery(name = "Videosnimak.findByTrajanje", query = "SELECT v FROM Videosnimak v WHERE v.trajanje = :trajanje")})
public class Videosnimak implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idVid")
    private Integer idVid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datumVremePostavljanja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVremePostavljanja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trajanje")
    private int trajanje;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "videosnimak")
    private List<Ocenjuje> ocenjujeList;
    @JoinColumn(name = "vlasnik", referencedColumnName = "idKor")
    @ManyToOne(optional = false)
    private Korisnik vlasnik;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "videosnimak")
    private List<Gledanje> gledanjeList;

    public Videosnimak() {
    }

    public Videosnimak(Integer idVid) {
        this.idVid = idVid;
    }

    public Videosnimak(Integer idVid, Date datumVremePostavljanja, String naziv, int trajanje) {
        this.idVid = idVid;
        this.datumVremePostavljanja = datumVremePostavljanja;
        this.naziv = naziv;
        this.trajanje = trajanje;
    }

    public Integer getIdVid() {
        return idVid;
    }

    public void setIdVid(Integer idVid) {
        this.idVid = idVid;
    }

    public Date getDatumVremePostavljanja() {
        return datumVremePostavljanja;
    }

    public void setDatumVremePostavljanja(Date datumVremePostavljanja) {
        this.datumVremePostavljanja = datumVremePostavljanja;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(int trajanje) {
        this.trajanje = trajanje;
    }

    @XmlTransient
    public List<Ocenjuje> getOcenjujeList() {
        return ocenjujeList;
    }

    public void setOcenjujeList(List<Ocenjuje> ocenjujeList) {
        this.ocenjujeList = ocenjujeList;
    }

    public Korisnik getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(Korisnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    @XmlTransient
    public List<Gledanje> getGledanjeList() {
        return gledanjeList;
    }

    public void setGledanjeList(List<Gledanje> gledanjeList) {
        this.gledanjeList = gledanjeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVid != null ? idVid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Videosnimak)) {
            return false;
        }
        Videosnimak other = (Videosnimak) object;
        if ((this.idVid == null && other.idVid != null) || (this.idVid != null && !this.idVid.equals(other.idVid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Videosnimak[ idVid=" + idVid + " ]";
    }

}
