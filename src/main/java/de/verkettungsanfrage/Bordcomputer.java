package de.verkettungsanfrage;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * JavaFX-Anwendung zur grafischen Darstellung eines Bordcomputers.
 *
 * Diese Klasse visualisiert die Zustände eines Auto-Objekts in Echtzeit
 * mit folgenden Hauptkomponenten:
 * - Animierter Tachometer mit Nadel und digitaler Geschwindigkeitsanzeige
 * - Tankfüllstandsanzeige mit Farbwechsel bei kritischem Niveau
 * - Motorstatus-Anzeige
 * - Steuerungsbuttons für alle Auto-Funktionen
 *
 * Das Fenster ist in der Größe veränderbar und unterstützt automatische
 * Scrollbars, wenn der Inhalt den sichtbaren Bereich überschreitet.
 *
 * Technische Features:
 * - AnimationTimer für flüssige 60 FPS Updates
 * - Timeline-Animationen für weiche Übergänge
 * - Canvas-basiertes Rendering für den Tachometer
 * - Responsive Design mit GridPane und ScrollPane
 *
 * @author Moboudni
 * @version 2.0
 * @since 2025
 */
public class Bordcomputer extends Application {

    /**
     * Das Auto-Objekt, dessen Zustand visualisiert wird.
     */
    private Auto auto;

    // UI-Elemente - Labels und Anzeigen

    /** Label zur Anzeige des Motorstatus (An/Aus). */
    private Label lblMotorStatus;

    /** Zentrale digitale Geschwindigkeitsanzeige (Ganzzahl-Teil). */
    private Label speedLabelCenter;

    /** Prozentanzeige des Tankstands in der großen Tankanzeige. */
    private Label tankPercentLabel;

    /** Digitale Geschwindigkeitsanzeige (Dezimalstellen-Teil). */
    private Label digitalSpeedFraction;

    /** Region für die animierte Flüssigkeitsdarstellung im Tank. */
    private Region tankLiquid;

    /** Canvas für die Darstellung des analogen Tachometers. */
    private Canvas tachoCanvas;

    /** Animation für den Welleneffekt im Tank. */
    private PauseTransition waveAnimation;

    /** Label zur Anzeige des Hupenstatus. */
    private Label lblHupeStatus;

    /** Mini-Tankanzeige im Zentrum des Tachometers (Füllstand). */
    private Rectangle tankMiniDisplay;

    /** Rahmen der Mini-Tankanzeige im Tachometer. */
    private Rectangle tankMiniFrame;

    // Tacho-Animation Variablen

    /**
     * Aktueller Winkel der Tachometer-Nadel in Grad.
     * Wird für die sanfte Interpolation zwischen Geschwindigkeiten verwendet.
     */
    private double currentAngle = 0;

    /**
     * AnimationTimer für den Hauptanimations-Loop.
     * Aktualisiert die UI kontinuierlich für flüssige Bewegungen.
     */
    private AnimationTimer gameLoop;

    /**
     * Zeitpunkt der letzten Update-Iteration in Nanosekunden.
     * Verwendet zur Begrenzung der Update-Frequenz.
     */
    private long lastUpdate = 0;

    /**
     * Startet die JavaFX-Anwendung und initialisiert das Hauptfenster.
     *
     * Erstellt ein Auto-Objekt mit initialen Werten:
     * - Modell: VW Golf
     * - Tankinhalt: 10 Liter
     * - Maximalgeschwindigkeit: 220 km/h
     *
     * @param stage das primäre Stage-Objekt von JavaFX
     */
    @Override
    public void start(Stage stage) {
        // Initialisierung des Auto-Objekts
        auto = new Auto("VW Golf", 10.0, 220);
        stage.setTitle("Bordcomputer - " + auto.getModell());
        stage.setResizable(true);

        // Hauptlayout erstellen
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f2f5;");
        root.setPadding(new Insets(20));
        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Layoutbereiche zuweisen
        root.setTop(createHeader());
        root.setCenter(createMainPanel());
        root.setBottom(createFooter());

        // ScrollPane für responsive Größenanpassung
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Scene scene = new Scene(scrollPane, 900, 680);
        stage.setScene(scene);
        stage.show();

        startGameLoop();
    }

