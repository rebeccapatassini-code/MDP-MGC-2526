package it.unicam.cs.mpgc.rpg130723.exception;

/**
 * Si lancia questa eccezione quando si tenta di aggiungere una stanza a un dungeon che ha già raggiunto il limite massimo.
 */
public class DungeonPienoException extends RuntimeException {
    public DungeonPienoException(String messaggio) {
        super(messaggio);
    }
}