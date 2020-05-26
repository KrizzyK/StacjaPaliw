package project;

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

import project.Logics.Klient;
import project.Logics.StacjaPaliw;
import project.Logics.Stanowisko;

public class Main extends Application {
    static final int szerokoscOkna = 1600, wysokoscOkna = 900;
    static final int szerokoscAuta = 10, wysokoscAuta = 5;

    StackPane[] samochodyNaStacji;          // zmienne pokazujace samochody w odpowiednich miejscach z odpowiednimi indeksami
    StackPane[] samochodyNaStanowiskach;    // methods -> x[i].setVisible(),
    StackPane[] samochodyPrzedKasa;         // ((Text) x[1].getChildren().get(1) ).setText("xdxd");
    StackPane[] samochodyPrzyKasach;

    StackPane[] widokStanowiskaZDanymi;

    int iloscStanowisk;
    int iloscKas;
    int maxIloscSamochodow;
    BorderPane layout = new BorderPane();

    public ObservableList<String> listaKomunikatow;
    public ObservableList<StackPane> listaAutNaStacji;
    public ObservableList<StackPane> listaAutPrzedKasa;

    public static StacjaPaliw stacjaPaliw;
    Image obrazAuta;

    @Override
    public void start(Stage window) throws Exception{
        obrazAuta = new Image(Main.class.getResourceAsStream("/resources/car.png"));
        // podstawowy layout
        layout.setTop( getTopMenuLayout() );
        layout.setRight( getRightMenuLayout() );
        layout.setBottom( getBottomMenuLayout() );

        window.setTitle("Stacja Paliw");
        window.setScene(new Scene(layout, szerokoscOkna, wysokoscOkna));
        window.show();

    }

    public void pokazSamochodNaStacji(int nrKlienta) {
        listaAutNaStacji.add(new StackPane(new ImageView(obrazAuta) , new Text(Integer.toString(nrKlienta))) );
    }
    public void ukryjSamochodNaStacji( int nrKlienta) {
        for (int i = 0; i < listaAutNaStacji.size(); i++) {
            if( ((Text) listaAutNaStacji.get(i).getChildren().get(1) ).getText().equals(Integer.toString(nrKlienta))  )
                listaAutNaStacji.remove(i);
        }
    }
    public void pokazSamochodPrzedKasa(int nrKlienta) {
        listaAutPrzedKasa.add(new StackPane(new ImageView(obrazAuta) , new Text(Integer.toString(nrKlienta))) );
    }
    public void ukryjSamochodPrzedKasa(int nrKlienta) {
        for (int i = 0; i < listaAutPrzedKasa.size(); i++) {
            if( ((Text) listaAutPrzedKasa.get(i).getChildren().get(1) ).getText().equals(Integer.toString(nrKlienta))  )
                listaAutPrzedKasa.remove(i);
        }
    }
    public void pokazSamochodNaStanowisku(int indeksMiejsca, int nrKlienta) {
        ((Text) samochodyNaStanowiskach[indeksMiejsca].getChildren().get(1) ).setText( Integer.toString(nrKlienta) );
        samochodyNaStanowiskach[indeksMiejsca].setVisible(true);
    }
    public void ukryjSamochodNaStanowisku(int indeksMiejsca) {
        samochodyNaStanowiskach[indeksMiejsca].setVisible(false);
    }