    /**
     * Startet den Hauptanimations-Loop für flüssige UI-Updates.
     *
     * Der AnimationTimer läuft mit bis zu 60 FPS und aktualisiert:
     * - Tachometer-Nadel mit Smooth-Interpolation
     * - Digitale Geschwindigkeitsanzeige
     * - Mini-Tankanzeige im Tacho
     * - Simuliert sanftes Ausrollen des Fahrzeugs
     *
     * Die Update-Rate ist auf alle 100 Millisekunden begrenzt,
     * um die CPU-Last zu reduzieren.
     */
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Begrenzung auf ~10 Updates pro Sekunde
                if (now - lastUpdate >= 100_000_000) {
                    if (auto.getGeschwindigkeit() > 0) {
                        // Simuliert sanften Geschwindigkeitsabfall beim Ausrollen
                        // Reduzierter Wert (0.2 statt 1.0) für realistisches Verhalten
                        if (auto.isMotorLaeuft() && auto.getGeschwindigkeit() > 10) {
                            auto.bremsen(0.2);
                        }
                    }

                    // UI-Aktualisierung
                    drawTacho(auto.getGeschwindigkeit());
                    updateTankMiniDisplay();

                    lastUpdate = now;
                }
            }
        };
        gameLoop.start();
    }

    /**
     * Erstellt den Kopfbereich der Anwendung.
     *
     * Enthält:
     * - Titel "Bordcomputer" in großer Schrift
     * - Automodell-Bezeichnung
     *
     * @return VBox mit zentriertem Header-Inhalt
     */
    private VBox createHeader() {
        Label title = new Label("Bordcomputer");
        title.setFont(Font.font("System", 36));
        title.setTextFill(Color.web("#2c3e50"));

        Label model = new Label(auto.getModell());
        model.setFont(Font.font("System", 24));
        model.setTextFill(Color.web("#34495e"));

        VBox header = new VBox(10, title, model);
        header.setAlignment(Pos.CENTER);
        return header;
    }

    /**
     * Erstellt das Hauptpanel mit allen Anzeigen und Steuerelementen.
     *
     * Layout-Struktur:
     * - Linke Spalte: Status, Tacho, Tankanzeige
     * - Rechte Spalte: Steuerungsbuttons
     *
     * Das GridPane ist responsive und passt sich der Fenstergröße an.
     *
     * @return GridPane mit dem gesamten Hauptinhalt
     */
    private GridPane createMainPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Spalten-Konfiguration für responsives Layout
        ColumnConstraints ccLeft = new ColumnConstraints();
        ccLeft.setHgrow(Priority.ALWAYS);
        ColumnConstraints ccRight = new ColumnConstraints();
        ccRight.setHgrow(Priority.NEVER);
        grid.getColumnConstraints().addAll(ccLeft, ccRight);

        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().add(rc);

        // Linke Seite mit Anzeigen
        VBox left = new VBox(20,
                createStatusBox(),
                createTacho(),
                createTank()
        );
        left.setAlignment(Pos.TOP_CENTER);
        left.setMaxHeight(Double.MAX_VALUE);

        // Rechte Seite mit Bedienelementen
        VBox right = createButtons();

        grid.add(left, 0, 0);
        grid.add(right, 1, 0);

        updateUI();
        return grid;
    }

    /**
     * Erstellt die Statusanzeige-Box.
     *
     * Zeigt an:
     * - Motorstatus (An/Aus) mit farblicher Kennzeichnung
     * - Hupenstatus (temporär sichtbar beim Hupen)
     *
     * @return VBox mit Statuslabels
     */
    private VBox createStatusBox() {
        lblMotorStatus = new Label("Motor: Aus");
        lblMotorStatus.setFont(Font.font(24));
        lblMotorStatus.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        lblHupeStatus = new Label("");
        lblHupeStatus.setFont(Font.font("Italic", 22));
        lblHupeStatus.setTextFill(Color.web("#e74c3c"));

        return new VBox(10, new Label("Status"), lblMotorStatus, lblHupeStatus);
    }

    /**
     * Erstellt den analogen Tachometer mit digitaler Anzeige.
     *
     * Komponenten:
     * - Canvas mit gezeichnetem Tachometer (Skala, Nadel)
     * - Zentrale digitale Geschwindigkeitsanzeige
     * - Dezimalstellen-Anzeige für präzise Geschwindigkeit
     * - Mini-Tankanzeige im Zentrum
     * - km/h Einheit
     *
     * Der Tachometer wird kontinuierlich durch den AnimationTimer aktualisiert.
     *
     * @return StackPane mit allen Tacho-Komponenten
     */
    private StackPane createTacho() {
        StackPane pane = new StackPane();
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Canvas für den analogen Tacho
        tachoCanvas = new Canvas(340, 340);
        StackPane canvasWrapper = new StackPane(tachoCanvas);
        canvasWrapper.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Digitale Geschwindigkeitsanzeige
        speedLabelCenter = new Label("0");
        speedLabelCenter.setFont(Font.font("System", 70));
        speedLabelCenter.setTextFill(Color.WHITE);

        // Dezimalstellen der Geschwindigkeit
        digitalSpeedFraction = new Label(".0");
        digitalSpeedFraction.setFont(Font.font("System", 30));
        digitalSpeedFraction.setTextFill(Color.WHITE);
        digitalSpeedFraction.setTranslateX(70);

        // Geschwindigkeitseinheit
        Label unit = new Label("km/h");
        unit.setFont(Font.font(28));
        unit.setTextFill(Color.LIGHTGRAY);

        // Mini-Tankanzeige im Tacho-Zentrum
        tankMiniFrame = new Rectangle(50, 10, Color.web("#444444"));
        tankMiniDisplay = new Rectangle(46, 6, Color.web("#27ae60"));
        tankMiniDisplay.setTranslateY(-10);
        tankMiniFrame.setTranslateY(-10);
        tankMiniDisplay.setArcWidth(5);
        tankMiniDisplay.setArcHeight(5);
        tankMiniFrame.setArcWidth(5);
        tankMiniFrame.setArcHeight(5);

        StackPane tankStack = new StackPane(tankMiniFrame, tankMiniDisplay);
        tankStack.setTranslateY(85);

        VBox center = new VBox(-10, speedLabelCenter, unit);
        center.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(canvasWrapper, center, digitalSpeedFraction, tankStack);
        drawTacho(0);
        updateTankMiniDisplay();
        return pane;
    }

    /**
     * Zeichnet den Tachometer auf dem Canvas.
     *
     * Visualisierung umfasst:
     * - Schwarzen Hintergrund-Kreis
     * - Farbige Geschwindigkeitsskala (Grün/Gelb/Rot basierend auf Geschwindigkeit)
     * - Teilstriche alle 10 km/h (große Striche alle 40 km/h)
     * - Beschriftung der Hauptwerte
     * - Animierte rote Nadel mit Smooth-Interpolation
     * - Zentraler Nadelaufhängungspunkt
     * - Zufallsrauschen für Realismus bei Bewegung
     *
     * Die Nadelbewegung wird geglättet durch lineare Interpolation,
     * um ruckartige Bewegungen zu vermeiden.
     *
     * @param speed die aktuelle Geschwindigkeit des Fahrzeugs in km/h
     */
    private void drawTacho(double speed) {
        GraphicsContext g = tachoCanvas.getGraphicsContext2D();
        g.clearRect(0, 0, 340, 340);

        double cx = 170, cy = 170, r = 140;
        int maxSpeed = auto.getMaxGeschwindigkeit();
        double maxScaleValue = 240.0;

        // Dunkler Hintergrund-Kreis
        g.setFill(Color.web("#1a1a1a"));
        g.fillOval(20, 20, 300, 300);

        double actualSpeed = (double) auto.getGeschwindigkeit();
        double visualSpeed = actualSpeed;

        // Sensor-Rauschen nur bei Bewegung hinzufügen (verhindert Flackern bei 0 km/h)
        if (actualSpeed > 0) {
            visualSpeed += Math.random();
        }

        visualSpeed = Math.min(visualSpeed, maxSpeed);

        // Geschwindigkeitsskala mit Farbcodierung
        for (int i = 0; i <= maxSpeed; i += 10) {
            double angle = Math.toRadians(120 + (i / (double) maxSpeed) * maxScaleValue);
            double x1 = cx + r * Math.cos(angle);
            double y1 = cy + r * Math.sin(angle);

            double lineLength = i % 40 == 0 ? 30 : 20;
            double x2 = cx + (r - lineLength) * Math.cos(angle);
            double y2 = cy + (r - lineLength) * Math.sin(angle);

            // Farbcodierung: Grün (sicher) -> Gelb (mittel) -> Rot (gefährlich)
            Color color;
            if (i <= maxSpeed * 0.55) {
                color = Color.LIME;
            } else if (i <= maxSpeed * 0.75) {
                color = Color.YELLOW;
            } else {
                color = Color.RED;
            }

            g.setStroke(color);
            g.setLineWidth(i % 40 == 0 ? 4 : 2);
            g.strokeLine(x1, y1, x2, y2);
        }

        // Zahlenbeschriftung
        g.setFill(Color.WHITE);
        g.setFont(Font.font(18));
        for (int i = 0; i <= maxSpeed; i += 40) {
            double angle = Math.toRadians(120 + (i / (double) maxSpeed) * maxScaleValue);
            double x = cx + (r - 55) * Math.cos(angle) - (i >= 100 ? 15 : 8);
            double y = cy + (r - 55) * Math.sin(angle) + 8;
            g.fillText(String.valueOf(i), x, y);
        }

        // Smooth-Nadel mit linearer Interpolation (20% pro Frame)
        double target = 120 + (visualSpeed / (double) maxSpeed) * maxScaleValue;
        currentAngle += (target - currentAngle) * 0.2;

        double rad = Math.toRadians(currentAngle);
        g.setStroke(Color.RED);
        g.setLineWidth(6);
        g.strokeLine(cx, cy, cx + 115 * Math.cos(rad), cy + 115 * Math.sin(rad));

        // Nadelaufhängung in der Mitte
        g.setFill(Color.web("#1a1a1a"));
        g.fillOval(cx - 15, cy - 15, 30, 30);
        g.setFill(Color.RED);
        g.fillOval(cx - 10, cy - 10, 20, 20);

        // Aktualisierung der digitalen Anzeige
        speedLabelCenter.setText(String.valueOf((int) visualSpeed));
        digitalSpeedFraction.setText(String.format(".%d", (int) ((visualSpeed - (int) visualSpeed) * 10)));
        digitalSpeedFraction.setTranslateY(speedLabelCenter.getLayoutY() + 10);
    }

    /**
     * Aktualisiert die Mini-Tankanzeige im Tachometer-Zentrum.
     *
     * Die Anzeige:
     * - Passt die Breite proportional zum Tankstand an
     * - Wechselt die Farbe bei kritischem Tankstand (Rot)
     * - Verwendet eine Timeline-Animation für weiche Übergänge
     * - Zentriert die Anzeige horizontal
     */
    private void updateTankMiniDisplay() {
        double percent = auto.getTankstand() / auto.getMaxTankstand();
        double width = 46 * percent;

        // Animierte Breitenänderung über 300ms
        new Timeline(new KeyFrame(Duration.millis(300),
                new KeyValue(tankMiniDisplay.widthProperty(), width)))
                .play();

        // Farbwechsel bei kritischem Tankstand
        String color = auto.istTankKritisch() ? "#e74c3c" : "#27ae60";
        tankMiniDisplay.setFill(Color.web(color));

        // Horizontale Zentrierung der Anzeige
        tankMiniDisplay.setTranslateX(width/2 - 23);
    }

    /**
     * Erstellt die große Tankfüllstandsanzeige.
     *
     * Features:
     * - Animierte Füllstandsanzeige mit Flüssigkeitsdarstellung
     * - Farbwechsel basierend auf Füllstand (Grün/Orange/Rot)
     * - Prozentanzeige in der Mitte
     * - Abgerundete Ecken für modernen Look
     * - Wellenanimation (simuliert)
     * - Clipping für saubere Darstellung innerhalb der Grenzen
     *
     * @return VBox mit der kompletten Tankanzeige
     */
    private VBox createTank() {
        StackPane tank = new StackPane();
        tank.setPrefSize(300, 160);
        tank.setStyle("-fx-background-color: #2c3e50; -fx-background-radius: 25;");
        tank.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Animierte Tankflüssigkeit
        tankLiquid = new Region();
        tankLiquid.setStyle("-fx-background-color: linear-gradient(to top, #3498db, #5dade2); -fx-background-radius: 23;");
        tankLiquid.setPrefHeight(0);

        // Wellenanimation initialisieren
        waveAnimation = new PauseTransition(Duration.millis(80));
        waveAnimation.setOnFinished(e -> waveAnimation.play());

        StackPane liquid = new StackPane(tankLiquid);
        liquid.setAlignment(Pos.BOTTOM_CENTER);

        // Clipping für abgerundete Ecken
        Rectangle clipRect = new Rectangle(300, 160);
        clipRect.setArcWidth(50);
        clipRect.setArcHeight(50);
        liquid.setClip(clipRect);

        // Prozentanzeige
        tankPercentLabel = new Label("40%");
        tankPercentLabel.setFont(Font.font("System", 40));
        tankPercentLabel.setTextFill(Color.WHITE);

        tank.getChildren().addAll(liquid, tankPercentLabel);
        VBox box = new VBox(15, new Label("Tankstand"), tank);
        box.setAlignment(Pos.CENTER);

        VBox.setVgrow(tank, Priority.NEVER);
        VBox.setVgrow(box, Priority.NEVER);

        updateTank();
        waveAnimation.play();
        return box;
    }

    /**
     * Aktualisiert die Füllhöhe und Farbe der großen Tankanzeige.
     *
     * Farbcodierung:
     * - Unter 10%: Dunkelrot (Leer)
     * - Unter 25%: Rot (Kritisch)
     * - Unter 50%: Orange (Mittel)
     * - Über 50%: Grün (Voll)
     *
     * Die Füllhöhe wird über eine Timeline-Animation mit Ease-Out-Interpolation
     * animiert, um einen natürlichen Tankvorgang zu simulieren.
     */
    private void updateTank() {
        double percent = auto.getTankstand() / auto.getMaxTankstand();
        double height = 150 * percent;

        // Animierte Höhenänderung über 800ms mit Ease-Out
        new Timeline(new KeyFrame(Duration.millis(800),
                new KeyValue(tankLiquid.prefHeightProperty(), height, Interpolator.EASE_OUT)))
                .play();

        // Dynamische Farbwahl basierend auf Füllstand
        String colorHex;
        if (percent < 0.1) {
            colorHex = "#c0392b"; // Dunkelrot (Leer)
        } else if (percent < 0.25) {
            colorHex = "#e74c3c"; // Rot (Kritisch)
        } else if (percent < 0.5) {
            colorHex = "#f39c12"; // Orange (Mittel)
        } else {
            colorHex = "#27ae60"; // Grün (Voll)
        }

        tankLiquid.setStyle("-fx-background-color: linear-gradient(to top, " + colorHex + ", " + colorHex + "dd); -fx-background-radius: 23;");
        tankPercentLabel.setText(String.format("%.0f%%", percent * 100));
    }

    /**
     * Erstellt das moderne Steuerungspanel mit logisch gruppierten Buttons.
     *
     * Button-Gruppen:
     * 1. Motor und Zündung
     *    - Motor starten/stoppen
     * 2. Fahren
     *    - Losfahren
     *    - Beschleunigen (+30 km/h)
     *    - Bremsen (-20 km/h)
     * 3. Zusatzfunktionen
     *    - Hupen
     *    - Tanken (+10 Liter)
     *
     * Design:
     * - Dunkles Panel mit abgerundeten Ecken
     * - Innenschatten für Tiefeneffekt
     * - Hover-Effekte auf allen Buttons
     * - Farbcodierung nach Funktion
     *
     * @return VBox mit dem kompletten Steuerungspanel
     */
    private VBox createButtons() {
        VBox box = new VBox(25);
        box.setPadding(new Insets(30));
        box.setMaxWidth(300);
        box.setStyle("-fx-background-color: #34495e; " +
                "-fx-background-radius: 25; " +
                "-fx-effect: innershadow(gaussian, #00000088, 15, 0, 0, 0);");

        // Gruppe 1: Motor/Zündung
        VBox motorGroup = createButtonGroup("Motor & Zündung");
        Button btnMotor = createMotorButton();
        motorGroup.getChildren().add(btnMotor);

        // Gruppe 2: Fahren
        VBox driveGroup = createButtonGroup("Fahren");
        Button btnLosfahren = btn("#2980b9", "Losfahren", () -> {
            auto.fahreLos();
            updateUI();
        });
        Button btnBeschleunigen = btn("#3498db", "Beschleunigen +30", () -> {
            auto.beschleunige(30.0);
            updateUI();
        });
        Button btnBremsen = btn("#e67e22", "Bremsen -20", () -> {
            auto.bremsen(20.0);
            updateUI();
        });
        driveGroup.getChildren().addAll(btnLosfahren, btnBeschleunigen, btnBremsen);

        // Gruppe 3: Zusatzfunktionen
        VBox utilityGroup = createButtonGroup("Zusatzfunktionen");
        Button btnHupen = btn("#9b59b6", "Hupen", () -> {
            auto.hupe();
            hupe();
        });
        Button btnTanken = btn("#1abc9c", "Tanken +10L", () -> {
            double vorher = auto.getTankstand();
            auto.tanken(10);

            // Benachrichtigung bei vollem Tank
            if (auto.getTankstand() == auto.getMaxTankstand() && vorher < auto.getMaxTankstand()) {
                lblHupeStatus.setText("Tank voll!");
                lblHupeStatus.setTextFill(Color.web("#f39c12"));
                PauseTransition pt = new PauseTransition(Duration.seconds(2));
                pt.setOnFinished(e -> lblHupeStatus.setText(""));
                pt.play();
            }
            updateUI();
        });
        utilityGroup.getChildren().addAll(btnHupen, btnTanken);

        box.getChildren().addAll(motorGroup, driveGroup, utilityGroup);
        return box;
    }

    /**
     * Erstellt einen Titel-Container für die logische Button-Gruppierung.
     *
     * Dient der besseren visuellen Strukturierung des Steuerungspanels.
     *
     * @param title der Titel der Button-Gruppe
     * @return VBox mit Titel-Label und Platz für Buttons
     */
    private VBox createButtonGroup(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", 18));
        titleLabel.setTextFill(Color.web("#ecf0f1"));
        titleLabel.setPadding(new Insets(0, 0, 5, 0));
        VBox group = new VBox(10);
        group.getChildren().add(titleLabel);
        return group;
    }

    /**
     * Erstellt den speziellen Motor-Button mit dynamischer Text- und Farbanpassung.
     *
     * Der Button ändert sein Aussehen basierend auf dem Motorstatus:
     * - Motor Aus: Grüner Button "Motor starten"
     * - Motor An: Roter Button "Motor stoppen"
     *
     * @return Button mit Motor-Steuerungsfunktion
     */
    private Button createMotorButton() {
        Button btnMotor = btn(auto.isMotorLaeuft() ? "#c0392b" : "#27ae60",
                auto.isMotorLaeuft() ? "Motor stoppen" : "Motor starten", null);
        btnMotor.setOnAction(e -> {
            if (auto.isMotorLaeuft()) {
                auto.stoppeMotor();
                btnMotor.setText("Motor starten");
                btnMotor.setStyle(getButtonStyle("#27ae60")); // Grün
            } else {
                auto.starteMotor();
                btnMotor.setText("Motor stoppen");
                btnMotor.setStyle(getButtonStyle("#c0392b")); // Rot
            }
            updateUI();
        });
        return btnMotor;
    }
    /**
     * Hilfsmethode zur Erzeugung von stylischen Buttons mit Hover-Effekten.
     *
     * Button-Features:
     * - Abgerundete Ecken (15px Radius)
     * - Fette, weiße Schrift
     * - Hand-Cursor bei Hover
     * - Transparenz-Effekt beim Hover (Alpha-Kanal auf CC)
     * - Einheitliche Größe (240x45 px)
     *
     * @param color die Hintergrundfarbe des Buttons (Hex-Code)
     * @param text der anzuzeigende Button-Text
     * @param action die auszuführende Aktion beim Klick (kann null sein)
     * @return ein konfigurierter Button mit allen Styling-Eigenschaften
     */
    private Button btn(String color, String text, Runnable action) {
        Button b = new Button(text);
        b.setPrefWidth(240);
        b.setPrefHeight(45);

        String baseStyle = getButtonStyle(color);
        String hoverStyle = "-fx-background-color: " + color + "cc; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-radius: 15; -fx-cursor: hand;";

        b.setStyle(baseStyle);

        // Hover-Effekt für bessere Benutzererfahrung
        b.setOnMouseEntered(e -> b.setStyle(hoverStyle));
        b.setOnMouseExited(e -> b.setStyle(baseStyle));

        if (action != null) b.setOnAction(e -> action.run());
        return b;
    }

    /**
     * Gibt den Basis-CSS-Stil für einen Button zurück.
     *
     * Verwendet für:
     * - Normale Button-Darstellung
     * - Wiederherstellung nach Hover-Effekt
     *
     * @param color die Hintergrundfarbe als Hex-Code
     * @return CSS-Stil-String für den Button
     */
    private String getButtonStyle(String color) {
        return "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16; -fx-font-weight: bold; -fx-background-radius: 15; -fx-cursor: hand;";
    }

    /**
     * Zeigt eine visuelle Hupen-Benachrichtigung an.
     *
     * Die Benachrichtigung:
     * - Zeigt "HUUUUUP!" im Hupenstatus-Label an
     * - Verschwindet automatisch nach 1,2 Sekunden
     * - Wird durch eine PauseTransition gesteuert
     */
    private void hupe() {
        lblHupeStatus.setText("HUUUUUP!");
        PauseTransition pt = new PauseTransition(Duration.seconds(1.2));
        pt.setOnFinished(e -> lblHupeStatus.setText(""));
        pt.play();
    }

    /**
     * Erstellt die Fußzeile der Anwendung.
     *
     * Enthält Copyright-Informationen in grauer Schrift,
     * zentriert am unteren Rand des Fensters.
     *
     * @return HBox mit dem Footer-Inhalt
     */
    private HBox createFooter() {
        Label f = new Label("© 2025 Moboudni – Bordcomputer Deluxe");
        f.setTextFill(Color.GRAY);
        HBox h = new HBox(f);
        h.setAlignment(Pos.CENTER);
        h.setPadding(new Insets(20, 0, 10, 0));
        return h;
    }

    /**
     * Zentrale Methode zur Aktualisierung aller UI-Elemente.
     *
     * Aktualisiert:
     * - Motorstatus-Label mit Text und Farbe
     * - Tankfüllstandsanzeige mit Animation
     *
     * Sollte nach jeder Zustandsänderung des Auto-Objekts aufgerufen werden,
     * um die Konsistenz zwischen Modell und Ansicht zu gewährleisten.
     */
    private void updateUI() {
        boolean an = auto.isMotorLaeuft();
        lblMotorStatus.setText("Motor: " + (an ? "An" : "Aus"));
        lblMotorStatus.setStyle("-fx-text-fill: " + (an ? "#27ae60" : "#e74c3c") + "; -fx-font-weight: bold;");

        updateTank();
    }

    /**
     * Haupteinstiegspunkt der JavaFX-Anwendung.
     *
     * Startet die Bordcomputer-Anwendung durch Aufruf der launch-Methode,
     * die wiederum die start-Methode aufruft.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        launch(args);
    }
}