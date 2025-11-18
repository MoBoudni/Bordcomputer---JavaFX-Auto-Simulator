package de.verkettungsanfrage;

/**
 * Repräsentiert ein Auto mit grundlegenden Funktionen und Zuständen.
 *
 * Diese Klasse implementiert das Fluent-Interface-Pattern (Method Chaining),
 * bei dem alle zustandsändernden Methoden die Auto-Instanz zurückgeben.
 * Dies ermöglicht verkettete Methodenaufrufe wie:
 * auto.starteMotor().beschleunige(50).tanken(10);
 *
 * Hauptfunktionalitäten:
 * - Motorsteuerung (Start/Stop)
 * - Geschwindigkeitsmanagement (Beschleunigen/Bremsen)
 * - Tankmanagement mit automatischem Verbrauch
 * - Hupe
 *
 * Das Auto verfügt über realistische Constraints:
 * - Motor kann nur bei vorhandenem Treibstoff gestartet werden
 * - Beschleunigung nur bei laufendem Motor möglich
 * - Maximale Geschwindigkeit und Tankkapazität sind begrenzt
 * - Automatisches Motorstopp bei leerem Tank
 *
 * @author Moboudni
 * @version 1.0
 * @since 2025
 */
public class Auto {

    /** Das Modell des Autos (z.B. "VW Golf"). */
    private String modell;

    /**
     * Aktueller Tankstand in Litern.
     * Wird als double gespeichert, um präzise Verbrauchsberechnungen zu ermöglichen.
     */
    private double tankstand;

    /**
     * Maximale Tankkapazität in Litern.
     * Standard: 50 Liter.
     */
    private final double maxTankstand = 50.0;

    /**
     * Aktuelle Geschwindigkeit in km/h.
     * Wird als double gespeichert für sanfte Animationen und präzise Berechnungen.
     */
    private double geschwindigkeit;

    /**
     * Maximale Geschwindigkeit des Autos in km/h.
     * Wird im Konstruktor festgelegt und ist unveränderbar.
     */
    private final int maxGeschwindigkeit;

    /**
     * Motorstatus: true = Motor läuft, false = Motor aus.
     */
    private boolean motorLaeuft;

    /**
     * Erstellt ein neues Auto-Objekt mit den angegebenen Eigenschaften.
     *
     * Der initiale Tankstand wird automatisch auf die maximale Tankkapazität
     * begrenzt, falls ein höherer Wert übergeben wird.
     * Die Geschwindigkeit wird initial auf 0 gesetzt und der Motor ist aus.
     *
     * @param modell der Name des Automodells (z.B. "VW Golf", "BMW 3er")
     * @param initialTankstand der initiale Füllstand des Tanks in Litern
     * @param maxGeschwindigkeit die maximale Geschwindigkeit des Autos in km/h
     */
    public Auto(String modell, double initialTankstand, int maxGeschwindigkeit) {
        this.modell = modell;
        this.tankstand = Math.min(initialTankstand, maxTankstand);
        this.maxGeschwindigkeit = maxGeschwindigkeit;
        this.geschwindigkeit = 0.0;
        this.motorLaeuft = false;
    }

    // ====================================================================
    // ZUSTANDSÄNDERNDE METHODEN (Method Chaining / Fluent Interface)
    // ====================================================================

    /**
     * Startet den Motor des Autos.
     *
     * Der Motor kann nur gestartet werden, wenn noch Treibstoff im Tank ist.
     * Bei leerem Tank bleibt der Motor aus.
     *
     * Anwendungsbeispiel:
     * auto.starteMotor().beschleunige(50);
     *
     * @return die aktuelle Auto-Instanz für Method Chaining
     */
    public Auto starteMotor() {
        if (tankstand > 0.0) {
            this.motorLaeuft = true;
        }
        return this;
    }

    /**
     * Stoppt den Motor des Autos.
     *
     * Beim Stoppen wird automatisch auch die Geschwindigkeit auf 0 gesetzt,
     * da das Auto ohne laufenden Motor nicht fahren kann.
     *
     * @return die aktuelle Auto-Instanz für Method Chaining
     */
    public Auto stoppeMotor() {
        this.motorLaeuft = false;
        this.geschwindigkeit = 0.0;
        return this;
    }

    /**
     * Tankt eine bestimmte Menge Benzin nach.
     *
     * Die Methode stellt sicher, dass der Tank nicht über die maximale
     * Kapazität hinaus gefüllt wird. Überschüssiger Treibstoff wird
     * automatisch abgeschnitten.
     *
     * @param liter die zu tankende Menge in Litern (darf positiv sein)
     * @return die aktuelle Auto-Instanz für Method Chaining
     */
    public Auto tanken(double liter) {
        this.tankstand = Math.min(maxTankstand, this.tankstand + liter);
        return this;
    }

    /**
     * Erhöht die Geschwindigkeit des Autos.
     *
     * Voraussetzungen für eine erfolgreiche Beschleunigung:
     * - Motor muss laufen
     * - Tank darf nicht leer sein
     *
     * Die Geschwindigkeit wird automatisch auf die Maximalgeschwindigkeit
     * begrenzt. Bei jeder Beschleunigung wird Treibstoff verbraucht.
     *
     * @param delta die Geschwindigkeitszunahme in km/h
     * @return die aktuelle Auto-Instanz für Method Chaining
     */
    public Auto beschleunige(double delta) {
        if (motorLaeuft && tankstand > 0) {
            this.geschwindigkeit = Math.min(maxGeschwindigkeit, this.geschwindigkeit + delta);
            verbrauchPruefen(delta);
        }
        return this;
    }

