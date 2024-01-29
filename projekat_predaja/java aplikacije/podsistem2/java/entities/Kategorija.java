package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "kategorija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kategorija.findAll", query = "SELECT k FROM Kategorija k"),
    @NamedQuery(name = "Kategorija.findByIdKat", query = "SELECT k FROM Kategorija k WHERE k.idKat = :idKat"),
    @NamedQuery(name = "Kategorija.findByNaziv", query = "SELECT k FROM Kategorija k WHERE k.naziv = :naziv")})
public class Kategorija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idKat")
    private Integer idKat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "naziv")
    private String naziv;
    @JoinTable(name = "pripada", joinColumns = {
        @JoinColumn(name = "idKat", referencedColumnName = "idKat")}, inverseJoinColumns = {
        @JoinColumn(name = "idVid", referencedColumnName = "idVid")})
    @ManyToMany
    private List<Videosnimak> videosnimakList;

    public Kategorija() {
    }

    public Kategorija(Integer idKat) {
        this.idKat = idKat;
    }

    public Kategorija(Integer idKat, String naziv) {
        this.idKat = idKat;
        this.naziv = naziv;
    }

    public Integer getIdKat() {
        return idKat;
    }

    public void setIdKat(Integer idKat) {
        this.idKat = idKat;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @XmlTransient
    public List<Videosnimak> getVideosnimakList() {
        return videosnimakList;
    }

    public void setVideosnimakList(List<Videosnimak> videosnimakList) {
        this.videosnimakList = videosnimakList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKat != null ? idKat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kategorija)) {
            return false;
        }
        Kategorija other = (Kategorija) object;
        if ((this.idKat == null && other.idKat != null) || (this.idKat != null && !this.idKat.equals(other.idKat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Kategorija[ idKat=" + idKat + " ]";
    }

}
