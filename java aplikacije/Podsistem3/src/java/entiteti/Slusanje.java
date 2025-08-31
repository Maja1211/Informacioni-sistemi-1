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
@Table(name = "slusanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Slusanje.findAll", query = "SELECT s FROM Slusanje s"),
    @NamedQuery(name = "Slusanje.findByIdSlusanje", query = "SELECT s FROM Slusanje s WHERE s.idSlusanje = :idSlusanje"),
    @NamedQuery(name = "Slusanje.findByIdKor", query = "SELECT s FROM Slusanje s WHERE s.idKor = :idKor"),
    @NamedQuery(name = "Slusanje.findByIdAudioS", query = "SELECT s FROM Slusanje s WHERE s.idAudioS = :idAudioS"),
    @NamedQuery(name = "Slusanje.findByDatumOd", query = "SELECT s FROM Slusanje s WHERE s.datumOd = :datumOd"),
    @NamedQuery(name = "Slusanje.findByVremeOd", query = "SELECT s FROM Slusanje s WHERE s.vremeOd = :vremeOd"),
    @NamedQuery(name = "Slusanje.findBySekundeOd", query = "SELECT s FROM Slusanje s WHERE s.sekundeOd = :sekundeOd"),
    @NamedQuery(name = "Slusanje.findBySekundeUkupno", query = "SELECT s FROM Slusanje s WHERE s.sekundeUkupno = :sekundeUkupno")})
public class Slusanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdSlusanje")
    private Integer idSlusanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAudioS")
    private int idAudioS;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumOd")
    @Temporal(TemporalType.DATE)
    private Date datumOd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VremeOd")
    @Temporal(TemporalType.TIME)
    private Date vremeOd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SekundeOd")
    private int sekundeOd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SekundeUkupno")
    private int sekundeUkupno;

    public Slusanje() {
    }

    public Slusanje(Integer idSlusanje) {
        this.idSlusanje = idSlusanje;
    }

    public Slusanje(Integer idSlusanje, int idKor, int idAudioS, Date datumOd, Date vremeOd, int sekundeOd, int sekundeUkupno) {
        this.idSlusanje = idSlusanje;
        this.idKor = idKor;
        this.idAudioS = idAudioS;
        this.datumOd = datumOd;
        this.vremeOd = vremeOd;
        this.sekundeOd = sekundeOd;
        this.sekundeUkupno = sekundeUkupno;
    }

    public Integer getIdSlusanje() {
        return idSlusanje;
    }

    public void setIdSlusanje(Integer idSlusanje) {
        this.idSlusanje = idSlusanje;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public int getIdAudioS() {
        return idAudioS;
    }

    public void setIdAudioS(int idAudioS) {
        this.idAudioS = idAudioS;
    }

    public Date getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(Date datumOd) {
        this.datumOd = datumOd;
    }

    public Date getVremeOd() {
        return vremeOd;
    }

    public void setVremeOd(Date vremeOd) {
        this.vremeOd = vremeOd;
    }

    public int getSekundeOd() {
        return sekundeOd;
    }

    public void setSekundeOd(int sekundeOd) {
        this.sekundeOd = sekundeOd;
    }

    public int getSekundeUkupno() {
        return sekundeUkupno;
    }

    public void setSekundeUkupno(int sekundeUkupno) {
        this.sekundeUkupno = sekundeUkupno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSlusanje != null ? idSlusanje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Slusanje)) {
            return false;
        }
        Slusanje other = (Slusanje) object;
        if ((this.idSlusanje == null && other.idSlusanje != null) || (this.idSlusanje != null && !this.idSlusanje.equals(other.idSlusanje))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Slusanje[ idSlusanje=" + idSlusanje + " ]";
    }
    
}
