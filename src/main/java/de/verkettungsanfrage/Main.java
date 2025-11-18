package de.verkettungsanfrage;

/**
 * Hauptklasse zur Demonstration der Method-Chaining-Funktionalität.
 *
 * Diese Klasse dient als Konsolenanwendung, die verschiedene Szenarien
 * der Auto-Klasse mit dem Fluent-Interface-Pattern (Method Chaining)
 * demonstriert.
 *
 * Demonstrierte Konzepte:
 * - Verkettung mehrerer Methodenaufrufe in einer Zeile
 * - Realistisches Fahrverhalten (Tanken, Starten, Fahren, Bremsen)
 * - Verschiedene Use-Cases von simpel bis extrem
 *
 * Method Chaining ermöglicht eine lesbare, flüssige API:
 * auto.starteMotor().fahreLos().beschleunige(50).bremsen(20);
 *
 * anstatt:
 * auto.starteMotor();
 * auto.fahreLos();
 * auto.beschleunige(50);
 * auto.bremsen(20);
 *
 * @author Moboudni
 * @version 2.1
 * @since 2025
 */
public class Main {

    /**
     * Einstiegspunkt der Konsolenanwendung.
     *
     * Führt zwei unterschiedliche Szenarien aus:
     *
     * Szenario 1 - Normales Fahrverhalten:
     * - Auto wird initialisiert mit wenig Treibstoff (5L)
     * - Tanken von 20 Litern
     * - Motor starten
     * - Losfahren und Beschleunigen
     * - Bremsen und Hupen
     *
     * Szenario 2 - Extremtest:
     * - Starke Beschleunigung bis nahe Maximalgeschwindigkeit
     * - Vollbremsung
     * - Motor stoppen
     *
     * Beide Szenarien zeigen die Flexibilität und Lesbarkeit
     * des Fluent-Interface-Patterns.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        // Initialisierung des Autos mit:
        // - Modell: "VW Golf"
        // - Initialer Tankstand: 5.0 Liter (kritisch niedrig)
        // - Maximalgeschwindigkeit: 200 km/h
        Auto meinAuto = new Auto("VW Golf", 5.0, 200);

        System.out.println("--- Start des Method-Chaining-Szenarios ---");

        // Szenario 1: Normales Fahrverhalten mit Method Chaining
        // Jede Methode gibt 'this' zurück, was die Verkettung ermöglicht
        meinAuto.zeigeZustand()      // Zeigt den initialen Zustand an
                .tanken(20.0)         // Tankt 20 Liter auf (gesamt: 25L)
                .starteMotor()        // Startet den Motor
                .fahreLos()           // Setzt Geschwindigkeit auf 1 km/h
                .beschleunige(50.0)   // Beschleunigt auf 51 km/h
                .bremsen(20.0)        // Reduziert auf 31 km/h
                .hupe()               // Betätigt die Hupe
                .zeigeZustand();      // Zeigt den Endzustand an

        // Szenario 2: Extremtest mit hoher Geschwindigkeit
        System.out.println("\n--- Zweites Szenario: Extremer Test ---");

        // Testet Grenzwerte und Sicherheitsmechanismen
        meinAuto.beschleunige(150.0)  // Beschleunigt auf ~181 km/h (nahe Maximum)
                .zeigeZustand()        // Zeigt Zustand bei hoher Geschwindigkeit
                .bremsen(180.0)        // Vollbremsung auf 1 km/h (Minimum ist 0)
                .stoppeMotor()         // Stoppt Motor (setzt Geschwindigkeit auf 0)
                .zeigeZustand();       // Zeigt finalen Zustand (stillstehendes Auto)

        // Hinweis: Die Geschwindigkeit wird durch maxGeschwindigkeit begrenzt
        // und kann nicht unter 0 fallen. Der Tankverbrauch wird bei jeder
        // Beschleunigung automatisch berechnet.
    }
}