    public void pokazSamochodyPrzyKasie(int indeksMiejsca, int nrKlienta) {
        ((Text) samochodyPrzyKasach[indeksMiejsca].getChildren().get(1) ).setText( Integer.toString(nrKlienta) );
        samochodyPrzyKasach[indeksMiejsca].setVisible(true);
    }
    public void ukryjSamochodPrzyKasie(int indeksMiejsca) {
        samochodyPrzyKasach[indeksMiejsca].setVisible(false);
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

        listaKomunikatow = FXCollections.observableArrayList();
        ListView<String> widokKomunikatow = new ListView<String>(listaKomunikatow);
        vbox.setMinWidth(800);
        vbox.getChildren().add(widokKomunikatow);

        return vbox;
    }
    public HBox getStationLayout(int liczbaStanowisk, int liczbaKas, int maxLiczbaSamochodow) {
        listaAutNaStacji = FXCollections.observableArrayList();
        ListView<StackPane> widokAutNaStacji = new ListView<>(listaAutNaStacji);
        widokAutNaStacji.setPadding(new Insets(0,75, 0, 10));
        widokAutNaStacji.setMinWidth(200);

        listaAutPrzedKasa = FXCollections.observableArrayList();
        ListView<StackPane> widokAutPrzedKasa = new ListView<>(listaAutPrzedKasa);
        widokAutPrzedKasa.setPadding(new Insets(0,75, 0, 75));
        widokAutPrzedKasa.setMinWidth(200);



        Insets carInsets = new Insets(10, 10, 10, 10 );

        //rozpoczynam rozmieszczanie pojazdow na stanowiskach
        Image obrazStanowiska = new Image(Main.class.getResourceAsStream("/resources/stanowisko.png"));
        ImageView[] widokStanowiska = new ImageView[liczbaStanowisk];
        ImageView[] widokAutaNaStanowisku = new ImageView[liczbaStanowisk];

        widokStanowiskaZDanymi = new StackPane[liczbaStanowisk];
        VBox[] stanowiskaZAutem = new VBox[liczbaStanowisk];
        samochodyNaStanowiskach = new StackPane[liczbaStanowisk];         // samochody na stanowiskach
        for (int i = 0; i < liczbaStanowisk; i++) {
            widokStanowiska[i] = new ImageView(obrazStanowiska);
            widokStanowiskaZDanymi[i] = new StackPane(widokStanowiska[i]);

            widokAutaNaStanowisku[i] = new ImageView( obrazAuta );

            samochodyNaStanowiskach[i] = new StackPane( widokAutaNaStanowisku[i] );
            samochodyNaStanowiskach[i].getChildren().add(new Text(Integer.toString(i)));
            samochodyNaStanowiskach[i].setPadding( carInsets );

            stanowiskaZAutem[i] = new VBox(widokStanowiskaZDanymi[i], samochodyNaStanowiskach[i]);
            stanowiskaZAutem[i].setPadding( carInsets );
        }

        HBox naStanowisku = new HBox();
        VBox kolumna1s = new VBox(), kolumna2s = new VBox();
        for (int i = 0; i < liczbaStanowisk/2; i++)     kolumna1s.getChildren().add(stanowiskaZAutem[i]);
        for (int i = liczbaStanowisk/2; i < liczbaStanowisk; i++)     kolumna2s.getChildren().add(stanowiskaZAutem[i]);
        naStanowisku.getChildren().addAll(kolumna1s, kolumna2s);
        naStanowisku.setPadding(new Insets(0,75, 0, 75));
        // konczenie stanowisk

        // rozpoczynanie kas
        Image obrazKasy = new Image(Main.class.getResourceAsStream("/resources/kasa.png"));
        ImageView[] widokKasy = new ImageView[liczbaKas];
        ImageView[] widokAutaPrzyKasie = new ImageView[liczbaKas];
        VBox[] kasyZAutem = new VBox[liczbaKas];

        samochodyPrzyKasach = new StackPane[liczbaKas];         // auta przy kasach
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
        przyKasach.setPadding(new Insets(0,10, 0, 75));


        return new HBox(widokAutNaStacji, naStanowisku, widokAutPrzedKasa, przyKasach);
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
            //stacjaPaliw.otworzStacje();
        });
        zamknijStacjePrzycisk.setOnAction(e -> {
            zamknijStacjePrzycisk.setDisable(true);
            otworzStacjePrzycisk.setDisable(false);
            //stacjaPaliw.zamknijStacje();
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

                stacjaPaliw = new StacjaPaliw(maxIloscSamochodow, iloscStanowisk, iloscKas, this);

                Thread[] klienci = new Klient[20];

                for (int i = 0; i < klienci.length; i++) {
                    klienci[i] = new Klient(i+1, stacjaPaliw);
                    klienci[i].start();
                }

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
    public void dodajDaneDoStanowisk(Stanowisko[] stanowiska) {
        // wyciagnij dane i wstaw
        for (int i = 0; i < iloscStanowisk ; i++) {
            widokStanowiskaZDanymi[i].getChildren().add( new Text("napisNaStanowisku") );
        }
    }
}
