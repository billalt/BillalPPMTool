package io.billal.ppmtool.services;

import io.billal.ppmtool.domain.Backlog;
import io.billal.ppmtool.domain.ProjectTask;
import io.billal.ppmtool.exceptions.ProjectNotFoundException;
import io.billal.ppmtool.repositories.BacklogRepository;
import io.billal.ppmtool.repositories.ProjectRepository;
import io.billal.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, Principal principal){


            //ProjectTasks to be add to a specific project, project != null, Backlog exists
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier ,principal).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);

            //Set the Backlog to ProjectTasks
            projectTask.setBacklog(backlog);

            //we want our project sequence to be like this: IDPRO-1 , IDPRO-2 ... IDPRO-100 ,IDPRO-101...
            Integer backlogSequence = backlog.getPTSequence();

            //Update the Baclog SEQUENCE
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            //Add Sequence to the ProjectTask
            projectTask.setProjectSequence(projectIdentifier + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority is null
            if( projectTask.getPriority() == null || projectTask.getPriority() == 0){
                projectTask.setPriority(3);
            }

            //INITIAL status when status is null
            if(projectTask.getStatus() == null || projectTask.getStatus().isEmpty() || projectTask.getStatus() == ""){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);

    }

    public Iterable<ProjectTask> findBacklogById(String project_id, Principal principal) {

        projectService.findProjectByIdentifier(project_id , principal);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(project_id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String sequence, Principal principal){

        projectService.findProjectByIdentifier(backlog_id , principal);

        //make sure we are searching on the right backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog == null){
            throw new ProjectNotFoundException("Project with ID: '" +backlog_id+"' does not exist");
        }

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(sequence);

        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task with ID: '" +sequence+"' does not found");
        }

        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task with ID: '" +sequence+"' does not exist in project: '"
             +backlog_id+"'");
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask projectTask, String backlog_id, String projectTask_id, Principal principal){
        ProjectTask projectTask1 = findPTByProjectSequence(backlog_id , projectTask_id, principal);
        projectTask1 = projectTask;
        return  projectTaskRepository.save(projectTask1);
    }

    public void deletePTByProjectSequence(String project_id, String projectTask_id, Principal principal){

        ProjectTask projectTask = findPTByProjectSequence(project_id , projectTask_id, principal);
        projectTaskRepository.delete(projectTask);
    }
}
