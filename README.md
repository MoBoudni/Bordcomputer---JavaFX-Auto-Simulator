# 🚗 Bordcomputer App (JavaFX & Maven)

Dieses Projekt implementiert eine Bordcomputer-Applikation mithilfe von JavaFX und demonstriert dabei fortgeschrittene Java-Konzepte wie das **Fluent Interface Pattern (Method Chaining)** und eine professionelle Testabdeckung mit JUnit 5 und AssertJ.

## 📋 Inhaltsverzeichnis
- [🎯 Überblick](#überblick)
- [✨ Features](#features)
- [✅ Unit Tests und AssertJ (Neu)](#unit-tests-und-assertj-neu)
- [🛠️ Technologien & Tools](#technologien--tools)
- [📦 Voraussetzungen](#voraussetzungen)
- [🚀 Installation](#installation)
- [💻 Verwendung](#verwendung)
- [📂 Projektstruktur](#projektstruktur)
- [🏗 Architektur](#architektur)
- [📚 Code-Dokumentation](#code-dokumentation)
- [📄 Lizenz](#lizenz)

## 🎯 Überblick
Der Bordcomputer ist eine Desktop-Anwendung, die ein Auto-Objekt mit allen wesentlichen Funktionen simuliert. Die Anwendung demonstriert das Fluent Interface Pattern (Method Chaining) und bietet eine ansprechende grafische Benutzeroberfläche mit Echtzeit-Animationen.

### Hauptmerkmale
- 🎨 Moderne, dunkle UI mit abgerundeten Elementen  
- ⚡ Flüssige 60-FPS-Animationen  
- 📊 Analoger Tachometer mit digitaler Anzeige  
- ⛽ Animierte Tankfüllstandsanzeige mit Farbcodierung  
- 🎮 Intuitive Steuerung über Button-Panel  
- 🔄 Responsive Design  

## ✨ Features

### Tachometer
- Analoger Tacho mit farbcodierter Skala (Grün → Gelb → Rot)
- Animierte Nadel mit Smooth-Interpolation
- Digitale Geschwindigkeitsanzeige
- Realistische Fahrdynamik

### Tankmanagement
- Große Tankanzeige mit Flüssigkeits-Animation und Wellen-Effekt
- Farbwechsel je nach Füllstand:
  - 🟢 Grün: > 50%
  - 🟠 Orange: 25–50%
  - 🔴 Rot: < 25%
- Tank kann nicht über 100 % gefüllt werden
- Visuelle Meldung „Tank voll!“

### Motor- & Fahrzeugfunktionen
- Motor starten/stoppen (farbliche Statusanzeige)
- Losfahren, Beschleunigen (+30 km/h), Bremsen (–20 km/h)
- Hupen mit „HUUUUUP!“-Animation
- Realistischer Kraftstoffverbrauch
- Automatischer Motorstopp bei leerem Tank

## ✅ Unit Tests und AssertJ (Neu)
Umfangreiche Tests mit **JUnit 5** und **AssertJ** für lesbare, flüssige Assertions.

**Wichtigste Tests:**
- Methodenverkettung (Fluent Interface)
- Maximalgeschwindigkeit wird nicht überschritten
- Motor startet nicht bei leerem Tank
- Tankstand bleibt immer ≤ 50.0 Liter

Maven-Dependency (pom.xml):
```xml
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>${assertj.version}</version>
    <scope>test</scope>
</dependency>
```

## 🛠️ Technologien & Tools
- Java 21
- Apache Maven
- JavaFX 21.0.1
- JUnit 5 + AssertJ
- JaCoCo (Code Coverage)
- Fluent Interface Pattern
- Canvas, Timeline, AnimationTimer

## 📦 Voraussetzungen
- JDK 21 oder höher
- Maven 3.x

## 🚀 Installation & Start

```bash
# Projekt klonen
git clone https://github.com/IhrUsername/bordcomputer-app.git
cd bordcomputer-app

# Bauen + Tests + JaCoCo-Report
mvn clean verify

# GUI starten (empfohlen)
mvn javafx:run

# Oder Konsolen-Demo
mvn exec:java -Dexec.mainClass="de.verkettungsanfrage.Main"
```

## 💻 Verwendung – Method Chaining Beispiel
```java
new Auto("VW Golf", 10.0, 220)
    .tanken(30.0)
    .starteMotor()
    .fahreLos()
    .beschleunige(80)
    .zeigeZustand();
```

## 📂 Projektstruktur
```
bordcomputer-app/
├── src/
│   └── main/java/de/verkettungsanfrage/
│       ├── Auto.java          # Fluent Model
│       ├── Bordcomputer.java  # JavaFX GUI
│       └── Main.java          # Konsolen-Demo
├── src/test/java/de/verkettungsanfrage/
│       └── AutoTest.java      # JUnit 5 + AssertJ Tests
├── pom.xml
└── README.md
```

## 🏗 Architektur
- **Fluent Interface**: Alle zustandsändernden Methoden geben `this` zurück
- **MVC-ähnlich**: `Auto.java` = Model, `Bordcomputer.java` = View/Controller

## 📚 Code-Dokumentation generieren
```bash
mvn javadoc:javadoc
```
→ Report unter `target/site/apidocs/index.html`


![Bordcomputer Screenshot](https://github.com/MoBoudni/Bordcomputer---JavaFX-Auto-Simulator/blob/a8298668db01c67ca577bdf7c0bcd8017ca841dd/Bordcomputer5.png)


## 📄 Lizenz
© 2025 Moboudni – Bordcomputer Deluxe  
Für Bildungszwecke frei verwendbar.
