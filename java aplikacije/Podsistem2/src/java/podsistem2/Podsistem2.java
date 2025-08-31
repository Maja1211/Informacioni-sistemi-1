/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entiteti.Audiosnimak;
import entiteti.Kategorija;
import entiteti.Korisnik;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
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

public class Podsistem2 {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private static ConnectionFactory connFactory;

    @Resource(lookup = "queue2request")
    private static Queue queue2request;

    @Resource(lookup = "queue2response")
    private static Queue queue2response;

    private static final EntityManagerFactory emfPodsistem2 = Persistence.createEntityManagerFactory("Podsistem2PU");
    private static final EntityManager emPodsistem2 = emfPodsistem2.createEntityManager();
    private static final EntityManagerFactory emfPodsistem1 = Persistence.createEntityManagerFactory("Podsistem1PU");
    private static final EntityManager emPodsistem1 = emfPodsistem1.createEntityManager();

    public static void main(String[] args) {
        JMSContext context = connFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue2request);
        JMSProducer producer = context.createProducer();

        System.out.println("Podsistem 2 je pokrenut...");

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
                    case 5:
                        System.out.println("Zahtev je kreiranje kategorije sa nazivom: " + podaci);
                        response = kreirajKategoriju(podaci);
                        break;
                    case 6:
                        System.out.println("Zahtev je kreiranje audio snimka sa podacima: " + podaci);
                        String[] data = podaci.split(",");
                        int idKor = Integer.parseInt(data[0]);
                        String naziv = data[1];
                        int trajanje = Integer.parseInt(data[2]);

                        Date datum = null;
                        Date vreme = null;

                        try {
                            SimpleDateFormat sdfDatum = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat sdfVreme = new SimpleDateFormat("HH:mm:ss");

                            datum = sdfDatum.parse(data[3]);
                            vreme = sdfVreme.parse(data[4]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            System.out.println("Neispravan format datuma ili vremena.");
                            continue;

                        }

                        response = kreirajAudioSnimak(idKor, naziv, trajanje, datum, vreme);
                        break;
                    case 7:
                        System.out.println("Zahtev je promena naziva audio snimka sa podacima: " + podaci);
                        String[] podaciZaNaziv = podaci.split(",", 2);
                        int idAS = Integer.parseInt(podaciZaNaziv[0]);
                        String noviNaziv = podaciZaNaziv[1];
                        response = promeniNazivAudioSnimka(idAS, noviNaziv);
                        break;
                    case 8:
                        System.out.println("Zahtev je dodavanje kategorije audio snimku sa podacima: " + podaci);
                        String[] data1 = podaci.split(",");
                        int IdAS = Integer.parseInt(data1[0]);
                        String nazivKat = data1[1];
                        response = dodajKategorijuAudioSnimku(IdAS, nazivKat);
                        break;
                    case 17:
                        System.out.println("Zahtev je brisanje audio snimka sa podacima: " + podaci);
                        String[] podaciZaBrisanje = podaci.split(",");
                        int idASZaBrisanje = Integer.parseInt(podaciZaBrisanje[0]);
                        int idKorZaBrisanje = Integer.parseInt(podaciZaBrisanje[1]);
                        response = obrisiAudioSnimak(idASZaBrisanje, idKorZaBrisanje);
                        break;
                    case 20:
                        System.out.println("Zahtev je dohvatanje svih kategorija.");
                        response = dohvatiSveKategorije();
                        break;
                    case 21:
                        System.out.println("Zahtev je dohvatanje svih audio snimaka.");
                        response = dohvatiSveAudioSnimke();
                        break;
                    case 22:
                        System.out.println("Zahtev je dohvatanje svih kategorija audio snimaka.");
                        int idAS1 = Integer.parseInt(podaci);
                        response = dohvatiKategorijeAudioSnimka(idAS1);
                        break;

                    default:
                        System.out.println("Nepoznat zahtev.");
                        response = Response.status(Response.Status.BAD_REQUEST).entity("Nepoznat zahtev").build();
                }

