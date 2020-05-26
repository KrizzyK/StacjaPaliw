package project.Logics;// opis: każde stanowisko ma ilosc dystrybutorow dostosowana do ilosci stanowisk
//  jesli jest 1 stanowisko -> dystrybutorow musi byc tyle, ile jest rodzajow paliwa
// jesli sa 2 stanowiska -> dystrybutorow moze byc np.: 3 lub wiecej na kazdym

import java.util.Arrays;

public class Stanowisko {
    private RodzajPaliwa[] dystrybutory;

    public Stanowisko(int liczbaStanowisk, int nrStanowiska) { // liczba dystrybutorow jest zalezna od ilosci stanowisk ->

        if(liczbaStanowisk <= 1) dystrybutory = new RodzajPaliwa[6]; // 1 stanowisko musi obslugiwac wszystkie paliwa
        else if(liczbaStanowisk >= 2 && liczbaStanowisk <= 3) dystrybutory = new RodzajPaliwa[3]; // jesli sa 2 stanowiska to kazde musi 3 paliwa obsluzyc
        else if(liczbaStanowisk>=4) dystrybutory = new RodzajPaliwa[2]; //po 2 dystrybutory, jesli duza liczba stanowisk

        for (int i = 0; i < dystrybutory.length; i++) {
            dystrybutory[i] = RodzajPaliwa.valueOf( ((nrStanowiska * 2) + i ) %6 );
        }

    }

    public boolean czyStanowiskoObslugujePaliwo(RodzajPaliwa rodzajPaliwa) {
        return Arrays.asList(dystrybutory).contains(rodzajPaliwa);
    }
}
