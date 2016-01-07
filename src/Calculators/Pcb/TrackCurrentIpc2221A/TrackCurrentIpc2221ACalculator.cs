﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;

using NinjaCalc.Core;
using NinjaCalc.Calculators.Pcb.TrackCurrentIpc2221A;

namespace NinjaCalc.Calculators.Pcb.TrackCurrentIpc2221A {
    class TrackCurrentIpc2221ACalculator : Calculator {

        public TrackCurrentIpc2221ACalculator()
            : base(
            "Track Current (IPC-2221A)",
            "PCB track current carrying capability calculator, using IPC-2221A.",
            "pack://application:,,,/Calculators/Basic/OhmsLaw/icon.png",
            new TrackCurrentIpc2221AView()) {

            TrackCurrentIpc2221AView view = (TrackCurrentIpc2221AView)this.View;

            //===============================================================================================//
            //============================================ VOLTAGE ==========================================//
            //===============================================================================================//

            //! @todo, Move these into the constructor for the base object?
            this.CalcVars.Add(
                "voltage",
                new CalcVar(
                    "voltage",
                    view.TextBoxVoltageValue,
                    view.VoltageUnits,
                    view.RadioButtonVoltage,
                    this.CalcVars,
                    (calcVars) => {
                        var current = calcVars["current"].RawVal;
                        var resistance = calcVars["resistance"].RawVal;
                        return current * resistance;
                    },
                    new NumberUnit[]{
                        new NumberUnit("mV", 0.001),
                        new NumberUnit("V", 1.0, NumberPreference.DEFAULT),
                    },
                    0.0));

            // Add validators
            this.CalcVars["voltage"].AddValidator(Validator.IsNumber(CalcValidationLevels.Error));
            this.CalcVars["voltage"].AddValidator(Validator.IsGreaterThanZero(CalcValidationLevels.Error));
        }
    }
}
