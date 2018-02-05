import React from 'react'
import Link from 'gatsby-link'

import { rhythm, scale } from '../utils/typography'
import { black, white } from '../styles'

const styles = {
  link: {
    boxShadow: 'none',
    textDecoration: 'none',
    color: 'inherit',
    paddingRight: rhythm(0.5),
  },
}

const Navigation = props => (
  <header style={{ color: white, backgroundColor: black, width: '100%' }}>
    <nav
      style={{
        ...scale(1),
        marginTop: 0,
        padding: rhythm(1),
      }}
    >
      <Link style={styles.link} to={'/'}>
        Thomas Sojka
      </Link>
    </nav>
  </header>
)

export default Navigation
