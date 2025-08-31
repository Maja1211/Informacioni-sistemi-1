/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Win 11
 */
@Embeddable
public class OmiljenepesmePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAudio")
    private int idAudio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKorisnik")
    private int idKorisnik;

    public OmiljenepesmePK() {
    }

    public OmiljenepesmePK(int idAudio, int idKorisnik) {
        this.idAudio = idAudio;
        this.idKorisnik = idKorisnik;
    }

    public int getIdAudio() {
        return idAudio;
    }

    public void setIdAudio(int idAudio) {
        this.idAudio = idAudio;
    }

    public int getIdKorisnik() {
        return idKorisnik;
    }

    public void setIdKorisnik(int idKorisnik) {
        this.idKorisnik = idKorisnik;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idAudio;
        hash += (int) idKorisnik;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OmiljenepesmePK)) {
            return false;
        }
        OmiljenepesmePK other = (OmiljenepesmePK) object;
        if (this.idAudio != other.idAudio) {
            return false;
        }
        if (this.idKorisnik != other.idKorisnik) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.OmiljenepesmePK[ idAudio=" + idAudio + ", idKorisnik=" + idKorisnik + " ]";
    }
    
}
