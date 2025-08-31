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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "pretplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pretplata.findAll", query = "SELECT p FROM Pretplata p"),
    @NamedQuery(name = "Pretplata.findByIdPretplata", query = "SELECT p FROM Pretplata p WHERE p.idPretplata = :idPretplata"),
    @NamedQuery(name = "Pretplata.findByIdKor", query = "SELECT p FROM Pretplata p WHERE p.idKor = :idKor"),
    @NamedQuery(name = "Pretplata.findByDatumPoc", query = "SELECT p FROM Pretplata p WHERE p.datumPoc = :datumPoc"),
    @NamedQuery(name = "Pretplata.findByVremePoc", query = "SELECT p FROM Pretplata p WHERE p.vremePoc = :vremePoc"),
    @NamedQuery(name = "Pretplata.findByCena", query = "SELECT p FROM Pretplata p WHERE p.cena = :cena")})
public class Pretplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdPretplata")
    private Integer idPretplata;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datumPoc")
    @Temporal(TemporalType.DATE)
    private Date datumPoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vremePoc")
    @Temporal(TemporalType.TIME)
    private Date vremePoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cena")
    private double cena;
    @JoinColumn(name = "IdPaket", referencedColumnName = "IdPaket")
    @ManyToOne(optional = false)
    private Paket idPaket;

    public Pretplata() {
    }

    public Pretplata(Integer idPretplata) {
        this.idPretplata = idPretplata;
    }

    public Pretplata(Integer idPretplata, int idKor, Date datumPoc, Date vremePoc, double cena) {
        this.idPretplata = idPretplata;
        this.idKor = idKor;
        this.datumPoc = datumPoc;
        this.vremePoc = vremePoc;
        this.cena = cena;
    }

    public Integer getIdPretplata() {
        return idPretplata;
    }

    public void setIdPretplata(Integer idPretplata) {
        this.idPretplata = idPretplata;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public Date getDatumPoc() {
        return datumPoc;
    }

    public void setDatumPoc(Date datumPoc) {
        this.datumPoc = datumPoc;
    }

    public Date getVremePoc() {
        return vremePoc;
    }

    public void setVremePoc(Date vremePoc) {
        this.vremePoc = vremePoc;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public Paket getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(Paket idPaket) {
        this.idPaket = idPaket;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPretplata != null ? idPretplata.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pretplata)) {
            return false;
        }
        Pretplata other = (Pretplata) object;
        if ((this.idPretplata == null && other.idPretplata != null) || (this.idPretplata != null && !this.idPretplata.equals(other.idPretplata))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Pretplata[ idPretplata=" + idPretplata + " ]";
    }
    
}
