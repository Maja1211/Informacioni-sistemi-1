package entiteti;

import entiteti.Paket;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-07-18T13:04:15")
@StaticMetamodel(Pretplata.class)
public class Pretplata_ { 

    public static volatile SingularAttribute<Pretplata, Date> datumPoc;
    public static volatile SingularAttribute<Pretplata, Integer> idKor;
    public static volatile SingularAttribute<Pretplata, Paket> idPaket;
    public static volatile SingularAttribute<Pretplata, Date> vremePoc;
    public static volatile SingularAttribute<Pretplata, Double> cena;
    public static volatile SingularAttribute<Pretplata, Integer> idPretplata;

}