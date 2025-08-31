package entiteti;

import entiteti.Audiosnimak;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2025-07-18T16:49:19")
@StaticMetamodel(Kategorija.class)
public class Kategorija_ { 

    public static volatile SingularAttribute<Kategorija, Integer> idKat;
    public static volatile ListAttribute<Kategorija, Audiosnimak> audiosnimakList;
    public static volatile SingularAttribute<Kategorija, String> naziv;

}