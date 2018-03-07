import React from 'react'
import FontAwesome from 'react-fontawesome'
import { Helmet } from 'react-helmet'

// Import typefaces
import 'typeface-montserrat'
import 'typeface-merriweather'

import profilePic from './profile-pic.jpg'
import { rhythm } from '../utils/typography'
import './grow.css'

const styles = {
  contactButton: backgroundColor => ({
    boxShadow: 'none',
    display: 'flex',
    marginBottom: 0,
    justifyContent: 'center',
    alignItems: 'center',
    width: 70,
    height: 70,
    backgroundColor,
  }),
}

class Bio extends React.Component {
  render() {
    return (
      <div style={{ width: '100%' }}>
        <Helmet>
          <link
            href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
            rel="stylesheet"
          />
        </Helmet>
        <ul
          style={{
            listStyle: 'none',
            display: 'flex',
            justifyContent: 'space-around',
          }}
        >
          <li className="grow">
            <a
              style={styles.contactButton('#33CCFF')}
              href="http://www.twitter.com/rollacaster"
            >
              <FontAwesome
                name="twitter"
                size="3x"
                style={{
                  color: 'white',
                }}
              />
            </a>
          </li>
          <li className="grow">
            <a
              href="http://www.github.com/rollacaster"
              style={styles.contactButton('#171515')}
            >
              <FontAwesome
                name="github"
                size="3x"
                style={{
                  color: 'white',
                }}
              />
            </a>
          </li>
          <li className="grow">
            <a
              href="mailto:thomas.sojka@tech.de"
              style={styles.contactButton('#888')}
            >
              <FontAwesome
                name="envelope"
                size="3x"
                style={{
                  color: 'white',
                }}
              />
            </a>
          </li>
        </ul>
      </div>
    )
  }
}

export default Bio
