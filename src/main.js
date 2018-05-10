// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import store from './store'
import { router } from './router'
import { sync } from 'vuex-router-sync'

import App from './App'
import VueMaterial from 'vue-material'
import 'vue-material/dist/vue-material.css'
import vSelect from 'vue-select'
import KeenUI from 'keen-ui'
import 'keen-ui/dist/keen-ui.css'
import VTooltip from 'v-tooltip'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import * as VueGoogleMaps from 'vue2-google-maps'
import VueSlider from 'vue-slider-component'

import Meta from 'vue-meta'

import VueAnalytics from 'vue-analytics'

import CalcValue from '@/misc/CalculatorEngineV2/view/CalcValue.vue'
import CalcValueAndUnit from './misc/CalculatorEngineV2/view/CalcValueAndUnit.vue'
import CalcUnits from './misc/CalculatorEngineV2/view/CalcUnits.vue'
import CalcVarString from './misc/CalculatorEngineV2/view/CalcVarString.vue'
import CalcVarCheckbox from './misc/CalculatorEngineV2/view/CalcVarCheckbox.vue'
import VariableRowVerbose from './misc/CalculatorEngineV2/view/VariableRowVerbose.vue'
// import router from './router'
import './style/style.css'
import TreeView from './misc/TreeView/TreeView'
import TreeItem from './misc/TreeView/TreeItem'
import Panel from './misc/Panel/Panel'
import MnButton from './misc/MnButton/MnButton'
import MnModal from './misc/MnModal/MnModal'
import MnMsgBox from './misc/MnMsgBox/MnMsgBox'
import InfoCollapsible from '@/components/InfoCollapsible/InfoCollapsible'

Vue.config.productionTip = false

Vue.use(VueMaterial)
Vue.component('v-select', vSelect)
Vue.use(KeenUI)
Vue.use(VTooltip)
Vue.use(ElementUI)
Vue.use(Meta)
Vue.component('vue-slider', VueSlider)

// =========================================== //
// ========== GOOGLE ANALYTICS SETUP ========= //
// =========================================== //

Vue.use(VueAnalytics, {
  id: 'UA-92834105-1', // Analytics ID for NinjaCalc (see Analytics settings)
  router,
  checkDuplicatedScript: true // This is useful to prevent duplicate script loading when prerendering
})

// =========================================== //
// ========== VUE GOOGLE MAPS PACKAGE ======== //
// =========================================== //

// We only want to include this component once, as it inserts a script tag in the html,
// and Google maps complains that it has been added twice and unexpected events may occur
if (!window.hasOwnProperty('__PRERENDER_INJECTED')) {
  Vue.use(VueGoogleMaps, {
    load: {
      key: 'AIzaSyBSBFQ3KuKEfQNXSAhQ1uhjVa1gXbHSlwk',
      libraries: 'places' // This is required if you use the Autocomplete plugin
      // OR: libraries: 'places,drawing'
      // OR: libraries: 'places,drawing,visualization'
      // (as you require)
    }
  })
}

// =========================================== //
// ==== CALCULATOR COMPONENT REGISTRATION ==== //
// =========================================== //

Vue.component('calc-value', CalcValue)
Vue.component('calc-value-and-unit', CalcValueAndUnit)
Vue.component('calc-units', CalcUnits)
Vue.component('calc-var-string', CalcVarString)
Vue.component('calc-var-checkbox', CalcVarCheckbox)
Vue.component('variable-row-verbose', VariableRowVerbose)

// ============================================================================================= //
// ============================= REGISTER OUR CUSTOM VUE COMPONENTS ============================ //
// ============================================================================================= //

Vue.component('tree-view', TreeView)
Vue.component('tree-item', TreeItem)
Vue.component('panel', Panel)
Vue.component('mn-button', MnButton)
Vue.component('mn-modal', MnModal)
Vue.component('mn-msgbox', MnMsgBox)
Vue.component('InfoCollapsible', InfoCollapsible)

// =========================================== //
// ====== IMPORT ROUTER, SYNC WITH STORE ===== //
// =========================================== //
sync(store, router)

// Recommended by https://github.com/chrisvfritz/prerender-spa-plugin
// so that prerender-spa-plugin does not output the contents of the page
// before the JS runs
document.addEventListener('DOMContentLoaded', function () {
  /* eslint-disable no-new */
  new Vue({
    el: '#app',
    store,
    router,
    components: { App },
    template: '<App/>'
  })
})
