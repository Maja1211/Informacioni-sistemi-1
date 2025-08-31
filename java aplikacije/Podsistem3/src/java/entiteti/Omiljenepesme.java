/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Win 11
 */
@Entity
@Table(name = "omiljenepesme")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Omiljenepesme.findAll", query = "SELECT o FROM Omiljenepesme o"),
    @NamedQuery(name = "Omiljenepesme.findByIdAudio", query = "SELECT o FROM Omiljenepesme o WHERE o.omiljenepesmePK.idAudio = :idAudio"),
    @NamedQuery(name = "Omiljenepesme.findByIdKorisnik", query = "SELECT o FROM Omiljenepesme o WHERE o.omiljenepesmePK.idKorisnik = :idKorisnik")})
public class Omiljenepesme implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OmiljenepesmePK omiljenepesmePK;

    public Omiljenepesme() {
    }

    public Omiljenepesme(OmiljenepesmePK omiljenepesmePK) {
        this.omiljenepesmePK = omiljenepesmePK;
    }

    public Omiljenepesme(int idAudio, int idKorisnik) {
        this.omiljenepesmePK = new OmiljenepesmePK(idAudio, idKorisnik);
    }

    public OmiljenepesmePK getOmiljenepesmePK() {
        return omiljenepesmePK;
    }

    public void setOmiljenepesmePK(OmiljenepesmePK omiljenepesmePK) {
        this.omiljenepesmePK = omiljenepesmePK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (omiljenepesmePK != null ? omiljenepesmePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Omiljenepesme)) {
            return false;
        }
        Omiljenepesme other = (Omiljenepesme) object;
        if ((this.omiljenepesmePK == null && other.omiljenepesmePK != null) || (this.omiljenepesmePK != null && !this.omiljenepesmePK.equals(other.omiljenepesmePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Omiljenepesme[ omiljenepesmePK=" + omiljenepesmePK + " ]";
    }
    
}
