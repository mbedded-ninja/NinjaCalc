<template>
  <div id="app" style="display: flex; width: 100%; height: 100%;">

    <!-- This is a long, thin menu down the left-hand side of the screen -->
    <div id="left-bar" style="width: 50px; height: 100%; background-color: #eeeeee;">
      <div id="menu-button-wrapper">
        <md-button id="menu-button" class="md-icon-button" @click.native="showLeftSideNav">
          <md-icon>menu</md-icon>
        </md-button>
      </div>
    </div>

    <!--Only show this if no calculators are open-->
    <div id="no-calc-screen" v-if="!this.$store.state.core.openCalcs.length"
        style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
        <div style="font-size: 30px;">No calculators are open! Do you wish to create one?</div>
        <div style="height: 20px;"></div>
        <ui-button @click.native="showCalculatorSelectionOverlay" color="primary" style="width: 200px;">New Calculator</ui-button>
    </div>

    <!-- The "editable" property allows you to close tabs. Note that by default this also adds a "+" button on the far-
    right of the tab header, but we disable this in CSS -->
    <el-tabs type="card" v-if="this.$store.state.core.openCalcs.length" v-model="activeTabId" editable @edit="handleTabsEdit">
      <!-- The name property is the unique ID which identifies the tab -->
      <el-tab-pane v-for="(item, index) in this.$store.state.core.openCalcs" :key="index" :label="item.name" :name="item.uniqueId.toString()">
        <component :is="item.componentName"></component>
      </el-tab-pane>
    </el-tabs>

    <!-- OBJECTS NOT IN DOC FLOW -->
    <calculator-selection-overlay
      v-if="this.$store.state.showCalculatorSelectionOverlay"></calculator-selection-overlay>
    <LeftSideMenu></LeftSideMenu>
  </div>
</template>

<script>
/* eslint-disable */

import Vue from 'vue'
import LeftSideMenu from '@/components/LeftSideMenu/LeftSideMenu'
import CalculatorSelectionOverlay from '@/components/CalculatorSelectionOverlay/CalculatorSelectionOverlay'

import { capacitorChargeCalculator } from '@/components/Calculators/Electronics/Basic/CapacitorCharge/Calc'
import { ohmsLawCalculator } from '@/components/Calculators/Electronics/Basic/OhmsLaw/Calc'
import { resistorDividerCalculator } from '@/components/Calculators/Electronics/Basic/ResistorDivider/Calc'
import { standardResistanceCalculator } from '@/components/Calculators/Electronics/Basic/StandardResistance/Calc'
import { lowPassRcCalculator } from '@/components/Calculators/Electronics/Filters/LowPassRC/Calc'
import { microstripImpedanceCalculator } from '@/components/Calculators/Electronics/Pcb/TrackImpedance/MicrostripImpedance/Calc'
import { trackCurrentIpc2152Calculator } from '@/components/Calculators/Electronics/Pcb/TrackCurrentIpc2152/Calc'
import { trackCurrentIpc2221ACalculator } from '@/components/Calculators/Electronics/Pcb/TrackCurrentIpc2221A/Calc'
import { viaCurrentIpc2221ACalculator } from '@/components/Calculators/Electronics/Pcb/ViaCurrentIpc2221A/Calc'
import { dewPointMagnusCalculator } from '@/components/Calculators/Electronics/Sensors/DewPointMagnus/Calc'
import { ntcThermistorTemperature } from '@/components/Calculators/Electronics/Sensors/NtcThermistor/Calc'
import { buckConverterCalculator } from '@/components/Calculators/Electronics/Smps/BuckConverter/Calc'

import { twoCoordinateGeodesics } from "@/components/Calculators/Geospatial/TwoCoordinateGeodesics/Calc";
import { mapPlotter } from "@/components/Calculators/Geospatial/MapPlotter/Calc";

import { crcCalculator } from '@/components/Calculators/Software/Crc/Calc'
import { pidTuner } from '@/components/Calculators/Software/PidTuner/Calc'

