package io.billal.ppmtool.web;

import io.billal.ppmtool.domain.ProjectTask;
import io.billal.ppmtool.services.MapValidationErrorService;
import io.billal.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService validationErrorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result , @PathVariable String backlog_id){

        ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);

        if(errorMap != null){
            return errorMap;
        }

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id,projectTask);

        return new ResponseEntity<ProjectTask>(projectTask1 , HttpStatus.CREATED);
    }

    @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id){
        return projectTaskService.findBacklogById(backlog_id);
    }

    @GetMapping("/{backlog_id}/{projectTask_id}")
    public  ResponseEntity<?> getProjectTask(@PathVariable String backlog_id , @PathVariable String projectTask_id){
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id,projectTask_id);
        return new ResponseEntity<ProjectTask>(projectTask , HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{projectTask_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask , BindingResult result , @PathVariable String backlog_id, @PathVariable String projectTask_id){
        ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);

        if(errorMap != null){
            return errorMap;
        }

        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask,backlog_id,projectTask_id);

        return new ResponseEntity<ProjectTask>(updatedProjectTask , HttpStatus.OK);
    }

    @DeleteMapping("/{project_id}/{projectTask_id}")
    public  ResponseEntity<?> deleteProjectTask(@PathVariable String project_id , @PathVariable String projectTask_id){
        projectTaskService.deletePTByProjectSequence(project_id , projectTask_id);
        return new ResponseEntity<String>("Project Task "+projectTask_id+" was deleted successfully",HttpStatus.OK);
    }

}
