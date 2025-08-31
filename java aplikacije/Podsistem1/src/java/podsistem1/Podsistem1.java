/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entiteti.Korisnik;
import entiteti.Mesto;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Response;

public class Podsistem1 {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private static ConnectionFactory connFactory;

    @Resource(lookup = "queue1request")
    private static Queue queue1request;

    @Resource(lookup = "queue1response")
    private static Queue queue1response;

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
    private static final EntityManager em = emf.createEntityManager();

    public static void main(String[] args) {
        JMSContext context = connFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue1request);
        JMSProducer producer = context.createProducer();

        System.out.println("Podsistem 1 je pokrenut...");

        while (true) {
            try {
                TextMessage receivedMsg = (TextMessage) consumer.receive();
                String text = receivedMsg.getText();
                System.out.println("Primljen zahtev: " + text);

                String[] parts = text.split("\\|", 2);
                int zahtev = Integer.parseInt(parts[0]);
                String podaci = parts.length > 1 ? parts[1] : "";

                Response response = null;

                switch (zahtev) {
                    case 1:
                        System.out.println("Zahtev je kreiranje grada sa nazivom: " + podaci);
                        response = kreirajGrad(podaci);
                        break;
                    case 2:
                        System.out.println("Zahtev je kreiranje korisnika sa podacima: " + podaci);
                        String[] korisnikPodaci = podaci.split(",");
                        String ime = korisnikPodaci[0];
                        String email = korisnikPodaci[1];
                        int godiste = Integer.parseInt(korisnikPodaci[2]);
                        char pol = korisnikPodaci[3].charAt(0);
                        String nazivMesta = korisnikPodaci[4];
                        response = kreirajKorisnika(ime, email, godiste, pol, nazivMesta);
                        break;
                    case 3:
                        System.out.println("Zahtev je promena email-a korisnika sa podacima: " + podaci);
                        String[] podaciZaEmail = podaci.split(",");
                        int idKorisnik = Integer.parseInt(podaciZaEmail[0]);
                        String noviEmail = podaciZaEmail[1];
                        response = promeniEmailKorisnika(idKorisnik, noviEmail);
                        break;
                    case 4:
                        System.out.println("Zahtev je promena mesta korisnika sa podacima: " + podaci);
                        String[] podaciZaPromenu = podaci.split(",");
                        int idK = Integer.parseInt(podaciZaPromenu[0]);
                        String mesto = podaciZaPromenu[1];
                        response = promeniMestoKorisnika(idK, mesto);
                        break;
                    case 18:
                        System.out.println("Zahtev je dohvatanje svih mesta.");
                        response = dohvatiSvaMesta();
                        break;
                    case 19:
                        System.out.println("Zahtev je dohvatanje svih korisnika.");
                        response = dohvatiSveKorisnike();
                        break;
                    default:
                        System.out.println("Nepoznat zahtev.");
                        response = Response.status(Response.Status.BAD_REQUEST).entity("Nepoznat zahtev").build();
                }

                ObjectMessage responseMsg = context.createObjectMessage();
                responseMsg.setIntProperty("status", response.getStatus());
                responseMsg.setObject((Serializable) response.getEntity());
                producer.send(queue1response, responseMsg);
                System.out.println("Odgovor poslat na red: " + response.getEntity());

            } catch (JMSException ex) {
                Logger.getLogger(Podsistem1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static Response kreirajGrad(String naziv) {
        List<Mesto> mesta = em.createNamedQuery("Mesto.findByNaziv", Mesto.class)
                .setParameter("naziv", naziv)
                .getResultList();

        if (!mesta.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Grad vec postoji.").build();
        }

        try {
            em.getTransaction().begin();
            Mesto novoMesto = new Mesto();
            novoMesto.setNaziv(naziv);
            em.persist(novoMesto);
            em.getTransaction().commit();

            System.out.println("Grad kreiran: " + naziv);
            return Response.status(Response.Status.OK).entity("Uspesno kreiran grad.").build();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju grada.").build();
        }
    }

    private static Integer dohvatiIdMestaPoNazivu(String naziv) {
        List<Mesto> mesta = em.createNamedQuery("Mesto.findByNaziv", Mesto.class)
                .setParameter("naziv", naziv)
                .getResultList();
        if (!mesta.isEmpty()) {
            return mesta.get(0).getIdMesto();
        }
        return null;
    }

    private static Response kreirajKorisnika(String ime, String email, int godiste, char pol, String nazivMesta) {
        List<Korisnik> korisnici = em.createQuery("SELECT k FROM Korisnik k WHERE k.email = :email", Korisnik.class)
                .setParameter("email", email)
                .getResultList();
        if (!korisnici.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Korisnik sa tim emailom vec postoji.").build();
        }
        Integer idMesta = dohvatiIdMestaPoNazivu(nazivMesta);
        if (idMesta == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska: Mesto sa tim nazivom ne postoji u bazi.").build();
        }

        try {
            em.getTransaction().begin();
            Mesto mesto = em.find(Mesto.class, idMesta);

            Korisnik korisnik = new Korisnik();
            korisnik.setIme(ime);
            korisnik.setEmail(email);
            korisnik.setGodiste(godiste);
            korisnik.setPol(pol);
            korisnik.setIdMesta(mesto);

            em.persist(korisnik);
            em.getTransaction().commit();

            System.out.println("Korisnik kreiran: " + ime);
            return Response.status(Response.Status.OK).entity("Uspesno kreiran korisnik.").build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju korisnika.").build();
        }
    }

    private static Response promeniEmailKorisnika(int idKorisnik, String noviEmail) {
        List<Korisnik> postojeciEmail = em.createQuery("SELECT k FROM Korisnik k WHERE k.email = :email", Korisnik.class)
                .setParameter("email", noviEmail)
                .getResultList();
        if (!postojeciEmail.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska: Vec postoji korisnik sa tim emailom.").build();
        }

        Korisnik korisnik = em.find(Korisnik.class, idKorisnik);
        if (korisnik == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Greska: Korisnik sa datim ID-em ne postoji.").build();
        }

        try {
            em.getTransaction().begin();
            korisnik.setEmail(noviEmail);
            em.getTransaction().commit();
            System.out.println("Email uspešno promenjen na: " + noviEmail);
            return Response.status(Response.Status.OK).entity("Email uspešno promenjen.").build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri promeni email-a.").build();
        }
    }

    private static Response promeniMestoKorisnika(int idKorisnik, String nazivMesta) {
        try {
            em.getTransaction().begin();

            Korisnik korisnik = em.find(Korisnik.class, idKorisnik);
            if (korisnik == null) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST).entity("Korisnik sa tim ID-em ne postoji.").build();
            }

            List<Mesto> mesta = em.createNamedQuery("Mesto.findByNaziv", Mesto.class)
                    .setParameter("naziv", nazivMesta)
                    .getResultList();

            if (mesta.isEmpty()) {
                em.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST).entity("Mesto sa tim nazivom ne postoji.").build();
            }

            Mesto novoMesto = mesta.get(0);
            korisnik.setIdMesta(novoMesto);
            em.merge(korisnik);
            em.getTransaction().commit();

            System.out.println("Promenjeno mesto korisniku sa ID: " + idKorisnik);
            return Response.status(Response.Status.OK).entity("Uspesno promenjeno mesto korisnika.").build();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri promeni mesta korisnika.").build();
        }
    }

    private static Response dohvatiSvaMesta() {
        try {
            List<Mesto> mesta = em.createNamedQuery("Mesto.findAll", Mesto.class).getResultList();
            StringBuilder sb = new StringBuilder();
            for (Mesto m : mesta) {
                sb.append("ID: ").append(m.getIdMesto())
                        .append(", Naziv: ").append(m.getNaziv())
                        .append("\n");
            }
            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greska pri dohvatanju mesta.").build();
        }
    }

    private static Response dohvatiSveKorisnike() {
        try {
            List<Korisnik> korisnici = em.createNamedQuery("Korisnik.findAll", Korisnik.class).getResultList();
            StringBuilder sb = new StringBuilder();
            for (Korisnik k : korisnici) {
                sb.append("ID: ").append(k.getIdKorisnik())
                        .append(", Ime: ").append(k.getIme())
                        .append(", Email: ").append(k.getEmail()).append(", Godiste: ").append(k.getGodiste())
                        .append(", Pol: ").append(k.getPol())
                        .append(", Mesto: ")
                        .append(k.getIdMesta().getNaziv())
                        .append("\n");
            }
            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greska pri dohvatanju korisnika.").build();
        }
    }

}
