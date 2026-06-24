package it.unicam.cs.mpgc.rpg130723.model.personaggi;

import it.unicam.cs.mpgc.rpg130723.model.enums.TipoTesoro;
import it.unicam.cs.mpgc.rpg130723.interfaces.Danneggiabile;

/**
 * Rappresenta un eroe avventuriero che tenta di attraversare il Dungeon.
 * Ha punti vita, un tipo di tesoro che lo attrae e un valore anima/ferita
 * che determina le conseguenze della sua sopravvivenza o morte.
 */
public class Eroe implements Danneggiabile{

    private final String nome;
    private final TipoTesoro interesse;
    private final int valoreAnima;
    private int puntiVita;

    public Eroe(String nome, int puntiVita, TipoTesoro interesse, int valoreAnima) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Il nome dell'eroe non può essere nullo o vuoto.");
        if (puntiVita <= 0)
            throw new IllegalArgumentException("I punti vita devono essere positivi.");
        if (interesse == null)
            throw new IllegalArgumentException("Il tipo di tesoro non può essere nullo.");
        if (valoreAnima <= 0)
            throw new IllegalArgumentException("Il valore anima deve essere positivo.");
        this.nome = nome;
        this.puntiVita = puntiVita;
        this.interesse = interesse;
        this.valoreAnima = valoreAnima;
    }

    public String getNome() {
        return nome;
    }

    public int getPuntiVita() {
        return puntiVita;
    }

    public TipoTesoro getInteresse() {
        return interesse;
    }

    public int getValoreAnima() {
        return valoreAnima;
    }

    public void subisciDanno(int danno) {
        if (danno < 0)
            throw new IllegalArgumentException("Il danno non può essere negativo.");
        this.puntiVita -= danno;
    }

    public boolean isVivo() {
        return puntiVita > 0;
    }
}