export default {
  name: 'app',
  metaInfo: {
    title: 'NinjaCalc',
    meta: [
      { charset: 'utf-8' },
      { vmid: 'description', name: 'description', content: 'Test description' }
    ]
  },
  components: {
    LeftSideMenu,
    CalculatorSelectionOverlay
  },
  data: function () {
    return {}
  },
  computed: {
    activeTabId () {
      return this.$store.state.activeTabId.toString()
    },
    route () {
      return this.$store.state.route
    }
  },
  methods: {
    showCalculatorSelectionOverlay: function () {
      this.$store.commit('showCalculatorSelectionOverlay', {
        trueFalse: true
      })
    },
    showLeftSideNav () {
      this.$store.commit('showLeftSideBar', {
        trueFalse: true
      })
    },
    handleTabsEdit (targetName, action) {
      console.log('App.handleTabEdit() called.')
      switch (action) {
        case 'add':
          // This should never get called, since we hid the "add" button!
          throw new Error('action.add() is not supported by App.handleTabsEdit().')
        case 'remove':
          this.$store.commit('closeCalculator', {
            // Need to convert string to integer
            uniqueId: parseInt(targetName)
          })
          break
        default:
          throw new Error('Provided action to handleTabsEdit() was not supported.')
      }
    },
    // This should be called everytime the $store.state.route object changes
    // (i.e. whenever the route path changes)
    // This function performs the state change required due to the route object changing
    handleRouteChange () {
      // Check to see if route is in the form "/tool/<tool-name>"
      let pattern = /^\/tool\//g
      let regex = new RegExp(pattern)
      let exec = regex.exec(this.route.path)
      console.log('exec = ')
      console.log(exec)
      if(!exec) {
        // "/tool/" at start of path was not found
        // console.error('"/tool/" was not found at start of route path.')
        return
      }
      console.log('regex.lastIndex = ' + regex.lastIndex)
      let toolName = this.route.path.substring(regex.lastIndex)
      // Remove any trailing forward-slash. This is needed because
      // using prerender-spa-plugin causes a '/' to be added to the end of
      // tool routes
      toolName = toolName.replace(/\//g, '')
      console.log('toolName = ' + toolName)
      // Make sure path is valid calculator
//        const toolName = this.route.path.substring(1, this.route.path.length)
      var foundCalc = this.$store.state.core.availableCalcs.find((element) => {
        return element.mainView.name === toolName
      })
      if (!foundCalc) {
        // If no tool was found, fail
        console.error('Tool "' + toolName + '" was not found.')
        return
      }

      // Hide the overlay
      this.$store.commit('showCalculatorSelectionOverlay', {
        trueFalse: false
      })

      // Tool was found, so open it!
      this.$store.dispatch('openCalc', {
        // Remove the first "/"
        componentName: toolName
      })
    }
  },
  watch: {
    route () {
      console.log('route() watcher called.')
      console.log(this.route)
      this.handleRouteChange()
    }
  },
  mounted () {

    console.log('NinjaCalc app mounted.')

    // ============================================ //
    // ========== ELECTRONICS -> BASIC ============ //
    // ============================================ //

    this.$store.dispatch('registerCalc', capacitorChargeCalculator)
    this.$store.dispatch('registerCalc', ohmsLawCalculator)
    this.$store.dispatch('registerCalc', resistorDividerCalculator)
    this.$store.dispatch('registerCalc', standardResistanceCalculator)

    // ============================================ //
    // ========= ELECTRONICS -> FILTERS =========== //
    // ============================================ //

    this.$store.dispatch('registerCalc', lowPassRcCalculator)

    // ============================================ //
    // =========== ELECTRONICS -> SMPS ============ //
    // ============================================ //

    this.$store.dispatch('registerCalc', buckConverterCalculator)

    // ============================================ //
    // ========= ELECTRONICS -> SENSORS =========== //
    // ============================================ //

    this.$store.dispatch('registerCalc', dewPointMagnusCalculator)
    this.$store.dispatch('registerCalc', ntcThermistorTemperature)

    // ============================================ //
    // =========== ELECTRONICS -> PCB ============= //
    // ============================================ //

    this.$store.dispatch('registerCalc', microstripImpedanceCalculator)
    this.$store.dispatch('registerCalc', trackCurrentIpc2221ACalculator)
    this.$store.dispatch('registerCalc', trackCurrentIpc2152Calculator)
    this.$store.dispatch('registerCalc', viaCurrentIpc2221ACalculator)

    // ============================================ //
    // ================= GEOSPATIAL =============== //
    // ============================================ //

    this.$store.dispatch('registerCalc', mapPlotter)
    this.$store.dispatch('registerCalc', twoCoordinateGeodesics)

    // ============================================ //
    // ================== SOFTWARE ================ //
    // ============================================ //

    this.$store.dispatch('registerCalc', crcCalculator)
    this.$store.dispatch('registerCalc', pidTuner)

    // Show the overlay by default.
    // THIS MUST BE DONE BEFORE handlerRouteChange() IS CALLED
    this.$store.commit('showCalculatorSelectionOverlay', {
      trueFalse: true
    })

    // Call this for the first time, since it may be already set,
    // and we only start listening for changes from this point onwards.
    this.handleRouteChange()
  }
}
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }

  #no-calc-screen {
    display: flex;

    width: 100%;
    height: 100%;

  }

  .centered {
    margin: auto;
  }

  *,
  *::before,
  *::after {
    box-sizing: border-box;
  }

  html {
    font-size: 100%;
  }

  /* ===== TAB STYLING ===== */
  div.el-tabs {
    width: 100%;
    height: 100%;

    display: flex;
    flex-direction: column;

    padding-top: 10px;
    padding-left: 10px;
    padding-right: 10px;
  }

  /* This hides the "+" button which is automatically added
  to the far-right of the tab header when the "editable"
  property is supplied to the tab component */
  span.el-tabs__new-tab {
    display: none;
  }

  div.el-tabs__content {
    height: 100%;
  }

  div.el-tab-pane {
    width: 100%;
    height: 100%;

    display: flex;

    /* This gives scroll-bars if calculator content is too large for screen in vertical direction */
    overflow-y: auto;
  }

  /* This targets the Material Design tooltips, which by default have
  a fixed height, which means that multiple lines of text do not work.
  This fixes this limitation.
   */
  span.md-tooltip {
    max-width: 300px !important;
    height: auto !important;
    font-size: 12px;

    /* Justify the left/right sides of the text within tooltip */
    text-align: justify;

    /* This makes the tooltip text within the span element wrap correctly. */
    white-space: normal;
  }

  div.calculator-container {
    /* margin: auto prevents the top and bottom of overflowing flex content from NOT being accessable within a flex */
    margin: auto;
  }

  div.modal-mask {
    display: flex !important;
  }

  div.modal-wrapper {
    display: flex !important;
    flex-direction: column !important;
    align-content: center !important;
    justify-content: center !important;
  }

</style>

