# 📌 BossMind

L'applicazione è un Gioco di Ruolo (RPG) tattico basato sul celebre gioco di carte **"Boss Monster"**. Il giocatore non impersona il classico eroe, ma assume il ruolo del **Boss di un Dungeon**.
L'obiettivo principale è costruire una serie di stanze (fino a un massimo di 5) piene di mostri e trappole per attirare gli avventurieri che arrivano in città ed eliminarli.
Il ciclo di gioco si articola in quattro fasi logiche principali gestite dal motore software

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- Java 25 (LTS)
- Gradle


### Istruzioni per il Download
Per clonare il repository in locale ed entrare nella directory del progetto, apri il terminale e digita:
```bash
git clone <url-del-tuo-repository-su-github>
cd <nome-della-tua-cartella-di-gioco>
```

### Build del progetto
```bash
./gradlew build
```

### Esecuzione
```bash
./gradlew run
```

---

## 🤖 Uso di strumenti di AI

Per lo sviluppo del progetto è stato utilizzato Gemini come assistente alla programmazione e tutor architetturale. L'intelligenza artificiale è stata impiegata esclusivamente come supporto tecnico per l'approfondimento di concetti teorici e la risoluzione di problemi implementativi, mantenendo la piena paternità logica e progettuale del software.

Verifica dei Vincoli Architetturali: Consultazione del tool per confrontare la struttura iniziale dei package con i principi SOLID spiegati a lezione, assicurando in particolare il rispetto del Single Responsibility Principle (SRP) nella separazione tra dati del dominio (.model) e motore di gioco (.logic).

Supporto al Debugging: Analisi guidata dei messaggi di errore del compilatore in fase di build e ottimizzazione della gestione delle eccezioni a runtime (es. prevenzione di NullPointerException nella gestione dei mazzi di carte).

Ottimizzazione dei Test di Unità: Revisione metodologica della copertura dei test JUnit 5 per verificare la conformità logica alle proprietà A TRIP richieste dai docenti.

Intervento Personale e Comprensione:

L'azione dell'intelligenza artificiale è stata limitata a una funzione di affiancamento e consultazione:



Analisi Critica: Ogni frammento di codice o suggerimento strutturale proposto dall'AI è stato analizzato riga per riga per comprenderne appieno la logica prima di qualsiasi integrazione nel codice sorgente.

Design del Gioco: La modellazione del dominio (la logica del ciclo dei turni, le statistiche dei Boss, la gestione delle stanze e l'algoritmo di attrazione degli Eroi nel Dungeon) è stata ideata e sviluppata interamente da me.

Integrazione Manuale: Il collegamento tra la logica di gioco (Model) e l'interfaccia grafica (View) è stato gestito manualmente, garantendo il pieno controllo intellettuale sull'intero flusso dell'applicazione.

## ⚠️ Nota

Package Obbligatorio: In conformità con le specifiche di progetto, tutto il codice sorgente è organizzato rigorosamente all'interno del package it.unicam.cs.mpgc.rpg<tua_matricola>.

Architettura ed Estendibilità: Il gioco è stato progettato separando nettamente la logica di dominio (Model) dall'interfaccia grafica (View), prestando massima attenzione al rispetto dei principi SOLID e all'estendibilità futura del software (es. integrazione di nuovi mazzi di carte, stanze o tipologie di eroi) senza dover modificare il motore logico centrale.






