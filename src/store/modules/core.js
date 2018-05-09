import Vue from 'vue'

const state = {
  // Complete list of calculators that the user can open.
  // These are presented to the user, but filtered first.
  availableCalcs: [],

  selCategory: [],

  // This is updated whenever the selected category is changed.
  calcsFilteredByCategory: [],

  searchText: '',

  // This is updated whenever the category or search text is changed.
  calcsFilteredByCategoryAndSearch: [],

  openCalcs: []
}

// mutations
const mutations = {
  registerCalc (state, payload) {
    state.availableCalcs.push(payload)
  },
  setSearchText (state, searchText) {
    state.searchText = searchText
  },
  updateFilteredCalcsOnSearchText (state) {
    // console.log('updateFilteredAvailableCalcs() called.')
    // Update the filtered available calculators. If the search text is '' (i.e.
    // empty), return all the calculators.
    if (state.searchText === '') {
      state.calcsFilteredByCategoryAndSearch = state.calcsFilteredByCategory
      // console.log('state.calcsFilteredByCategoryAndSearch = ')
      // console.log(state.calcsFilteredByCategoryAndSearch)
      return
    }
    state.calcsFilteredByCategoryAndSearch = state.calcsFilteredByCategory.filter(calc => {
      // Create regex pattern from search text
      const regex = new RegExp(state.searchText, 'gi')
      // Search in calculator title (display name)
      if (calc.displayName.match(regex)) return true
      // Search through the tags
      for (var tag of calc.tags) {
        if (tag.match(regex)) return true
      }
    })
  },
  setSelCategory (state, category) {
    state.selCategory = category
  },
  openCalc (state, payload) {
    console.log('openCalc() called with payload =')
    console.log(payload)
    // Find a unique ID to use
    let maxId = 0
    state.openCalcs.forEach((calc, index) => {
      if (calc.uniqueId > maxId) {
        maxId = calc.uniqueId
      }
    })
    const newUniqueId = maxId + 1

    // Check to make sure componentName is valid
    const foundCalc = state.availableCalcs.find((element) => {
      return element.mainView.name === payload.componentName
    })
    if (!foundCalc) {
      throw new Error('openCalc() requested to open "' + payload.componentName + '", but no calculator with this ID was found in the array of available calculators.')
    }

    state.openCalcs.push({
      name: foundCalc.displayName,
      componentName: foundCalc.mainView.name,
      // Unique ID is used as a unique tab ID
      uniqueId: newUniqueId
    })
  },
  updateFilteredCalcsOnCategory (state) {
    // console.log('$state.core.mutations.updateFilteredCalcsOnCategory() called.')

    state.calcsFilteredByCategory = state.availableCalcs.filter(calc => {
      // console.log('calc =')
      // console.log(calc)
      if (state.selCategory.length > calc.category.length) {
        // console.log('Selected category more specific than calc category, excluding...')
        return false
      }
      for (var i = 0; i < state.selCategory.length; i++) {
        if (state.selCategory[i] !== calc.category[i]) {
          // console.log('Selected category does not mactch calc category, excluding...')
          return false
        }
      }

      // console.log('Selected category matches calculator category.')
      return true
    })

    // console.log('calcsFilteredByCategory =')
    // console.log(state.calcsFilteredByCategory)
  }
}

const actions = {
  /**
   * Call this to register a calculator with the app. This is typically done at
   * start-up.
   * @param context
   * @param value     The calculator you wish to register.
   */
  registerCalc ({state, commit, rootState}, value) {
    Vue.component(value.mainView.name, value.mainView)
    commit('registerCalc', value)
    commit('updateFilteredCalcsOnCategory')
    commit('updateFilteredCalcsOnSearchText')
  },
  openCalc  ({state, commit, rootState}, value) {
    console.log('core.actions.openCalc() called.')
    commit('openCalc', value)
    commit('setLastCalcAsActive')
  },
  setSearchText ({state, commit, rootState}, value) {
    commit('setSearchText', value)
    commit('updateFilteredCalcsOnSearchText')
  },
  setSelCategory ({state, commit, rootState}, value) {
    console.log('core.actions.setCategory() called.')
    commit('setSelCategory', value)
    commit('updateFilteredCalcsOnCategory')
    commit('updateFilteredCalcsOnSearchText')
  }
}

export default {
  state,
  mutations,
  actions
}
