package project.Logics;

import javafx.application.Platform;
import project.Main;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class StacjaPaliw {
    private volatile Main guiClass;

    private volatile Lock wjazdNaStacjeLock = new ReentrantLock();
    private volatile Condition czyJestMiejsceNaStacji = wjazdNaStacjeLock.newCondition();
    private volatile Condition czyOtworzyliStacje = wjazdNaStacjeLock.newCondition();
    private volatile boolean czyOtwarta = true;

    private volatile Stanowisko[] stanowiska;
    private volatile Lock stanowiskaLock = new ReentrantLock();
    private volatile Condition[] czyZwolnionoStanowisko;
    private volatile boolean[] czyStanowiskoWolne;

    private volatile Lock kasyLock = new ReentrantLock();
    private volatile Condition[] czyZwolnionoKase;
    private volatile boolean[] czyKasaWolna;

    private static volatile Map<RodzajPaliwa, Integer> iloscPaliwNaStacji;
    static {
        iloscPaliwNaStacji = new LinkedHashMap<>();
        iloscPaliwNaStacji.put(RodzajPaliwa.benzyna95, 100);
        iloscPaliwNaStacji.put(RodzajPaliwa.benzyna98, 100);
        iloscPaliwNaStacji.put(RodzajPaliwa.verva98, 100);
        iloscPaliwNaStacji.put(RodzajPaliwa.ekoDieselUltra, 100);
        iloscPaliwNaStacji.put(RodzajPaliwa.EkoDieselUltra2, 100);
        iloscPaliwNaStacji.put(RodzajPaliwa.vervaOn, 100);
    }


    private int maxKlientow;
    private int iloscKlientow = 0;

    public StacjaPaliw(int maxKlientow, int liczbaStanowisk, int liczbaKas, Main guiClass) {
        this.guiClass = guiClass;
        this.maxKlientow = maxKlientow;

        czyZwolnionoKase = new Condition[liczbaKas];
        czyKasaWolna = new boolean[liczbaKas];
        for (int i = 0; i < liczbaKas; i++) {
            czyKasaWolna[i] = true;
            czyZwolnionoKase[i] = kasyLock.newCondition();
        }


        stanowiska = new Stanowisko[liczbaStanowisk];
        czyZwolnionoStanowisko = new Condition[liczbaStanowisk];
        czyStanowiskoWolne = new boolean[liczbaStanowisk];

        for (int i = 0; i < stanowiska.length; i++) {
            stanowiska[i] = new Stanowisko(liczbaStanowisk, i);
            czyZwolnionoStanowisko[i] = stanowiskaLock.newCondition();
            czyStanowiskoWolne[i] = true;
        }
    }


    public void zamknijStacje(){
        wjazdNaStacjeLock.lock();
        Platform.runLater( () -> guiClass.listaKomunikatow.add(0,"Zamykam stacje.") );

        czyOtwarta = false;
        wjazdNaStacjeLock.unlock();
    }
    public void otworzStacje() {
        wjazdNaStacjeLock.lock();
        guiClass.listaKomunikatow.add(0,"Otwieram stacje.");

        czyOtwarta = true;
        czyOtworzyliStacje.signalAll();
        wjazdNaStacjeLock.unlock();
    }

    public void wjazdKlienta(Klient klient) throws InterruptedException {
        wjazdNaStacjeLock.lock();
        while(maxKlientow == iloscKlientow) czyJestMiejsceNaStacji.await();
        while(czyOtwarta == false ) czyOtworzyliStacje.await();

        iloscKlientow++;
        int iloscKl = iloscKlientow;
        Platform.runLater( () -> {
            guiClass.listaKomunikatow.add(0,"Wchodzi: " +  klient.getName() + ". Ilosc klientow w srodku: " + iloscKl);
            guiClass.pokazSamochodNaStacji( klient.getIdKlienta());
        } );

        wjazdNaStacjeLock.unlock();
    }
    public void wyjazdKlienta(Klient klient) {
        wjazdNaStacjeLock.lock();

        iloscKlientow--;
        Platform.runLater( () -> {
                    guiClass.listaKomunikatow.add(0,"Wychodzi: " +  klient.getName() + ". Ilosc klientow w srodku: " + iloscKlientow);
                } );


        czyJestMiejsceNaStacji.signal();

        wjazdNaStacjeLock.unlock();
    }

    public void dostawaPaliwa(RodzajPaliwa rodzajPaliwa, int ileLitrow) {
        iloscPaliwNaStacji.put(rodzajPaliwa, iloscPaliwNaStacji.get(rodzajPaliwa) + ileLitrow );
    }
    // jesli nie ma ani troche paliwa -> odjedz bez zaplaty
    // jesli jest ale nie wystarczajaco -> zatankuj ile mozesz i zaplac
    // jesli jest wystarczajaco -> wszystko gra
    // return code -> true, jesli zatankowano (cokolwiek), wiec trzeba zaplacic
    // return code -> false, jesli nie zatankowano nic
    public boolean zatankujSamochod(ZamowienieKlienta zamowienie, String klient) throws InterruptedException {
        RodzajPaliwa rodzajPaliwa = zamowienie.getRodzajPaliwa() ;
        int ileLitrow = zamowienie.getIleLitrow() ;
        int paliwaNaStacji = iloscPaliwNaStacji.get(rodzajPaliwa);
        if(paliwaNaStacji == 0)  {
            Platform.runLater( () -> guiClass.listaKomunikatow.add(0,klient + " nie zatankowal - brak paliwa. Opuszczanie stacji bez zaplaty") );
            return  false; // brak tego paliwa
        }

        int ileZostanieNaStacji = paliwaNaStacji - ileLitrow;
        if(ileZostanieNaStacji < 0 ) { // niewystarczajaca ilosc paliwa
            iloscPaliwNaStacji.put(rodzajPaliwa, 0 );
            return true;
        }
        iloscPaliwNaStacji.put(rodzajPaliwa, ileZostanieNaStacji ); // paliwa jest wystarczajaco -> tankowanie wszystkiego
        Platform.runLater( () ->guiClass.listaKomunikatow.add(0, klient + " zatankowal: " + zamowienie+ ", stanPaliw = " + stanPaliwaNaStacji()) );
        return true;
    }
    //zwraca nrZajetegoStanowiska
    public int zajmijStanowisko(RodzajPaliwa rodzajPaliwa, Klient klient) throws Exception {
        stanowiskaLock.lock();

        int nrStanowiska = znajdzStanowisko(rodzajPaliwa);
        while( czyStanowiskoWolne[nrStanowiska] == false )
            czyZwolnionoStanowisko[nrStanowiska].await();

        Platform.runLater( () -> {
            guiClass.listaKomunikatow.add(0,klient.getName() + " rozpoczyna tankowanie przy stanowisku " + nrStanowiska );
            guiClass.ukryjSamochodNaStacji(klient.getIdKlienta() );
            guiClass.pokazSamochodNaStanowisku(nrStanowiska, klient.getIdKlienta() );
        });

        czyStanowiskoWolne[nrStanowiska] = false;

        stanowiskaLock.unlock();
        return nrStanowiska;
    }




    public void opuscStanowisko(int nrStanowiska, Klient klient) {
        stanowiskaLock.lock();

        czyStanowiskoWolne[nrStanowiska] = true;
        Platform.runLater( () ->  guiClass.ukryjSamochodNaStanowisku(nrStanowiska) );


        czyZwolnionoStanowisko[nrStanowiska].signal();
//        Platform.runLater( () -> {
//            guiClass.pokazSamochodPrzedKasa(klient.getIdKlienta());
//        });

        stanowiskaLock.unlock();
    }

    public int zajmijKase(Klient klient) throws InterruptedException {
        kasyLock.lock();
        int nrKasy = znajdzKase();
        while(czyKasaWolna[nrKasy] == false)
            czyZwolnionoKase[nrKasy].await();

        czyKasaWolna[nrKasy] = false;

        Platform.runLater( () -> {
            guiClass.ukryjSamochodPrzedKasa(klient.getIdKlienta());
            guiClass.pokazSamochodyPrzyKasie(nrKasy, klient.getIdKlienta());
            guiClass.listaKomunikatow.add(0,"Dokonywanie zaplaty przez " + klient.getName()  );
        });

        kasyLock.unlock();
        return nrKasy;
    }

    public void zwolnijKase(int nrKasy, Klient klient) {
        kasyLock.lock();

        czyKasaWolna[nrKasy] = true;
        czyZwolnionoKase[nrKasy].signal();

        Platform.runLater( () -> {
            guiClass.listaKomunikatow.add(0,"Dokonano zaplaty przez " + klient.getName() );
            guiClass.ukryjSamochodPrzyKasie(nrKasy);
        } );
        kasyLock.unlock();
    }

    // funkcja zwraca indeks jednego z zajetych, jesli kazde jest zajete, w przeciwnym wypadku indeks pierwszego wolnego
    private int znajdzStanowisko(RodzajPaliwa rodzajPaliwa) throws Exception {
        for (int i = 0; i < stanowiska.length; i++) {
            if( czyStanowiskoWolne[i] && stanowiska[i].czyStanowiskoObslugujePaliwo(rodzajPaliwa) ) return i;
        }
        for (int i = 0; i < stanowiska.length; i++) {
            if(stanowiska[i].czyStanowiskoObslugujePaliwo(rodzajPaliwa)) return i;
        }
        throw new Exception("znajdzStanowisko method exception");
    }

    // funkcja zwraca LOSOWY indeks jednej z zajetych, jesli kazda jest zajete, w przeciwnym wypadku indeks pierwszego wolnego
    private int znajdzKase() {
        for (int i = 0; i < czyKasaWolna.length; i++) {
            if(czyKasaWolna[i]) return i;
        }
        System.out.println("Wybieram random");
        Random generator = new Random();
        return generator.nextInt( czyKasaWolna.length );
    }

    public String stanPaliwaNaStacji() {
        String str = "[";
        for (Map.Entry<RodzajPaliwa, Integer> entry : iloscPaliwNaStacji.entrySet()) str = str.concat( entry.getValue().toString() +", " );
        str = str.concat("]");
        return str;
    }
    public String stanStanowisk() {
        String str = "[";
        for (int i = 0; i < czyStanowiskoWolne.length; i++) {
            str = str.concat(boolToInt(czyStanowiskoWolne[i]) + ", ");
        }
        str = str.concat("]");
        return str;
    }
    public String stanKas() {
        String str = "[";
        for (int i = 0; i < czyKasaWolna.length; i++) {
            str = str.concat( boolToInt(czyKasaWolna[i]) + ", " );
        }
        str = str.concat("]");
        return str;
    }
    private String boolToInt(Boolean b) {
        if(b) return "1";
        return  "0";
    }

    public void stanWKolejceDoKasy(Klient klient) {
        Platform.runLater( () -> {
            guiClass.pokazSamochodPrzedKasa(klient.getIdKlienta());
        } );
    }
}
