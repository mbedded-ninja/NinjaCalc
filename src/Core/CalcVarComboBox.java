package Core;


import javafx.scene.control.*;

public class CalcVarComboBox extends CalcVarBase {

    protected String rawVal;
    /// <summary>
    /// Holds the "raw" (unscaled, unrounded) value for this variable.
    /// </summary>
    public String getRawVal() {

        this.OnRawValueRead();
        return this.rawVal;
    }

    public void setRawVal(String value) {

        // Only change if different
        if (this.rawVal != value) {
            this.rawVal = value;
            this.OnRawValueChanged();
        }
    }


    ComboBox CalculatorComboBox;

    String[] ComboBoxOptions;

    String HelpText;

    public CalcVarComboBox(
        String name,
        ComboBox comboBox,
        String[] comboBoxOptions,
        String helpText) {

        super(
                name,
                // Equation function is not used, should
                // we be passing null here instead???
                () -> 0.0
        );

        this.CalculatorComboBox = comboBox;
        this.ComboBoxOptions = comboBoxOptions;

        // Populate combobox
        //this.CalculatorComboBox.ItemsSource = this.ComboBoxOptions;

        // Set-up event handler for combo-box
        //this.CalculatorComboBox.SelectionChanged += this.ComboBoxChanged;

        // Select default (make sure this is done after event handler is installed!)
        //this.CalculatorComboBox.SelectedItem = this.ComboBoxOptions[0];

        // Save the help text
        this.HelpText = helpText;

        // We need to use a TextBlock so we can do advanced formatting
        //var toolTip = new System.Windows.Controls.TextBlock();

        // Tooltip content is help info plus validation results
        //toolTip.Inlines.Add(this.HelpText);

        // Setting a max width prevents the tooltip from getting rediculuosly large when there is a long help info string.
        // Keeping this quite small also makes the tooltip easier to read.
        //toolTip.MaxWidth = 300;
        // Important to allow wrapping as we are restricting the max. width!
        //toolTip.TextWrapping = System.Windows.TextWrapping.Wrap;

        //this.CalculatorComboBox.ToolTip = toolTip;

    }

    /*
    void ComboBoxChanged(object sender, EventArgs e) {

        ComboBox calculatorComboBox = (ComboBox)sender;
        Console.WriteLine("ComboBoxChanged() called. Selected item is now = \"" + (string)calculatorComboBox.SelectedItem + "\".");

        // Set the raw value, this will raise the RawValueChanged event.
        this.RawVal = (string)calculatorComboBox.SelectedItem;

        // We need to notify all dependants
        // Should these be listening to the RawValueChanged event instead???
        this.ForceDependantOutputsToRecalculate();
    }*/

}
