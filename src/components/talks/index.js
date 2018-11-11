import React from 'react'

import { rhythm } from '../../utils/typography'

const TalkIndex = ({ talks }) => (
  <div>
    <h1>Talks</h1>
    <div>
      <h3
        style={{
          marginBottom: rhythm(1 / 4),
        }}
      >
        <a
          style={{ boxShadow: 'none' }}
          href={'https://www.youtube.com/watch?v=juMLwOTxnvw'}
          target="_blank"
        >
          Compose all the things
        </a>
      </h3>
      <small>29 October, 2018</small>
      <p>
        Functional composition can help to build flexible and readable APIs. It
        is a concept that can be translated into a variety of programming
        languages and domains. This talks explores functional composition
        through the lens of a JavaScript Frontend Developer.
      </p>
    </div>
    <div>
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
        Lighting Talk about scraping the{' '}
        <a href="https://reactiveconf.com">ReactiveConf</a> Lightning Talk
        Proposals with Functors, Monads and Traversables.
      </p>
    </div>
  </div>
)

export default TalkIndex
