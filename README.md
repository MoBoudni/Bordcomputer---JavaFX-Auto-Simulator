# 🚗 Bordcomputer - JavaFX Auto-Simulator

Eine moderne JavaFX-Anwendung zur Visualisierung und Steuerung eines virtuellen Autos mit realistischem Bordcomputer-Display.

![Bordcomputer Screenshot](screenshot.png)

## 📋 Inhaltsverzeichnis

- [Überblick](#überblick)
- [Features](#features)
- [Technologien](#technologien)
- [Voraussetzungen](#voraussetzungen)
- [Installation](#installation)
- [Verwendung](#verwendung)
- [Projektstruktur](#projektstruktur)
- [Architektur](#architektur)
- [Code-Dokumentation](#code-dokumentation)
- [Lizenz](#lizenz)

## 🎯 Überblick

Der Bordcomputer ist eine Desktop-Anwendung, die ein Auto-Objekt mit allen wesentlichen Funktionen simuliert. Die Anwendung demonstriert das **Fluent Interface Pattern (Method Chaining)** und bietet eine ansprechende grafische Benutzeroberfläche mit Echtzeit-Animationen.

### Hauptmerkmale

- 🎨 Moderne, dunkle UI mit abgerundeten Elementen
- ⚡ Flüssige 60-FPS-Animationen
- 📊 Analoger Tachometer mit digitaler Anzeige
- ⛽ Animierte Tankfüllstandsanzeige mit Farbcodierung
- 🎮 Intuitive Steuerung über Button-Panel
- 🔄 Responsive Design mit Scroll-Unterstützung

## ✨ Features

### Tachometer
- Analoger Tacho mit farbcodierter Skala (Grün → Gelb → Rot)
- Animierte Nadel mit Smooth-Interpolation
- Digitale Geschwindigkeitsanzeige mit Dezimalstellen
- Mini-Tankanzeige im Zentrum
- Realistische Sensor-Rausch-Simulation

### Tankmanagement
- Große Tankanzeige mit Füllstandsvisualisierung
- Farbwechsel basierend auf Füllstand:
  - 🟢 Grün: > 50%
  - 🟠 Orange: 25-50%
  - 🔴 Rot: 10-25%
  - 🔴 Dunkelrot: < 10%
- Animierte Übergänge bei Tankveränderungen
- Automatischer Motorstopp bei leerem Tank

### Motorsteuerung
- Motor starten/stoppen mit Statusanzeige
- Kraftstoffverbrauch bei Beschleunigung
- Sanftes Ausrollen bei fehlendem Gas
- Realistische Fahrdynamik

### Fahrzeugfunktionen
- **Losfahren**: Anfahren aus dem Stand
- **Beschleunigen**: +30 km/h pro Klick
- **Bremsen**: -20 km/h pro Klick
- **Hupen**: Visuelle Benachrichtigung
- **Tanken**: +10 Liter pro Klick

## 🛠 Technologien

- **Java**: 21
- **JavaFX**: 21.0.1
- **Maven**: 3.x
- **Design Pattern**: Fluent Interface (Method Chaining)
- **Animation**: AnimationTimer, Timeline, KeyFrame

## 📦 Voraussetzungen

- Java Development Kit (JDK) 21 oder höher
- Maven 3.x
- JavaFX 21.0.1 (wird automatisch über Maven geladen)

## 🚀 Installation

### 1. Repository klonen

```bash
git clone https://github.com/IhrUsername/bordcomputer-app.git
cd bordcomputer-app
```

### 2. Projekt bauen

```bash
mvn clean install
```

### 3. Anwendung starten

**Option A: Mit Maven**
```bash
mvn javafx:run
```

**Option B: Konsolen-Demo**
```bash
mvn exec:java -Dexec.mainClass="de.verkettungsanfrage.Main"
```

## 💻 Verwendung

### GUI-Anwendung

Nach dem Start der Anwendung sehen Sie:

1. **Linke Seite**: Statusanzeige, Tachometer, Tankfüllstand
2. **Rechte Seite**: Steuerungspanel mit Buttons

#### Typischer Ablauf:
1. ✅ **Motor starten** (grüner Button)
2. 🚗 **Losfahren** (blaue Buttons)
3. ⚡ **Beschleunigen** mehrfach klicken
4. 🛑 **Bremsen** zur Geschwindigkeitsreduktion
5. ⛽ **Tanken** bei niedrigem Tankstand
6. 📯 **Hupen** zur Demo der Zusatzfunktionen

### Method Chaining in Code

```java
// Fluent Interface Beispiel
Auto meinAuto = new Auto("VW Golf", 10.0, 220);

meinAuto.starteMotor()
        .fahreLos()
        .beschleunige(50.0)
        .bremsen(20.0)
        .hupe()
        .tanken(10.0);
```

## 📂 Projektstruktur

```
bordcomputer-app/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── de/
│       │       └── verkettungsanfrage/
│       │           ├── Auto.java           # Model-Klasse mit Fluent Interface
│       │           ├── Bordcomputer.java   # JavaFX GUI-Anwendung
│       │           └── Main.java           # Konsolen-Demo
│       └── resources/
│           └── styles.css                  # CSS-Styling
│
├── pom.xml                                 # Maven-Konfiguration
└── README.md                               # Diese Datei
```

## 🏗 Architektur

### Design Patterns

#### 1. Fluent Interface (Method Chaining)
Alle zustandsändernden Methoden der `Auto`-Klasse geben `this` zurück:

```java
public Auto beschleunige(double delta) {
    if (motorLaeuft && tankstand > 0) {
        this.geschwindigkeit = Math.min(maxGeschwindigkeit, this.geschwindigkeit + delta);
        verbrauchPruefen(delta);
    }
    return this;  // Ermöglicht Verkettung
}
```

#### 2. MVC-Inspiration
- **Model**: `Auto.java` - Geschäftslogik und Zustand
- **View**: `Bordcomputer.java` - UI-Komponenten
- **Controller**: Integriert in `Bordcomputer.java` - Event-Handler

### Klassen-Übersicht

#### Auto.java
Kernklasse mit folgenden Verantwortlichkeiten:
- Zustandsverwaltung (Motor, Geschwindigkeit, Tank)
- Geschäftslogik (Verbrauch, Limits)
- Fluent Interface für alle Aktionen

**Wichtige Methoden:**
- `starteMotor()` / `stoppeMotor()`
- `beschleunige(double)` / `bremsen(double)`
- `tanken(double)` / `fahreLos()`
- `istTankKritisch()` - Prüfung auf kritischen Tankstand

#### Bordcomputer.java
JavaFX-Anwendung mit:
- Canvas-basiertem Tachometer
- Timeline-Animationen für weiche Übergänge
- AnimationTimer für 60-FPS-Updates
- Responsives Layout (BorderPane, GridPane)

**UI-Komponenten:**
- `createTacho()` - Analoger Tacho mit digitaler Anzeige
- `createTank()` - Animierte Tankfüllstandsanzeige
- `createButtons()` - Steuerungspanel
- `drawTacho(double)` - Canvas-Rendering

#### Main.java
Konsolen-Demo mit zwei Szenarien:
1. Normales Fahrverhalten
2. Extremtest (Grenzwerte)

## 📚 Code-Dokumentation

Alle Klassen sind vollständig mit **JavaDoc** (auf Deutsch) dokumentiert:

- ✅ Klassen-Dokumentation mit Zweck und Kontext
- ✅ Methoden-Dokumentation mit `@param`, `@return`, `@throws`
- ✅ Erklärung von Geschäftslogik und Algorithmen
- ✅ Inline-Kommentare für komplexe Berechnungen

### JavaDoc generieren

```bash
mvn javadoc:javadoc
```

Die HTML-Dokumentation wird in `target/site/apidocs/` erstellt.

## 🎨 Styling

Die Anwendung verwendet ein modernes Dark-Theme:

- **Primärfarben**: Dunkelgrau (#34495e), Blautöne
- **Akzentfarben**: Grün (Motor), Rot (Kritisch), Orange (Warnung)
- **Schriftart**: Segoe UI (Windows), Sans-serif (Fallback)
- **Effekte**: Abgerundete Ecken, Innenschatten, Hover-Effekte

CSS-Anpassungen in `src/main/resources/styles.css`

## 🔧 Konfiguration

### Maven-Properties (pom.xml)

```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <javafx.version>21.0.1</javafx.version>
</properties>
```

### Auto-Konfiguration

Im Code anpassbar:

```java
// Bordcomputer.java, Zeile ~61
auto = new Auto("VW Golf", 10.0, 220);
//              Modell      Tank   Max-Speed
```

## 🐛 Bekannte Einschränkungen

- Keine Persistierung des Zustands (Daten gehen beim Schließen verloren)
- Kein Rückwärtsgang implementiert
- Vereinfachte Verbrauchslogik (linear zur Beschleunigung)

## 🚀 Erweiterungsmöglichkeiten

- 💾 Speichern/Laden des Fahrzeugzustands
- 🎵 Sound-Effekte (Motor, Hupe)
- 📊 Statistiken (Durchschnittsverbrauch, Gesamtstrecke)
- 🎮 Tastatursteuerung (Pfeiltasten)
- 🌐 Multi-Fahrzeug-Modus
- 📱 Responsive Mobile-Ansicht

## 📄 Lizenz

Dieses Projekt ist für Bildungszwecke erstellt.

© 2025 Moboudni - Bordcomputer Deluxe

---

## 👤 Autor

**Moboudni**

- GitHub: [@MoBoudni](https://github.com/MoBoudni)

## 🙏 Danksagungen

- JavaFX Community für die hervorragende Dokumentation
- OpenJFX Team für die moderne UI-Bibliothek

---

**Happy Coding! 🚗💨**
