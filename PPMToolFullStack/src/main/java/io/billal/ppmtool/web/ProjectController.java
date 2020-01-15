package io.billal.ppmtool.web;


import io.billal.ppmtool.domain.Project;
import io.billal.ppmtool.services.MapValidationErrorService;
import io.billal.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService validationErrorService;

    @PostMapping("")
    public ResponseEntity<?>  createNewProject(@Valid @RequestBody Project project, BindingResult result){

        ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);
        if(errorMap != null){
            return errorMap;
        }

        Project project1 = projectService.saveOrUpdateProject(project);
        return  new ResponseEntity<Project>(project1 , HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId){
        Project project = projectService.findProjectByIdentifier(projectId);
        return new ResponseEntity<Project>(project , HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(){
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId){
        projectService.deleteProjectByIdentifier(projectId);
        return new ResponseEntity<String >("Project with ID '" + projectId + "' was deleted" , HttpStatus.OK);
    }
}
