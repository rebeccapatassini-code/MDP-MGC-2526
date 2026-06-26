package it.unicam.cs.mpgc.rpg130723;

import it.unicam.cs.mpgc.rpg130723.logic.GameEngine;
import it.unicam.cs.mpgc.rpg130723.model.Dungeon;
import it.unicam.cs.mpgc.rpg130723.model.carte.Stanza;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Boss;
import it.unicam.cs.mpgc.rpg130723.model.personaggi.Eroe;
import it.unicam.cs.mpgc.rpg130723.persistence.DatabaseLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Punto di ingresso del gioco BossMind RPG.
 * Gestisce il loop principale e l'interazione con il giocatore via console.
 * In futuro sostituibile con una View JavaFX senza modificare il core (OCP).
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== BOSSMIND RPG ===");
        System.out.println("Sei il Boss di un Dungeon.");
        System.out.println("Accumula 10 Anime prima di subire 5 Ferite!");
        System.out.println();

        DatabaseLoader loader = new DatabaseLoader();
        List<Stanza> tutteLeStanze = new ArrayList<>();
        tutteLeStanze.addAll(loader.caricaStanzeMostro());
        tutteLeStanze.addAll(loader.caricaStanzeTrappola());
        List<Eroe> tuttiGliEroi = loader.caricaEroi();

        Boss boss = new Boss("DarkLord", "Il signore oscuro del dungeon", 10);
        Dungeon dungeon = new Dungeon();

        GameEngine engine = new GameEngine(boss, dungeon, (d, e) -> {
            long icone = d.getStanze().stream()
                    .filter(s -> s.getTesoroFornito() == e.getInteresse())
                    .count();
            return icone >= 2;
        });

        System.out.println("Benvenuto, " + boss.getNome() + "!");
        System.out.println();

        while (!engine.isPartitaFinita()) {
            System.out.println("---------------------------------------");
            System.out.println("--- NUOVO TURNO ---");
            System.out.println("Anime: " + boss.getAnime() + "/10 | Ferite: " + boss.getFerite() + "/5");
            System.out.println();

            // Pesca 3 carte casuali
            Collections.shuffle(tutteLeStanze);
            List<Stanza> cartePescate = new ArrayList<>();
            cartePescate.add(tutteLeStanze.get(0));
            cartePescate.add(tutteLeStanze.get(1));
            cartePescate.add(tutteLeStanze.get(2));

            // Genera 2 eroi casuali freschi
            Collections.shuffle(tuttiGliEroi);
            List<Eroe> nuoviEroi = new ArrayList<>();
            nuoviEroi.add(new Eroe(
                    tuttiGliEroi.get(0).getNome(),
                    tuttiGliEroi.get(0).getPuntiVita(),
                    tuttiGliEroi.get(0).getInteresse(),
                    tuttiGliEroi.get(0).getValoreAnima()
            ));
            nuoviEroi.add(new Eroe(
                    tuttiGliEroi.get(1).getNome(),
                    tuttiGliEroi.get(1).getPuntiVita(),
                    tuttiGliEroi.get(1).getInteresse(),
                    tuttiGliEroi.get(1).getValoreAnima()
            ));

            engine.faseInizioTurno(cartePescate, nuoviEroi);

            // Prima mostra gli eroi nel villaggio
            System.out.println("👥 Eroi nel Villaggio:");
            nuoviEroi.forEach(e -> System.out.println(
                    "  - " + e.getNome() +
                            " | HP: " + e.getPuntiVita() +
                            " | Interesse: " + e.getInteresse()));
            System.out.println();

            // Poi mostra la mano completa
            List<Stanza> mano = engine.getMano();
            System.out.println("🃏 La tua mano (" + mano.size() + " carte):");
            for (int i = 0; i < mano.size(); i++) {
                System.out.println("  " + i + ") " + mano.get(i).getNome()
                        + " | Danno: " + mano.get(i).getValoreDanno()
                        + " | Tesoro: " + mano.get(i).getTesoroFornito());
            }
            System.out.println();

            // Scelta del giocatore
            if (!dungeon.isPieno()) {
                int scelta = -1;
                while (scelta < 0 || scelta >= mano.size()) {
                    System.out.print("Scegli quale carta aggiungere al Dungeon (0-"
                            + (mano.size() - 1) + "): ");
                    scelta = scanner.nextInt();
                }
                engine.faseCostruzione(scelta);
            } else {
                System.out.println("Dungeon pieno! Puoi sostituire una stanza esistente.");
                System.out.println("Scegli quale carta della mano usare (0-"
                        + (mano.size() - 1) + "): ");
                int cartaScelta = scanner.nextInt();
                System.out.println("Scegli quale stanza del Dungeon sostituire (0-4): ");
                int stanzaDaSostituire = scanner.nextInt();
                engine.faseSostituzione(cartaScelta, stanzaDaSostituire);
            }

            // Mostra dungeon aggiornato
            System.out.println();
            System.out.println("🏰 Dungeon attuale:");
            dungeon.getStanze().forEach(s -> System.out.println(
                    "  - " + s.getNome() +
                            " | Danno: " + s.getValoreDanno() +
                            " | Tesoro: " + s.getTesoroFornito()));
            System.out.println();

            // Fase richiamo e avventura
            List<Eroe> attratti = engine.faseRichiamo();
            if (attratti.isEmpty()) {
                System.out.println("Nessun eroe attirato questo turno.");
            } else {
                System.out.println("⚔️ Eroi che entrano nel Dungeon:");
                attratti.forEach(e -> System.out.println(
                        "  - " + e.getNome() +
                                " | HP: " + e.getPuntiVita()));
                engine.faseAvventura(attratti);
            }

            System.out.println();
            System.out.println("Anime: " + boss.getAnime() + "/10 | Ferite: " + boss.getFerite() + "/5");
            System.out.println();
        }

        System.out.println("=== FINE PARTITA ===");
        if (boss.haVinto()) {
            System.out.println("🎉 HAI VINTO! Hai accumulato 10 Anime!");
        } else {
            System.out.println("💀 HAI PERSO! Hai subito 5 Ferite!");
        }

        scanner.close();
    }
}