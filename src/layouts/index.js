import React from 'react'
import { Container } from 'react-responsive-grid'

import { rhythm } from '../utils/typography'
import Navigation from '../components/navigation'

class Template extends React.Component {
  render() {
    const { children } = this.props

    return (
      <div>
        <Navigation />

        <Container
          style={{ maxWidth: rhythm(32), padding: `0 ${rhythm(3 / 4)}` }}
        >
          {children()}
        </Container>
      </div>
    )
  }
}

export default Template
