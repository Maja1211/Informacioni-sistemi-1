/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entiteti.Audiosnimak;
import entiteti.Korisnik;
import entiteti.Ocena;
import entiteti.Omiljenepesme;
import entiteti.OmiljenepesmePK;
import entiteti.Paket;
import entiteti.Pretplata;
import entiteti.Slusanje;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class Podsistem3 {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private static ConnectionFactory connFactory;

    @Resource(lookup = "queue3request")
    private static Queue queue3request;

    @Resource(lookup = "queue3response")
    private static Queue queue3response;

    private static final EntityManagerFactory emfPodsistem3 = Persistence.createEntityManagerFactory("Podsistem3PU");
    private static final EntityManager emPodsistem3 = emfPodsistem3.createEntityManager();

    private static final EntityManagerFactory emfPodsistem2 = Persistence.createEntityManagerFactory("Podsistem2PU");
    private static final EntityManager emPodsistem2 = emfPodsistem2.createEntityManager();

    private static final EntityManagerFactory emfPodsistem1 = Persistence.createEntityManagerFactory("Podsistem1PU");
    private static final EntityManager emPodsistem1 = emfPodsistem1.createEntityManager();

    public static void main(String[] args) {
        JMSContext context = connFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue3request);
        JMSProducer producer = context.createProducer();

        System.out.println("Podsistem 3 je pokrenut...");

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
                    case 9:
                        System.out.println("Zahtev je kreiranje paketa sa podacima: " + podaci);
                        String[] podaciPaketa = podaci.split(",");
                        String naziv = podaciPaketa[0];
                        double cena = Double.parseDouble(podaciPaketa[1]);
                        response = kreirajPaket(naziv, cena);
                        break;
                    case 10:
                        System.out.println("Zahtev je promena mesečne cene paketa sa podacima: " + podaci);
                        String[] podaciCena = podaci.split(",");
                        int idPaket = Integer.parseInt(podaciCena[0]);
                        double novaCena = Double.parseDouble(podaciCena[1]);
                        response = promeniCenuPaketa(idPaket, novaCena);
                        break;
                    case 11:
                        System.out.println("Zahtev je kreiranje pretplate sa podacima: " + podaci);
                        String[] podaciPretplata = podaci.split(",");
                        int idKor = Integer.parseInt(podaciPretplata[0]);
                        int idPaket1 = Integer.parseInt(podaciPretplata[1]);
                        String datumPoc = podaciPretplata[2];
                        String vremePoc = podaciPretplata[3];
                        response = kreirajPretplatu(idKor, idPaket1, datumPoc, vremePoc);
                        break;
                    case 12:
                        System.out.println("Zahtev je kreiranje slušanja sa podacima: " + podaci);
                        String[] podaciZaSlusanje = podaci.split(",");
                        int idKor1 = Integer.parseInt(podaciZaSlusanje[0]);
                        int idAS = Integer.parseInt(podaciZaSlusanje[1]);
                        String datumOd = podaciZaSlusanje[2];
                        String vremeOd = podaciZaSlusanje[3];
                        int sekundeOd = Integer.parseInt(podaciZaSlusanje[4]);
                        int sekundeUkupno = Integer.parseInt(podaciZaSlusanje[5]);
                        response = kreirajSlusanje(idKor1, idAS, datumOd, vremeOd, sekundeOd, sekundeUkupno);
                        break;
                    case 13:
                        System.out.println("Zahtev je dodavanje pesme u omiljene sa podacima: " + podaci);
                        String[] dataOmiljene = podaci.split(",");
                        int idAudio = Integer.parseInt(dataOmiljene[0]);
                        int idKorisnik = Integer.parseInt(dataOmiljene[1]);
                        response = dodajPesmuUOmiljene(idAudio, idKorisnik);
                        break;
                    case 14:
                        System.out.println("Zahtev je kreiranje ocene.");
                        String[] podaci14 = podaci.split(",");
                        int broj = Integer.parseInt(podaci14[0]);
                        String datum = podaci14[1];
                        String vreme = podaci14[2];
                        int idKor2 = Integer.parseInt(podaci14[3]);
                        int idAS2 = Integer.parseInt(podaci14[4]);
                        response = kreirajOcenu(broj, datum, vreme, idKor2, idAS2);
                        break;
                    case 15:
                        System.out.println("Zahtev je menjanje ocene.");
                        String[] data15 = podaci.split(",");
                        int idKor3 = Integer.parseInt(data15[0]);
                        int idAS3 = Integer.parseInt(data15[1]);
                        int broj1 = Integer.parseInt(data15[2]);
                        response = menjajOcenu(idKor3, idAS3, broj1);
                        break;
                    case 16:
                        System.out.println("Zahtev je brisanje ocene.");
                        String[] dataBrisanjeOcene = podaci.split(",");
                        int idKorOcena = Integer.parseInt(dataBrisanjeOcene[0]);
                        int idASOcena = Integer.parseInt(dataBrisanjeOcene[1]);
                        response = obrisiOcenu(idKorOcena, idASOcena);
                        break;
                    case 23:
                        System.out.println("Zahtev je dohvatanje svih paketa.");
                        response = dohvatiPakete();
                        break;
                    case 24:
                        System.out.println("Zahtev je dohvatanje svih pretplata korisnika.");
                        int idKorPretplata = Integer.parseInt(podaci);
                        response = dohvatiPretplate(idKorPretplata);
                        break;
                    case 25:
                        System.out.println("Zahtev je dohvatanje svih slusanja za audio snimak.");
                        int idAudioSlusanja = Integer.parseInt(podaci);
                        response = dohvatiSlusanja(idAudioSlusanja);
                        break;
                    case 26:
                        System.out.println("Zahtev je dohvatanje svih ocena za audio snimak.");
                        int idAudioOcene = Integer.parseInt(podaci);
                        response = dohvatiOcene(idAudioOcene);
                        break;
                    case 27:
                        System.out.println("Zahtev je dohvatanje omiljenih pesama korisnika.");
                        int idKorOmiljene = Integer.parseInt(podaci);
                        response = dohvatiOmiljenePesme(idKorOmiljene);
                        break;

                    default:
                        System.out.println("Nepoznat zahtev.");
                        response = Response.status(Response.Status.BAD_REQUEST)
                                .entity("Nepoznat zahtev").build();
                }

                ObjectMessage responseMsg = context.createObjectMessage();
                responseMsg.setIntProperty("status", response.getStatus());
                responseMsg.setObject((Serializable) response.getEntity());
                producer.send(queue3response, responseMsg);
                System.out.println("Odgovor poslat na red: " + response.getEntity());

            } catch (JMSException ex) {
                Logger.getLogger(Podsistem3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static Response kreirajPaket(String naziv, double cena) {
        try {
            List<Paket> paketi = emPodsistem3.createQuery("SELECT p FROM Paket p WHERE p.naziv = :naziv", Paket.class)
                    .setParameter("naziv", naziv)
                    .getResultList();
            if (!paketi.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Paket sa tim nazivom već postoji.").build();
            }

            emPodsistem3.getTransaction().begin();
            Paket noviPaket = new Paket();
            noviPaket.setNaziv(naziv);
            noviPaket.setCena(cena);
            emPodsistem3.persist(noviPaket);
            emPodsistem3.getTransaction().commit();

            System.out.println("Paket uspešno kreiran: " + naziv);
            return Response.status(Response.Status.OK)
                    .entity("Uspešno kreiran paket.").build();

        } catch (Exception e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri kreiranju paketa.").build();
        }
    }

    private static Response promeniCenuPaketa(int idPaket, double novaCena) {
        try {
            emPodsistem3.getTransaction().begin();

            Paket paket = emPodsistem3.find(Paket.class, idPaket);
            if (paket == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Paket sa tim ID-em ne postoji.").build();
            }

            paket.setCena(novaCena);
            emPodsistem3.merge(paket);
            emPodsistem3.getTransaction().commit();

            System.out.println("Promenjena cena paketa sa ID: " + idPaket + " na novu cenu: " + novaCena);
            return Response.status(Response.Status.OK)
                    .entity("Uspešno promenjena cena paketa.").build();
        } catch (Exception e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri promeni cene paketa.").build();
        }
    }

    private static Response kreirajPretplatu(int idKor, int idPaket, String datumPoc, String vremePoc) {
        try {
            emPodsistem3.getTransaction().begin();

            Korisnik korisnik = emPodsistem1.find(Korisnik.class, idKor);
            if (korisnik == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik sa tim ID-em ne postoji.").build();
            }

            Paket paket = emPodsistem3.find(Paket.class, idPaket);
            if (paket == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Paket sa tim ID-em ne postoji.").build();
            }

            SimpleDateFormat datumFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat vremeFormat = new SimpleDateFormat("HH:mm:ss");

            Date datum = datumFormat.parse(datumPoc);
            Date vreme = vremeFormat.parse(vremePoc);

            List<Pretplata> pretplate = emPodsistem3.createQuery(
                    "SELECT p FROM Pretplata p WHERE p.idKor = :idKor AND p.idPaket.idPaket = :idPaket", Pretplata.class)
                    .setParameter("idKor", idKor)
                    .setParameter("idPaket", idPaket)
                    .getResultList();

            for (Pretplata p : pretplate) {
                Date kraj = new Date(p.getDatumPoc().getTime() + (long) 30 * 24 * 60 * 60 * 1000);
                if (datum.before(kraj)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
                    String datumKraja = sdf.format(kraj);
                    emPodsistem3.getTransaction().rollback();
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Korisnik već ima aktivnu pretplatu koja traje do " + datumKraja + ".").build();
                }
            }

            Pretplata novaPretplata = new Pretplata();
            novaPretplata.setIdKor(idKor);
            novaPretplata.setIdPaket(paket);
            novaPretplata.setDatumPoc(datum);
            novaPretplata.setVremePoc(vreme);
            novaPretplata.setCena(paket.getCena());

            emPodsistem3.persist(novaPretplata);
            emPodsistem3.getTransaction().commit();

            System.out.println("Pretplata uspešno kreirana.");
            return Response.status(Response.Status.OK).entity("Uspesno kreirana pretplata.").build();

        } catch (ParseException e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Neispravan format datuma ili vremena.").build();
        } catch (Exception e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greska pri kreiranju pretplate.").build();
        }
    }

    private static Response kreirajSlusanje(int idKor, int idAudioS, String datumOd, String vremeOd, int sekundeOd, int sekundeUkupno) {
        try {
            SimpleDateFormat Datum = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat Vreme = new SimpleDateFormat("HH:mm:ss");

            Date datum = Datum.parse(datumOd);
            Date vreme = Vreme.parse(vremeOd);

            emPodsistem3.getTransaction().begin();

            Korisnik korisnik = emPodsistem1.find(Korisnik.class, idKor);
            if (korisnik == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik sa datim ID-em ne postoji.").build();
            }

            Audiosnimak audioSnimak = emPodsistem2.find(Audiosnimak.class, idAudioS);
            if (audioSnimak == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak sa datim ID-em ne postoji.").build();
            }

            Date datumKreiranja = audioSnimak.getDatum();
            Date vremeKreiranja = audioSnimak.getVreme();

            if (datum.before(datumKreiranja)
                    || (datum.equals(datumKreiranja) && vreme.before(vremeKreiranja))) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datum i vreme slušanja ne mogu biti pre kreiranja audio snimka.").build();
            }

            int trajanje = audioSnimak.getTrajanje();

            if (sekundeOd < 0 || sekundeOd >= trajanje) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Sekunde od koje se sluša moraju biti u okviru trajanja audio snimka.").build();
            }

            if (sekundeUkupno < 0 || (sekundeOd + sekundeUkupno) > trajanje) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Ukupno odslušane sekunde ne mogu prelaziti trajanje snimka.").build();
            }

            Slusanje novoSlusanje = new Slusanje();
            novoSlusanje.setIdKor(idKor);
            novoSlusanje.setIdAudioS(idAudioS);
            novoSlusanje.setDatumOd(datum);
            novoSlusanje.setVremeOd(vreme);
            novoSlusanje.setSekundeOd(sekundeOd);
            novoSlusanje.setSekundeUkupno(sekundeUkupno);

            emPodsistem3.persist(novoSlusanje);
            emPodsistem3.getTransaction().commit();

            System.out.println("Slušanje uspešno kreirano.");
            return Response.status(Response.Status.OK).entity("Slušanje uspešno kreirano.").build();

        } catch (ParseException e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Neispravan format datuma ili vremena.").build();
        } catch (Exception e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri kreiranju slušanja.").build();
        }
    }

    private static Response dodajPesmuUOmiljene(int idAudio, int idKorisnik) {
        try {
            emPodsistem3.getTransaction().begin();

            Korisnik korisnik = emPodsistem1.find(Korisnik.class, idKorisnik);
            if (korisnik == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik sa tim ID-em ne postoji.").build();
            }

            Audiosnimak audioSnimak = emPodsistem2.find(Audiosnimak.class, idAudio);
            if (audioSnimak == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak sa tim ID-em ne postoji.").build();
            }

            Omiljenepesme omiljena = emPodsistem3.find(Omiljenepesme.class, new OmiljenepesmePK(idAudio, idKorisnik));
            if (omiljena != null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak je već u omiljenim kod korisnika.").build();
            }

            OmiljenepesmePK pk = new OmiljenepesmePK(idAudio, idKorisnik);
            Omiljenepesme omiljenaPesma = new Omiljenepesme(pk);
            emPodsistem3.persist(omiljenaPesma);
            emPodsistem3.getTransaction().commit();

            System.out.println("Pesma uspešno dodata u omiljene.");
            return Response.status(Response.Status.OK)
                    .entity("Pesma uspešno dodata u omiljene.").build();

        } catch (Exception e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri dodavanju pesme u omiljene.").build();
        }
    }

    private static Response kreirajOcenu(int broj, String datumStr, String vremeStr, int idKor, int idAS) {
        try {
            SimpleDateFormat Datum = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat Vreme = new SimpleDateFormat("HH:mm:ss");

            Date datum = Datum.parse(datumStr);
            Date vreme = Vreme.parse(vremeStr);

            emPodsistem3.getTransaction().begin();

            Korisnik korisnik = emPodsistem1.find(Korisnik.class, idKor);
            if (korisnik == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik sa tim ID-em ne postoji.").build();
            }

            Audiosnimak audioSnimak = emPodsistem2.find(Audiosnimak.class, idAS);
            if (audioSnimak == null) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Audio snimak sa tim ID-em ne postoji.").build();
            }

            List<Ocena> postojeceOcene = emPodsistem3.createQuery(
                    "SELECT o FROM Ocena o WHERE o.idKor = :idKor AND o.idAS = :idAS", Ocena.class)
                    .setParameter("idKor", idKor)
                    .setParameter("idAS", idAS)
                    .getResultList();

            if (!postojeceOcene.isEmpty()) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik je već ocenio ovaj audio snimak.").build();
            }

            Date datumKreiranja = audioSnimak.getDatum();
            Date vremeKreiranja = audioSnimak.getVreme();

            if (datum.before(datumKreiranja)
                    || (datum.equals(datumKreiranja) && vreme.before(vremeKreiranja))) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datum i vreme kreiranja ocene ne mogu biti pre datuma kreiranja audio snimka.").build();
            }

            Ocena novaOcena = new Ocena();
            novaOcena.setIdKor(idKor);
            novaOcena.setIdAS(idAS);
            novaOcena.setDatum(datum);
            novaOcena.setVreme(vreme);
            novaOcena.setBroj(broj);

            emPodsistem3.persist(novaOcena);
            emPodsistem3.getTransaction().commit();

            System.out.println("Ocena uspešno kreirana.");
            return Response.status(Response.Status.OK).entity("Ocena uspešno kreirana.").build();

        } catch (Exception e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri kreiranju ocene.").build();
        }
    }

    private static Response menjajOcenu(int idKor, int idAS, int broj) {
        try {
            emPodsistem3.getTransaction().begin();

            List<Ocena> ocene = emPodsistem3.createQuery(
                    "SELECT o FROM Ocena o WHERE o.idKor = :idKorisnik AND o.idAS = :idAudio", Ocena.class)
                    .setParameter("idKorisnik", idKor)
                    .setParameter("idAudio", idAS)
                    .getResultList();

            if (ocene.isEmpty()) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Ne postoji ocena tog korisnika za taj audio snimak.").build();
            }

            Ocena ocena = ocene.get(0);
            ocena.setBroj(broj);
            Date trenutno = new Date();
            ocena.setDatum(trenutno);
            ocena.setVreme(trenutno);

            emPodsistem3.merge(ocena);
            emPodsistem3.getTransaction().commit();

            return Response.status(Response.Status.OK)
                    .entity("Ocena uspešno izmenjena.").build();
        } catch (Exception e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri menjanju ocene.").build();
        }
    }

    private static Response obrisiOcenu(int idKor, int idAS) {
        try {
            emPodsistem3.getTransaction().begin();

            List<Ocena> ocene = emPodsistem3.createQuery(
                    "SELECT o FROM Ocena o WHERE o.idKor = :idKorisnik AND o.idAS = :idAudio", Ocena.class)
                    .setParameter("idKorisnik", idKor)
                    .setParameter("idAudio", idAS)
                    .getResultList();

            if (ocene.isEmpty()) {
                emPodsistem3.getTransaction().rollback();
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik nema ocenu za taj audio snimak.").build();
            }

            Ocena ocena = ocene.get(0);
            emPodsistem3.remove(ocena);
            emPodsistem3.getTransaction().commit();

            System.out.println("Ocena uspešno obrisana.");
            return Response.status(Response.Status.OK)
                    .entity("Ocena uspešno obrisana.").build();

        } catch (Exception e) {
            if (emPodsistem3.getTransaction().isActive()) {
                emPodsistem3.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri brisanju ocene.").build();
        }
    }

    private static Response dohvatiPakete() {
        try {
            List<Paket> paketi = emPodsistem3.createNamedQuery("Paket.findAll", Paket.class).getResultList();

            if (paketi.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Ne postoje paketi.").build();
            }

            StringBuilder sb = new StringBuilder();
            for (Paket p : paketi) {
                sb.append("ID Paketa: ").append(p.getIdPaket()).append("\n")
                        .append("Naziv Paketa: ").append(p.getNaziv()).append("\n")
                        .append("Cena: ").append(p.getCena()).append(" EUR\n\n");
            }

            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri dohvatanju paketa.").build();
        }
    }

    private static Response dohvatiPretplate(int idKor) {
        try {
            List<Pretplata> pretplate = emPodsistem3.createQuery(
                    "SELECT p FROM Pretplata p WHERE p.idKor = :idKor", Pretplata.class)
                    .setParameter("idKor", idKor)
                    .getResultList();

            if (pretplate.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik nema nijednu pretplatu.").build();
            }

            SimpleDateFormat Datum = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat Vreme = new SimpleDateFormat("HH:mm:ss");

            StringBuilder sb = new StringBuilder();
            for (Pretplata p : pretplate) {
                sb.append("ID Paketa: ").append(p.getIdPaket().getIdPaket()).append("\n")
                        .append("Naziv Paketa: ").append(p.getIdPaket().getNaziv()).append("\n")
                        .append("Cena: ").append(p.getCena()).append(" EUR\n")
                        .append("Datum početka pretplate: ").append(Datum.format(p.getDatumPoc())).append("\n")
                        .append("Vreme početka pretplate: ").append(Vreme.format(p.getVremePoc())).append("\n\n");
            }

            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri dohvatanju pretplata.").build();
        }
    }

    private static Response dohvatiSlusanja(int idAudioS) {
        try {
            List<Slusanje> slusanja = emPodsistem3.createQuery(
                    "SELECT s FROM Slusanje s WHERE s.idAudioS = :idAudio", Slusanje.class)
                    .setParameter("idAudio", idAudioS)
                    .getResultList();

            if (slusanja.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Ne postoje slušanja za dati audio snimak.").build();
            }

            SimpleDateFormat Datum = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat Vreme = new SimpleDateFormat("HH:mm:ss");

            StringBuilder sb = new StringBuilder();
            for (Slusanje s : slusanja) {
                sb.append("ID Korisnika: ").append(s.getIdKor()).append("\n")
                        .append("Datum: ").append(Datum.format(s.getDatumOd())).append("\n")
                        .append("Vreme: ").append(Vreme.format(s.getVremeOd())).append("\n")
                        .append("Sekunde od: ").append(s.getSekundeOd()).append("\n")
                        .append("Ukupno odslušano sekundi: ").append(s.getSekundeUkupno()).append("\n\n");
            }

            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri dohvatanju slušanja.").build();
        }
    }

    private static Response dohvatiOcene(int idAS) {
        try {
            List<Ocena> ocene = emPodsistem3.createQuery(
                    "SELECT o FROM Ocena o WHERE o.idAS = :idAS", Ocena.class)
                    .setParameter("idAS", idAS)
                    .getResultList();

            if (ocene.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Ne postoje ocene za dati audio snimak.").build();
            }

            SimpleDateFormat Datum = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat Vreme = new SimpleDateFormat("HH:mm:ss");

            StringBuilder sb = new StringBuilder();
            for (Ocena o : ocene) {
                sb.append("Korisnik ID: ").append(o.getIdKor()).append("\n")
                        .append("Ocena: ").append(o.getBroj()).append("\n")
                        .append("Datum: ").append(Datum.format(o.getDatum())).append("\n")
                        .append("Vreme: ").append(Vreme.format(o.getVreme())).append("\n\n");
            }
            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri dohvatanju ocena.").build();
        }
    }

    private static Response dohvatiOmiljenePesme(int idKorisnik) {
        try {
            List<Omiljenepesme> omiljene = emPodsistem3.createQuery(
                    "SELECT o FROM Omiljenepesme o WHERE o.omiljenepesmePK.idKorisnik = :idKorisnik", Omiljenepesme.class)
                    .setParameter("idKorisnik", idKorisnik)
                    .getResultList();

            if (omiljene.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Korisnik nema nijednu omiljenu pesmu.").build();
            }

            StringBuilder sb = new StringBuilder();
            for (Omiljenepesme o : omiljene) {
                sb.append("ID Audio Snimka: ").append(o.getOmiljenepesmePK().getIdAudio()).append("\n");
            }

            return Response.status(Response.Status.OK).entity(sb.toString()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Greška pri dohvatanju omiljenih pesama.").build();
        }
    }

}
