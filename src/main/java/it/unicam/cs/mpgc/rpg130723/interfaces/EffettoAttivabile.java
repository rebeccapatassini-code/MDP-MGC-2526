package it.unicam.cs.mpgc.rpg130723.interfaces;

import it.unicam.cs.mpgc.rpg130723.logic.GameContext;

/**
 * Permette l'esecuzione di effetti dinamici legati al contesto di gioco.
 */
public interface EffettoAttivabile {
    void eseguiEffetto(GameContext context);
}
