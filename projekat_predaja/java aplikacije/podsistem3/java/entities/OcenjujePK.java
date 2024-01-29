package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class OcenjujePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idVid")
    private int idVid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idKor")
    private int idKor;

    public OcenjujePK() {
    }

    public OcenjujePK(int idVid, int idKor) {
        this.idVid = idVid;
        this.idKor = idKor;
    }

    public int getIdVid() {
        return idVid;
    }

    public void setIdVid(int idVid) {
        this.idVid = idVid;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idVid;
        hash += (int) idKor;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OcenjujePK)) {
            return false;
        }
        OcenjujePK other = (OcenjujePK) object;
        if (this.idVid != other.idVid) {
            return false;
        }
        if (this.idKor != other.idKor) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.OcenjujePK[ idVid=" + idVid + ", idKor=" + idKor + " ]";
    }

}
