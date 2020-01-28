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
import java.security.Principal;


@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService validationErrorService;

    @PostMapping("")
    public ResponseEntity<?>  createNewProject(@Valid @RequestBody Project project, BindingResult result , Principal principal){

        ResponseEntity<?> errorMap = validationErrorService.mapValidationService(result);
        if(errorMap != null){
            return errorMap;
        }

        Project project1 = projectService.saveOrUpdateProject(project , principal.getName());
        return  new ResponseEntity<Project>(project1 , HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId , Principal principal){
        Project project = projectService.findProjectByIdentifier(projectId , principal);
        return new ResponseEntity<Project>(project , HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal){
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId , Principal principal){
        projectService.deleteProjectByIdentifier(projectId , principal);
        return new ResponseEntity<String >("Project with ID '" + projectId + "' was deleted" , HttpStatus.OK);
    }
}
