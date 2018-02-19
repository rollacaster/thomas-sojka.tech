import React from 'react'
import get from 'lodash/get'
import Link from 'gatsby-link'

import { rhythm } from '../../utils/typography'

class Blogs extends React.Component {
  render() {
    const { posts } = this.props

    return (
      <div>
        <h1>Posts</h1>
        {posts.map(({ node }) => {
          const title = get(node, 'frontmatter.title') || node.fields.slug
          return (
            <div key={node.fields.slug}>
              <h3
                style={{
                  marginBottom: rhythm(1 / 4),
                }}
              >
                <Link style={{ boxShadow: 'none' }} to={node.fields.slug}>
                  {title}
                </Link>
              </h3>
              <small>{node.frontmatter.date}</small>
              <p dangerouslySetInnerHTML={{ __html: node.excerpt }} />
            </div>
          )
        })}
      </div>
    )
  }
}

export default Blogs
