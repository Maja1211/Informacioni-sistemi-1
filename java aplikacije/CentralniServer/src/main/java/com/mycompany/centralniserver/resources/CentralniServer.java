/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.centralniserver.resources;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("zahtev")
public class CentralniServer {

    @Resource(lookup = "queue1request")
    public Queue queue1request;
    @Resource(lookup = "queue1response")
    public Queue queue1response;
    @Resource(lookup = "queue2request")
    public Queue queue2request;
    @Resource(lookup = "queue2response")
    public Queue queue2response;
    @Resource(lookup = "queue3request")
    public Queue queue3request;
    @Resource(lookup = "queue3response")
    public Queue queue3response;

    @Resource(lookup = "jms/__defaultConnectionFactory")
    public ConnectionFactory connFactory;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @POST
    @Path("1/{naziv}")
    public Response kreirajGrad(@PathParam("naziv") String naziv) {
        String zahtev = "1|" + naziv;
        int brPodsistema = 1;
        int brZahteva = 1;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue1response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za kreiranje grada: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue1request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);

            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 1.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 1.").build();
            }

            int status = objMsg.getIntProperty("status");
            Object entity = objMsg.getObject();
            System.out.println("Primljen odgovor od Podsistem1: status=" + status + ", entity=" + entity);

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju grada.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @POST
    @Path("2/{ime}/{email}/{godiste}/{pol}/{nazivMesta}")
    public Response kreirajKorisnika(@PathParam("ime") String ime, @PathParam("email") String email,
            @PathParam("godiste") int godiste, @PathParam("pol") char pol, @PathParam("nazivMesta") String nazivMesta) {
        String zahtev = "2|" + ime + "," + email + "," + Integer.toString(godiste) + "," + pol + "," + nazivMesta;
        int brPodsistema = 1;
        int brZahteva = 2;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue1response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za kreiranje korisnika: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue1request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);

            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 1.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 1.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju korisnika.").build();

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @PUT
    @Path("3/{IdKorisnik}/{email}")
    public Response promeniEmailKorisnika(@PathParam("IdKorisnik") int IdKorisnik, @PathParam("email") String email) {
        String zahtev = "3|" + Integer.toString(IdKorisnik) + "," + email;
        int brPodsistema = 1;
        int brZahteva = 3;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue1response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za promenu emaila korisnika: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue1request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 1.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 1.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri promeni emaila korisnika.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @PUT
    @Path("4/{IdKorisnik}/{nazivMesta}")
    public Response promeniMestoKorisnika(@PathParam("IdKorisnik") int IdKorisnik, @PathParam("nazivMesta") String nazivMesta) {
        String zahtev = "4|" + Integer.toString(IdKorisnik) + "," + nazivMesta;
        int brPodsistema = 1;
        int brZahteva = 4;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue1response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za promenu mesta korisnika: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue1request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 1.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 1.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri promeni mesta korisnika.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @POST
    @Path("5/{naziv}")
    public Response kreirajKategoriju(@PathParam("naziv") String naziv) {
        String zahtev = "5|" + naziv;
        int brPodsistema = 2;
        int brZahteva = 5;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue2response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za kreiranje kategorije: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue2request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 2.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 2.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju kategorije.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @POST
    @Path("6/{IdKor}/{naziv}/{trajanje}/{datum}/{vreme}")
    public Response kreirajAudioSnimak(
            @PathParam("IdKor") int IdKor,
            @PathParam("naziv") String naziv,
            @PathParam("trajanje") int trajanje,
            @PathParam("datum") String datumStr,
            @PathParam("vreme") String vremeStr) {

        String zahtev;
        try {
            SimpleDateFormat sdfDatum = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfVreme = new SimpleDateFormat("HH:mm:ss");
            Date datum = sdfDatum.parse(datumStr);
            Date vreme = sdfVreme.parse(vremeStr);
            zahtev = "6|" + IdKor + "," + naziv + "," + trajanje + "," + datumStr + "," + vremeStr;
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Neispravan datum ili vreme.").build();
        }

        int brPodsistema = 2;
        int brZahteva = 6;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue2response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za kreiranje audio snimka: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue2request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 2.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 2.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju audio snimka.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @PUT
    @Path("7/{IdAS}/{naziv}")
    public Response promeniNazivAudioSnimka(@PathParam("IdAS") int IdAS,
            @PathParam("naziv") String naziv) {
        String zahtev = "7|" + Integer.toString(IdAS) + "," + naziv;
        int brPodsistema = 2;
        int brZahteva = 7;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue2response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za promenu naziva audio snimka: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue2request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 2.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 2.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri promeni naziva audio snimka.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @PUT
    @Path("8/{IdAS}/{nazivKat}")
    public Response dodajKategorijuAudioSnimku(@PathParam("IdAS") int IdAS, @PathParam("nazivKat") String nazivKat) {
        String zahtev = "8|" + Integer.toString(IdAS) + "," + nazivKat;
        int brPodsistema = 2;
        int brZahteva = 8;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue2response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dodavanje kategorije audio snimku: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue2request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 2.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 2.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dodavanju kategorije audio snimiku.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @POST
    @Path("9/{naziv}/{cena}")
    public Response kreirajPaket(@PathParam("naziv") String naziv, @PathParam("cena") double cena) {
        String zahtev = "9|" + naziv + "," + Double.toString(cena);
        int brPodsistema = 3;
        int brZahteva = 9;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za kreiranje paketa: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju paketa.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @PUT
    @Path("10/{IdPaket}/{cena}")
    public Response promeniMesecnuCenuPaketa(@PathParam("IdPaket") int IdPaket,
            @PathParam("cena") double cena) {
        String zahtev = "10|" + Integer.toString(IdPaket) + "," + Double.toString(cena);
        int brZahteva = 10;
        int brPodsistema = 3;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za promenu mesecne cene paketa: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri promeni mesecne cene paketa.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @POST
    @Path("11/{IdKor}/{IdPaket}/{datumPoc}/{vremePoc}")
    public Response kreirajPretplatu(
            @PathParam("IdKor") int IdKor,
            @PathParam("IdPaket") int IdPaket,
            @PathParam("datumPoc") String datumPoc,
            @PathParam("vremePoc") String vremePoc) {

        String zahtev;
        try {
            SimpleDateFormat Datum = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat Vreme = new SimpleDateFormat("HH:mm:ss");
            Date datum = Datum.parse(datumPoc);
            Date vreme = Vreme.parse(vremePoc);
            zahtev = "11|" + IdKor + "," + IdPaket + "," + datumPoc + "," + vremePoc;
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Neispravan datum ili vreme.").build();
        }

        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za kreiranje pretplate: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju pretplate.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @POST
    @Path("12/{IdKor}/{IdAudioS}/{DatumOd}/{VremeOd}/{SekundeOd}/{SekundeUkupno}")
    public Response kreirajSlusanje(
            @PathParam("IdKor") int IdKor,
            @PathParam("IdAudioS") int IdAudioS,
            @PathParam("DatumOd") String datumOd,
            @PathParam("VremeOd") String vremeOd,
            @PathParam("SekundeOd") int SekundeOd,
            @PathParam("SekundeUkupno") int SekundeUkupno) {

        String zahtev;
        try {
            SimpleDateFormat Datum = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat Vreme = new SimpleDateFormat("HH:mm:ss");
            Date datum = Datum.parse(datumOd);
            Date vreme = Vreme.parse(vremeOd);
            zahtev = "12|" + IdKor + "," + IdAudioS + "," + datumOd + "," + vremeOd + "," + SekundeOd + "," + SekundeUkupno;
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Neispravan datum ili vreme.").build();
        }

        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za kreiranje slusanja: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju slusanja.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @PUT
    @Path("13/{IdAudio}/{IdKorisnik}")
    public Response dodajPesmuUOmiljene(@PathParam("IdAudio") int IdAudio, @PathParam("IdKorisnik") int IdKorisnik) {
        String zahtev = "13|" + Integer.toString(IdAudio) + "," + Integer.toString(IdKorisnik);
        int brPodsistema = 3;
        int brZahteva = 13;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dodavanje pesme u omiljene: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dodavanju pesme u omiljene.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @POST
    @Path("14/{broj}/{datum}/{vreme}/{IdKor}/{IdAS}")
    public Response kreirajOcenu(
            @PathParam("broj") int broj,
            @PathParam("datum") String datumStr,
            @PathParam("vreme") String vremeStr,
            @PathParam("IdKor") int IdKor,
            @PathParam("IdAS") int IdAS) {

        String zahtev;
        try {
            SimpleDateFormat sdfDatum = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfVreme = new SimpleDateFormat("HH:mm:ss");
            Date datum = sdfDatum.parse(datumStr);
            Date vreme = sdfVreme.parse(vremeStr);

            zahtev = "14|" + broj + "," + datumStr + "," + vremeStr + "," + IdKor + "," + IdAS;
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Neispravan datum ili vreme.").build();
        }

        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za kreiranje ocene: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri kreiranju ocene.").build();
    }

    @PUT
    @Path("15/{IdKor}/{IdAS}/{broj}")
    public Response menjajOcenu(
            @PathParam("IdKor") int IdKor,
            @PathParam("IdAS") int IdAS,
            @PathParam("broj") int broj) {

        String zahtev = "15|" + IdKor + "," + IdAS + "," + broj;

        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za menjanje ocene: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri menjanju ocene.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @DELETE
    @Path("16/{IdKor}/{IdAS}")
    public Response obrisiOcenu(@PathParam("IdKor") int IdKor,
            @PathParam("IdAS") int IdAS) {
        String zahtev = "16|" + Integer.toString(IdKor) + "," + Integer.toString(IdAS);
        int brPodsistema = 3;
        int brZahteva = 16;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za brisanje ocene: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri brisanju ocene.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @DELETE
    @Path("17/{IdAS}/{IdKor}")
    public Response obrisiAudioSnimak(@PathParam("IdAS") int IdAS,
            @PathParam("IdKor") int IdKor) {
        String zahtev = "17|" + Integer.toString(IdAS) + "," + Integer.toString(IdKor);
        int brPodsistema = 2;
        int brZahteva = 17;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue2response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za brisanje audio snimka: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue2request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 2.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 2.").build();
            }
            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri brisanju audio snimka.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("18")
    public Response dohvatiSvaMesta() {
        String zahtev = "18|";
        int brPodsistema = 1;
        int brZahteva = 18;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue1response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje mesta: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue1request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 1.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 1.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju mesta.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("19")
    public Response dohvatiSveKorisnike() {
        String zahtev = "19|";
        int brPodsistema = 1;
        int brZahteva = 19;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue1response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje korisnika: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue1request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 1.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 1.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju korisnika.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("20")
    public Response dohvatiSveKategorije() {
        String zahtev = "20|";
        int brPodsistema = 2;
        int brZahteva = 20;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue2response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje svih kategorija: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue2request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 2.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 2.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju kategorija.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("21")
    public Response dohvatiSveAudioSnimke() {
        String zahtev = "21|";
        int brPodsistema = 2;
        int brZahteva = 21;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue2response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje svih audio snimaka: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue2request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 2.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 2.").build();
            }
            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju audio snimaka.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("22/{IdAS}")
    public Response dohvatiKategorijeAudioSnimka(@PathParam("IdAS") int IdAS) {
        String zahtev = "22|" + Integer.toString(IdAS);
        int brPodsistema = 2;
        int brZahteva = 22;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue2response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje svih kategorija audio snimaka: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue2request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 2.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 2.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju kategorija audio snimka.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("23")
    public Response dohvatiPakete() {
        String zahtev = "23|";
        int brPodsistema = 3;
        int brZahteva = 23;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje paketa: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju paketa.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("24/{IdKor}")
    public Response dohvatiPretplate(@PathParam("IdKor") int IdKor) {
        String zahtev = "24|" + Integer.toString(IdKor);
        int brPodsistema = 3;
        int brZahteva = 24;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje pretplate: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }
            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju pretplata.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("25/{IdAudioS}")
    public Response dohvatiSlusanja(@PathParam("IdAudioS") int IdAudioS) {
        String zahtev = "25|" + Integer.toString(IdAudioS);
        int brPodsistema = 3;
        int brZahteva = 25;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje slusanja: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju slusanja.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("26/{IdAS}")
    public Response dohvatiOcene(@PathParam("IdAS") int IdAS) {
        String zahtev = "26|" + Integer.toString(IdAS);
        int brPodsistema = 3;
        int brZahteva = 26;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje ocene: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju ocena.").build();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @GET
    @Path("27/{IdKorisnik}")
    public Response dohvatiOmiljenePesme(@PathParam("IdKorisnik") int IdKorisnik) {
        String zahtev = "27|" + Integer.toString(IdKorisnik);
        int brPodsistema = 3;
        int brZahteva = 27;
        try {
            JMSContext context = connFactory.createContext();
            JMSConsumer consumer = context.createConsumer(queue3response);
            JMSProducer producer = context.createProducer();

            TextMessage txtMsg = context.createTextMessage(zahtev);
            System.out.println("Šaljem zahtev za dohvatanje omiljene pesme korisnika: " + zahtev);

            while (consumer.receiveNoWait() != null) {
                System.out.println("Uklonjena stara poruka iz reda.");
            }

            producer.send(queue3request, txtMsg);

            ObjectMessage objMsg = (ObjectMessage) consumer.receive(5000);
            if (objMsg == null) {
                System.out.println("Nema odgovora od Podsistema 3.");
                consumer.close();
                context.close();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Nema odgovora od Podsistema 3.").build();
            }

            consumer.close();
            context.close();

            return Response.status(objMsg.getIntProperty("status")).entity(objMsg.getObject()).build();

        } catch (JMSException ex) {
            Logger.getLogger(CentralniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dohvatanju omiljenih pesama.").build();
    }

}
