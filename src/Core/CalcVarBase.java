package Core;


import java.util.*;

/**
 * The base class that all calculator variables inherit from.
 *
 * @author gbmhunter
 * @since 2015-11-02
 */
public abstract class CalcVarBase {

    /**
     * The name of the calculator variable. Used when debugging.
     */
    public String name;

    /**
     * Keeps track of all registered listeners to the raw value being read.
     */
    private List<ICalcVarBaseCallback> rawValueReadListeners = new ArrayList<ICalcVarBaseCallback>();

    /**
     * Use this to subscribe to the calculator's raw value changing.
     * @param toAdd     The listener to add.
     */
    public void addRawValueReadListener(ICalcVarBaseCallback toAdd) {
        rawValueReadListeners.add(toAdd);
    }

    protected void OnRawValueRead() {
        // Notify everybody that may be interested.
        for (ICalcVarBaseCallback listener : rawValueReadListeners)
            listener.execute(this);
    }


    private List<ICalcVarBaseCallback> rawValueChangedListeners = new ArrayList<ICalcVarBaseCallback>();

    public void addRawValueChangedListener(ICalcVarBaseCallback toAdd) {
        rawValueChangedListeners.add(toAdd);
    }

    protected void OnRawValueChanged() {
        // Notify everybody that may be interested.
        for (ICalcVarBaseCallback hl : rawValueChangedListeners)
            hl.execute(this);
    }


    /// <summary>
    /// Designed to be assigned to when Calculator.CalculateDependencies() is run. This is not calculated in this class's constructor,
    /// but rather once all calculator variables and their equations have been added to the calculator.
    /// </summary>
    public ArrayList<CalcVarBase> Dependencies;

    /// <summary>
    /// Designed to be assigned to when Calculator.CalculateDependencies() is run. This is not calculated in this class's constructor,
    /// but rather once all calculator variables and their equations have been added to the calculator.
    /// </summary>
    public ArrayList<CalcVarBase> Dependants;

    /// <summary>
    /// Set to true to disable the updating of the text box when this CalcVar's Calculate() method
    /// is called.
    /// </summary>
    public Boolean DisableUpdate;

    /// <summary>
    /// Gets and sets the equation function which is used to calculate the value
    /// of this calculator variable when it is an output.
    /// </summary>
    public IEquationFunction equationFunction;

    public CalcVarDirections direction;

    public IDirectionFunction directionFunction;



    public void Calculate() {
        // Default implementation is to just return
        // (and do nothing)
        System.out.println("WARNING: BaseCalcVar.Calculate() called, this is an empty function.");
        return;
    }


    /**
     * Constructor for CalcVarBase.
     * @param name                  The name of the calculator variable.
     * @param equationFunction      A function which calculates and returns the value that this calculator variable should be.
     * @param directionFunction     A function which determines the direction (input or output) of this calculator variable.
     */
    public CalcVarBase(String name, IEquationFunction equationFunction, IDirectionFunction directionFunction) {

        // Save the name
        this.name = name;

        // Initialise empty lists to keep track of this calculators dependencies
        // and dependants
        this.Dependencies = new ArrayList<CalcVarBase>();
        this.Dependants = new ArrayList<CalcVarBase>();

        // Save equation function
        this.equationFunction = equationFunction;

        // Save direction function
        this.directionFunction = directionFunction;
    }


    public void ForceDependantOutputsToRecalculate() {
        System.out.println("ForceDependantOutputsToRecalculate() called.");
        // We need to re-calculate any this calculator variables dependants, if they are outputs
        for (int i = 0; i < this.Dependants.size(); i++) {
            if (this.Dependants.get(i).direction == CalcVarDirections.Output) {
                System.out.println("Calling Calculate() on variable \"" + this.Dependants.get(i).name + "\".");
                this.Dependants.get(i).Calculate();
            }
        }
    }

    public abstract void updateUIFromDirection();

}
