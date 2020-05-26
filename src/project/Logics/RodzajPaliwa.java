package project.Logics;

import java.util.HashMap;
import java.util.Map;

public enum RodzajPaliwa {
    benzyna95(0), benzyna98(1),
    verva98(2), ekoDieselUltra(3),
    EkoDieselUltra2(4), vervaOn(5);

    private int value;
    private static Map map = new HashMap<>();

    private RodzajPaliwa(int value) {
        this.value = value;
    }

    static {
        for (RodzajPaliwa paliwo : RodzajPaliwa.values()) {
            map.put(paliwo.value, paliwo);
        }
    }

    public static RodzajPaliwa valueOf(int paliwo) {
        return (RodzajPaliwa) map.get(paliwo);
    }

    public int getValue() {
        return value;
    }

}

// wziete z https://www.orlen.pl/PL/DlaBiznesu/Paliwa/Strony/default.aspx