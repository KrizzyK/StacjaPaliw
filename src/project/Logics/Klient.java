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

            boolean czyZatankowano = stacjaPaliw.zatankujSamochod(zamowienieKlienta, getName());

            if(czyZatankowano) { // jesli bylo co tankowac
                Thread.sleep(gen.nextInt(4000)+ 1000); // czas tankowania
            }

            stacjaPaliw.opuscStanowisko(nrStanowiska, this);

            if(czyZatankowano) {
                int nrKasy = stacjaPaliw.zajmijKase(this);

                Thread.sleep(gen.nextInt(5000));    // czas zaplaty

                stacjaPaliw.zwolnijKase(nrKasy, this);
            }

            stacjaPaliw.wyjazdKlienta( this );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getIdKlienta() {
        return idKlienta;
    }
}
