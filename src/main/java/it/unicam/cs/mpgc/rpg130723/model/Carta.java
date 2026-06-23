package it.unicam.cs.mpgc.rpg130723.model;

/**
 * Classe astratta base per tutte le carte del gioco.
 * Contiene gli attributi comuni a ogni carta: nome e descrizione.
 * È astratta perché non ha senso istanziare una "carta generica",
 * ma solo le sue sottoclassi concrete.
 */
public abstract class Carta {

    private final String nome;
    private final String descrizione;

    protected Carta(String nome, String descrizione) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Il nome della carta non può essere nullo o vuoto.");
        if (descrizione == null || descrizione.isBlank())
            throw new IllegalArgumentException("La descrizione della carta non può essere nulla o vuota.");
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
