
package Calculators.Software.Crc;

// SYSTEM INCLUDES

import Core.*;
import Core.CalcVar.CalcVarDirections;
import Core.CalcVar.CalcVarNumerical;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

// USER INCLUDES

/**
 * CRC calculator.
 *
 * @author          gbmhunter <gbmhunter@gmail.com> (www.mbedded.ninja)
 * @last-modified   2016-06-15
 * @since           2016-06-15
 */
public class CrcCalcModel extends Calculator {

    //===============================================================================================//
    //========================================= FXML Bindings =======================================//
    //===============================================================================================//

    @FXML
    private Label crc16CcittValue;


    @FXML
    private WebView infoWebView;

    //===============================================================================================//
    //====================================== CALCULATOR VARIABLES ===================================//
    //===============================================================================================//

    public CalcVarNumerical voltage = new CalcVarNumerical();
    public CalcVarNumerical current = new CalcVarNumerical();
    public CalcVarNumerical resistance = new CalcVarNumerical();

    //===============================================================================================//
    //=========================================== CONSTRUCTOR =======================================//
    //===============================================================================================//

    public CrcCalcModel() {

        super("CRC Values",
                "Calculate various CRC values from provided data.",
                new String[]{"Software"},
                new String[]{"crc"});

        super.setIconImagePath(getClass().getResource("grid-icon.png"));


        //===============================================================================================//
        //======================================== LOAD .FXML FILE ======================================//
        //===============================================================================================//

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CrcCalcView.fxml"));
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
        final String htmlFile = "info.html";
        URL url = getClass().getResource(htmlFile);
        engine.load(url.toExternalForm());




        //===============================================================================================//
        //========================================= VOLTAGE (i/o) =======================================//
        //===============================================================================================//

//        this.voltage.setName("voltage");
//        this.voltage.setValueTextField(this.voltageValueTextField);
//        this.voltage.setEquationFunction((IEquationFunction & Serializable)() -> {
//            // Read dependency variables
//            Double current = this.current.getRawVal();
//            Double resistance = this.resistance.getRawVal();
//            return current * resistance;
//        });
//        this.voltage.setUnits(new NumberUnitMultiplier[]{
//                new NumberUnitMultiplier("V", 1e0, NumberPreference.DEFAULT),
//        });
//        this.voltage.setNumDigitsToRound(4);
//        this.voltage.setDirectionFunction(() -> {
//            if (voltageRadioButton.isSelected()) return CalcVarDirections.Output;
//            else return CalcVarDirections.Input;
//        });
//        this.voltage.setDefaultRawValue(null);
//        this.voltage.setHelpText("The voltage across the resistor.");
//        this.voltage.setIsEngineeringNotationEnabled(true);
//
//        //====================== VALIDATORS ===================//
//        this.voltage.addValidator(Validator.IsNumber(CalcValidationLevels.Error));
//        this.voltage.addValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));
//
//        this.calcVars.add(this.voltage);



        //===============================================================================================//
        //============================================== FINAL ==========================================//
        //===============================================================================================//

        this.findDependenciesAndDependants();
        this.refreshDirectionsAndUpdateUI();
        this.recalculateAllOutputs();
        this.validateAllVariables();

    }

}
