package project.Logics;

import java.util.Random;

public class ZamowienieKlienta {
    private RodzajPaliwa rodzajPaliwa;
    private int ileLitrow;
    private static volatile Random generatorZamowienia = new Random();

    public ZamowienieKlienta() { // calkowicie losowe wartości
        int los = generatorZamowienia.nextInt(6);
        rodzajPaliwa = RodzajPaliwa.valueOf(los);
        los = generatorZamowienia.nextInt(10) + 1;
        ileLitrow = los;
    }

    public RodzajPaliwa getRodzajPaliwa() {
        return rodzajPaliwa;
    }

    public int getIleLitrow() {
        return ileLitrow;
    }

    @Override
    public String toString() {
        return "Zamowienie=" + rodzajPaliwa.name() + " " + ileLitrow + "}";
    }
}
