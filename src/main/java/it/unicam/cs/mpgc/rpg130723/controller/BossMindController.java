package it.unicam.cs.mpgc.rpg130723.controller;

import it.unicam.cs.mpgc.rpg130723.logic.GameEngine;
import it.unicam.cs.mpgc.rpg130723.model.Dungeon;
import it.unicam.cs.mpgc.rpg130723.model.carte.Stanza;
import it.unicam.cs.mpgc.rpg130723.model.carte.StanzaMostro;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Boss;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130723.model.enums.TipoTesoro;
import it.unicam.cs.mpgc.rpg130723.persistence.JsonProvider;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

/**
 * Controller principale di BossMind.
 * Responsabilità: collegare Model agli elementi FXML via Property Binding,
 * reagire ai click e delegare la logica al GameEngine, aggiornare la Vista.
 */
public class BossMindController implements Initializable {

    @FXML private Label       labelAnime;
    @FXML private Label       labelFerite;
    @FXML private ProgressBar progressAnime;
    @FXML private ProgressBar progressFerite;
    @FXML private Label       labelFase;
    @FXML private Button      btnFineTurno;
    @FXML private Label       labelSlotDungeon;

    @FXML private VBox slot0, slot1, slot2, slot3, slot4;
    @FXML private Label slotNome0, slotNome1, slotNome2, slotNome3, slotNome4;
    @FXML private Label slotTipo0, slotTipo1, slotTipo2, slotTipo3, slotTipo4;
    @FXML private Label slotDanno0, slotDanno1, slotDanno2, slotDanno3, slotDanno4;
    @FXML private Label slotTesoro0, slotTesoro1, slotTesoro2, slotTesoro3, slotTesoro4;

    @FXML private HBox     manoBox;
    @FXML private Label    labelManoCount;
    @FXML private VBox     villaggioBox;
    @FXML private TextArea areaLog;

    private final IntegerProperty animeProperty  = new SimpleIntegerProperty(0);
    private final IntegerProperty feriteProperty = new SimpleIntegerProperty(0);
    private final StringProperty  faseProperty   = new SimpleStringProperty("—");

    private GameEngine   engine;
    private Boss         boss;
    private Dungeon      dungeon;
    private List<Stanza> tutteStanze;
    private List<Eroe>   tuttiEroi;

    private int  cartaSelezionataIndex = -1;
    private VBox cartaSelezionataNode  = null;

    private VBox[]  slotNodes;
    private Label[] slotNomi, slotTipi, slotDanni, slotTesori;

