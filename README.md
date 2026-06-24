# 🎮 BossMind RPG

L'applicazione è un **Gioco di Ruolo di strategia e gestione**, in cui il giocatore assume il ruolo del custode di un complesso sistema di dungeon. L'obiettivo principale è progettare e potenziare un labirinto sotterraneo, popolandolo di mostri e trappole, per intercettare e neutralizzare gli eroi che tentano di esplorarlo.
Il sistema è strutturato come un **Dungeon Crawler inverso**, dove il ciclo di gioco è coordinato da un motore software modulare che gestisce fasi logiche distinte (Costruzione, Richiamo e Avventura), garantendo una netta separazione tra la logica di dominio e l'esecuzione.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 21
- Gradle

### Istruzioni

```bash
git clone <url-del-repository>
cd BossMind
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

### Esecuzione dei test
```bash
./gradlew test
```

---

## 🤖 Uso di strumenti di AI

Durante lo sviluppo sono stati utilizzati i seguenti strumenti di AI:

- **Claude e Gemini** come sviluppatori di supporto per:
  - Consultazione per adattare le regole del gioco cartaceo originale a una modalità single-player fluida e per strutturare il passaggio di informazioni tra la logica di controllo del dominio (.logic).
  - Supporto nella strutturazione dei flussi dei test di unità (BattleTest) per verificare la corretta gestione dei danni dell'eroe.

- **NotebookLM** come consulente tecnico per:
  - Verifica della coerenza con i principi insegnati a lezione

È importante sottolineare che tutto il codice prodotto è stato 
**compreso, revisionato e integrato personalmente**. 
Gli strumenti AI, infatti, sono stati esclusivamente usati come supporto ragionato, 
non come sostituto del processo di apprendimento.

📌 Per una descrizione più dettagliata delle scelte progettuali 
e dell'uso dell'AI, consultare la **Wiki del repository**.
