package de.verkettungsanfrage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit-Tests für die Auto-Klasse.
 * Diese Testklasse testet alle Funktionalitäten der Auto-Klasse isoliert,
 * einschließlich des Fluent-Patterns (Method Chaining).
 *
 * @author mboud
 * @version 1.0
 * @since 2025
 */
@DisplayName("Auto Unit Tests")
class AutoTest {

    private Auto auto;

    /**
     * Initialisiert ein neues Auto_objekt vor jedem Test.
     * Standard-Konfiguration: VW Golf, 20L Tank, 200 Km/h Max-Geschwindigkeit
     */
    @BeforeEach
    void setUp(){
        auto = new Auto("VW Golf", 20.0, 200);
    }

    //====================== Konstruktor-Tests =====================

    @Nested
    @DisplayName("Konstruktor Tests")
    class KonstruktorTests{

        @Test
        @DisplayName("Sollte Auto mit korrekten Startwerten erstellen")
        void sollteAutoMitKorrektenStartwertenErstellen() {
            // Arrange & Act
            Auto neuesAuto = new Auto("BMW 3er", 15.0, 220);

            // Assert
            assertThat(neuesAuto.getModell()).isEqualTo("BMW 3er");
            assertThat(neuesAuto.getTankstand()).isEqualTo(15.0);
            assertThat(neuesAuto.getMaxGeschwindigkeit()).isEqualTo(220);
            assertThat(neuesAuto.getGeschwindigkeit()).isEqualTo(0.0);
            assertThat(neuesAuto.isMotorLaeuft()).isFalse();
        }

        @Test
        @DisplayName("Sollte Tankstand auf Maaximum begrenzen")
        void sollteTankstandAufMaximumBegrenzen(){
            // Arrange & Act - Versuche mit 100L zu initialisieren
            Auto vollgetanktesAuto = new Auto("Audi A4", 100.0, 250);

            // Assert - Sollte auf maxTankstand (50L) begrenzt sein
            assertThat(vollgetanktesAuto.getTankstand()).isEqualTo(50.0);
        }
        @Test
        @DisplayName("Sollte mit leerem Tank erstellt werden können")
        void sollteMitLeeremTankErstelltWerdenKoennen() {
            // Arrange & Act
            Auto leeresAuto = new Auto("Tesla Model 3", 0.0, 200);

            // Assert
            assertThat(leeresAuto.getTankstand()).isEqualTo(0.0);
            assertThat(leeresAuto.isMotorLaeuft()).isFalse();
        }
    }

    //====================== Motor-Tests =====================

    @Nested
    @DisplayName("Motor Tests")
    class MotorTests {

        @Test
        @DisplayName("Sollte Motor erfolgreich starten mit Treibstoff")
        void sollteMotorErfolgreichStartenMitTreibstoff() {
            // Act
            auto.starteMotor();

            // Assert
            assertThat(auto.isMotorLaeuft()).isTrue();
        }
        @Test
        @DisplayName("Sollte Motor nicht starten bei leerem Tank")
        void sollteMotorNichtStartenBeiLeeremTank() {
            // Arrange
            Auto leeresAuto = new Auto("VW Golf", 0.0, 200);

            // Act
            leeresAuto.starteMotor();

            // Assert
            assertThat(leeresAuto.isMotorLaeuft()).isFalse();
        }

