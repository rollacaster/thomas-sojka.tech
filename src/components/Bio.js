import React from 'react'

// Import typefaces
import 'typeface-montserrat'
import 'typeface-merriweather'

import profilePic from './profile-pic.jpg'
import { rhythm } from '../utils/typography'

class Bio extends React.Component {
  render() {
    return (
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          marginBottom: rhythm(2.5),
        }}
      >
        <img
          src={profilePic}
          alt={`Thomas Sojka`}
          style={{
            marginRight: rhythm(1 / 2),
            marginBottom: 0,
            width: rhythm(2),
            height: rhythm(2),
            borderRadius: 100,
          }}
        />
        <p style={{ marginBottom: 0 }}>
          Written by{' '}
          <a href="https://twitter.com/rollacaster">
            <strong>Thomas Sojka</strong>
          </a>
        </p>
      </div>
    )
  }
}

export default Bio
