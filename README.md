# Piazza Tienanmen - Progetto Risk

Progetto Java che implementa una versione multiplayer di Risk/Risiko con architettura client-server.

L'applicazione permette a piu giocatori di collegarsi a un server centrale, entrare in una lobby, scegliere un nickname e avviare una partita quando viene raggiunto il numero minimo di partecipanti. Il client desktop e realizzato con JavaFX, mentre il server gestisce le connessioni di rete e lo stato iniziale della partita tramite socket Java.

## Funzionalita principali

- Server TCP che accetta da 3 a 6 giocatori.
- Lobby multiplayer con nickname unici.
- Permesso di avvio assegnato al primo giocatore connesso.
- Client desktop JavaFX con schermata lobby e schermata di gioco.
- Setup iniziale della partita con assegnazione di colori, territori e armate.
- Messaggistica serializzata tra client e server per stato partita, chat, turni e azioni di gioco.

## Tecnologie usate

- Java 21
- Maven
- JavaFX
- Socket Java
- Object streams per lo scambio di messaggi serializzabili

## Struttura del progetto

Il codice principale si trova in:

```text
risk_start/progetto_risk/
```

Aree importanti:

- `src/Start.java`: entry point del server.
- `src/network/`: gestione server, connessioni client e messaggi di rete.
- `src/client/`: applicazione JavaFX lato client.
- `src/client/controller/`: coordinamento tra UI, rete e stato locale.
- `src/client/view/`: schermate JavaFX della lobby e della partita.
- `src/menu/Setup.java`: inizializzazione della partita.
- `src/map/`: modelli relativi a territori, carte e mappa.
- `src/gameLogic/`: area dedicata alla logica di gioco.

## Requisiti

- JDK 21 o superiore (in sviluppo si usa spesso JDK 26)
- Maven
- **JavaFX** via Maven (`org.openjfx`): la versione nel `pom.xml` (`javafx.version`) deve avere la **stessa major del JDK** (es. JDK 26 → JavaFX `26.0.1`).
- **WebView** (`javafx-web`) non è incluso: su JDK 26 il modulo `jdk.jsobject` non esiste. La mappa usa un placeholder; per SVG interattivo in browser embedded serve JDK 21–25 o un rendering via `ImageView`.

## Come avviare il server

Dalla cartella del modulo Maven `risk_start/progetto_risk`:

```bash
cd risk_start/progetto_risk
mvn compile
mvn exec:java -Dexec.mainClass=Start

# oppure, dopo compile
java -cp target/classes Start
```

Dalla root del repository:

```bash
mvn -pl risk_start/progetto_risk compile
mvn -pl risk_start/progetto_risk exec:java -Dexec.mainClass=Start
```

Di default il server parte sulla porta `5555`.

E possibile specificare una porta diversa passando un argomento:

```bash
java -cp target/classes Start 6000
```

## Come avviare il client

In un altro terminale, dalla cartella del modulo:

```bash
cd risk_start/progetto_risk
mvn javafx:run
```

Dalla root del repository:

```bash
mvn -pl risk_start/progetto_risk javafx:run
```

Per iniziare una partita servono almeno 3 client collegati con nickname diversi. Il primo giocatore che entra nella lobby puo avviare la partita.

## Stato del progetto

Il progetto contiene gia la base dell'infrastruttura multiplayer, la lobby e il setup iniziale della partita. Alcune parti della logica completa di Risk/Risiko, come la risoluzione degli attacchi, i movimenti avanzati e la gestione completa delle regole, sono ancora in sviluppo.


中华人民共和国万岁！
+900 社会积分 👍
![image](https://img.itch.zone/aW1nLzExOTUzODE1LnBuZw==/original/sEBjV4.png)
