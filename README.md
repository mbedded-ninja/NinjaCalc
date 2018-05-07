# NinjaCalc

#### An embedded engineering calculator suite for doing calculations in a breeze.

- Author: gbmhunter <gbmhunter@gmail.com> ([www.mbedded.ninja](http://www.mbedded.ninja))
- Created: 2015-11-02
- Company: mbedded.ninja
- Project: NinjaTerm
- Language: Javascript (vue.js)/HTML/CSS
- IDE: VS Code
- Documentation Format: JSDoc
- License: GPLv3

# Motivation

The goal of this is to provide a easy-to-use web application to help you with all those small, frequent calculations you do while doing embedded engineering (or other forms of engineering). Whether it be a low-pass LC filter, a resistor divider, trying to find PCB track current, or even just Ohm's law (o.k., I hope you don't forget that one, but it is included none-the-less), this app makes them easy to find, use, and have confidence in the answers.

# To Use

Go to http://ninja-calc.mbedded.ninja/

# Features

See the [project home page](http://mbedded-ninja.github.io/NinjaCalc/) for a list of features.

# Developing

1. Download/clone this repository into a folder on your computer.
1. Run `npm install` to install dependencies (make sure node/npm is installed on your system).
1. Open the project in WebStorm (`.idea/workspace.xml` file is included in repo), or your favourite text/code editor.
1. Develop!
1. Type `npm run dev` to start up the app in development mode (it's hot reloadable).
1. Type `npm run tests` to run unit and e2e tests. Type `npm run unit` to run just the unit tests, `npm run e2e` to run just the e2e tests.

# Releasing

1. Make sure you are on the `develop` branch and all changes from `feature/xxx` branches have been merged into it.
1. Update `changelog.md` with a list of changes since the last release, under a heading which is the new version number (e.g. `v1.2.0`).
1. Commit the changes made above to the `develop` branch.
1. Merge the `develop` branch into the `master` branch. **MAKE SURE** that you create a new commit while doing this, even if fast-forward is possible.
1. Tag the commit on the master branch with the version number (e.g. `v1.2.0`).
1. Push all branches to the remote GitHub repo.
1. To deploy the new files, firstly run `npm run build`. This will create production files in the `dist/` directory.
1. Run `./deploy.sh` (from a UNIX-style shell, e.g. Linux, cygwin, MinGW). This will copy the production files from `dist/` to the remote server. Note: SSH authentication is required for this work (currently only gbmhunter@gmail.com can do this step).

# Changelog

See [changelog.md](../blob/master/changelog.md).

# Contributors

See the [project home page](http://mbedded-ninja.github.io/NinjaCalc/) for a list of contributors.


# Dependencies

## Runtime
- big-integer: Used in the CRC calculator
- chart.js: Used by the PID tuner.
- d3: Used in the two coordinate geodesic calculator.
- element-ui@1.4.13: NOTE. Tabs are specific to used version. v2 doesn't seem to work.
- keen-ui: Buttons used.
- prerender-spa-plugin: Used to prerender routes to tools (e.g. /tool/pid-tuner) so that they are SEO friendly.
- topojson-client: Used in the two coordinate geodesic calculator.
- v-tooltip: Tooltips.
- versor: Used in the two coordinate geodesic calculator.
- vue2-google-maps: Used in the map plotter tool.
- vue-material@0.7.5: Buttons, tooltips used, sidenav used. NOTE: Sidenav not available in latest version.
- vue-meta: Used to update tags within the HTML head (e.g. title, description, image) with a vue component.
- vue-select: General purpose UI select element.
- vue-slider-component: Sliders used in the PID tuner tool.
- vuex: Centralized state management for app.
- vuex-router-sync: Used with vuex and the router.

## Development
raw-loader: Webpack raw loader for reading text files. Used in the PID tuner tool.

# Changes To webpack.base.conf.js

Added new rule to load text files (used in the PID tuner).
```
{
    test: /\.txt$/,
    use: 'raw-loader'
},

