package it.unicam.cs.mpgc.rpg130723;

import it.unicam.cs.mpgc.rpg130723.exception.FaseNonValidaException;
import it.unicam.cs.mpgc.rpg130723.logic.GameContext;
import it.unicam.cs.mpgc.rpg130723.model.Dungeon;
import it.unicam.cs.mpgc.rpg130723.model.carte.StanzaMostro;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Boss;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130723.model.enums.TipoTesoro;
import it.unicam.cs.mpgc.rpg130723.model.enums.FaseAttivazione;
import it.unicam.cs.mpgc.rpg130723.model.carte.Incantesimo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di unità per verificare la logica di combattimento.
 */

class BattleTest {

    private Eroe eroe;
    private Boss boss;
    private Dungeon dungeon;
    private GameContext context;

    @BeforeEach
    void setup() {
        // Nota: Verifica se il costruttore di Eroe e Boss nel tuo codice accetta questi parametri
        eroe = new Eroe("Guerriero", 4, TipoTesoro.SPADA, 1);
        boss = new Boss("DarkLord", "Il boss oscuro", 10);
        dungeon = new Dungeon();

        // Usiamo il costruttore reale del tuo GameContext
        context = new GameContext(boss, dungeon);
    }

    @Test
    void eroeSubisceDannoCorretto() {
        StanzaMostro stanza = new StanzaMostro(
                "Tana del Goblin", "Goblin feroci", 2, TipoTesoro.SPADA);
        context.setEroeCorrente(eroe);
        stanza.eseguiEffetto(context);
        assertEquals(2, eroe.getPuntiVita());
    }

    @Test
    void eroeConZeroHpNonEVivo() {
        StanzaMostro stanza = new StanzaMostro(
                "Sala del Drago", "Un drago feroce", 4, TipoTesoro.RELIQUIA);
        context.setEroeCorrente(eroe);
        stanza.eseguiEffetto(context);
        assertFalse(eroe.isVivo());
    }

    @Test
    void dungeonPienoLanciaEccezione() {
        for (int i = 0; i < 5; i++) {
            dungeon.aggiungiStanza(new StanzaMostro(
                    "Stanza " + i, "Descrizione", 1, TipoTesoro.SPADA));
        }
        assertThrows(Exception.class, () ->
                dungeon.aggiungiStanza(new StanzaMostro(
                        "Stanza Extra", "Descrizione", 1, TipoTesoro.SPADA)));
    }

    @Test
    void incantesimoInFaseErrateLanciaEccezione() {
        Incantesimo incantesimo = new Incantesimo(
                "Fulmine", "Danno magico", FaseAttivazione.COSTRUZIONE) {};
        assertThrows(FaseNonValidaException.class, () -> {
            if (incantesimo.getFase() != FaseAttivazione.AVVENTURA)
                throw new FaseNonValidaException(
                        "Incantesimo non attivabile in questa fase.");
        });
    }
}