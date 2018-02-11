import React from 'react'

import { rhythm } from '../utils/typography'

const TalkIndex = ({ talks }) => (
  <div>
    <h1>Talks</h1>
    <h3
      style={{
        marginBottom: rhythm(1 / 4),
      }}
    >
      <a
        style={{ boxShadow: 'none' }}
        href={'https://www.youtube.com/watch?v=ae_3svi5Eg0'}
        target="_blank"
      >
        Webscraping with algebraic structures in JS
      </a>
    </h3>
    <small>26 October, 2017</small>
    <p>
      Lighting Talks about scraping the ReactiveConf Lightning Talk Proposals
      with Functors, Monads and Traversables.
    </p>
  </div>
)

export default TalkIndex
