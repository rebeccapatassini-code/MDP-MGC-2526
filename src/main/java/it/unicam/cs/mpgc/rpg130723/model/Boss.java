package it.unicam.cs.mpgc.rpg130723.model;

/**
 * Rappresenta il Boss controllato dal giocatore.
 * Estende Carta per avere nome e descrizione.
 * Tiene traccia delle Anime accumulate (vittoria a 10)
 * e delle Ferite subite (sconfitta a 5).
 */
public class Boss extends Carta {

    private final int puntiEsperienza;
    private int anime;
    private int ferite;

    public Boss(String nome, String descrizione, int puntiEsperienza) {
        super(nome, descrizione);
        if (puntiEsperienza <= 0)
            throw new IllegalArgumentException("I punti esperienza devono essere positivi.");
        this.puntiEsperienza = puntiEsperienza;
        this.anime = 0;
        this.ferite = 0;
    }

    public int getPuntiEsperienza() {
        return puntiEsperienza;
    }

    public int getAnime() {
        return anime;
    }

    public int getFerite() {
        return ferite;
    }

    public void aggiungiAnima() {
        this.anime++;
    }

    public void subisciFerita() {
        this.ferite++;
    }

    public boolean haVinto() {
        return anime >= 10;
    }

    public boolean haPerso() {
        return ferite >= 5;
    }
}
