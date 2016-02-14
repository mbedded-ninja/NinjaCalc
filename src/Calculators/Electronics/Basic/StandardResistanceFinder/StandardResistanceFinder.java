
package Calculators.Electronics.Basic.StandardResistanceFinder;

// SYSTEM INCLUDES

import Core.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;

// USER INCLUDES

/**
 * Calculator for finding a E-series resistance (standard resistance) which is closest to the user's
 * desired resistance.
 *
 * @author gbmhunter
 * @since 2013-09-17
 * @last-modified 2016-02-14
 */
public class StandardResistanceFinder extends Calculator {

    //===============================================================================================//
    //========================================= FXML Bindings =======================================//
    //===============================================================================================//

    @FXML private WebView infoWebView;

    @FXML private TextField desiredResistanceValue;
    @FXML private ComboBox desiredResistanceUnits;

    @FXML private ComboBox eSeriesComboBox;

    @FXML private TextField actualResistanceValue;
    @FXML private ComboBox actualResistanceUnits;

    @FXML private TextField textFieldResistanceValue;

    //===============================================================================================//
    //====================================== CALCULATOR VARIABLES ===================================//
    //===============================================================================================//

    public CalcVarNumericalInput desiredResistance;
    public CalcVarComboBox eSeries;
    public CalcVarNumericalOutput actualResistance;

    //===============================================================================================//
    //=========================================== CONSTRUCTOR =======================================//
    //===============================================================================================//

    public StandardResistanceFinder() {

        super( "Standard Resistance Finder",
                ".",
                new String[]{ "Electronics", "Basic" },
                new String[]{"ohm, resistor, resistance, voltage, current, law, vir"});

        super.setIconImagePath(getClass().getResource("grid-icon.png"));

        //===============================================================================================//
        //======================================== LOAD .FXML FILE ======================================//
        //===============================================================================================//

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StandardResistanceFinder.fxml"));
        //fxmlLoader.setRoot(this.view);
        fxmlLoader.setController(this);
        try {
            // Create a UI node from the FXML file, and save it to the view variable.
            // This will be used by the main window to create a new instance of this calculator when
            // the "Open" button is clicked.
            this.view = fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        //===============================================================================================//
        //================================ LOAD WEB VIEW FOR INFO SECTION ===============================//
        //===============================================================================================//

        WebEngine engine = this.infoWebView.getEngine();
        final String htmlFile= "info.html";
        URL url = getClass().getResource(htmlFile);
        engine.load(url.toExternalForm());

        //===============================================================================================//
        //================================== DESIRED RESISTANCE (input) =================================//
        //===============================================================================================//

        this.desiredResistance = new CalcVarNumericalInput(
                "desiredResistance",             // Debug name
                this.desiredResistanceValue,     // Textbox for value (UI object)
                this.desiredResistanceUnits,     // Combobox for units (UI object)
                new NumberUnit[]{
                        new NumberUnit("mΩ", 1e-3),
                        new NumberUnit("Ω", 1e0, NumberPreference.DEFAULT),
                        new NumberUnit("kΩ", 1e3),
                        new NumberUnit("MΩ", 1e6),
                        new NumberUnit("GΩ", 1e9),
                },
                4,                          // Num. digits to round to
                null,                       // Default value
                "The current you want the PCB track to be able to handle." // Help info
        );

        //========== VALIDATORS ===========//
        this.desiredResistance.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.desiredResistance.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));
        this.desiredResistance.addValidator(
                new Validator(() -> {
                    return ((this.desiredResistance.getRawVal() < 274e-3) ? CalcValidationLevels.Warning : CalcValidationLevels.Ok);
                },
                        "Current is below the minimum value (274mA) extracted from the universal graph in IPC-2152." +
                                " Results might not be as accurate (extrapolation will occur)."));
        this.desiredResistance.addValidator(
                new Validator(() -> {
                    return ((this.desiredResistance.getRawVal() > 26.0) ? CalcValidationLevels.Warning : CalcValidationLevels.Ok);
                },
                        "Current is above the maximum value (26A) extracted from the universal graph in IPC-2152." +
                                " Results might not be as accurate (extrapolation will occur)."));

        this.calcVars.add(this.desiredResistance);

        //===============================================================================================//
        //========================================= IS PLANE PRESENT ====================================//
        //===============================================================================================//

        this.eSeries = new CalcVarComboBox(
                "eSeries",                          // Debug name
                eSeriesComboBox,                    // Combobox to attach to (UI element)
                new String[] {                      // Options for Combobox
                        "E6",
                        "E12",
                },
                () -> CalcVarDirections.Input,      // Always an input
                "The E-series you wish to select a resistance from.");  // Tooltip text

        this.calcVars.add(this.eSeries);

        //===============================================================================================//
        //===================================== ACTUAL RESISTANCE (output) ==============================//
        //===============================================================================================//

        this.actualResistance = new CalcVarNumericalOutput(
                "actualResistance",
                this.actualResistanceValue,
                this.actualResistanceUnits,
                () -> {

                    // Read in variables
                    //Double trackCurrent = this.TrackCurrent.getRawVal();
                    //Double tempRise = this.TempRise.getRawVal();

                    //return unadjustedTrackCrosssectionalAreaM2;
                    return 0.0;

                },
                new NumberUnit[]{
                        new NumberUnit("mΩ", 1e-3),
                        new NumberUnit("Ω", 1e0, NumberPreference.DEFAULT),
                        new NumberUnit("kΩ", 1e3),
                        new NumberUnit("MΩ", 1e6),
                        new NumberUnit("GΩ", 1e9),
                },
                4,
                "The closest resistance to your desired resistance that belongs to an E-series (which normally means you can by a resistor with this exact resistance).");

        // Add validators
        this.actualResistance.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
        this.actualResistance.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));

        this.calcVars.add(this.actualResistance);

        //===============================================================================================//
        //============================================== FINAL ==========================================//
        //===============================================================================================//

        this.findDependenciesAndDependants();
        this.refreshDirectionsAndUpdateUI();
        this.recalculateAllOutputs();
        this.validateAllVariables();

    }

}

