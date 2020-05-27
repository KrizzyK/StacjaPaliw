package project.Logics;// opis: każde stanowisko ma ilosc dystrybutorow dostosowana do ilosci stanowisk
//  jesli jest 1 stanowisko -> dystrybutorow musi byc tyle, ile jest rodzajow paliwa
// jesli sa 2 stanowiska -> dystrybutorow moze byc np.: 3 lub wiecej na kazdym

import java.util.Arrays;

public class Stanowisko {
    private RodzajPaliwa[] dystrybutory;
    private String napisNaStanowisku;

    public Stanowisko(int liczbaStanowisk, int nrStanowiska) { // liczba dystrybutorow jest zalezna od ilosci stanowisk ->
        napisNaStanowisku = new String();

        if(liczbaStanowisk <= 1) dystrybutory = new RodzajPaliwa[6]; // 1 stanowisko musi obslugiwac wszystkie paliwa
        else if(liczbaStanowisk>=4) dystrybutory = new RodzajPaliwa[2]; //po 2 dystrybutory, jesli duza liczba stanowisk
        else if(liczbaStanowisk >= 2 && liczbaStanowisk <= 3) dystrybutory = new RodzajPaliwa[3]; // jesli sa 2/3 stanowiska to kazde obsluguje 3 paliwa
        if(liczbaStanowisk == 2) {
            for (int i = 0; i < dystrybutory.length; i++) {
                dystrybutory[i] = RodzajPaliwa.valueOf( ((nrStanowiska * 3) + i ) %6 );
                napisNaStanowisku =  napisNaStanowisku + dystrybutory[i].getShortString()+ ",";
            }
        } else {
            for (int i = 0; i < dystrybutory.length; i++) {
                dystrybutory[i] = RodzajPaliwa.valueOf( ((nrStanowiska * 2) + i ) %6 );
                napisNaStanowisku =  napisNaStanowisku + dystrybutory[i].getShortString()+ ",";
            }
        }

    }

    public String getNapisNaStanowisku() {
        return napisNaStanowisku;
    }

    public boolean czyStanowiskoObslugujePaliwo(RodzajPaliwa rodzajPaliwa) {
        return Arrays.asList(dystrybutory).contains(rodzajPaliwa);
    }
}
