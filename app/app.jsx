//!
//! @file               app.jsx
//! @author             Geoffrey Hunter <gbmhunter@gmail.com> (www.mbedded.ninja)
//! @created            2015-11-02
//! @last-modified      2015-11-20
//! @brief              Contains the "redux" actions for the NinjaCalc app.
//! @details
//!     See README.rst in repo root dir for more info.

//import React, { Component } from 'react';

// npm modules
import React from 'react';
import ReactDOM from 'react-dom';
// Redux utility functions
import { compose, createStore, applyMiddleware } from 'redux';
import { Provider, connect } from 'react-redux';
import thunk from 'redux-thunk';
var Select = require('react-select');
import Dropdown from 'react-dropdown';
import { Input, Tooltip, OverlayTrigger, Popover, Tabs, Tab } from 'react-bootstrap';
var PureRenderMixin = require('react-addons-pure-render-mixin');
var _ = require('lodash');
var Latex = require('react-latex');
var ReactRadioGroup = require('react-radio-group');

// User modules
import AbsoluteGrid from './utility/react-absolute-grid/AbsoluteGrid.js';


const finalCreateStore = compose(
  // Enables your middleware:
  applyMiddleware(thunk) // any Redux middleware, e.g. redux-thunk
  
  // Provides support for DevTools:
  //! @warning  This will cause the entire state/action history to be re-run everytime a
  //!     new action is dispatched, which could cause performance issues!
  //devTools()
)(createStore);

//=========== REDUCER ===========//
import defaultReducer from './reducers/calc-reducer.js';
import * as calcActions from './actions/calc-actions.js';
//console.log(defaultReducer);

// Create store. Note that there is only one of these for the entire app.
const store = finalCreateStore(defaultReducer);


//============== LOAD CALCULATORS ============//

import * as lt3745Calc from './calculators/chip-specific/lt3745/lt3745.js';
import ohmsLawCalc from './calculators/basic/ohms-law/ohms-law.js';
import * as resistorDividerCalc from './calculators/basic/resistor-divider/resistor-divider.js';




// Calculators are loaded into Redux state in the onMount function of the react App


//class App extends React.Component {

var App = React.createClass({

	//mixins: [PureRenderMixin],

	componentDidMount: function() {

		// Load in the calculators so the app knows about them
		this.props.dispatch(calcActions.addCalc(ohmsLawCalc));
		this.props.dispatch(calcActions.addCalc(lt3745Calc.data));		
		this.props.dispatch(calcActions.addCalc(resistorDividerCalc.data));
	},

	handleSelect(key) {
		//alert('selected ' + key);
		this.props.dispatch(calcActions.setActiveTab(key));		
	},

	//! @brief		Called when the text within the "search" input changes.
	//! @details	Dispatches a setSearchTerm event, which then updates the input and filters the calculator grid results.
	onSearchInputChange(event) {
	    console.log('onSearchInputChange() called with event.target.value = ');
	    console.log(event.target.value);

	    this.props.dispatch(calcActions.setSearchTerm(event.target.value));
 	},

 	//! @brief		This function determines what calculator element to render inside the tab.
 	//! @details	We need this because the UI structure of each calculator may be different.
 	renderCalc: function(calculator, key) {

		// Create the view for the specific calculator.
		// Note that since this isn't created in JSX (i.e. <CalcView>...</CalcView>),
		// we have to use React.createFactory
		return React.createElement(calculator.get('view'), { key: key, data: calculator, dispatch: this.props.dispatch });
 	},

	render: function() {

		var that = this;

		// We have to inject the dispatch function as a prop to all of the grid elements so we
		// can do something when the 'Load' button is clicked. The function pointer can't be added
		// in the reducer because the reducer has no knowledge of it.
		var items = this.props.state.get('gridElements').toJS().map((gridElement) => {
			gridElement.dispatch = this.props.dispatch;
			return gridElement;
		});

		return (
			<div className="app">	
				{/* Tabs are the main view element on the UI */}
				<Tabs activeKey={this.props.state.get('activeTabKey')} onSelect={this.handleSelect}>
					{/* First tab is static and non-removable */}
					<Tab eventKey={0} title="Calculators">
						{/* This is used to narrow down on the desired calculator */}
						<Input
					        type="text"
					        value={this.props.state.get('searchTerm')}
					        placeholder="Enter text"
					        label="Search for calculator"
					        hasFeedback
					        ref="input"
					        groupClassName="group-class"
					        labelClassName="label-class"
					        onChange={this.onSearchInputChange} />					        
						<br />
						<div>
							{/* Item width and height determine the size of the card. Note that if the card is too big it can make the
							height larger, but not the width */}
							<AbsoluteGrid
								items={items}
								itemWidth={240}
								itemHeight={360}
								responsive={true}
								zoom={1}
								animation="transform 300ms ease"/>							
						</div>
					</Tab>
					{
						/* Let's create a visual tab for every calculator in the openCalculators array */
						this.props.state.get('openCalculators').map(function(calculator, index) {
							return (
								<Tab key={index+1} eventKey={index+1} title={calculator.get('name')}>
									{
										/* This next line of code inserts the entire calculator into the tab element.
										We also need to pass in a key to prevent it from getting re-rendered when it doesn't have to */
										that.renderCalc(calculator, index+1)
									}										
								</Tab>
							);
						})
					}
				</Tabs>											
			</div>
		);
	}
});



//! @brief    Selects what props to inject into app.
//! @details  Currently injecting everything.
function mapStateToProps(state) {
  return {
    /*serialPort: state.serialPort,
    loggingState: state.logging,
    stats: state.stats,
    util: state.util,*/
    // Map everything at the moment, might change in future
    state: state,    
  };
}


// Inject dispatch and state into app
App = connect(mapStateToProps)(App);

// Wrapping the app in Provider allows us to use Redux
console.log(document);
//console.log('document.getElementById(\'content\') = ');
console.log(document.getElementById('content'));

var appRender = ReactDOM.render(
  <div id='redux-wrapper-div' style={{height: '100%'}}>
    <Provider store={store}>
        <App />
    </Provider>
  </div>,
  document.getElementById('content')
);




