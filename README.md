🚗 Bordcomputer App (JavaFX & Maven)

Dieses Projekt implementiert eine Bordcomputer-Applikation mithilfe von JavaFX und demonstriert dabei fortgeschrittene Java-Konzepte wie das Fluent Interface Pattern (Method Chaining) und eine professionelle Testabdeckung mit JUnit 5 und AssertJ.

![Bordcomputer Screenshot](https://github.com/MoBoudni/Bordcomputer---JavaFX-Auto-Simulator/blob/a8298668db01c67ca577bdf7c0bcd8017ca841dd/Bordcomputer5.png)

📋 Inhaltsverzeichnis

 - 🎯 Überblick
 - ✨ Features
 - ✅ Unit Tests und AssertJ (Neu)
 - 🛠️ Technologien & Tools
 - 📦 Voraussetzungen
 - 🚀 Installation
 - 💻 Verwendung
 - 📂 Projektstruktur
 - 🏗 Architektur
 - 📚 Code-Dokumentation
 - 📄 Lizenz

🎯 Überblick

Der Bordcomputer ist eine Desktop-Anwendung, die ein Auto-Objekt mit allen wesentlichen Funktionen simuliert. Die Anwendung demonstriert das Fluent Interface Pattern (Method Chaining) und bietet eine ansprechende grafische Benutzeroberfläche mit Echtzeit-Animationen.

Hauptmerkmale

 🎨 Moderne, dunkle UI mit abgerundeten Elementen
 ⚡ Flüssige 60-FPS-Animationen
 📊 Analoger Tachometer mit digitaler Anzeige
 ⛽ Animierte Tankfüllstandsanzeige mit Farbcodierung
 🎮 Intuitive Steuerung über Button-Panel
 🔄 Responsive Design mit Scroll-Unterstützung

✨ Features

Tachometer
 
 - Analoger Tacho mit farbcodierter Skala (Grün → Gelb → Rot)
 - Animierte Nadel mit Smooth-Interpolation
 - Digitale Geschwindigkeitsanzeige mit Dezimalstellen
 - Mini-Tankanzeige im Zentrum
 - Realistische Sensor-Rausch-Simulation

Tankmanagement

 - Große Tankanzeige mit Füllstandsvisualisierung
 - Farbwechsel basierend auf Füllstand:
   - 🟢 Grün: > 50%
   - 🟠 Orange: 25-50%
   - 🔴 Rot: 10-25%
   - 🔴 Dunkelrot: < 10%
 - Animierte Übergänge bei Tankveränderungen
 - Automatischer Motorstopp bei leerem Tank
   
Motorsteuerung

 - Motor starten/stoppen mit Statusanzeige
 - Kraftstoffverbrauch bei Beschleunigung
 - Sanftes Ausrollen bei fehlendem Gas
 - Realistische Fahrdynamik

Fahrzeugfunktionen

 - Losfahren: Anfahren aus dem Stand
 - Beschleunigen: +30 km/h pro Klick
 - Bremsen: -20 km/h pro Klick
 - Hupen: Visuelle Benachrichtigung
 - Tanken: +10 Liter pro Klick

✅ Unit Tests und AssertJ (Neu)

Zur Gewährleistung der Korrektheit der Autologik werden umfangreiche Unit Tests verwendet.

Wichtigste Änderungen (Commit-Zusammenfassung):

1. AssertJ Integration: Die Bibliothek AssertJ wurde über die pom.xml als Test-Dependency hinzugefügt, um die Lesbarkeit und Ausdrucksstärke der Assertions in der Testklasse (AutoTest.java) zu verbessern.
2. Testabdeckung: Es wurden Tests implementiert, um die korrekte Funktion der Methodenverkettung, der Geschwindigkeitsbegrenzung (Maximalgeschwindigkeit) und der Motorlogik (z.B. Starten ohne Treibstoff) sicherzustellen.

Maven Dependency (pom.xml Auszug):

    <dependency>
       <groupId>org.assertj</groupId>
       <artifactId>assertj-core</artifactId>
       <version>${assertj.version}</version>
       <scope>test</scope>
    </dependency>

🛠️ Technologien & Tools

Hauptsprache: Java 21
Build-Tool: Apache Maven
UI-Framework: JavaFX 21.0.1
Unit Testing: JUnit 5
Fluent Assertions: AssertJ
Code Coverage: JaCoCo
Design Pattern: Fluent Interface (Method Chaining)
Animation: AnimationTimer, Timeline, KeyFrame

📦 Voraussetzungen

Java Development Kit (JDK) 21 oder höher

Maven 3.x

JavaFX 21.0.1 (wird automatisch über Maven geladen)

🚀 Installation

1. Repository klonen

   git clone [https://github.com/IhrUsername/bordcomputer-app.git](https://github.com/MoBoudni/bordcomputer-app.git)
   cd bordcomputer-app


2. Projekt bauen und Tests ausführen

Um das Projekt zu bauen, die Tests auszuführen und den JaCoCo Code Coverage Report zu generieren, verwenden Sie den folgenden Maven-Befehl:

   mvn clean verify

Der JaCoCo-Report wird anschließend unter target/site/jacoco/index.html gespeichert.

3. Anwendung starten

Option A: GUI-Start mit Maven

  mvn javafx:run


Option B: Konsolen-Demo

  mvn exec:java -Dexec.mainClass="de.verkettungsanfrage.Main"


💻 Verwendung

Method Chaining in Code

Die Klasse de.verkettungsanfrage.Auto wurde so konzipiert, dass alle zustandsändernden Methoden die aktuelle Instanz (this) des Autos zurückgeben. Dies ermöglicht eine intuitive und lesbare Kette von Operationen:

// Fluent Interface Beispiel
Auto meinAuto = new Auto("VW Golf", 10.0, 220);

meinAuto.tanken(30.0)
        .starteMotor()
        .fahreLos()
        .beschleunige(80.0)
        .zeigeZustand();


📂 Projektstruktur

bordcomputer-app/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── de/
│       │       └── verkettungsanfrage/
│       │           ├── Auto.java           # Model-Klasse (Fluent Interface)
│       │           ├── Bordcomputer.java   # JavaFX GUI-Anwendung (View/Controller)
│       │           └── Main.java           # Konsolen-Demo
│       └── resources/
│           └── styles.css                  # CSS-Styling (optional)
│
│   └── test/
│       └── java/
│           └── de/
│               └── verkettungsanfrage/
│                   └── AutoTest.java       # Unit Tests mit JUnit 5 und AssertJ
│
├── pom.xml                                 # Maven-Konfiguration
└── README.md                               # Diese Datei


🏗 Architektur

Design Patterns

1. Fluent Interface (Method Chaining)

Alle zustandsändernden Methoden der Auto-Klasse geben this zurück:

public Auto beschleunige(double delta) {
    // ... Logik ...
    return this; // Ermöglicht Verkettung
}


2. MVC-Inspiration

Model: Auto.java - Geschäftslogik und Zustand

View/Controller: Bordcomputer.java - UI-Komponenten und Event-Handler

Klassen-Übersicht

Auto.java

Kernklasse mit folgenden Verantwortlichkeiten:

Zustandsverwaltung (Motor, Geschwindigkeit, Tank)

Geschäftslogik (Verbrauch, Limits)

Fluent Interface für alle Aktionen

Bordcomputer.java

JavaFX-Anwendung mit:

Canvas-basiertem Tachometer

Timeline-Animationen für weiche Übergänge

AnimationTimer für 60-FPS-Updates

Responsivem Layout

📚 Code-Dokumentation

Alle Klassen sind vollständig mit JavaDoc (auf Deutsch) dokumentiert. Sie können die HTML-Dokumentation mit folgendem Maven-Befehl generieren:

mvn javadoc:javadoc


Die Dokumentation wird in target/site/apidocs/ erstellt.

📄 Lizenz

Dieses Projekt ist für Bildungszwecke erstellt.

© 2025 Moboudni - Bordcomputer Deluxe

Happy Coding! 🚗💨
