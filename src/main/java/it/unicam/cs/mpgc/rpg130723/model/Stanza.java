package it.unicam.cs.mpgc.rpg130723.model;

import it.unicam.cs.mpgc.rpg130723.model.enums.TipoTesoro;

/**
 * Classe astratta che rappresenta una stanza del Dungeon.
 * Ogni stanza ha un valore di danno e un tipo di tesoro che espone,
 * che serve ad attrarre determinati Eroi.
 * È astratta perché esistono solo StanzaMostro e StanzaTrappola.
 */
public abstract class Stanza extends Carta {
    private final int valoreDanno;
    private final TipoTesoro tesoroFornito;

    protected Stanza(String nome, String descrizione, int valoreDanno, TipoTesoro tesoroFornito) {
        super(nome, descrizione);
        if (valoreDanno < 0)
            throw new IllegalArgumentException("Il valore di danno non può essere negativo.");
        if (tesoroFornito == null)
            throw new IllegalArgumentException("Il tesoro fornito non può essere nullo.");
        this.valoreDanno = valoreDanno;
        this.tesoroFornito = tesoroFornito;
    }

    public int getValoreDanno() {
        return valoreDanno;
    }

    public TipoTesoro getTesoroFornito() {
        return tesoroFornito;
    }
}
