# JavaFX setup

This project uses OpenJFX through Maven.

## IntelliJ IDEA

1. Open the project.
2. If IntelliJ asks to load the Maven project, accept.
3. Use this client main class:

```text
client.RiskClientApp
```

4. Use this server main class:

```text
Start
```

## Maven command

When Maven is available on the system, run the JavaFX client with:

```bash
mvn javafx:run
```

## Map SVG

Put the SVG map here:

```text
src/client/assets/risk-map.svg
```

`MapView` verifica che il file esista (senza WebView, compatibile con JDK 26).
Per anteprima interattiva: esportare in PNG e usare `ImageView`, oppure JDK 21–25 + dipendenza `javafx-web`.
