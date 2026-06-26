package it.unicam.cs.mpgc.rpg130723.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg130723.model.carte.StanzaMostro;
import it.unicam.cs.mpgc.rpg130723.model.carte.StanzaTrappola;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Eroe;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Carica i dati statici del gioco dai file JSON nella cartella resources.
 * Usa Gson per deserializzare i file in oggetti Java (Aggiungere
 * nuove stanze non richiede modifiche al codice, solo al file JSON).
 */
public class JsonProvider {

    private final Gson gson;

    public JsonProvider() {
        this.gson = new Gson();
    }

    public List<StanzaMostro> caricaStanzeMostro() {
        return caricaDaJson("stanze_mostro.json",
                new TypeToken<List<StanzaMostro>>(){}.getType());
    }

    public List<StanzaTrappola> caricaStanzeTrappola() {
        return caricaDaJson("stanze_trappola.json",
                new TypeToken<List<StanzaTrappola>>(){}.getType());
    }

    public List<Eroe> caricaEroi() {
        return caricaDaJson("eroi.json",
                new TypeToken<List<Eroe>>(){}.getType());
    }

    private <T> T caricaDaJson(String nomeFile, Type tipo) {
        InputStream stream = getClass()
                .getClassLoader()
                .getResourceAsStream(nomeFile);
        if (stream == null)
            throw new IllegalStateException(
                    "File non trovato nelle resources: " + nomeFile);
        return gson.fromJson(new InputStreamReader(stream), tipo);
    }
}