import React, { Component } from "react";
import ProjectItem from "./Project/ProjectItem";
import CreateProjectButton from "./Project/CreateProjectButton";
import { connect } from "react-redux";
import { getProjects } from "../actions/projectActions";
import PropTypes from "prop-types";

class Dashboard extends Component {
  //lifecycle hooks
  componentDidMount() {
    this.props.getProjects();
  }

  render() {
    const { projects } = this.props.project;
    const projectCount = projects.length;

    let projectCountMessage;
    const moreThan10Projects = (
      <h1 className="lead text-center bg-info ">
        You have {projectCount} Projects
      </h1>
    );

    const lessThan10Projects = (
      <h1 className="lead text-center bg-warning text-light ">
        You have {projectCount} Projects
      </h1>
    );

    const noProjects = (
      <h1 className="lead text-center bg-danger text-light ">
        You have {projectCount} Projects
      </h1>
    );
    if (projectCount < 1) {
      projectCountMessage = noProjects;
    } else if (projectCount > 0 && projectCount < 10) {
      projectCountMessage = lessThan10Projects;
    } else {
      projectCountMessage = moreThan10Projects;
    }

    return (
      <div className="projects">
        <div className="container">
          <div className="row">
            <div className="col-md-12">
              <h1 className="display-4 text-center">Projects</h1>
              {projectCountMessage}
              <br />
              <CreateProjectButton />
              <br />
              <hr />
              {projects.map(project => (
                <ProjectItem key={project.id} project={project} />
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

Dashboard.propTypes = {
  project: PropTypes.object.isRequired,
  getProjects: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
  project: state.project
});

export default connect(mapStateToProps, { getProjects })(Dashboard);
