package it.unicam.cs.mpgc.rpg130723.interfaces;

import it.unicam.cs.mpgc.rpg130723.model.Dungeon;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Eroe;

/**
 * Calcola l'attrazione degli eroi verso un dungeon in base alle icone tesoro.
 */
public interface ValutatoreTesoro {
    boolean vieneAttirato(Dungeon dungeon, Eroe eroe);
}