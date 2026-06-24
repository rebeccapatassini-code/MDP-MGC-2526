package it.unicam.cs.mpgc.rpg130723.model.carte;

import it.unicam.cs.mpgc.rpg130723.logic.GameContext;
import it.unicam.cs.mpgc.rpg130723.model.enums.TipoTesoro;

/**
 * Stanza caratterizzata da una trappola che danneggia gli eroi.
 * Implementa eseguiEffetto() applicando il danno della stanza all'eroe corrente.
 */
public class StanzaTrappola extends Stanza {

    public StanzaTrappola(String nome, String descrizione, int valoreDanno, TipoTesoro tesoroFornito) {
        super(nome, descrizione, valoreDanno, tesoroFornito);
    }

    @Override
    public void eseguiEffetto(GameContext context) {
        context.getEroeCorrente().subisciDanno(getValoreDanno());
    }
}