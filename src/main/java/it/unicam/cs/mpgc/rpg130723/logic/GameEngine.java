package it.unicam.cs.mpgc.rpg130723.logic;

import it.unicam.cs.mpgc.rpg130723.interfaces.ValutatoreTesoro;
import it.unicam.cs.mpgc.rpg130723.model.Dungeon;
import it.unicam.cs.mpgc.rpg130723.model.carte.Stanza;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Boss;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Eroe;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordina la sequenza di gioco turno per turno.
 * Gestisce le quattro fasi: Inizio, Costruzione, Richiamo e Avventura.
 * Si occupa solo della logica di flusso del gioco,
 * non della rappresentazione dei dati né della persistenza.
 */
public class GameEngine {

    private static final int MAX_CARTE_MANO = 3;
    private static final int EROI_PER_TURNO = 2;

    private final Boss boss;
    private final Dungeon dungeon;
    private final ValutatoreTesoro valutatore;
    private final GameContext context;
    private final List<Eroe> villaggio;
    private final List<Stanza> mano;

    public GameEngine(Boss boss, Dungeon dungeon, ValutatoreTesoro valutatore) {
        if (boss == null)
            throw new IllegalArgumentException("Il boss non può essere nullo.");
        if (dungeon == null)
            throw new IllegalArgumentException("Il dungeon non può essere nullo.");
        if (valutatore == null)
            throw new IllegalArgumentException("Il valutatore non può essere nullo.");
        this.boss = boss;
        this.dungeon = dungeon;
        this.valutatore = valutatore;
        this.context = new GameContext(boss, dungeon);
        this.villaggio = new ArrayList<>();
        this.mano = new ArrayList<>();
    }


    public void faseInizioTurno(List<Stanza> cartePescate, List<Eroe> nuoviEroi) {
        if (cartePescate == null || cartePescate.isEmpty())
            throw new IllegalArgumentException("Le carte pescate non possono essere nulle.");
        if (nuoviEroi == null || nuoviEroi.size() != EROI_PER_TURNO)
            throw new IllegalArgumentException("Devono arrivare esattamente 2 eroi per turno.");
        for (Stanza carta : cartePescate) {
            if (mano.size() < MAX_CARTE_MANO)
                mano.add(carta);
        }
        villaggio.addAll(nuoviEroi);
    }


    public void faseCostruzione(int indiceCarta) {
        if (indiceCarta < 0 || indiceCarta >= mano.size())
            throw new IllegalArgumentException("Indice carta non valido: " + indiceCarta);
        Stanza scelta = mano.get(indiceCarta);
        dungeon.aggiungiStanza(scelta);
        mano.remove(indiceCarta);
        // NON svuotiamo la mano! Le altre carte restano per il turno dopo
    }


    public List<Eroe> faseRichiamo() {
        List<Eroe> attratti = new ArrayList<>();
        for (Eroe eroe : villaggio) {
            if (valutatore.vieneAttirato(dungeon, eroe)) {
                attratti.add(eroe);
            }
        }
        villaggio.clear();
        return attratti;
    }


    public void faseAvventura(List<Eroe> eroi) {
        for (Eroe eroe : eroi) {
            context.setEroeCorrente(eroe);
            for (Stanza stanza : dungeon.getStanze()) {
                stanza.eseguiEffetto(context);
                if (!eroe.isVivo()) {
                    boss.aggiungiAnima();
                    break;
                }
            }
            if (eroe.isVivo()) {
                boss.subisciFerita();
            }
        }
    }

    public void faseSostituzione(int indiceCarta, int indiceStanza) {
        if (indiceCarta < 0 || indiceCarta >= mano.size())
            throw new IllegalArgumentException("Indice carta non valido.");
        Stanza scelta = mano.get(indiceCarta);
        dungeon.sostituisciStanza(indiceStanza, scelta);
        mano.remove(indiceCarta);
    }

    public boolean isPartitaFinita() {
        return boss.haVinto() || boss.haPerso();
    }

    public List<Stanza> getMano() {
        return new ArrayList<>(mano);
    }

    public Boss getBoss() {
        return boss;
    }
}