        @Test
        @DisplayName("Sollte Motor stoppen und Geschwindigkeit auf 0 setzen")
        void sollteMotorStoppenUndGeschwindigkeitAufNullSetzen() {
            // Arrange
            auto.starteMotor().fahreLos().beschleunige(50.0);
            double geschwindigkeitVorher = auto.getGeschwindigkeit();

            // Act
            auto.stoppeMotor();

            // Assert
            assertThat(geschwindigkeitVorher).isGreaterThan(0.0);
            assertThat(auto.isMotorLaeuft()).isFalse();
            assertThat(auto.getGeschwindigkeit()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Sollte Method Chaining für Motor unterstützen")
        void sollteMethodChainingFuerMotorUnterstuetzen() {
            // Act
            Auto result = auto.starteMotor();

            // Assert - Sollte dieselbe Instanz zurückgeben
            assertThat(result).isSameAs(auto);
        }
    }

    // ===================== Tank-Tests =====================

    @Nested
    @DisplayName("Tank Tests")
    class TankTests {

        @Test
        @DisplayName("Sollte erfolgreich tanken")
        void sollteErfolgreichTanken() {
            // Arrange
            double tankstandVorher = auto.getTankstand();

            // Act
            auto.tanken(10.0);

            // Assert
            assertThat(auto.getTankstand()).isEqualTo(tankstandVorher + 10.0);
        }

        @Test
        @DisplayName("Sollte Tankstand auf Maximum begrenzen beim Tanken")
        void sollteTankstandAufMaximumBegrenzenBeimTanken() {
            // Act
            auto.tanken(100.0); // Versuche mehr als maxTankstand zu tanken

            // Assert
            assertThat(auto.getTankstand()).isEqualTo(50.0); // maxTankstand
        }

        @Test
        @DisplayName("Sollte kritischen Tankstand erkennen")
        void sollteKritischenTankstandErkennen() {
            // Arrange
            Auto fastLeeresAuto = new Auto("VW Golf", 4.0, 200); // 4L = 8% von 50L

            // Assert
            assertThat(fastLeeresAuto.istTankKritisch()).isTrue();
        }

        @Test
        @DisplayName("Sollte nicht-kritischen Tankstand erkennen")
        void sollteNichtKritischenTankstandErkennen() {
            // Arrange
            Auto vollesAuto = new Auto("VW Golf", 30.0, 200);

            // Assert
            assertThat(vollesAuto.istTankKritisch()).isFalse();
        }

        @Test
        @DisplayName("Sollte Method Chaining für Tanken unterstützen")
        void sollteMethodChainingFuerTankenUnterstuetzen() {
            // Act
            Auto result = auto.tanken(10.0);

            // Assert
            assertThat(result).isSameAs(auto);
        }
    }
        // =====================  Geschwindigkeits-Tests =====================

        @Nested
        @DisplayName("Geschwindigkeits Tests")
        class GeschwindigkeitsTests {

            @Test
            @DisplayName("Sollte losfahren mit Minimalgeschwindigkeit")
            void sollteLosfahrenMitMinimalgeschwindigkeit() {
                // Arrange
                auto.starteMotor();

                // Act
                auto.fahreLos();

                // Assert
                assertThat(auto.getGeschwindigkeit()).isEqualTo(1.0);
            }

            @Test
            @DisplayName("Sollte nicht losfahren ohne laufenden Motor")
            void sollteNichtLosfahrenOhneLaufendenMotor() {
                // Act
                auto.fahreLos();

                // Assert
                assertThat(auto.getGeschwindigkeit()).isEqualTo(0.0);
            }

            @Test
            @DisplayName("Sollte nicht losfahren bei leerem Tank")
            void sollteNichtLosfahrenBeiLeeremTank() {
                // Arrange
                Auto leeresAuto = new Auto("VW Golf", 0.0, 200);
                leeresAuto.starteMotor();

                // Act
                leeresAuto.fahreLos();

                // Assert
                assertThat(leeresAuto.getGeschwindigkeit()).isEqualTo(0.0);
            }

            @Test
            @DisplayName("Sollte beschleunigen wenn Motor läuft")
            void sollteBeschleunigenWennMotorLaeuft() {
                // Arrange
                auto.starteMotor().fahreLos();
                double geschwindigkeitVorher = auto.getGeschwindigkeit();

                // Act
                auto.beschleunige(30.0);

                // Assert
                assertThat(auto.getGeschwindigkeit()).isEqualTo(geschwindigkeitVorher + 30.0);
            }

            @Test
            @DisplayName("Sollte nicht beschleunigen ohne laufenden Motor")
            void sollteNichtBeschleunigenOhneLaufendenMotor() {
                // Act
                auto.beschleunige(50.0);

                // Assert
                assertThat(auto.getGeschwindigkeit()).isEqualTo(0.0);
            }

            @Test
            @DisplayName("Sollte Geschwindigkeit auf Maximum begrenzen")
            void sollteGeschwindigkeitAufMaximumBegrenzen() {
                // Arrange
                auto.starteMotor().fahreLos();

                // Act
                auto.beschleunige(300.0); // Mehr als maxGeschwindigkeit

                // Assert
                assertThat(auto.getGeschwindigkeit()).isEqualTo(200.0); // maxGeschwindigkeit
            }

            @Test
            @DisplayName("Sollte bremsen und Geschwindigkeit reduzieren")
            void sollteBremsenUndGeschwindigkeitReduzieren() {
                // Arrange
                auto.starteMotor().fahreLos().beschleunige(50.0);
                double geschwindigkeitVorher = auto.getGeschwindigkeit();

                // Act
                auto.bremsen(20.0);

                // Assert
                assertThat(auto.getGeschwindigkeit()).isEqualTo(geschwindigkeitVorher - 20.0);
            }

            @Test
            @DisplayName("Sollte Geschwindigkeit nicht unter 0 gehen beim Bremsen")
            void sollteGeschwindigkeitNichtUnterNullGehenBeimBremsen() {
                // Arrange
                auto.starteMotor().fahreLos();

                // Act
                auto.bremsen(100.0); // Mehr bremsen als Geschwindigkeit vorhanden

                // Assert
                assertThat(auto.getGeschwindigkeit()).isEqualTo(0.0);
            }

            @ParameterizedTest
            @ValueSource(doubles = {10.0, 30.0, 50.0, 100.0})
            @DisplayName("Sollte verschiedene Beschleunigungswerte korrekt verarbeiten")
            void sollteVerschiedeneBeschleunigungswerteKorrektVerarbeiten(double delta) {
                // Arrange
                auto.starteMotor().fahreLos();

                // Act
                auto.beschleunige(delta);

                // Assert
                assertThat(auto.getGeschwindigkeit()).isGreaterThan(1.0);
            }
        }

        // =====================  Verbrauchs-Tests =====================

        @Nested
        @DisplayName("Verbrauchs Tests")
        class VerbrauchsTests {

            @Test
            @DisplayName("Sollte Treibstoff beim Beschleunigen verbrauchen")
            void sollteTreibstoffBeimBeschleunigenVerbrauchen() {
                // Arrange
                auto.starteMotor().fahreLos();
                double tankstandVorher = auto.getTankstand();

                // Act
                auto.beschleunige(50.0);

                // Assert
                assertThat(auto.getTankstand()).isLessThan(tankstandVorher);
            }

            @Test
            @DisplayName("Sollte Motor stoppen bei leerem Tank während der Fahrt")
            void sollteMotorStoppenBeiLeeremTankWaehrendDerFahrt() {
                // Arrange - Auto mit sehr wenig Treibstoff
                Auto fastLeeresAuto = new Auto("VW Golf", 0.01, 200);
                fastLeeresAuto.starteMotor().fahreLos();

                // Act - Starke Beschleunigung bis Tank leer
                for (int i = 0; i < 100; i++) {
                    fastLeeresAuto.beschleunige(10.0);
                }

                // Assert
                assertThat(fastLeeresAuto.getTankstand()).isEqualTo(0.0);
                assertThat(fastLeeresAuto.isMotorLaeuft()).isFalse();
            }

            @Test
            @DisplayName("Sollte kein Treibstoff beim Bremsen verbrauchen")
            void sollteKeinTreibstoffBeimBremsenVerbrauchen() {
                // Arrange
                auto.starteMotor().fahreLos().beschleunige(50.0);
                double tankstandVorher = auto.getTankstand();

                // Act
                auto.bremsen(20.0);

                // Assert
                assertThat(auto.getTankstand()).isEqualTo(tankstandVorher);
            }
        }

        // =====================  Method Chaining Tests =====================

        @Nested
        @DisplayName("Method Chaining Tests")
        class MethodChainingTests {

            @Test
            @DisplayName("Sollte komplexe Methodenverkettung unterstützen")
            void sollteKomplexeMethodenverkettungUnterstuetzen() {
                // Act
                Auto result = auto.starteMotor()
                        .fahreLos()
                        .beschleunige(30.0)
                        .beschleunige(20.0)
                        .bremsen(10.0)
                        .tanken(5.0)
                        .hupe()
                        .zeigeZustand();

                // Assert
                assertThat(result).isSameAs(auto);
                assertThat(auto.isMotorLaeuft()).isTrue();
                assertThat(auto.getGeschwindigkeit()).isGreaterThan(0.0);
            }

            @Test
            @DisplayName("Sollte alle Methoden für Fluent Interface zurückgeben")
            void sollteAlleMethodenFuerFluentInterfaceZurueckgeben() {
                // Assert - Alle Methoden sollten Auto zurückgeben
                assertThat(auto.starteMotor()).isInstanceOf(Auto.class);
                assertThat(auto.stoppeMotor()).isInstanceOf(Auto.class);
                assertThat(auto.tanken(10.0)).isInstanceOf(Auto.class);
                assertThat(auto.beschleunige(10.0)).isInstanceOf(Auto.class);
                assertThat(auto.bremsen(10.0)).isInstanceOf(Auto.class);
                assertThat(auto.fahreLos()).isInstanceOf(Auto.class);
                assertThat(auto.hupe()).isInstanceOf(Auto.class);
                assertThat(auto.zeigeZustand()).isInstanceOf(Auto.class);
            }
        }

        // ======== Integrations-Tests (Komplexe Szenarien) =========

        @Nested
        @DisplayName("Integrations Tests")
        class IntegrationsTests {

            @Test
            @DisplayName("Szenario: Normale Fahrt mit Tanken")
            void szenarioNormaleFahrtMitTanken() {
                // Arrange
                Auto testAuto = new Auto("VW Golf", 5.0, 200);

                // Act - Komplettes Fahrszenario
                testAuto.tanken(20.0)
                        .starteMotor()
                        .fahreLos()
                        .beschleunige(50.0)
                        .beschleunige(30.0)
                        .bremsen(20.0);

                // Assert
                assertThat(testAuto.isMotorLaeuft()).isTrue();
                assertThat(testAuto.getGeschwindigkeit()).isEqualTo(61.0); // 1 + 50 + 30 - 20
                assertThat(testAuto.getTankstand()).isLessThan(25.0);
                assertThat(testAuto.getTankstand()).isGreaterThan(0.0);
            }

            @Test
            @DisplayName("Szenario: Vollbremsung aus hoher Geschwindigkeit")
            void szenarioVollbremsungAusHoherGeschwindigkeit() {
                // Arrange & Act
                auto.starteMotor()
                        .fahreLos()
                        .beschleunige(150.0)
                        .bremsen(200.0);

                // Assert
                assertThat(auto.getGeschwindigkeit()).isEqualTo(0.0);
                assertThat(auto.isMotorLaeuft()).isTrue(); // Motor läuft noch
            }

            @Test
            @DisplayName("Szenario: Tank wird während Fahrt leer")
            void szenarioTankWirdWaehrendFahrtLeer() {
                // Arrange
                Auto fastLeeresAuto = new Auto("VW Golf", 0.05, 200);

                // Act - Fahren bis Tank leer
                fastLeeresAuto.starteMotor().fahreLos();
                for (int i = 0; i < 200; i++) {
                    fastLeeresAuto.beschleunige(10.0);
                }

                // Assert
                assertThat(fastLeeresAuto.getTankstand()).isEqualTo(0.0);
                assertThat(fastLeeresAuto.isMotorLaeuft()).isFalse();
                assertThat(fastLeeresAuto.getGeschwindigkeit()).isEqualTo(0.0);
            }
        }

        // =====================  Getter-Tests =====================

        @Nested
        @DisplayName("Getter Tests")
        class GetterTests {

            @Test
            @DisplayName("Sollte Modell korrekt zurückgeben")
            void sollteModellKorrektZurueckgeben() {
                assertThat(auto.getModell()).isEqualTo("VW Golf");
            }

            @Test
            @DisplayName("Sollte maximale Tankkapazität korrekt zurückgeben")
            void sollteMaximaleTankkapazitaetKorrektZurueckgeben() {
                assertThat(auto.getMaxTankstand()).isEqualTo(50.0);
            }

            @Test
            @DisplayName("Sollte maximale Geschwindigkeit korrekt zurückgeben")
            void sollteMaximaleGeschwindigkeitKorrektZurueckgeben() {
                assertThat(auto.getMaxGeschwindigkeit()).isEqualTo(200);
            }
        }
}


