import React from 'react'

import { rhythm } from '../utils/typography'
import Navigation from '../components/navigation'

class Template extends React.Component {
  render() {
    const { children } = this.props

    return (
      <div>
        <Navigation />

        <div
          style={{
            maxWidth: rhythm(32),
            padding: `0 ${rhythm(3 / 4)}`,
            margin: 'auto',
          }}
        >
          {children()}
        </div>
      </div>
    )
  }
}

export default Template
