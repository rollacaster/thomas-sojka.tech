import React from 'react'
import Helmet from 'react-helmet'

import Bio from '../components/Bio'
import Talks from '../components/talks'
import Projects from '../components/projects'
import Blogs from '../components/blogs'

const Home = ({
  data: { site: { siteMetadata: { title } }, allMarkdownRemark: { edges } },
}) => (
  <div style={{ display: 'flex', flexWrap: 'wrap' }}>
    <Helmet title={title} />
    <Blogs posts={edges} />
    <Projects />
    <Talks />
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