                ObjectMessage responseMsg = context.createObjectMessage();
                responseMsg.setIntProperty("status", response.getStatus());
                responseMsg.setObject((Serializable) response.getEntity());
                producer.send(queue2response, responseMsg);
                System.out.println("Odgovor poslat na red: " + response.getEntity());

            } catch (JMSException ex) {
                Logger.getLogger(Podsistem2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static Response kreirajKategoriju(String naziv) {
        List<Kategorija> kategorije = emPodsistem2.createNamedQuery("Kategorija.findByNaziv", Kategorija.class)
                .setParameter("naziv", naziv)
                .getResultList();
        if (!kategorije.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Kategorija vec postoji.").build();
        }

        try {
            emPodsistem2.getTransaction().begin();
            Kategorija novaKategorija = new Kategorija();
            novaKategorija.setNaziv(naziv);
            emPodsistem2.persist(novaKategorija);
            emPodsistem2.getTransaction().commit();

            System.out.println("Kategorija kreirana: " + naziv);
            return Response.status(Response.Status.OK).entity("Uspesno kreirana kategorija.").build();
        } catch (Exception e) {
            if (emPodsistem2.getTransaction().isActive()) {
                emPodsistem2.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju kategorije.").build();
        }
    }

    private static Response kreirajAudioSnimak(int idKor, String naziv, int trajanje, Date datum, Date vreme) {
        try {
            Korisnik korisnik = emPodsistem1.find(Korisnik.class, idKor);
            if (korisnik == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Korisnik sa datim ID-em ne postoji u Podsistemu 1.").build();
            }

            emPodsistem2.getTransaction().begin();

            Audiosnimak audio = new Audiosnimak();
            audio.setIdKor(idKor);
            audio.setNaziv(naziv);
            audio.setTrajanje(trajanje);
            audio.setDatum(datum);
            audio.setVreme(vreme);

            emPodsistem2.persist(audio);
            emPodsistem2.getTransaction().commit();

            System.out.println("Audio snimak kreiran: " + naziv);
            return Response.status(Response.Status.OK).entity("Uspesno kreiran audio snimak.").build();
        } catch (Exception e) {
            if (emPodsistem2.getTransaction().isActive()) {
                emPodsistem2.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju audio snimka.").build();
        }
    }

    private static Response promeniNazivAudioSnimka(int idAS, String noviNaziv) {
        try {
            emPodsistem2.getTransaction().begin();

            Audiosnimak audioSnimak = emPodsistem2.find(Audiosnimak.class, idAS);
            if (audioSnimak == null) {
                emPodsistem2.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak sa tim ID-em ne postoji.").build();
            }

            audioSnimak.setNaziv(noviNaziv);
            emPodsistem2.merge(audioSnimak);
            emPodsistem2.getTransaction().commit();

            System.out.println("Naziv audio snimka uspešno promenjen na: " + noviNaziv);
            return Response.status(Response.Status.OK)
                    .entity("Naziv audio snimka uspešno promenjen.").build();
        } catch (Exception e) {
            if (emPodsistem2.getTransaction().isActive()) {
                emPodsistem2.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri promeni naziva audio snimka.").build();
        }
    }

    private static Response dodajKategorijuAudioSnimku(int idAS, String nazivKategorije) {
        try {
            emPodsistem2.getTransaction().begin();

            Audiosnimak audioSnimak = emPodsistem2.find(Audiosnimak.class, idAS);
            if (audioSnimak == null) {
                emPodsistem2.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak sa tim ID-em ne postoji.").build();
            }

            List<Kategorija> kategorije = emPodsistem2.createNamedQuery("Kategorija.findByNaziv", Kategorija.class)
                    .setParameter("naziv", nazivKategorije)
                    .getResultList();

            if (kategorije.isEmpty()) {
                emPodsistem2.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Kategorija sa tim nazivom ne postoji.").build();
            }

            Kategorija kategorija = kategorije.get(0);

            if (audioSnimak.getKategorijaList().contains(kategorija)) {
                emPodsistem2.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak već ima tu kategoriju.").build();
            }

            audioSnimak.getKategorijaList().add(kategorija);
            emPodsistem2.merge(audioSnimak);
            emPodsistem2.getTransaction().commit();

            System.out.println("Kategorija uspešno dodeljena audio snimku.");
            return Response.status(Response.Status.OK)
                    .entity("Kategorija uspešno dodeljena audio snimku.").build();
        } catch (Exception e) {
            if (emPodsistem2.getTransaction().isActive()) {
                emPodsistem2.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri dodeli kategorije audio snimku.").build();
        }
    }

    private static Response obrisiAudioSnimak(int idAS, int idKor) {
        try {
            emPodsistem2.getTransaction().begin();

            Audiosnimak audioSnimak = emPodsistem2.find(Audiosnimak.class, idAS);
            if (audioSnimak == null) {
                emPodsistem2.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak sa datim ID-em ne postoji.").build();
            }

            if (audioSnimak.getIdKor() != idKor) {
                emPodsistem2.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik nema pravo da obriše ovaj audio snimak.").build();
            }

            emPodsistem2.remove(audioSnimak);
            emPodsistem2.getTransaction().commit();

            System.out.println("Audio snimak uspešno obrisan. ID: " + idAS);
            return Response.status(Response.Status.OK)
                    .entity("Audio snimak uspešno obrisan.").build();
        } catch (Exception e) {
            if (emPodsistem2.getTransaction().isActive()) {
                emPodsistem2.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri brisanju audio snimka.").build();
        }
    }

    private static Response dohvatiSveKategorije() {
        try {
            List<Kategorija> kategorije = emPodsistem2.createNamedQuery("Kategorija.findAll", Kategorija.class).getResultList();
            StringBuilder sb = new StringBuilder();
            for (Kategorija k : kategorije) {
                sb.append("ID: ").append(k.getIdKat())
                        .append(", Naziv: ").append(k.getNaziv())
                        .append("\n");
            }
            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greska pri dohvatanju kategorija.").build();
        }
    }

    private static Response dohvatiSveAudioSnimke() {
        try {
            List<Audiosnimak> snimci = emPodsistem2.createNamedQuery("Audiosnimak.findAll", Audiosnimak.class).getResultList();
            StringBuilder sb = new StringBuilder();
            SimpleDateFormat datum = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat vreme = new SimpleDateFormat("HH:mm:ss");

            for (Audiosnimak a : snimci) {
                sb.append("ID: ").append(a.getIdAS())
                        .append(", Naziv: ").append(a.getNaziv())
                        .append(", Korisnik ID: ").append(a.getIdKor())
                        .append(", Trajanje: ").append(a.getTrajanje())
                        .append(", Datum: ").append(datum.format(a.getDatum()))
                        .append(", Vreme: ").append(vreme.format(a.getVreme()))
                        .append("\n");
            }

            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greska pri dohvatanju audio snimaka.").build();
        }
    }

    private static Response dohvatiKategorijeAudioSnimka(int idAS) {
        try {
            Audiosnimak audioSnimak = emPodsistem2.find(Audiosnimak.class, idAS);

            if (audioSnimak == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak sa tim ID-em ne postoji.").build();
            }

            List<Kategorija> kategorije = audioSnimak.getKategorijaList();

            if (kategorije.isEmpty()) {
                return Response.status(Response.Status.OK)
                        .entity("Audio snimak nema nijednu kategoriju.").build();
            }

            StringBuilder sb = new StringBuilder();
            for (Kategorija k : kategorije) {
                sb.append("ID: ").append(k.getIdKat())
                        .append(", Naziv: ").append(k.getNaziv())
                        .append("\n");
            }

            System.out.println("Pronađeno kategorija za audio snimak " + idAS + ": " + kategorije.size());
            return Response.status(Response.Status.OK)
                    .entity(sb.toString()).build();

        } catch (Exception e) {
            if (emPodsistem2.getTransaction().isActive()) {
                emPodsistem2.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri dohvatanju kategorija audio snimka.").build();
        }
    }

}
