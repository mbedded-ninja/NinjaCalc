<template>
    <div class="app" style="display: flex; flex-direction: column; justify-content: center; align-items: center; width: 100%; overflow: auto; min-height: min-content;">

      <InfoCollapsible title="Info" style="max-width: 800px;">
        <p>This tool can be used to explore how changing the P, I and D terms of a PID controller can effect the response of the system. It can be used to simulate various processes (a.k.a. plants or systems), and then can be used to tune the PID controller appropriately.</p>
        <p>Two pre-designed processes (a mass/spring/damper and small R/C jet engine) or a custom user-defined process can be used. To setup your own process select 'User Defined' as the process and then click 'Edit Process'. From there, see the code comments for further instructions/guidance.</p>
        <p>The mass/spring/damper and jet engine processes are sensitive to the simulation time step. Both are modelled by assuming specific variables remain constant over a small time step. For this reason, the model may be inaccurate if the time step is too large. A time step between 10-50ms seem to work well in most cases.</p>
        <p>CV stands for <span style="font-style: italic;">control variable</span>, this is the variable that we have control over, and is an input to the process. The PID controller controls this variable.</p>
        <p>PV stands for <span style="font-style: italic;">process variable</span>, this is the variable we don't have direct control over, but we want to get it to a specific set-point. The PID controller seeks to brind the error between the set-point and the PV down to 0.</p>
        <p style="text-decoration: underline;">Run modes:</p>
        <ul>
          <li><span style="font-style: italic;">Manual CV Control (no PID):</span> This run mode does not use the PID controller. You control the CV manually with a slider, and can see how this effects the PV. This is useful to get an idea on how the system reacts to a change in the CV.</li>
          <li><span style="font-style: italic;">Manual PV Control (PID):</span> This run mode uses the PID controller. You manually set the PV set point with a slider, and the PID controller will try and get the process to this set point.</li>
          <li><span style="font-style: italic;">Automatic PV Step Changes (PID):</span> This run mode uses the PID controller. The PV set point is toggled automatically between two constant values (i.e. step changes). This is useful for tuning as you can tweak the PID values while watching the response to each step change. The two values that the PV is toggled between can be set using Automatic Step Change PV Value 1 and 2 under the Simulation Settings panel.</li>
        </ul>

        <p>For more information on PID controllers, please see <a href="http://blog.mbedded.ninja/programming/general/pid-control">http://blog.mbedded.ninja/programming/general/pid-control</a>.</p>
      </InfoCollapsible>

        <!-- H FLEX -->
        <div style="display: flex;">
            <!-- The style "min-height: min-content" is required so that this flex box"s height expands to the maximum width
            of any of it"s children -->
            <div id="controls" style="display: flex; flex-direction: column; width: 370px;">
                <!-- ========================================= -->
                <!-- ========== SIMULATION SETTINGS ========== -->
                <!-- ========================================= -->
                <ui-collapsible title="Simulation Settings" :open="true" class="panel">
                    <div style="display: flex; flex-direction: column; align-items: center;">

                        <div style="display: flex; align-items: center;">
                          Process:&nbsp;
                          <select v-bind:value="selProcessName" v-on:change="handleSelProcessChanged" :disabled="simulationRunning" style="width: 150px; height: 30px; background-color: transparent;">
                            <option v-for="option in processes" v-bind:value="option.name" v-bind:key="option.name">
                              {{ option.name }}
                            </option>
                          </select>
                        </div>
                        <div style="height: 10px;"/> <!-- V SPACER -->
                        <div>
                          <mn-button
                                  variant="success"
                                  :onClick="processEdit"
                                  :disabledB="simulationRunning"
                                  style="width: 140px; height: 30px;">
                              Edit Process
                          </mn-button>
                        </div>

                        <div style="height: 20px;"/>

                        <div>
                          <table>
                            <tr>
                              <td>Simulation Tick Period (ms):</td>
                              <td><input v-model.number="simulationConfig.tickPeriod_ms" :disabled="simulationRunning" style="width: 80px;"/></td>
                            </tr>
                            <tr>
                              <td>Plot Every N Ticks:</td>
                              <td><input v-model.number="simulationConfig.plotEveryNTicks" :disabled="simulationRunning" style="width: 80px;"/></td>
                            </tr>
                          </table>
                        </div>
                        <div style="height: 20px;"/>

                        <div style="display: flex;">
                          <div>
                          <span class="panel-subheading">Control Variable</span>
                          <table>
                            <tr>
                              <td>Name: </td>
                              <td style="font-style: italic; color: grey;">{{ this.simulationConfig.controlVarName }}</td>
                            </tr>
                            <tr>
                              <td>Units: </td>
                              <td style="font-style: italic; color: grey;">{{ this.simulationConfig.controlVarUnits }}</td>
                            </tr>
                            <tr>
                              <td>min</td>
                              <td><input v-model="pidConfig.controlVariableLimits.min"
                                      v-on:change="controlVariableLimitsChanged"
                                      :disabled="simulationRunning" style="width: 80px;"/>
                              </td>
                            </tr>
                            <tr>
                              <td>max</td>
                              <td><input v-model="pidConfig.controlVariableLimits.max"
                                      v-on:change="controlVariableLimitsChanged"
                                      :disabled="simulationRunning" style="width: 80px;"/>
                              </td>
                            </tr>
                          </table>
                          </div>
                          <div style="width: 20px;"/>
                          <div>
                          <span class="panel-subheading">Process Variable</span>
                          <table>
                            <tr>
                              <td>Name: </td>
                              <td style="font-style: italic; color: grey;">{{ this.simulationConfig.processVarName }}</td>
                            </tr>
                            <tr>
                              <td>Units: </td>
                              <td style="font-style: italic; color: grey;">{{ this.simulationConfig.processVarUnits }}</td>
                            </tr>
                            <tr>
                              <td>min</td>
                              <td><input v-model="simulationConfig.processVarLimMin"
                                      v-on:change="processVarLimitsChanged"
                                      :disabled="simulationRunning" style="width: 80px;"/>
                              </td>
                            </tr>
                            <tr>
                              <td>max</td>
                              <td><input v-model="simulationConfig.processVarLimMax"
                                      v-on:change="processVarLimitsChanged"
                                      :disabled="simulationRunning" style="width: 80px;"/>
                              </td>
                            </tr>
                          </table>
                          </div>
                        </div>

                        <div style="height: 10px;"/>

                        <div style="display: flex; justify-content: center; align-items: center;">
                            <div>Run Mode:</div>
                            &nbsp;
                            <select v-model="simulationConfig.runMode" :options="runModes" :disabled="simulationRunning" style="width: 240px; height: 30px; background-color: transparent;">
                                <option v-for="option in runModes" v-bind:value="option"  v-bind:key="option">
                                {{ option }}
                                </option>
                            </select>
                        </div>
                        <div style="height: 10px;"/>
                        <div style="display: flex; flex-direction: column;">
                          <span class="panel-subheading">Automatic Step Change</span>
                          <table>
                            <tr>
                              <td>Value 1:&nbsp;</td>
                              <td>
                                <input v-model="simulationConfig.processVarStepChangeVal1"
                                    :disabled="simulationConfig.runMode !== simulationRunModesEnum.AUTO_PV_STEP_CHANGES || simulationRunning"
                                    style="width: 100px;"/>
                              </td>
                              <td>Value 2:&nbsp;</td>
                              <td>
                                <input v-model="simulationConfig.processVarStepChangeVal2"
                                    :disabled="simulationConfig.runMode !== simulationRunModesEnum.AUTO_PV_STEP_CHANGES || simulationRunning"
                                    style="width: 100px;"/>
                                </td>
                            </tr>
                          </table>
                          <div style="height: 5px;"/>
                          <div>Step Period(ms):&nbsp;
                            <input v-model="simulationConfig.stepChangePeriod_ms"
                                :disabled="simulationConfig.runMode !== simulationRunModesEnum.AUTO_PV_STEP_CHANGES || simulationRunning"
                                style="width: 100px;"/>
                          </div>
                        </div>
                        <div style="height: 10px;"/>
                        <mn-button
                                :variant="!simulationRunning ? 'success' : 'danger'"
                                :onClick="startStopSimulation"
                                style="width: 150px; height: 50px;">
                                <div style="font-size: 18px;">{{ !simulationRunning ? "START" : "STOP" }}</div>
                        </mn-button>
                    </div>
                </ui-collapsible>

                <div style="height: 10px;" />

                <!-- =================== -->
                <!-- ===== CONTROLS ==== -->
                <!-- =================== -->
                <ui-collapsible title="Controls" :open="true" class="panel">
                    <div style="display: flex; flex-direction: column;">
                        <span class="panel-subheading">
                            CV ({{ this.simulationConfig.controlVarName + ", " + this.simulationConfig.controlVarUnits }})
                        </span>
                        <div>
                            <div style="height: 40px;"/>
                            <vue-slider
                                ref="slider"
                                v-model="controlVarRounded"
                                :min="Number(pidConfig.controlVariableLimits.min)"
                                :max="Number(pidConfig.controlVariableLimits.max)"
                                :interval="(Number(pidConfig.controlVariableLimits.max) - Number(pidConfig.controlVariableLimits.min)) / 100.0"
                                :disabled="simulationConfig.runMode === simulationRunModesEnum.MANUAL_CONTROL_PV ||
                                        simulationConfig.runMode === simulationRunModesEnum.AUTO_PV_STEP_CHANGES"
                                style="width:300px;" />
                        </div>
                        <span class="panel-subheading">
                            SP ({{ this.simulationConfig.processVarName + ", " + this.simulationConfig.processVarUnits }})
                        </span>
                        <div>
                            <div style="height: 40px;"/>
                            <vue-slider
                                ref="slider"
                                v-model="setPoint"
                                :min="simulationConfig.processVarLimMin" :max="simulationConfig.processVarLimMax"
                                :interval="(Number(simulationConfig.processVarLimMax) - Number(simulationConfig.processVarLimMin)) / 100.0"
                                :disabled="simulationConfig.runMode === simulationRunModesEnum.MANUAL_CONTROL_CV ||
                                        simulationConfig.runMode === simulationRunModesEnum.AUTO_PV_STEP_CHANGES"
                                style="width:300px;"/>
                        </div>
                    </div>
                </ui-collapsible>

                <div style="height: 10px;" />

                <!-- =================== -->
                <!-- === PID SETTINGS == -->
                <!-- =================== -->
                <ui-collapsible title="PID Settings" :open="true" class="panel">
                  <div style="display: flex; flex-direction: column;">
                    <span class="panel-subheading">PID Constants</span>
                    <table class="sliders">
                    <tbody>
                        <tr>
                            <td>Constant</td>
                            <td>Min.</td>
                            <td>Max.</td>
                            <td>Value</td>
                        </tr>
                        <tr>
                            <td>P</td>
                            <td><input v-model="pidConfig.constants.p.min" class="pid-limit"/></td>
                            <td><input v-model="pidConfig.constants.p.max" class="pid-limit"/></td>
                            <td style="width: 200px;">
                            <vue-slider ref="slider" v-model="pidConfig.constants.p.value"
                            :min="Number(pidConfig.constants.p.min)" :max="Number(pidConfig.constants.p.max)"
                            :interval="(Number(pidConfig.constants.p.max) - Number(pidConfig.constants.p.min)) / 100.0"/></td>
                        </tr>
                        <tr>
                            <td>I</td>
                            <td><input v-model="pidConfig.constants.i.min" class="pid-limit"/></td>
                            <td><input v-model="pidConfig.constants.i.max" class="pid-limit"/></td>
                            <td style="width: 200px;">
                            <vue-slider ref="slider" v-model="pidConfig.constants.i.value"
                            :min="Number(pidConfig.constants.i.min)" :max="Number(pidConfig.constants.i.max)"
                            :interval="(Number(pidConfig.constants.i.max) - Number(pidConfig.constants.i.min)) / 100.0"/>
                            </td>
                        </tr>
                        <tr>
                            <td>D</td>
                            <td><input v-model="pidConfig.constants.d.min" class="pid-limit"/></td>
                            <td><input v-model="pidConfig.constants.d.max" class="pid-limit"/></td>
                            <td style="width: 200px;">
                            <vue-slider ref="slider" v-model="pidConfig.constants.d.value"
                            :min="Number(pidConfig.constants.d.min)" :max="Number(pidConfig.constants.d.max)"
                            :interval="(Number(pidConfig.constants.d.max) - Number(pidConfig.constants.d.min)) / 100.0"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <div style="height: 20px;"/>

                    <!-- INTEGRAL LIMITING SETTINGS -->
                    <div id="integral-limiting-container" style="display: flex; flex-direction: column; justify-content: center;">
                    <span class="panel-subheading">Integral Limiting (Windup Control)</span>
                    <div style="height: 10px;"/>
                    <div>
                        Mode:
                        <select v-model="pidConfig.integralLimitConfig.mode" @change="setIntegralLimitingMode" style="width: 300px; height: 30px; background-color: transparent;">
                            <option v-for="option in integralLimitModes" v-bind:value="option" v-bind:key="String(option)">
                                {{ String(option) }}
                            </option>
                        </select>
                    </div>
                    <div style="height: 10px;"/>
                    <div style="display: flex; justify-content: center;">
                        min <input v-model="pidConfig.integralLimitConfig.constantMin" @change="setIntegralLimitingMode" :disabled="areIntegralLimitingConstantsDisabled" style="width: 50px;"/>
                        &nbsp;&nbsp;&nbsp;
                        max <input v-model="pidConfig.integralLimitConfig.constantMax" @change="setIntegralLimitingMode" :disabled="areIntegralLimitingConstantsDisabled" style="width: 50px;"/>
                    </div>
                    </div> <!-- <div id="integral-limiting-container"> -->
                    </div>
                </ui-collapsible> <!-- <panel title="PID Settings"> -->
            </div> <!-- <div id="controls" style="display: flex;"> -->

            <div style="width: 30px;"/> <!-- H SPACER -->

            <!-- PROCESS VARIABLE AND PID SET-POINT CHART -->
            <div style="display: flex; flex-direction: column;">
                <div style="width: 800px; height: 400px;">
                    <canvas id="myChart" width="800" height="400"></canvas>
                </div>

                <!-- PID TERMS CHART -->
                <div style="width: 800px; height: 400px;">
                    <canvas id="pidTermsChart" width="800" height="400"></canvas>
                </div>
            </div>
        </div>

        <div style="height: 20px;"/>

        <!-- ============================================= -->
        <!-- =========== PROCESS EDIT MODEAL ============= -->
        <!-- ========== (not in document flow) =========== -->
        <mn-modal v-if="showProcessEditModal" @close="showProcessEditModal = false">
            <div slot="body" style="display: flex; flex-direction: column; width: 100%; height: 100%;">
                <textarea v-model="plantCodeString" style="flex-grow: 2;"/>
                <div style="height: 10px;"/>
                <mn-button variant="success" :onClick="processHideModalAndLoad" style="width: 120px; height: 30px; align-self: flex-end;">Close</mn-button>
            </div>
        </mn-modal>

        <mn-msgbox v-if="showSelProcessChangeConfirm" type="confirm" @ok="handleSelProcessChangeConfirm" @cancel="handleSelProcessChangeCancel">
            This will load a new process. All existing changes and chart data will be lost. Continue?
        </mn-msgbox>
    </div>
