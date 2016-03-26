package Core;

import javafx.scene.control.*;

/**
 * A specialisation of a generic CalcVar which is for variables which are always
 * an output. Removes the ability to add a input/output radio button, or a default value.
 * @author gbmhunter
 * @since 2015-11-02
 */
public class CalcVarNumericalOutput extends CalcVarNumerical {

    /**
     *
     * @param name              Debug name for variable
     * @param calcValTextBox    The textbox used for the variables value
     * @param unitsComboBox     The combobox used to select the variables units. Can be null.
     * @param equation
     * @param units
     * @param numDigitsToRound
     * @param helpText
     */
    public CalcVarNumericalOutput(
        String name,
        TextField calcValTextBox,
        ComboBox unitsComboBox,
        IEquationFunction equation,
        NumberUnit[] units,
        int numDigitsToRound,
        String helpText) {

        // The CalcVarNumerical constructor
        super(
            name,
            calcValTextBox,
            unitsComboBox,
            equation,
            units,
            numDigitsToRound,
            // This is always going to be an output!
            () -> CalcVarDirections.Output,
            0.0,
            helpText);

        if (equation == null) {
            throw new IllegalArgumentException("The equation provided to a CalVarNumericalOutput cannot be null.");
        }


    }

}