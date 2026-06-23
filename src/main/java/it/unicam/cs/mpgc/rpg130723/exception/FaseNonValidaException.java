package it.unicam.cs.mpgc.rpg130723.exception;

/**
 * Si lancia questa eccezione quando si tenta di attivare un effetto o una carta nella fase di turno sbagliata.
 */
public class FaseNonValidaException extends RuntimeException {
    public FaseNonValidaException(String messaggio) {
        super(messaggio);
    }
}
