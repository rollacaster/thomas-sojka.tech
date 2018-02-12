import React from 'react'
import Link from 'gatsby-link'
import get from 'lodash/get'
import Helmet from 'react-helmet'

import Bio from '../components/Bio'
import { rhythm } from '../utils/typography'
import TalkIndex from '../talks'
import Projects from '../projects'

class BlogIndex extends React.Component {
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

const Home = ({
  data: { site: { siteMetadata: { title } }, allMarkdownRemark: { edges } },
}) => (
  <div style={{ display: 'flex', flexWrap: 'wrap' }}>
    <Helmet title={title} />
    <div style={{ padding: `${rhythm(1)} ${rhythm(3 / 4)} 0` }}>
      <BlogIndex posts={edges} />
    </div>
    <div style={{ padding: `${rhythm(1)} ${rhythm(3 / 4)} 0` }}>
      <Projects />
    </div>
    <div style={{ padding: `${rhythm(1)} ${rhythm(3 / 4)} 0` }}>
      <TalkIndex />
    </div>
  </div>
)

export default Home

export const pageQuery = graphql`
  query IndexQuery {
    site {
      siteMetadata {
        title
      }
    }
    allMarkdownRemark(sort: { fields: [frontmatter___date], order: DESC }) {
      edges {
        node {
          excerpt
          fields {
            slug
          }
          frontmatter {
            date(formatString: "DD MMMM, YYYY")
            title
          }
        }
      }
    }
  }
`
