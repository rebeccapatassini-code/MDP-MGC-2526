package it.unicam.cs.mpgc.rpg130723.logic;

import it.unicam.cs.mpgc.rpg130723.model.Dungeon;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Boss;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Eroe;

/**
 * Contiene lo stato corrente della partita.
 * Viene passato agli effetti delle stanze e degli incantesimi
 * per evitare dipendenze dirette dal GameEngine.
 */
public class GameContext {

    private final Boss boss;
    private final Dungeon dungeon;
    private Eroe eroeCorrente;

    public GameContext(Boss boss, Dungeon dungeon) {
        if (boss == null)
            throw new IllegalArgumentException("Il boss non può essere nullo.");
        if (dungeon == null)
            throw new IllegalArgumentException("Il dungeon non può essere nullo.");
        this.boss = boss;
        this.dungeon = dungeon;
    }

    public Boss getBoss() {
        return boss;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Eroe getEroeCorrente() {
        return eroeCorrente;
    }

    public void setEroeCorrente(Eroe eroeCorrente) {
        this.eroeCorrente = eroeCorrente;
    }
}