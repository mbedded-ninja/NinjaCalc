import PropTypes from "prop-types"

export class CalcVarInput extends React.Component {
  constructor(props) {
    super(props)
  }

  render() {
    // If the variable is an output, make the input
    // text box readonly so the user cannot change it
    let readonly = false;
    let disabled = false;
    if (this.props.direction === "output") {
      readonly = true;
      disabled = true;
    }
    
    const calcVar = this.props.calc.calcVars[this.props.id]
    console.log(calcVar)
    const validationState = calcVar.validation.state;

    let valueToDisp = null
    if (calcVar.type === 'numeric') {
      valueToDisp = calcVar.dispVal
    } else {
      valueToDisp = calcVar.value
    }

    return (
      <div>
        <input
          name={this.props.id}
          className={validationState}
          value={valueToDisp}
          onChange={this.props.valueChanged}
          style={{ width: this.props.width }}
          readOnly={readonly}
          disabled={disabled}
          style={{ width: this.props.width }}
        ></input>
        <style jsx>{`
          .var-name {
            padding-right: 10px;
          }

          input.warning {
            border: 1px solid orange;
          }

          input.warning:focus {
            outline: 2px solid orange;
          }

          input.error {
            border: 1px solid red;
          }

          input.error:focus {
            outline: 2px solid red;
          }
        `}</style>
      </div>
    );
  }
}

CalcVarInput.defaultProps = {
  width: 150,
};

CalcVarInput.propTypes = {
  id: PropTypes.string.isRequired,
  calc: PropTypes.object.isRequired,
  width: PropTypes.number,
};