    /**
     * Verringert die Geschwindigkeit des Autos (Bremsen).
     *
     * Die Geschwindigkeit wird auf mindestens 0 km/h begrenzt.
     * Negative Geschwindigkeiten (Rückwärtsfahren) sind nicht möglich.
     *
     * Das Bremsen verbraucht keinen Treibstoff und funktioniert auch
     * bei ausgeschaltetem Motor.
     *
     * @param delta die Geschwindigkeitsabnahme in km/h
     * @return die aktuelle Auto-Instanz für Method Chaining
     */
    public Auto bremsen(double delta) {
        this.geschwindigkeit = Math.max(0.0, this.geschwindigkeit - delta);
        return this;
    }

    /**
     * Setzt das Auto in Bewegung (Anfahren).
     *
     * Diese Methode wird verwendet, um aus dem Stillstand anzufahren.
     * Sie setzt die Geschwindigkeit auf einen minimalen Anfangswert (1 km/h).
     *
     * Voraussetzungen:
     * - Motor muss laufen
     * - Tank darf nicht leer sein
     * - Auto muss still stehen (Geschwindigkeit = 0)
     *
     * @return die aktuelle Auto-Instanz für Method Chaining
     */
    public Auto fahreLos() {
        if (motorLaeuft && tankstand > 0 && geschwindigkeit == 0) {
            this.geschwindigkeit = 1.0;
            verbrauchPruefen(1.0);
        }
        return this;
    }

    /**
     * Zeigt den aktuellen Zustand des Autos an.
     *
     * Diese Methode kann für Debugging oder Konsolenausgaben verwendet werden.
     * Die konkrete Implementierung kann nach Bedarf erfolgen.
     *
     * @return die aktuelle Auto-Instanz für Method Chaining
     */
    public Auto zeigeZustand() {
        // Implementierung kann bei Bedarf erweitert werden
        // Beispiel: System.out.println("Auto: " + modell + ", Tank: " + tankstand + "L");
        return this;
    }

    /**
     * Betätigt die Hupe des Autos.
     *
     * Diese Methode dient als Platzhalter für die Hupenfunktion.
     * Die visuelle/akustische Darstellung wird im Bordcomputer gehandhabt.
     * Die Hupe funktioniert unabhängig vom Motorstatus.
     *
     * @return die aktuelle Auto-Instanz für Method Chaining
     */
    public Auto hupe() {
        // Logik für Hupe wird extern (z.B. im Bordcomputer) behandelt
        return this;
    }

    // ====================================================================
    // INTERNE HILFSMETHODEN
    // ====================================================================

    /**
     * Simuliert den Kraftstoffverbrauch basierend auf der Geschwindigkeitsänderung.
     *
     * Der Verbrauch ist proportional zur Geschwindigkeitsänderung:
     * - Größere Beschleunigungen verbrauchen mehr Treibstoff
     * - Kleine Änderungen (z.B. Ausrollen) verbrauchen wenig
     *
     * Bei leerem Tank wird der Motor automatisch gestoppt.
     *
     * Verbrauchsformel: 0.001 * delta Liter pro Beschleunigung
     *
     * @param delta die Geschwindigkeitsänderung in km/h
     */
    private void verbrauchPruefen(double delta) {
        // Vereinfachte Verbrauchslogik: 0.1% des Deltas als Verbrauch
        double verbrauch = 0.001 * delta;
        this.tankstand = Math.max(0.0, this.tankstand - verbrauch);

        // Automatischer Motorstopp bei leerem Tank
        if (tankstand == 0.0) {
            stoppeMotor();
        }
    }

    // ====================================================================
    // GETTER-METHODEN (Lesezugriff auf den Zustand)
    // ====================================================================

    /**
     * Gibt das Modell des Autos zurück.
     *
     * @return das Automodell als String
     */
    public String getModell() {
        return modell;
    }

    /**
     * Gibt den aktuellen Tankstand zurück.
     *
     * @return der Tankstand in Litern
     */
    public double getTankstand() {
        return tankstand;
    }

    /**
     * Gibt die maximale Tankkapazität zurück.
     *
     * @return die maximale Tankkapazität in Litern
     */
    public double getMaxTankstand() {
        return maxTankstand;
    }

    /**
     * Gibt die aktuelle Geschwindigkeit zurück.
     *
     * @return die Geschwindigkeit in km/h
     */
    public double getGeschwindigkeit() {
        return geschwindigkeit;
    }

    /**
     * Gibt die maximale Geschwindigkeit des Autos zurück.
     *
     * @return die Maximalgeschwindigkeit in km/h
     */
    public int getMaxGeschwindigkeit() {
        return maxGeschwindigkeit;
    }

    /**
     * Prüft, ob der Motor aktuell läuft.
     *
     * @return true wenn der Motor läuft, false wenn er aus ist
     */
    public boolean isMotorLaeuft() {
        return motorLaeuft;
    }

    /**
     * Prüft, ob der Tankstand kritisch niedrig ist.
     *
     * Ein kritischer Tankstand liegt vor, wenn weniger als 10% der
     * maximalen Tankkapazität vorhanden sind (< 5 Liter bei 50L Tank).
     * Dies kann für Warnungen im Bordcomputer verwendet werden.
     *
     * @return true wenn der Tankstand unter 10% liegt, sonst false
     */
    public boolean istTankKritisch() {
        return tankstand < (maxTankstand * 0.1);
    }
}