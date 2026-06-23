package it.unicam.cs.mpgc.rpg130723.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta il Dungeon del Boss.
 * Gestisce la lista delle stanze visibili (massimo 5).
 * Non si occupa della logica di gioco (SRP):
 * sa solo come è fatto il Dungeon, non come si gioca.
 */
public class Dungeon {

    private static final int MAX_STANZE = 5;
    private final List<Stanza> stanze;

    public Dungeon() {
        this.stanze = new ArrayList<>();
    }

    public void aggiungiStanza(Stanza stanza) {
        if (stanza == null)
            throw new IllegalArgumentException("La stanza non può essere nulla.");
        if (stanze.size() >= MAX_STANZE)
            throw new IllegalStateException("Il Dungeon è pieno: massimo " + MAX_STANZE + " stanze.");
        stanze.add(stanza);
    }

    public void sostituisciStanza(int indice, Stanza nuovaStanza) {
        if (nuovaStanza == null)
            throw new IllegalArgumentException("La nuova stanza non può essere nulla.");
        if (indice < 0 || indice >= stanze.size())
            throw new IndexOutOfBoundsException("Indice stanza non valido: " + indice);
        stanze.set(indice, nuovaStanza);
    }

    public List<Stanza> getStanze() {
        return Collections.unmodifiableList(stanze);
    }

    public int getNumeroStanze() {
        return stanze.size();
    }

    public boolean isPieno() {
        return stanze.size() >= MAX_STANZE;
    }
}
