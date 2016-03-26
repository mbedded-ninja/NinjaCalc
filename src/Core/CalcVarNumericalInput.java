package Core;

import javafx.scene.control.*;

/**
 * A specialisation of a generic CalcVar which is for variables which are always
 * an input. Removes the ability to add a input/output radio button and provide
 * an equation.
 * @author gbmhunter
 * @since 2015-11-02
 */
public class CalcVarNumericalInput extends CalcVarNumerical {

    public CalcVarNumericalInput(
        String name,
        TextField calcValTextBox,
        ComboBox unitsComboBox,
        NumberUnit[] units,
        int numDigitsToRound,
        Double defaultRawValue,
        String helpText) {

        super(
            name,
            calcValTextBox,
            unitsComboBox,
            //null,
            //null,
            null,
            units,
            numDigitsToRound,
            () -> CalcVarDirections.Input,
            defaultRawValue,
            helpText);

    }

}