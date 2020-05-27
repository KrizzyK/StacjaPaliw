package project.Logics;

import java.util.HashMap;
import java.util.Map;

public enum RodzajPaliwa {
    benzyna95(0), benzyna98(1),
    verva98(2), EkoDieselUltra(3),
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

    public String getShortString() {
        System.out.println("Siem");
        switch(this) {
            case verva98: return "v98";
            case vervaOn: return "vOn";
            case benzyna98: return "b98";
            case EkoDieselUltra: return  "EDU";
            case EkoDieselUltra2: return "ED2";
            case benzyna95: return "b96";
        }
        System.out.println("Sieee");
        return "";
    }

}

// wziete z https://www.orlen.pl/PL/DlaBiznesu/Paliwa/Strony/default.aspx