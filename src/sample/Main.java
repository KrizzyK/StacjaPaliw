package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    static final int szerokoscOkna = 1600, wysokoscOkna = 900;
    static final int szerokoscAuta = 10, wysokoscAuta = 5;

    StackPane[] samochodyNaStanowiskach;

    int iloscStanowisk;
    int iloscKas;
    int maxIloscSamochodow;
    BorderPane layout = new BorderPane();


    @Override
    public void start(Stage window) throws Exception{
        // podstawowy layout
        layout.setTop( getTopMenuLayout() );
        layout.setRight( getRightMenuLayout() );
        layout.setBottom( getBottomMenuLayout() );

        window.setTitle("Stacja Paliw");
        window.setScene(new Scene(layout, szerokoscOkna, wysokoscOkna));
        window.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


    public VBox getRightMenuLayout() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Text title = new Text("Komunikaty");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        ObservableList<String> listaKomunikatow = FXCollections.observableArrayList();
        ListView<String> widokKomunikatow = new ListView<String>(listaKomunikatow);
        vbox.getChildren().add(widokKomunikatow);

        return vbox;
    }
    public HBox getStationLayout(int liczbaStanowisk, int liczbaKas, int maxLiczbaSamochodow) {
        Insets carInsets = new Insets(10, 10, 10, 10 );

        Image obrazAuta = new Image(Main.class.getResourceAsStream("/resources/car.png"));
        ImageView[] widokAuta = new ImageView[maxLiczbaSamochodow];
        StackPane[] samochodyNaStacji = new StackPane[maxLiczbaSamochodow];         // samochody ktore wjechaly i czekaja na zajecie stanowiska
        for (int i = 0; i < maxLiczbaSamochodow; i++) {
            widokAuta[i] = new ImageView( obrazAuta );
            samochodyNaStacji[i] = new StackPane( widokAuta[i] );
            samochodyNaStacji[i].getChildren().add(new Text(Integer.toString(i)));
            samochodyNaStacji[i].setPadding( carInsets );
        }
        HBox naStacji = new HBox();
        VBox kolumna1 = new VBox(), kolumna2 = new VBox();

        for (int i = 0; i < maxLiczbaSamochodow/2; i++)     kolumna1.getChildren().add(samochodyNaStacji[i]);
        for (int i = maxLiczbaSamochodow/2; i < maxLiczbaSamochodow; i++)     kolumna2.getChildren().add(samochodyNaStacji[i]);
        naStacji.getChildren().addAll(kolumna1, kolumna2);
        naStacji.setPadding(new Insets(0,75, 0, 0));
        // zakonczono rozmieszczanie pojazdow ktore sa na stacji i oczekuja na stanowisko

        //rozpoczynam rozmieszczanie pojazdow na stanowiskach
        Image obrazStanowiska = new Image(Main.class.getResourceAsStream("/resources/stanowisko.png"));
        ImageView[] widokStanowiska = new ImageView[liczbaStanowisk];
        ImageView[] widokAutaNaStanowisku = new ImageView[liczbaStanowisk];

        VBox[] stanowiskaZAutem = new VBox[liczbaStanowisk];
        samochodyNaStanowiskach = new StackPane[liczbaStanowisk];         // samochody na stanowiskach
        for (int i = 0; i < liczbaStanowisk; i++) {
            widokStanowiska[i] = new ImageView(obrazStanowiska);
            widokAutaNaStanowisku[i] = new ImageView( obrazAuta );

            samochodyNaStanowiskach[i] = new StackPane( widokAutaNaStanowisku[i] );
            samochodyNaStanowiskach[i].getChildren().add(new Text(Integer.toString(i)));
            samochodyNaStanowiskach[i].setPadding( carInsets );

            stanowiskaZAutem[i] = new VBox(widokStanowiska[i], samochodyNaStanowiskach[i]);
            stanowiskaZAutem[i].setPadding( carInsets );
        }

        HBox naStanowisku = new HBox();
        VBox kolumna1s = new VBox(), kolumna2s = new VBox();
        for (int i = 0; i < liczbaStanowisk/2; i++)     kolumna1s.getChildren().add(stanowiskaZAutem[i]);
        for (int i = liczbaStanowisk/2; i < liczbaStanowisk; i++)     kolumna2s.getChildren().add(stanowiskaZAutem[i]);
        naStanowisku.getChildren().addAll(kolumna1s, kolumna2s);
        naStanowisku.setPadding(new Insets(0,200, 0, 200));
        // konczenie stanowisk

        //poczatek kolejki do kas

        ImageView[] widokAutaPrzedKasa = new ImageView[maxLiczbaSamochodow];
        StackPane[] samochodyPrzedKasa = new StackPane[maxLiczbaSamochodow];         // samochody ktore czekaja na kase
        for (int i = 0; i < maxLiczbaSamochodow; i++) {
            widokAutaPrzedKasa[i] = new ImageView( obrazAuta );
            samochodyPrzedKasa[i] = new StackPane( widokAutaPrzedKasa[i] );
            samochodyPrzedKasa[i].getChildren().add(new Text(Integer.toString(i)));
            samochodyPrzedKasa[i].setPadding( carInsets );
        }
        HBox przedKasa = new HBox();
        VBox kolumna1p = new VBox(), kolumna2p = new VBox();

        for (int i = 0; i < maxLiczbaSamochodow/2; i++)     kolumna1p.getChildren().add(samochodyPrzedKasa[i]);
        for (int i = maxLiczbaSamochodow/2; i < maxLiczbaSamochodow; i++)     kolumna2p.getChildren().add(samochodyPrzedKasa[i]);
        przedKasa.getChildren().addAll(kolumna1p, kolumna2p);
        przedKasa.setPadding(new Insets(0,75, 0, 0));

        //koniec kolejki do kas

        // rozpoczynanie kas
        Image obrazKasy = new Image(Main.class.getResourceAsStream("/resources/kasa.png"));
        ImageView[] widokKasy = new ImageView[liczbaKas];
        ImageView[] widokAutaPrzyKasie = new ImageView[liczbaKas];
        VBox[] kasyZAutem = new VBox[liczbaKas];

        StackPane[] samochodyPrzyKasach = new StackPane[liczbaKas];         // auta przy kasach
        for (int i = 0; i < liczbaKas; i++) {
            widokKasy[i] = new ImageView(obrazKasy);
            widokAutaPrzyKasie[i] = new ImageView( obrazAuta );

            samochodyPrzyKasach[i] = new StackPane( widokAutaPrzyKasie[i] );
            samochodyPrzyKasach[i].getChildren().add(new Text(Integer.toString(i)));
            samochodyPrzyKasach[i].setPadding( carInsets );

            kasyZAutem[i] = new VBox(widokKasy[i], samochodyPrzyKasach[i]);
            kasyZAutem[i].setPadding( carInsets );
        }
        HBox przyKasach = new HBox();
        VBox kolumna1k = new VBox(), kolumna2k = new VBox();
        for (int i = 0; i < liczbaKas/2; i++)     kolumna1k.getChildren().add(kasyZAutem[i]);
        for (int i = liczbaKas/2; i < liczbaKas; i++)     kolumna2k.getChildren().add(kasyZAutem[i]);
        przyKasach.getChildren().addAll(kolumna1k, kolumna2k);
        przyKasach.setPadding(new Insets(0,200, 0, 200));


        return new HBox(naStacji, naStanowisku, przedKasa, przyKasach);
    }



    public HBox getTopMenuLayout() {

        // ustawienia layoutu
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        // przyciski i ich akcje
        Button otworzStacjePrzycisk, zamknijStacjePrzycisk;

        otworzStacjePrzycisk = new Button("Otworz Stacje");
        otworzStacjePrzycisk.setPrefSize(100, 20);
        otworzStacjePrzycisk.setDisable(true);

        zamknijStacjePrzycisk = new Button("Zamknij Stacje");
        zamknijStacjePrzycisk.setPrefSize(100, 20);

        otworzStacjePrzycisk.setOnAction(e -> {
            otworzStacjePrzycisk.setDisable(true);
            zamknijStacjePrzycisk.setDisable(false);
        });
        zamknijStacjePrzycisk.setOnAction(e -> {
            zamknijStacjePrzycisk.setDisable(true);
            otworzStacjePrzycisk.setDisable(false);
        });
        // napisy i inne...



        hbox.getChildren().addAll(otworzStacjePrzycisk, zamknijStacjePrzycisk);
        return hbox;
    }
    public VBox getBottomMenuLayout() {
        // 1 row
        Text text1 = new Text("Ilosc stanowisk: ");
        TextField iloscStanowiskField = new TextField("4");
        HBox hBox1 = new HBox(text1, iloscStanowiskField);
        // 2 row
        Text text2 = new Text("Ilosc kas: ");
        TextField iloscKasField = new TextField("3");
        HBox hBox2 = new HBox(text2, iloscKasField);
        // 3 row
        Text text3 = new Text("Maksymalna ilosc samochodow: ");
        TextField maxIloscSamochodowField = new TextField("15");
        HBox hBox3 = new HBox(text3, maxIloscSamochodowField);

        Text komunikat = new Text();

        Button rozpocznijSym = new Button("Rozpocznij symulacje");
        rozpocznijSym.setOnAction( event ->  {
            if(checkIfInteger( iloscStanowiskField ) && checkIfInteger( iloscKasField ) && checkIfInteger( maxIloscSamochodowField )) {
                komunikat.setText("");
                iloscStanowisk = Integer.parseInt(iloscStanowiskField.getText());
                iloscKas = Integer.parseInt(iloscKasField.getText());
                maxIloscSamochodow = Integer.parseInt(maxIloscSamochodowField.getText());
                layout.setCenter( getStationLayout(iloscStanowisk, iloscKas, maxIloscSamochodow) );
                // rozpocznij symulacje

                rozpocznijSym.setDisable(true);
            } else {
                komunikat.setText("Zle dane!");
            }
        });
        VBox vBox = new VBox(hBox1, hBox2, hBox3, rozpocznijSym, komunikat);
        return vBox;


    }

    public boolean checkIfInteger(TextField input) {
        try {
            int inputInt = Integer.parseInt(input.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
