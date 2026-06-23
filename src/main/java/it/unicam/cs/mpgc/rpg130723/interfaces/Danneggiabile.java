package it.unicam.cs.mpgc.rpg130723.interfaces;

/**
 * Definisce il contratto per le entità che possono subire modifiche ai punti vita
 * e garantisce il disaccoppiamento tra il motore di gioco e i modelli concreti.
 */
public interface Danneggiabile {
    void subisciDanno(int danno);
    boolean isVivo();
}