</template>

<script>
import Chart from 'chart.js'
import vueSlider from 'vue-slider-component'

import * as Constants from '@/Config.js'
import RcJetEngineProcessTxt from './Processes/RcJetEngineProcess.txt'
import SpringMassDamperProcessTxt from './Processes/SpringMassDamperProcess.txt'
import UserDefinedProcessTxt from './Processes/UserDefinedProcess.txt'

import { Pid, IntegralLimitModes } from './Pid'
// import OgImagePath from './og-image.png'
var OgImagePath = require('./og-image.png')

const SimulationRunModes = {
  MANUAL_CONTROL_CV: 'Manual CV Control (no PID)',
  MANUAL_CONTROL_PV: 'Manual PV Control (PID)',
  AUTO_PV_STEP_CHANGES: 'Automatic PV Step Changes (PID)'
}

export default {
  name: 'pid-tuner',
  // Meta info, used by the vue-meta plugin
  metaInfo: {
    meta: [      
      { vmid: 'og:title', property: 'og:title', content: 'PID Tuner' },
      { vmid: 'og:type', property: 'og:type', content: 'website' },
      { vmid: 'og:image', property: 'og:image', content: Constants.HOSTNAME + OgImagePath },
      { vmid: 'description', name: 'description', content: 'A tool to help you tune a PID controller. Can simulate both predefined and user-defined process/plants/systems. Incl. real-time graphs of simulation.' }
    ]
  },
  components: {
    vueSlider
  },
  data () {
    return {
      processes: [
        { name: 'Mass/Spring/Damper', code: SpringMassDamperProcessTxt },
        { name: 'R/C Jet Engine', code: RcJetEngineProcessTxt },
        { name: 'User Defined', code: UserDefinedProcessTxt }
      ],
      selProcessNameTemp: '',
      selProcessName: '',
      showSelProcessChangeConfirm: false,

      plantCodeString: '', // This is the non-eval()'d plant code, either provided from file or user-defined
      plantCode: null, // This gets populated by eval() when the Start/Stop simulation button is clicked
      showProcessEditModal: false, // Set to true when the 'Edit Process' button is clicked

      controlVariable: 0.0,
      processVariable: 0.0,
      setPoint: 0.0,

      duration_s: 0.0,

      simulationRunModesEnum: SimulationRunModes,
      runModes: [],
      // selectedRunMode: SimulationRunModes.AUTO_PV_STEP_CHANGES,

      chartConfig: {
        type: 'line',
        data: {
          datasets: [
            {
              label: 'Measured Value',
              backgroundColor: 'rgba(0, 0, 255, 0.5)',
              borderColor: 'rgba(0, 0, 255, 0.5)',
              data: [],
              fill: false
            }
          ]
        },
        options: {
          title: {
            display: true,
            text: 'Process Output And PID Set-Point'
          },
          scales: {
            xAxes: [
              {
                type: 'linear',
                scaleLabel: {
                  display: true,
                  labelString: 'Time (s)'
                }
              }
            ],
            yAxes: [
              {
                scaleLabel: {
                  display: true,
                  labelString: 'n/a'
                }
              }
            ]
          }
        }
      },
      chart: null,

      pidTermsChartConfig: {
        type: 'line',
        data: {
          datasets: [
            // Draw output first as first dataset has highest Z value (i.e. drawn ontop of other
            // UI elements)
            {
              label: 'Output (P + I + D)',
              backgroundColor: 'rgba(255, 60, 92, 0.8)',
              borderColor: 'rgba(255, 60, 92, 0.8)',
              data: [],
              fill: false
            },
            {
              label: 'P',
              backgroundColor: 'rgba(84, 255, 60, 0.8)',
              borderColor: 'rgba(84, 255, 60, 0.8)',
              data: [],
              fill: false
            },
            {
              label: 'I',
              backgroundColor: 'rgba(60, 255, 201, 0.8)',
              borderColor: 'rgba(60, 255, 201, 0.8)',
              data: [],
              fill: false
            },
            {
              label: 'D',
              backgroundColor: 'rgba(60, 205, 255, 0.8)',
              borderColor: 'rgba(60, 205, 255, 0.8)',
              data: [],
              fill: false
            }
          ]
        },
        options: {
          title: {
            display: true,
            text: 'PID Controller Output'
          },
          scales: {
            xAxes: [
              {
                type: 'linear',
                scaleLabel: {
                  display: true,
                  labelString: 'Time (s)'
                }
              }
            ],
            yAxes: [
              {
                scaleLabel: {
                  display: true,
                  labelString: 'PID Effort'
                }
              }
            ]
          }
        }
      }, // pidTermsChartConfig: {
      pidTermsChart: null,
      // Make sure the following indicies stay in sync with the
      // datasets defined in the pidTermsChartConfig variable above!
      outputDataIndex: 0,
      pTermDataIndex: 1,
      iTermDataIndex: 2,
      dTermDataIndex: 3,

      simulationRunning: false,
      modelTickTimer: null,
      // modelUpdateTimer: null,

      maxNumDataPoints: 100,
      pid: new Pid(0.0006, 0.0006, 0.0), // PID constants get overriden by values set from sliders

      simulationConfig: {
        // These get overwritten when a process is loaded (process.getDefaults())
        processVarName: 'n/a',
        processVarUnits: 'n/a',
        processVarStepChangeVal1: 0.0,
        processVarStepChangeVal2: 10.0,
        processVarLimMin: 0.0,
        processVarLimMax: 100000.0,
        controlVarName: 'n/a',
        controlVarUnits: 'n/a',
        tickPeriod_ms: 50,
        plotEveryNTicks: 2,
        stepChangePeriod_ms: 4000, // Period between PV step changes when in 'Automatic PV Step Changes (PID)' run mode
        runMode: SimulationRunModes.MANUAL_CONTROL_CV
      },

      tickCount: 0, // Local state to keep track of how many ticks have occured since simulation start

      pidConfig: {
        // These get overwritten when a process is loaded (process.getDefaults())
        constants: {
          p: {
            min: 0.0,
            max: 1.0,
            value: 0.5
          },
          i: {
            min: 0.0,
            max: 1.0,
            value: 0.5
          },
          d: {
            min: 0.0,
            max: 1.0,
            value: 0.5
          }
        },
        integralLimitConfig: {
          mode: 'Output Limited',
          constantMin: -1,
          constantMax: 1
        },
        controlVariableLimits: {
          enabled: false,
          min: 0.0,
          max: 1.0
        }
      }, // pidConfig

      areIntegralLimitingConstantsDisabled: false,
      integralLimitModes: []
    }
  },
  computed: {
    controlVarRounded: {
      get: function () {
        return this.controlVariable.toPrecision(4)
      },
      set: function (newValue) {
        this.controlVariable = Number.parseFloat(newValue)
      }
    },
    pidEnabled () {
      // console.log('Computing pidEnabled...')
      if (
        this.simulationConfig.runMode ===
          this.simulationRunModesEnum.MANUAL_CONTROL_PV ||
        this.simulationConfig.runMode ===
          this.simulationRunModesEnum.AUTO_PV_STEP_CHANGES
      ) {
        // console.log('Returning true.')
        return true
      } else if (
        this.simulationConfig.runMode ===
        this.simulationRunModesEnum.MANUAL_CONTROL_CV
      ) {
        return false
      } else {
        throw new Error(
          'Unexpected simulation run mode. this.simulationConfig.runMode = ' +
            this.simulationConfig.runMode
        )
      }
    },
    simulationTickPeriod_s () {
      return this.simulationConfig.tickPeriod_ms / 1000.0
    }
  },
  methods: {
    addSetPointLine () {
      // console.log('Adding set point line to chart (if not already added).')
      // Add set point line to chart
      if (this.chartConfig.data.datasets.length === 1) {
        this.chartConfig.data.datasets.push({
          label: 'Set Point',
          backgroundColor: 'rgba(255,0,0,.5)',
          borderColor: 'rgba(255,0,0,.5)',
          data: [],
          fill: false
        })
        this.chart.update()
      }
    },
    // Resets UI. Resets simulation time, tick, set point, PID controller. Clears data from charts.
    clearAll () {
      this.tickCount = 0
      this.duration_s = 0

      this.controlVariable = 0.0
      this.processVariable = 0.0
      this.setPoint = 0.0

      this.pid.reset()

      this.chartConfig.data.datasets.forEach(function (element) {
        element.data = []
      })
      this.chart.update()

      this.pidTermsChartConfig.data.datasets.forEach(function (element) {
        element.data = []
      })
      this.pidTermsChart.update()
    },
    controlVariableLimitsChanged () {
      console.log('Control variable limits changed. min = ' + this.pidConfig.controlVariableLimits.min + ', max = ' + this.pidConfig.controlVariableLimits.max)
      this.pid.setOutputLimits(
        this.pidConfig.controlVariableLimits.enabled,
        Number(this.pidConfig.controlVariableLimits.min),
        Number(this.pidConfig.controlVariableLimits.max)
      )
    },
    handleSelProcessChangeCancel () {
      console.log('handleSelProcessChangeCancel() called')
      // Hide confirm box
      this.showSelProcessChangeConfirm = false
    },
    handleSelProcessChanged (event) {
      console.log('handleSelProcessChanged() called. event = ')
      console.log(event.srcElement.selectedIndex)

      this.selProcessNameTemp = event.srcElement.selectedIndex

      // Show confirm box
      this.showSelProcessChangeConfirm = true
    },
    handleSelProcessChangeConfirm () {
      console.log('handleSelProcessChangeConfirm() called')

      // Hide confirm box
      this.showSelProcessChangeConfirm = false

      this.selProcessName = this.processes[this.selProcessNameTemp].name

      this.processLoad()
    },
    processEdit () {
      console.log('processEdit() called.')
      this.showProcessEditModal = true
    },
    processHideModalAndLoad () {
      // Called when close() button inside edit process modal is clicked
      console.log('processHideModalAndLoad() called.')
      this.showProcessEditModal = false

      // We want to load any changes user has made to code
      console.log('Loading user changes to process code...')
      this.updateAfterLoadingProcess()
    },
    processLoad () {
      console.log('processLoad() called.')

      // Clear all existing data/setup
      this.clearAll()

      // Load the file containing the process code if not user defined process
      // if (this.selProcessName !== 'User Defined') {
      console.log('Loading predefined process code...')
      var self = this
      let selProcess = this.processes.find(function (element) {
        return element.name === self.selProcessName
      })
      // console.log('selProcess = ')
      console.log(selProcess)
      this.plantCodeString = selProcess.code
      // }
      // console.log('this.plantCodeString = ')
      // console.log(this.plantCodeString)
      // console.log('plantCode = ')
      console.log(this.plantCode)

      this.updateAfterLoadingProcess()
    },
    processVarLimitsChanged () {
      console.log('processVarLimitsChanged() called.')
    },
    updateAfterLoadingProcess () {
      console.log('updateAfterLoadingProcess() called.')

      console.log('eval()\'ing plant code...')
      // eslint-disable-next-line no-eval
      this.plantCode = eval(this.plantCodeString)
      console.log(this.plantCode)
      // Set PID tuner defaults
      const defaults = this.plantCode.getDefaults()
      // console.log('defaults =')
      console.log(defaults)

      if (defaults !== null) {
        console.log('Default values found.')
        this.simulationConfig = defaults.simulationConfig
        this.pidConfig = defaults.pidConfig
      } else console.log('Default values NOT found.')

      this.controlVariableLimitsChanged()

      // Update chart axes titles
      this.chartConfig.options.scales.yAxes[0].scaleLabel.labelString =
        this.simulationConfig.processVarName +
        ' (' +
        this.simulationConfig.processVarUnits +
        ')'

      this.pidTermsChartConfig.options.scales.yAxes[0].scaleLabel.labelString =
        this.simulationConfig.controlVarName +
        ' (' +
        this.simulationConfig.controlVarUnits +
        ')'

      this.plantCode.init()
    },
    performAutoSetPointChange () {
      console.log(
        'performAutoSetPointChange() called. processVarStepChangeVal2 = ' +
          this.simulationConfig.processVarStepChangeVal2)
      if (this.setPoint === this.simulationConfig.processVarStepChangeVal1) {
        this.setPoint = this.simulationConfig.processVarStepChangeVal2
      } else {
        this.setPoint = this.simulationConfig.processVarStepChangeVal1
      }
    },
    setIntegralLimitingMode () {
      // console.log('setIntegralLimitingMode() called.')
      if (this.pidConfig.integralLimitConfig.mode === IntegralLimitModes.NONE) {
        this.pid.setIntegralLimit({
          mode: IntegralLimitModes.NONE
        })
      } else if (
        this.pidConfig.integralLimitConfig.mode ===
        IntegralLimitModes.CONSTANT_LIMITED
      ) {
        this.pid.setIntegralLimit({
          mode: IntegralLimitModes.CONSTANT_LIMITED,
          min: Number(this.pidConfig.integralLimitConfig.constantMin),
          max: Number(this.pidConfig.integralLimitConfig.constantMax)
        })
      } else if (
        this.pidConfig.integralLimitConfig.mode ===
        IntegralLimitModes.OUTPUT_LIMITED
      ) {
        this.pid.setIntegralLimit({
          mode: IntegralLimitModes.OUTPUT_LIMITED
        })
      } else {
        throw new Error('Integral limiting mode unrecognized.')
      }

      // Set the boolean which determines if UI elements are disabled
      if (
        this.pidConfig.integralLimitConfig.mode ===
        IntegralLimitModes.CONSTANT_LIMITED
      ) {
        this.areIntegralLimitingConstantsDisabled = false
      } else {
        this.areIntegralLimitingConstantsDisabled = true
      }
    },
    startStopSimulation () {
      if (!this.simulationRunning) {
        // START
        console.log('Starting simulation...')

        this.modelTickTimer = window.setInterval(() => {
          this.tick()
        }, this.simulationTickPeriod_s * 1000.0)

        // this.modelUpdateTimer = window.setInterval(() => {
        // this.update()
        // }, this.plotPeriod_s * 1000.0)

        this.simulationRunning = true
      } else {
        // STOP
        console.log('Stopping simulation...')
        clearInterval(this.modelTickTimer)
        clearInterval(this.modelUpdateTimer)
        clearInterval(this.autoStepChangeTimer)
        this.simulationRunning = false
      }
    },
    // This updates the simulation. Should be called more frequently than update().
    tick () {
      if (this.simulationConfig.runMode === SimulationRunModes.AUTO_PV_STEP_CHANGES) {
        const numTicksBeforeChange = parseInt(
          this.simulationConfig.stepChangePeriod_ms / this.simulationConfig.tickPeriod_ms,
          10)
        // console.log('numTicksBeforeChange = ' + numTicksBeforeChange)
        if (this.tickCount !== 0 && this.tickCount % numTicksBeforeChange === 0) {
          this.performAutoSetPointChange()
        }
      }

      if (this.pidEnabled) {
        this.pid.setSetPoint(this.setPoint)
        this.controlVariable = this.pid.run(
          this.processVariable,
          this.simulationTickPeriod_s
        )
        // console.log('controlVariable = ' + this.controlVariable)
      }

      this.processVariable = this.plantCode.update(
        this.controlVariable,
        this.simulationTickPeriod_s
      )
      // console.log('New processVariable = ' + this.processVariable)

      this.duration_s += this.simulationTickPeriod_s

      // Work out whether we need to update charts on this tick
      if (this.tickCount % this.simulationConfig.plotEveryNTicks === 0) {
        this.update()
      }

      this.tickCount++
    },
    // This updates the UI. Called by window.setInterval(). Should be called no more frequently
    // than tick()
    update () {
      this.chartConfig.data.datasets[0].data.push({
        x: this.duration_s,
        y: this.processVariable
      })

      // Limit number of data points
      if (
        this.chartConfig.data.datasets[0].data.length > this.maxNumDataPoints
      ) {
        this.chartConfig.data.datasets[0].data.shift()
      }

      // If in run mode where PID is used, update set point also
      if (this.pidEnabled) {
        this.chartConfig.data.datasets[1].data.push({
          x: this.duration_s,
          y: this.setPoint
        })
      }

      // Limit number of data points
      if (this.chartConfig.data.datasets[1].data.length > this.maxNumDataPoints) {
        this.chartConfig.data.datasets[1].data.shift()
      }

      this.chart.update()

      // ===== PID TERMS CHART ===== //

      const pidTerms = this.pid.getLastTerms()

      // P
      this.pidTermsChartConfig.data.datasets[this.pTermDataIndex].data.push({
        x: this.duration_s,
        y: pidTerms.p
      })

      // Limit number of data points
      if (this.pidTermsChartConfig.data.datasets[this.pTermDataIndex].data.length > this.maxNumDataPoints) {
        this.pidTermsChartConfig.data.datasets[this.pTermDataIndex].data.shift()
      }

      // I
      this.pidTermsChartConfig.data.datasets[this.iTermDataIndex].data.push({
        x: this.duration_s,
        y: pidTerms.i
      })

      // Limit number of data points
      if (this.pidTermsChartConfig.data.datasets[this.iTermDataIndex].data.length > this.maxNumDataPoints) {
        this.pidTermsChartConfig.data.datasets[this.iTermDataIndex].data.shift()
      }

      // D
      this.pidTermsChartConfig.data.datasets[this.dTermDataIndex].data.push({
        x: this.duration_s,
        y: pidTerms.d
      })

      // Limit number of data points
      if (this.pidTermsChartConfig.data.datasets[this.dTermDataIndex].data.length > this.maxNumDataPoints) {
        this.pidTermsChartConfig.data.datasets[this.dTermDataIndex].data.shift()
      }

      // OUTPUT
      this.pidTermsChartConfig.data.datasets[this.outputDataIndex].data.push({
        x: this.duration_s,
        y: pidTerms.output
      })

      // Limit number of data points
      if (this.pidTermsChartConfig.data.datasets[this.outputDataIndex].data.length > this.maxNumDataPoints) {
        this.pidTermsChartConfig.data.datasets[this.outputDataIndex].data.shift()
      }

      this.pidTermsChart.update()
    },

    updatePidConstants () {
      this.pid.setConstants(
        this.pidConfig.constants.p.value,
        this.pidConfig.constants.i.value,
        this.pidConfig.constants.d.value
      )
    }
  },
  watch: {
    pidEnabled (val) {
      console.log('pidEnabled changed.')
      if (val) this.addSetPointLine()
    },
    pidConfig: {
      handler (val) {
        console.log('pidConstants changed.')
        this.updatePidConstants()
      },
      deep: true
    }
  }, // watch: {
  mounted () {
    // Draw plant output and set-point chart
    const plantOutputChartContext = document.getElementById('myChart')
    this.chart = new Chart(plantOutputChartContext, this.chartConfig)

    // Draw PID terms chart
    const pidTermsChartContext = document.getElementById('pidTermsChart')
    this.pidTermsChart = new Chart(
      pidTermsChartContext,
      this.pidTermsChartConfig
    )

    // Set default process (mass-spring-damper), and then load it
    this.selProcessName = this.processes[0].name
    this.processLoad()

    // Set the run mode to auto by default. This should trigger watch
    // this.selectedRunMode = SimulationRunModes.AUTO_PV_STEP_CHANGES

    this.updatePidConstants()
    this.controlVariableLimitsChanged()

    let self = this

    // Populate simulation run modes select box
    Object.keys(SimulationRunModes).forEach(function (key) {
      let obj = SimulationRunModes[key]
      self.runModes.push(obj)
    })
    console.log('this.runModes = ')
    console.log(this.runModes)

    // Populate integral limit modes select box
    Object.keys(IntegralLimitModes).forEach(function (key) {
      let obj = IntegralLimitModes[key]
      self.integralLimitModes.push(obj)
    })

    // Set default integral limiting mode
    this.pidConfig.integralLimitingMode = IntegralLimitModes.OUTPUT_LIMITED
    this.setIntegralLimitingMode()

    if (this.pidEnabled) this.addSetPointLine()
  } // mounted() {
}
/* eslint-enable */
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style scoped>
.diagram-container {
  position: relative;
}

.diagram-container > * {
  position: absolute;
}

h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}

/* Table with sliders. Make row height large enough that slider value fits into it */
.sliders tr {
  height: 50px;
}

.sliders input {
  width: 50px;
}

span.panel-subheading {
  text-align: center;
  font-weight: bold;
}

/* We need this to make list items display the bullet points! For some reason some other CSS is overriding how lists
are displayed. */
ul {
  list-style: disc outside;
}

ul li {
  display: list-item;
}
</style>

<style>
.panel div.ui-collapsible__header {
  color: white;
  background-color: rgb(204, 29, 255) !important;
}
</style>
