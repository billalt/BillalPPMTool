package io.billal.ppmtool.services;


import io.billal.ppmtool.domain.Backlog;
import io.billal.ppmtool.domain.Project;
import io.billal.ppmtool.domain.User;
import io.billal.ppmtool.exceptions.ProjectIdException;
import io.billal.ppmtool.exceptions.ProjectNotFoundException;
import io.billal.ppmtool.repositories.BacklogRepository;
import io.billal.ppmtool.repositories.ProjectRepository;
import io.billal.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){

        if(project.getId() != null){
            Project existProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existProject != null &&(!existProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existProject == null){
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier() + "' cannot be updated because is doesn't exist!");
            }
        }

        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }

    }

    public Project findProjectByIdentifier(String projectId, Principal principal){

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("ProjectID '" + projectId.toUpperCase() + "' doesn't exist");
        }

        if(!project.getProjectLeader().equals(principal.getName())){
            throw new ProjectNotFoundException("Project '" + projectId.toUpperCase() + "' not found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, Principal principal){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null){
            throw new ProjectIdException("Cannot delete this ID '" + projectId.toUpperCase() + "' the ID does not exist");
        }
        if(!project.getProjectLeader().equals(principal.getName())){
            throw new ProjectNotFoundException("Project not found in your account");
        }
        projectRepository.delete(project);
    }
}
