package it.unicam.cs.mpgc.rpg130723.controller;

import it.unicam.cs.mpgc.rpg130723.logic.GameEngine;
import it.unicam.cs.mpgc.rpg130723.model.Dungeon;
import it.unicam.cs.mpgc.rpg130723.model.carte.Stanza;
import it.unicam.cs.mpgc.rpg130723.model.carte.StanzaMostro;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Boss;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130723.persistence.JsonProvider;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

/**
 * Controller principale di BossMind.
 *
 * Responsabilità:
 *  - collegare i dati del Model agli elementi FXML via Property Binding
 *  - reagire ai click dell'utente e delegare la logica al GameEngine
 *  - aggiornare la Vista dopo ogni azione
 */
public class BossMindController implements Initializable {

    // FXML: HUD
    @FXML private Label       labelAnime;
    @FXML private Label       labelFerite;
    @FXML private ProgressBar progressAnime;
    @FXML private ProgressBar progressFerite;
    @FXML private Label       labelFase;
    @FXML private Button      btnFineTurno;
    @FXML private Label       labelSlotDungeon;

    // FXML: Slot dungeon
    @FXML private VBox slot0, slot1, slot2, slot3, slot4;
    @FXML private Label slotNome0, slotNome1, slotNome2, slotNome3, slotNome4;
    @FXML private Label slotTipo0, slotTipo1, slotTipo2, slotTipo3, slotTipo4;
    @FXML private Label slotDanno0, slotDanno1, slotDanno2, slotDanno3, slotDanno4;
    @FXML private Label slotTesoro0, slotTesoro1, slotTesoro2, slotTesoro3, slotTesoro4;

    // FXML: Mano e Villaggio
    @FXML private HBox  manoBox;
    @FXML private Label labelManoCount;
    @FXML private VBox  villaggioBox;
    @FXML private TextArea areaLog;

    // Properties osservabili (collegate alla View via binding)
    private final IntegerProperty animeProperty  = new SimpleIntegerProperty(0);
    private final IntegerProperty feriteProperty = new SimpleIntegerProperty(0);
    private final StringProperty  faseProperty   = new SimpleStringProperty("—");

    // Model / Engine
    private GameEngine   engine;
    private Boss         boss;
    private Dungeon      dungeon;
    private List<Stanza> tutteStanze;
    private List<Eroe>   tuttiEroi;

    // Stato selezione carta
    private int  cartaSelezionataIndex = -1;
    private VBox cartaSelezionataNode  = null;

    // Array paralleli ai 5 slot FXML
    private VBox[]  slotNodes;
    private Label[] slotNomi, slotTipi, slotDanni, slotTesori;

    // Initializable

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        raggruppaSlotInArray();
        collegaBinding();
        inizializzaPartita();
    }

    private void raggruppaSlotInArray() {
        slotNodes  = new VBox[]  { slot0, slot1, slot2, slot3, slot4 };
        slotNomi   = new Label[] { slotNome0, slotNome1, slotNome2, slotNome3, slotNome4 };
        slotTipi   = new Label[] { slotTipo0, slotTipo1, slotTipo2, slotTipo3, slotTipo4 };
        slotDanni  = new Label[] { slotDanno0, slotDanno1, slotDanno2, slotDanno3, slotDanno4 };
        slotTesori = new Label[] { slotTesoro0, slotTesoro1, slotTesoro2, slotTesoro3, slotTesoro4 };
    }

    /**
     * Property Binding: collega le proprietà osservabili agli elementi FXML.
     * Da questo momento ogni set() sulle properties aggiorna la View in automatico.
     */
    private void collegaBinding() {
        labelAnime.textProperty().bind(animeProperty.asString("%d / 10"));
        labelFerite.textProperty().bind(feriteProperty.asString("%d / 5"));
        progressAnime.progressProperty().bind(animeProperty.divide(10.0));
        progressFerite.progressProperty().bind(feriteProperty.divide(5.0));
        labelFase.textProperty().bind(faseProperty);
    }

    // Setup partita

    private void inizializzaPartita() {
        JsonProvider loader = new JsonProvider();
        tutteStanze = new ArrayList<>();
        tutteStanze.addAll(loader.caricaStanzeMostro());
        tutteStanze.addAll(loader.caricaStanzeTrappola());
        tuttiEroi = loader.caricaEroi();

        boss   = new Boss("DarkLord", "Il signore oscuro", 10);
        dungeon = new Dungeon();
        engine  = new GameEngine(boss, dungeon, this::valutaAttrazione);

        log("⚔ Benvenuto, " + boss.getNome() + "! Accumula 10 Anime prima di subire 5 Ferite.");
        avviaNuovoTurno();
    }

    /** Logica di attrazione: un eroe entra se almeno 2 stanze mostrano il suo tesoro. */
    private boolean valutaAttrazione(Dungeon d, Eroe e) {
        return d.getStanze().stream()
                .filter(s -> s.getTesoroFornito() == e.getInteresse())
                .count() >= 2;
    }

    // Gestione turno

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

    // Aggiornamento View

    /** Aggiorna le Properties: il binding propaga automaticamente alle Label e ProgressBar. */
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
                slotTipi[i].setText(emojiStanza(s));
                slotDanni[i].setText("⚔ Danno: " + s.getValoreDanno());
                slotTesori[i].setText("💰 " + tesoro(s.getTesoroFornito().name()));
                slotNodes[i].getStyleClass().add(
                        s instanceof StanzaMostro ? "dungeon-slot-mostro" : "dungeon-slot-trappola");
            } else {
                slotNomi[i].setText("— vuoto —");
                slotTipi[i].setText(""); slotDanni[i].setText(""); slotTesori[i].setText("");
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
        Label icona = new Label(emojiStanza(stanza));  icona.getStyleClass().add("carta-icona");
        Label nome  = new Label(stanza.getNome());      nome.getStyleClass().add("carta-nome");
        Label stat  = new Label("⚔ " + stanza.getValoreDanno() + "  💰 " + tesoro(stanza.getTesoroFornito().name()));
        stat.getStyleClass().add("carta-stat");

        VBox card = new VBox(6, icona, nome, stat);
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
            Label l = new Label("Nessun eroe"); l.getStyleClass().add("section-subtitle");
            villaggioBox.getChildren().add(l);
            return;
        }
        eroi.forEach(e -> villaggioBox.getChildren().add(eroeCardNode(e)));
    }

    private VBox eroeCardNode(Eroe eroe) {
        Label icona     = new Label(emojiEroe(eroe));       icona.getStyleClass().add("eroe-icona");
        Label nome      = new Label(eroe.getNome());         nome.getStyleClass().add("eroe-nome");
        Label hp        = new Label("❤ HP: " + eroe.getPuntiVita()); hp.getStyleClass().add("eroe-stat");
        Label interesse = new Label("🔎 " + tesoro(eroe.getInteresse().name())); interesse.getStyleClass().add("eroe-interesse");

        VBox card = new VBox(4, icona, nome, hp, interesse);
        card.getStyleClass().add("eroe-card");
        return card;
    }

    // Fine partita

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

    // Utilità

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

    private void log(String msg) { areaLog.appendText(msg + "\n"); }

    private String emojiStanza(Stanza s) {
        return s instanceof StanzaMostro ? "👹" : "⚙";
    }

    private String emojiEroe(Eroe e) {
        return switch (e.getInteresse()) {
            case SPADA           -> "⚔";
            case LIBRO_MAGICO    -> "🧙";
            case SACCO_DI_MONETE -> "🗡";
            case RELIQUIA        -> "✝";
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
}
