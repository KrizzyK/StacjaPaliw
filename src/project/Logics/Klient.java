package project.Logics;

import java.util.Random;

public class Klient extends Thread {

    private int idKlienta;
    private ZamowienieKlienta zamowienieKlienta;
    private StacjaPaliw stacjaPaliw;
    Random gen;


    public Klient(int idKlienta, StacjaPaliw stacjaPaliw) {
        super("Klient" + idKlienta);
        this.idKlienta = idKlienta;
        this.stacjaPaliw = stacjaPaliw;
        zamowienieKlienta = new ZamowienieKlienta();
        gen = new Random();
    }

    @Override
    public void run() {
        int nrStanowiska;
        try {
            stacjaPaliw.wjazdKlienta( this );
            Thread.sleep(gen.nextInt(4000));

            nrStanowiska = stacjaPaliw.zajmijStanowisko( zamowienieKlienta.getRodzajPaliwa(), this );

            boolean czyZatankowano = zatankuj();

            stacjaPaliw.opuscStanowisko(nrStanowiska, this);

            if(czyZatankowano) zaplacPrzyKasie();

            stacjaPaliw.wyjazdKlienta( this );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean zatankuj() throws InterruptedException {
        Random gen = new Random();
        Thread.sleep(gen.nextInt(5000)); // czas tankowania

        boolean czyZatankowano = stacjaPaliw.zatankujSamochod(zamowienieKlienta, getName());
        return czyZatankowano;
    }

    private void zaplacPrzyKasie() throws InterruptedException {
        stacjaPaliw.stanWKolejceDoKasy(this);
        int nrKasy = stacjaPaliw.zajmijKase(this);
        Thread.sleep(gen.nextInt(5000));    // czas zaplaty
        stacjaPaliw.zwolnijKase(nrKasy, this);
    }


    public int getIdKlienta() {
        return idKlienta;
    }
}
