package it.unicam.cs.mpgc.rpg130723.model.carte;

import it.unicam.cs.mpgc.rpg130723.model.enums.FaseAttivazione;

/**
 * Classe astratta che rappresenta un incantesimo del Boss.
 * Ogni incantesimo può essere attivato solo durante una fase
 * specifica del turno (COSTRUZIONE o AVVENTURA).
 * È astratta perché esistono solo sottoclassi concrete
 * come IncantesimoDanno, IncantesimoTeletrasporto, ecc.
 */
public abstract class Incantesimo extends Carta {

    private final FaseAttivazione fase;

    protected Incantesimo(String nome, String descrizione, FaseAttivazione fase) {
        super(nome, descrizione);
        if (fase == null)
            throw new IllegalArgumentException("La fase di attivazione non può essere nulla.");
        this.fase = fase;
    }

    public FaseAttivazione getFase() {
        return fase;
    }
}