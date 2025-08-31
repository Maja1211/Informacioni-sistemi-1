package com.mycompany.klijent;

import java.util.Scanner;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class Klijent {

    private static final String BASE_URL = "http://localhost:8080/CentralniServer/api/zahtev/";
    private static Client client;
    private static Scanner scanner;

    public static void main(String[] args) {
        client = ClientBuilder.newClient();
        scanner = new Scanner(System.in);
        boolean radi = true;

        while (radi) {
            prikaziMeni();
            System.out.println("Unesite broj zahteva: ");
            String unos = scanner.nextLine();

            try {
                int opcija = Integer.parseInt(unos);

                switch (opcija) {
                    case 1:
                        kreirajGrad();
                        break;
                    case 2:
                        kreirajKorisnika();
                        break;
                    case 3:
                        promeniEmailKorisnika();
                        break;
                    case 4:
                        promeniMestoKorisnika();
                        break;
                    case 5:
                        kreirajKategoriju();
                        break;
                    case 6:
                        kreirajAudioSnimak();
                        break;
                    case 7:
                        promeniNazivAudioSnimka();
                        break;
                    case 8:
                        dodajKategorijuAudioSnimku();
                        break;
                    case 9:
                        kreirajPaket();
                        break;
                    case 10:
                        promeniCenuPaketa();
                        break;
                    case 11:
                        kreirajPretplatu();
                        break;
                    case 12:
                        kreirajSlusanje();
                        break;
                    case 13:
                        dodajOmiljenuPesmu();
                        break;
                    case 14:
                        kreirajOcenu();
                        break;
                    case 15:
                        menjajOcenu();
                        break;
                    case 16:
                        obrisiOcenu();
                        break;
                    case 17:
                        obrisiAudioSnimak();
                        break;
                    case 18:
                        dohvatiSvaMesta();
                        break;
                    case 19:
                        dohvatiSveKorisnike();
                        break;
                    case 20:
                        dohvatiSveKategorije();
                        break;
                    case 21:
                        dohvatiSveAudioSnimke();
                        break;
                    case 22:
                        dohvatiKategorijeAudioSnimka();
                        break;
                    case 23:
                        dohvatiPakete();
                        break;
                    case 24:
                        dohvatiPretplate();
                        break;
                    case 25:
                        dohvatiSlusanja();
                        break;
                    case 26:
                        dohvatiOcene();
                        break;
                    case 27:
                        dohvatiOmiljenePesme();
                        break;
                    case 28:
                        radi = false;
                        break;
                    default:
                        System.out.println("Nepostojeca opcija. Pokusajte ponovo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Unos nevalidan, pokušajte ponovo.");
            }
        }
        client.close();
    }

    private static void prikaziMeni() {
        System.out.println("MENI :");
        System.out.println(" 1. Kreiraj grad.");
        System.out.println(" 2. Kreiraj korisnika.");
        System.out.println(" 3. Promeni mejl korisnika.");
        System.out.println(" 4. Promeni mesto korisnika.");
        System.out.println(" 5. Kreiraj kategoriju.");
        System.out.println(" 6. Kreiraj audio snimak.");
        System.out.println(" 7. Promeni naziv audio snimku.");
        System.out.println(" 8. Dodaj kategoriju audio snimku.");
        System.out.println(" 9. Kreiraj paket.");
        System.out.println("10. Promeni mesecnu cenu paketa.");
        System.out.println("11. Kreiraj pretplatu korisnika na paket.");
        System.out.println("12. Kreiraj slusanje.");
        System.out.println("13. Dodaj audio snimak u omiljene.");
        System.out.println("14. Kreiraj ocenu korisnika za audio snimak.");
        System.out.println("15. Izmeni ocenu audio snimku.");
        System.out.println("16. Obrisi ocenu audio snimku.");
        System.out.println("17. Obrisi audio snimak.");
        System.out.println("18. Dohvati mesta.");
        System.out.println("19. Dohvati korisnike.");
        System.out.println("20. Dohvati kategorije.");
        System.out.println("21. Dohvati audio snimke.");
        System.out.println("22. Dohvati kategorije za audio snimak.");
        System.out.println("23. Dohvati sve pakete.");
        System.out.println("24. Dohvati pretplate za korisnika.");
        System.out.println("25. Dohvati sva slusanja za audio snimak.");
        System.out.println("26. Dohvati sve ocene za audio snimak.");
        System.out.println("27. Dohvati listu omiljenih audio snimaka za korisnika.");
        System.out.println("28. Zavrsi program.");

    }

    private static void kreirajGrad() {
        System.out.println("Unesite naziv grada: ");
        String naziv = scanner.nextLine();
        posaljiPostZahtev("1/" + naziv);
    }

    private static void kreirajKorisnika() {
        System.out.println("Unesite ime: ");
        String ime = scanner.nextLine();
        System.out.println("Unesite email: ");
        String email = scanner.nextLine();
        System.out.println("Unesite godiste: ");
        int godiste = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite pol(m/z): ");
        char pol = scanner.nextLine().charAt(0);
        System.out.println("Unesite naziv mesta: ");
        String nazivMesta = scanner.nextLine();
        posaljiPostZahtev("2/" + ime + "/" + email + "/" + godiste + "/" + pol + "/" + nazivMesta);
    }

    private static void promeniEmailKorisnika() {
        System.out.println("Unesite ID korisnika: ");
        int idKorisnik = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite novi email: ");
        String email = scanner.nextLine();
        posaljiPutZahtev("3/" + idKorisnik + "/" + email);
    }

    private static void promeniMestoKorisnika() {
        System.out.println("Unesite ID korisnika: ");
        int idKorisnik = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite novi naziv mesta: ");
        String nazivMesta = scanner.nextLine();
        posaljiPutZahtev("4/" + idKorisnik + "/" + nazivMesta);
    }

    private static void kreirajKategoriju() {
        System.out.println("Unesite naziv kategorije: ");
        String naziv = scanner.nextLine();
        posaljiPostZahtev("5/" + naziv);
    }

    private static void kreirajAudioSnimak() {
        System.out.println("Unesite ID korisnika: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite naziv snimka: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite trajanje u sekundama: ");
        int trajanje = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite datum postavljanja(yyyy-MM-dd): ");
        String datum = scanner.nextLine();
        System.out.println("Unesite vreme postavljanja(HH:mm:ss): ");
        String vreme = scanner.nextLine();
        posaljiPostZahtev("6/" + idKor + "/" + naziv + "/" + trajanje + "/" + datum + "/" + vreme);
    }

    private static void promeniNazivAudioSnimka() {
        System.out.println("Unesite ID audio snimka: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite novi naziv snimka: ");
        String naziv = scanner.nextLine();
        posaljiPutZahtev("7/" + idAS + "/" + naziv);
    }

    private static void dodajKategorijuAudioSnimku() {
        System.out.println("Unesite ID audio snimka: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite naziv kategorije: ");
        String nazivKat = scanner.nextLine();
        posaljiPutZahtev("8/" + idAS + "/" + nazivKat);
    }

    private static void kreirajPaket() {
        System.out.println("Unesite naziv paketa: ");
        String naziv = scanner.nextLine();
        System.out.println("Unesite cenu: ");
        double cena = scanner.nextDouble();
        scanner.nextLine();
        posaljiPostZahtev("9/" + naziv + "/" + cena);
    }

    private static void promeniCenuPaketa() {
        System.out.println("Unesite ID paketa: ");
        int idPaket = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite novu cenu: ");
        double cena = scanner.nextDouble();
        scanner.nextLine();
        posaljiPutZahtev("10/" + idPaket + "/" + cena);
    }

    private static void kreirajPretplatu() {
        System.out.println("Unesite ID korisnika: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite ID paketa: ");
        int idPaketa = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite datum pocetka pretplate(yyyy-MM-dd): ");
        String datumPoc = scanner.nextLine();
        System.out.println("Unesite vreme pocetka pretplate(HH:mm:ss): ");
        String vremePoc = scanner.nextLine();
        posaljiPostZahtev("11/" + idKor + "/" + idPaketa + "/" + datumPoc + "/" + vremePoc);
    }

    private static void kreirajSlusanje() {
        System.out.println("Unesite ID korisnika: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite ID audio snimka: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite datum slusanja(yyyy-MM-dd): ");
        String datumOd = scanner.nextLine();
        System.out.println("Unesite vreme slusanja(HH:mm:ss): ");
        String vremeOd = scanner.nextLine();
        System.out.println("Unesite sekundu od koje se slusa: ");
        int sekundeOd = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite koliko sekundi je odslusano: ");
        int sekundeUkupno = scanner.nextInt();
        scanner.nextLine();
        posaljiPostZahtev("12/" + idKor + "/" + idAS + "/" + datumOd + "/" + vremeOd + "/" + sekundeOd + "/" + sekundeUkupno);
    }

    private static void dodajOmiljenuPesmu() {
        System.out.println("Unesite ID audio snimka: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite ID korisnika: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        posaljiPutZahtev("13/" + idAS + "/" + idKor);
    }

    private static void kreirajOcenu() {
        System.out.println("Unesite ocenu od 1 do 5: ");
        int broj = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite datum kreiranja ocene(yyyy-MM-dd): ");
        String datum = scanner.nextLine();
        System.out.println("Unesite vreme kreiranja ocene(HH:mm:ss): ");
        String vreme = scanner.nextLine();
        System.out.println("Unesite ID korisnika koji kreira ocenu: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite ID audio snimka kome se dodeljuje ocena: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        posaljiPostZahtev("14/" + broj + "/" + datum + "/" + vreme + "/" + idKor + "/" + idAS);
    }

    private static void menjajOcenu() {
        System.out.println("Unesite novu ocenu od 1 do 5: ");
        int broj = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite ID korisnika koji menja ocenu: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite ID audio snimka kome se menja ocena: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        posaljiPutZahtev("15/" + idKor + "/" + idAS + "/" + broj);
    }

    private static void obrisiOcenu() {
        System.out.println("Unesite ID korisnika koji je kreirao ocenu koju zelite da izbrisete: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite ID audio snimka kome se brise ocena: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        posaljiDeleteZahtev("16/" + idKor + "/" + idAS);
    }

    private static void obrisiAudioSnimak() {
        System.out.println("Unesite ID audio snimka koga zelite da obrisete: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Unesite ID korisnika koji brise audio snimak: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        posaljiDeleteZahtev("17/" + idAS + "/" + idKor);
    }

    private static void dohvatiSvaMesta() {
        posaljiGetZahtev("18/");
    }

    private static void dohvatiSveKorisnike() {
        posaljiGetZahtev("19/");
    }

    private static void dohvatiSveKategorije() {
        posaljiGetZahtev("20/");
    }

    private static void dohvatiSveAudioSnimke() {
        posaljiGetZahtev("21/");
    }

    private static void dohvatiKategorijeAudioSnimka() {
        System.out.println("Unesite ID audio snimka za koga zelite da dohvatite kategorije: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        posaljiGetZahtev("22/" + idAS);
    }

    private static void dohvatiPakete() {
        posaljiGetZahtev("23/");
    }

    private static void dohvatiPretplate() {
        System.out.println("Unesite ID korisnika za koga zelite da dohvatite pretplate: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        posaljiGetZahtev("24/" + idKor);
    }

    private static void dohvatiSlusanja() {
        System.out.println("Unesite ID audio snimka za koga zelite da dohvatite slusanja: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        posaljiGetZahtev("25/" + idAS);
    }

    private static void dohvatiOcene() {
        System.out.println("Unesite ID audio snimka za koga zelite da dohvatite ocene: ");
        int idAS = scanner.nextInt();
        scanner.nextLine();
        posaljiGetZahtev("26/" + idAS);
    }

    private static void dohvatiOmiljenePesme() {
        System.out.println("Unesite ID korisnika za koga zelite da dohvatite omiljene pesme: ");
        int idKor = scanner.nextInt();
        scanner.nextLine();
        posaljiGetZahtev("27/" + idKor);
    }

    private static void posaljiPostZahtev(String path) {
        Response zahtev = client.target(BASE_URL + path).request().post(Entity.text(""));
        handleResponse(zahtev);
    }

    private static void posaljiPutZahtev(String path) {
        Response zahtev = client.target(BASE_URL + path).request().put(Entity.text(""));
        handleResponse(zahtev);
    }

    private static void posaljiGetZahtev(String path) {
        Response zahtev = client.target(BASE_URL + path).request().get();
        handleResponse(zahtev);
    }

    private static void posaljiDeleteZahtev(String path) {
        Response zahtev = client.target(BASE_URL + path).request().delete();
        handleResponse(zahtev);
    }

    private static void handleResponse(Response zahtev) {
        String entity = zahtev.readEntity(String.class);
        System.out.println("Odgovor: " + entity + " (status: " + zahtev.getStatus() + ")");
    }

}
