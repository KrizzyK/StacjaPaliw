package project.Logics;

import java.util.Random;

public class Klient extends Thread {

    private int idKlienta;
    private ZamowienieKlienta zamowienieKlienta;
    private StacjaPaliw stacjaPaliw;


    public Klient(int idKlienta, StacjaPaliw stacjaPaliw) {
        super("Klient" + idKlienta);
        this.idKlienta = idKlienta;
        this.stacjaPaliw = stacjaPaliw;
        zamowienieKlienta = new ZamowienieKlienta();
    }

    @Override
    public void run() {
        int nrStanowiska;
        try {
            Random gen = new Random();
            stacjaPaliw.wjazdKlienta( this );
            Thread.sleep(gen.nextInt(4000));

            nrStanowiska = stacjaPaliw.zajmijStanowisko( zamowienieKlienta.getRodzajPaliwa(), this );

            boolean czyZatankowano = zatankuj();

            stacjaPaliw.opuscStanowisko(nrStanowiska, this);

            if(czyZatankowano) idzDoKasy();

            stacjaPaliw.wyjazdKlienta( this );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean zatankuj() throws InterruptedException {
        Random gen = new Random();
        boolean czyZatankowano = stacjaPaliw.zatankujSamochod(zamowienieKlienta, getName());

        return czyZatankowano;
    }

    private void idzDoKasy() throws InterruptedException {
        stacjaPaliw.zaplacPrzyKasie(this);
    }


    public int getIdKlienta() {
        return idKlienta;
    }
}
