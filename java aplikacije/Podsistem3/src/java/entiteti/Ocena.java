/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Win 11
 */
@Entity
@Table(name = "ocena")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ocena.findAll", query = "SELECT o FROM Ocena o"),
    @NamedQuery(name = "Ocena.findByIdOcena", query = "SELECT o FROM Ocena o WHERE o.idOcena = :idOcena"),
    @NamedQuery(name = "Ocena.findByBroj", query = "SELECT o FROM Ocena o WHERE o.broj = :broj"),
    @NamedQuery(name = "Ocena.findByDatum", query = "SELECT o FROM Ocena o WHERE o.datum = :datum"),
    @NamedQuery(name = "Ocena.findByVreme", query = "SELECT o FROM Ocena o WHERE o.vreme = :vreme"),
    @NamedQuery(name = "Ocena.findByIdKor", query = "SELECT o FROM Ocena o WHERE o.idKor = :idKor"),
    @NamedQuery(name = "Ocena.findByIdAS", query = "SELECT o FROM Ocena o WHERE o.idAS = :idAS")})
public class Ocena implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdOcena")
    private Integer idOcena;
    @Basic(optional = false)
    @NotNull
    @Column(name = "broj")
    private int broj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum")
    @Temporal(TemporalType.DATE)
    private Date datum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vreme")
    @Temporal(TemporalType.TIME)
    private Date vreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAS")
    private int idAS;

    public Ocena() {
    }

    public Ocena(Integer idOcena) {
        this.idOcena = idOcena;
    }

    public Ocena(Integer idOcena, int broj, Date datum, Date vreme, int idKor, int idAS) {
        this.idOcena = idOcena;
        this.broj = broj;
        this.datum = datum;
        this.vreme = vreme;
        this.idKor = idKor;
        this.idAS = idAS;
    }

    public Integer getIdOcena() {
        return idOcena;
    }

    public void setIdOcena(Integer idOcena) {
        this.idOcena = idOcena;
    }

    public int getBroj() {
        return broj;
    }

    public void setBroj(int broj) {
        this.broj = broj;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Date getVreme() {
        return vreme;
    }

    public void setVreme(Date vreme) {
        this.vreme = vreme;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public int getIdAS() {
        return idAS;
    }

    public void setIdAS(int idAS) {
        this.idAS = idAS;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOcena != null ? idOcena.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ocena)) {
            return false;
        }
        Ocena other = (Ocena) object;
        if ((this.idOcena == null && other.idOcena != null) || (this.idOcena != null && !this.idOcena.equals(other.idOcena))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Ocena[ idOcena=" + idOcena + " ]";
    }
    
}