    private Image imgMostro, imgTrappola, imgSpade,
            imgSpada, imgLibro, imgMonete, imgReliquia,
            imgAnime, imgFerite;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        raggruppaSlotInArray();
        caricaImmagini();
        collegaBinding();
        impostaIconeHUD();
        inizializzaPartita();
    }

    private void raggruppaSlotInArray() {
        slotNodes  = new VBox[]  { slot0, slot1, slot2, slot3, slot4 };
        slotNomi   = new Label[] { slotNome0, slotNome1, slotNome2, slotNome3, slotNome4 };
        slotTipi   = new Label[] { slotTipo0, slotTipo1, slotTipo2, slotTipo3, slotTipo4 };
        slotDanni  = new Label[] { slotDanno0, slotDanno1, slotDanno2, slotDanno3, slotDanno4 };
        slotTesori = new Label[] { slotTesoro0, slotTesoro1, slotTesoro2, slotTesoro3, slotTesoro4 };
    }


    private void caricaImmagini() {
        imgMostro   = caricaPng("Mostro.png");
        imgTrappola = caricaPng("Trappola.png");
        imgSpade    = caricaPng("Spade.png");
        imgSpada    = caricaPng("Spada.png");
        imgLibro    = caricaPng("LibroMagico.png");
        imgMonete   = caricaPng("Monete.png");
        imgReliquia = caricaPng("Reliquia.png");
        imgAnime    = caricaPng("Anime.png");
        imgFerite   = caricaPng("Ferite.png");
    }

    private Image caricaPng(String nomeFile) {
        var stream = getClass().getResourceAsStream("/images/" + nomeFile);
        return stream != null ? new Image(stream) : null;
    }

    private ImageView iconaView(Image img, int size) {
        ImageView iv = new ImageView();
        if (img != null) iv.setImage(img);
        iv.setFitWidth(size);
        iv.setPreserveRatio(true);
        return iv;
    }


    private void collegaBinding() {
        labelAnime.textProperty().bind(animeProperty.asString("%d / 10"));
        labelFerite.textProperty().bind(feriteProperty.asString("%d / 5"));
        progressAnime.progressProperty().bind(animeProperty.divide(10.0));
        progressFerite.progressProperty().bind(feriteProperty.divide(5.0));
        labelFase.textProperty().bind(faseProperty);
    }

    private void impostaIconeHUD() {
        labelAnime.setGraphic(iconaView(imgAnime, 22));
        labelFerite.setGraphic(iconaView(imgFerite, 22));
    }


    private void inizializzaPartita() {
        JsonProvider loader = new JsonProvider();
        tutteStanze = new ArrayList<>();
        tutteStanze.addAll(loader.caricaStanzeMostro());
        tutteStanze.addAll(loader.caricaStanzeTrappola());
        tuttiEroi = loader.caricaEroi();

        boss    = new Boss("DarkLord", "Il signore oscuro", 10);
        dungeon = new Dungeon();
        engine  = new GameEngine(boss, dungeon, this::valutaAttrazione);

        log("⚔ Benvenuto, " + boss.getNome() + "! Accumula 10 Anime prima di subire 5 Ferite.");
        avviaNuovoTurno();
    }

    private boolean valutaAttrazione(Dungeon d, Eroe e) {
        return d.getStanze().stream()
                .filter(s -> s.getTesoroFornito() == e.getInteresse())
                .count() >= 2;
    }


    private void avviaNuovoTurno() {
        faseProperty.set("COSTRUZIONE");
        log("─── NUOVO TURNO ───");
        List<Stanza> pescate   = pescaCarteCasuali(3);
        List<Eroe>   nuoviEroi = pescaEroiCasuali(2);
        engine.faseInizioTurno(pescate, nuoviEroi);
        aggiornaMano();
        aggiornaVillaggio(nuoviEroi);
        aggiornaHUD();
        deselezionaCarta();
        btnFineTurno.setDisable(false);
    }

    @FXML
    private void onFineTurno() {
        if (cartaSelezionataIndex < 0) {
            log("⚠ Seleziona prima una carta dalla mano!");
            return;
        }
        if (dungeon.isPieno()) {
            log("Dungeon pieno! Clicca uno slot del dungeon per sostituire.");
            faseProperty.set("SOSTITUZIONE");
            btnFineTurno.setDisable(true);
            return;
        }
        engine.faseCostruzione(cartaSelezionataIndex);
        log("🏰 Stanza costruita nel dungeon.");
        eseguiRichiamoEAvventura();
    }

    @FXML
    private void onSlotDungeonClicked(javafx.scene.input.MouseEvent event) {
        if (!"SOSTITUZIONE".equals(faseProperty.get())) return;
        if (cartaSelezionataIndex < 0) { log("⚠ Seleziona prima una carta!"); return; }
        int indice = Integer.parseInt(((VBox) event.getSource()).getUserData().toString());
        if (indice >= dungeon.getNumeroStanze()) { log("⚠ Slot vuoto, scegline uno occupato."); return; }
        engine.faseSostituzione(cartaSelezionataIndex, indice);
        log("♻ Stanza sostituita nello slot " + (indice + 1) + ".");
        eseguiRichiamoEAvventura();
    }

    private void eseguiRichiamoEAvventura() {
        faseProperty.set("RICHIAMO");
        List<Eroe> attratti = engine.faseRichiamo();
        if (attratti.isEmpty()) {
            log("🏘 Nessun eroe attirato.");
        } else {
            faseProperty.set("AVVENTURA");
            engine.faseAvventura(attratti);
            attratti.forEach(e -> log(e.isVivo()
                    ? "  💀 " + e.getNome() + " sopravvissuto → +1 Ferita"
                    : "  💎 " + e.getNome() + " sconfitto → +1 Anima"));
        }
        aggiornaDungeon();
        aggiornaMano();
        aggiornaHUD();
        aggiornaVillaggio(Collections.emptyList());
        if (engine.isPartitaFinita()) mostraFinePartita();
        else                          avviaNuovoTurno();
    }


    private void aggiornaHUD() {
        animeProperty.set(boss.getAnime());
        feriteProperty.set(boss.getFerite());
        labelSlotDungeon.setText("(" + dungeon.getNumeroStanze() + " / 5 stanze)");
    }

    private void aggiornaDungeon() {
        List<Stanza> stanze = dungeon.getStanze();
        for (int i = 0; i < 5; i++) {
            slotNodes[i].getStyleClass().removeAll("dungeon-slot-mostro", "dungeon-slot-trappola");
            if (i < stanze.size()) {
                Stanza s = stanze.get(i);
                slotNomi[i].setText(s.getNome());

                Image imgTipo = s instanceof StanzaMostro ? imgMostro : imgTrappola;
                slotTipi[i].setText("");
                slotTipi[i].setGraphic(iconaView(imgTipo, 36));

                slotDanni[i].setText(" " + s.getValoreDanno());
                slotDanni[i].setGraphic(iconaView(imgSpade, 16));

                slotTesori[i].setText(" " + tesoro(s.getTesoroFornito().name()));
                slotTesori[i].setGraphic(iconaView(imgTesoro(s.getTesoroFornito()), 16));

                slotNodes[i].getStyleClass().add(
                        s instanceof StanzaMostro ? "dungeon-slot-mostro" : "dungeon-slot-trappola");
            } else {
                slotNomi[i].setText("— vuoto —");
                slotTipi[i].setText("");
                slotTipi[i].setGraphic(null);
                slotDanni[i].setText("");
                slotDanni[i].setGraphic(null);
                slotTesori[i].setText("");
                slotTesori[i].setGraphic(null);
            }
        }
        labelSlotDungeon.setText("(" + stanze.size() + " / 5 stanze)");
    }

    private void aggiornaMano() {
        manoBox.getChildren().clear();
        List<Stanza> mano = engine.getMano();
        labelManoCount.setText("(" + mano.size() + " carte)");
        for (int i = 0; i < mano.size(); i++) {
            manoBox.getChildren().add(cartaManoNode(mano.get(i), i));
        }
    }

    private VBox cartaManoNode(Stanza stanza, int index) {
        Image imgTipo = stanza instanceof StanzaMostro ? imgMostro : imgTrappola;
        ImageView icona = iconaView(imgTipo, 36);

        Label nome = new Label(stanza.getNome());
        nome.getStyleClass().add("carta-nome");

        Label danno = new Label(" " + stanza.getValoreDanno());
        danno.setGraphic(iconaView(imgSpade, 14));
        danno.getStyleClass().add("carta-stat");

        Label tesoro = new Label(" " + tesoro(stanza.getTesoroFornito().name()));
        tesoro.setGraphic(iconaView(imgTesoro(stanza.getTesoroFornito()), 14));
        tesoro.getStyleClass().add("carta-stat");

        VBox card = new VBox(6, icona, nome, danno, tesoro);
        card.getStyleClass().add("carta-mano");
        card.setOnMouseClicked(e -> selezionaCarta(card, index));
        return card;
    }

    private void selezionaCarta(VBox card, int index) {
        if (cartaSelezionataNode != null)
            cartaSelezionataNode.getStyleClass().remove("carta-mano-selezionata");
        if (cartaSelezionataIndex == index) {
            deselezionaCarta();
        } else {
            cartaSelezionataIndex = index;
            cartaSelezionataNode  = card;
            card.getStyleClass().add("carta-mano-selezionata");
            log("Selezionata: " + engine.getMano().get(index).getNome());
        }
    }

    private void deselezionaCarta() {
        cartaSelezionataIndex = -1;
        cartaSelezionataNode  = null;
    }

    private void aggiornaVillaggio(List<Eroe> eroi) {
        villaggioBox.getChildren().clear();
        if (eroi.isEmpty()) {
            Label l = new Label("Nessun eroe");
            l.getStyleClass().add("section-subtitle");
            villaggioBox.getChildren().add(l);
            return;
        }
        eroi.forEach(e -> villaggioBox.getChildren().add(eroeCardNode(e)));
    }

    private VBox eroeCardNode(Eroe eroe) {
        ImageView icona = iconaView(imgTesoro(eroe.getInteresse()), 32);

        Label nome = new Label(eroe.getNome());
        nome.getStyleClass().add("eroe-nome");

        Label hp = new Label(" HP: " + eroe.getPuntiVita());
        hp.setGraphic(iconaView(imgFerite, 14));
        hp.getStyleClass().add("eroe-stat");

        Label interesse = new Label(" " + tesoro(eroe.getInteresse().name()));
        interesse.setGraphic(iconaView(imgTesoro(eroe.getInteresse()), 14));
        interesse.getStyleClass().add("eroe-interesse");

        VBox card = new VBox(4, icona, nome, hp, interesse);
        card.getStyleClass().add("eroe-card");
        return card;
    }

    private void mostraFinePartita() {
        btnFineTurno.setDisable(true);
        faseProperty.set("FINE");
        boolean vittoria = boss.haVinto();
        log(vittoria ? "══ HAI VINTO! ══" : "══ HAI PERSO! ══");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("BossMind");
        alert.setHeaderText(vittoria ? "🎉 VITTORIA!" : "💀 SCONFITTA");
        alert.setContentText(vittoria
                ? "Hai accumulato 10 Anime!\nIl tuo Dungeon è leggendario."
                : "Hai subito 5 Ferite!\nGli eroi hanno distrutto il tuo Dungeon.");
        alert.showAndWait();
        javafx.application.Platform.exit();
    }

    private List<Stanza> pescaCarteCasuali(int n) {
        Collections.shuffle(tutteStanze);
        return new ArrayList<>(tutteStanze.subList(0, n));
    }

    private List<Eroe> pescaEroiCasuali(int n) {
        Collections.shuffle(tuttiEroi);
        List<Eroe> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Eroe o = tuttiEroi.get(i);
            lista.add(new Eroe(o.getNome(), o.getPuntiVita(), o.getInteresse(), o.getValoreAnima()));
        }
        return lista;
    }

    /** Mappa TipoTesoro → immagine corrispondente. */
    private Image imgTesoro(TipoTesoro tipo) {
        return switch (tipo) {
            case SPADA           -> imgSpada;
            case LIBRO_MAGICO    -> imgLibro;
            case SACCO_DI_MONETE -> imgMonete;
            case RELIQUIA        -> imgReliquia;
        };
    }

    private String tesoro(String enumName) {
        return switch (enumName) {
            case "SPADA"           -> "Spada";
            case "LIBRO_MAGICO"    -> "Libro Magico";
            case "SACCO_DI_MONETE" -> "Sacco di Monete";
            case "RELIQUIA"        -> "Reliquia";
            default                -> enumName;
        };
    }

    private void log(String msg) { areaLog.appendText(msg + "\n"); }
}