package entiteti;

import entiteti.Kategorija;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-07-18T16:49:19")
@StaticMetamodel(Audiosnimak.class)
public class Audiosnimak_ { 

    public static volatile SingularAttribute<Audiosnimak, Date> datum;
    public static volatile ListAttribute<Audiosnimak, Kategorija> kategorijaList;
    public static volatile SingularAttribute<Audiosnimak, Integer> idKor;
    public static volatile SingularAttribute<Audiosnimak, Date> vreme;
    public static volatile SingularAttribute<Audiosnimak, Integer> trajanje;
    public static volatile SingularAttribute<Audiosnimak, String> naziv;
    public static volatile SingularAttribute<Audiosnimak, Integer> idAS;

}