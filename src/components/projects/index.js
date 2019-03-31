import React from 'react'

const projects = [
  {
    title: 'sketches',
    link: 'https://rollacaster.github.io/sketches/',
    description: 'Exploring creative visual programming.',
  },
  {
    title: 'elcontext',
    link: 'https://github.com/rollacaster/elcontext',
    description:
      'Context-based actions for emacs. Runs an emacs command based on your location, time and directory.',
  },
  {
    title: 'costa',
    link: 'https://github.com/rollacaster/costa',
    description:
      'Keep track of your daily costs across all devices. Record your costs using an iOS or Android App and sync them to your dashboard at home.',
  },
  {
    title: 'react-animated-donut',
    link: 'https://www.npmjs.com/package/react-animated-donut',
    description: 'Animated donut chart. React component written with d3-shape.',
  },
]

const Projects = props => (
  <div>
    <h1>Projects</h1>
    {projects.map(project => (
      <Project {...project} />
    ))}
  </div>
)

const Project = ({ title, description, link }) => (
  <div>
    <h3>
      <a style={{ boxShadow: 'none' }} href={link} target="_blank">
        {title}
      </a>
    </h3>
    <p>{description}</p>
  </div>
)

export default Projects
