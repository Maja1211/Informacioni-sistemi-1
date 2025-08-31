/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Win 11
 */
@Entity
@Table(name = "audiosnimak")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Audiosnimak.findAll", query = "SELECT a FROM Audiosnimak a"),
    @NamedQuery(name = "Audiosnimak.findByIdAS", query = "SELECT a FROM Audiosnimak a WHERE a.idAS = :idAS"),
    @NamedQuery(name = "Audiosnimak.findByIdKor", query = "SELECT a FROM Audiosnimak a WHERE a.idKor = :idKor"),
    @NamedQuery(name = "Audiosnimak.findByNaziv", query = "SELECT a FROM Audiosnimak a WHERE a.naziv = :naziv"),
    @NamedQuery(name = "Audiosnimak.findByTrajanje", query = "SELECT a FROM Audiosnimak a WHERE a.trajanje = :trajanje"),
    @NamedQuery(name = "Audiosnimak.findByDatum", query = "SELECT a FROM Audiosnimak a WHERE a.datum = :datum"),
    @NamedQuery(name = "Audiosnimak.findByVreme", query = "SELECT a FROM Audiosnimak a WHERE a.vreme = :vreme")})
public class Audiosnimak implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdKor")
    private int idKor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "naziv")
    private String naziv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "trajanje")
    private int trajanje;
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

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdAS")
    private Integer idAS;
    @JoinTable(name = "audiokategorija", joinColumns = {
        @JoinColumn(name = "IdAS", referencedColumnName = "IdAS")}, inverseJoinColumns = {
        @JoinColumn(name = "IdKat", referencedColumnName = "IdKat")})
    @ManyToMany
    private List<Kategorija> kategorijaList;

    public Audiosnimak() {
    }

    public Audiosnimak(Integer idAS) {
        this.idAS = idAS;
    }

    public Audiosnimak(Integer idAS, int idKor, String naziv, int trajanje, Date datum, Date vreme) {
        this.idAS = idAS;
        this.idKor = idKor;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.datum = datum;
        this.vreme = vreme;
    }

    public Integer getIdAS() {
        return idAS;
    }

    public void setIdAS(Integer idAS) {
        this.idAS = idAS;
    }


    @XmlTransient
    public List<Kategorija> getKategorijaList() {
        return kategorijaList;
    }

    public void setKategorijaList(List<Kategorija> kategorijaList) {
        this.kategorijaList = kategorijaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAS != null ? idAS.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Audiosnimak)) {
            return false;
        }
        Audiosnimak other = (Audiosnimak) object;
        if ((this.idAS == null && other.idAS != null) || (this.idAS != null && !this.idAS.equals(other.idAS))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Audiosnimak[ idAS=" + idAS + " ]";
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
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
    
}
