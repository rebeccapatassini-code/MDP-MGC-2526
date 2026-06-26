# 🎮 BossMind RPG

**BossMind** è un Gioco di Ruolo (RPG) di strategia e gestione strutturato come un **Dungeon Crawler inverso**. Il giocatore assume il ruolo di un Boss, custode di un dungeon, con l'obiettivo di progettare un sistema di stanze e trappole per intercettare ed eliminare gli eroi avventurieri che vogliono attraversarlo.
Il progetto è focalizzato sulla modularità del codice e sulla separazione tra logica di dominio e persistenza dati, seguendo i principi **SOLID**.

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 25 (LTS)
- Gradle 9.3

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
  - Verifica della coerenza tra l'implementazione e i principi **SOLID** e **Clean Code**.

È importante sottolineare che tutto il codice prodotto è stato 
**compreso, revisionato e integrato personalmente**. 
Gli strumenti AI, infatti, sono stati esclusivamente usati come supporto ragionato, 
non come sostituto del processo di apprendimento.

📌 Per una descrizione più dettagliata delle scelte progettuali 
e dell'uso dell'AI, consultare la **Wiki del repository**.
