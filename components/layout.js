

import Nav from '~/components/nav'
import Head from 'next/head'


export default ({ children, title = 'This is the default title' }) => (
  <div>
    <header>
      {/* <Nav /> */}
    </header>

    <main>
      { children }
    </main>

    {/* GLOBAL STYLES */}
    <style jsx global>{`
        .hbox {
          display: flex;
          flex-direction: row;
          jusity-content: center;
          align-items: center;
        }
        .vbox {
          display: flex;
          flex-direction: column;
          jusity-content: center;
          align-items: center;
        }
        p, h1, h2, h3, ul, li, a, div, td, th, input {    
          font-family: 'avenir next' ,'avenir', sans-serif;
        }
    `}</style>
  </div>
)