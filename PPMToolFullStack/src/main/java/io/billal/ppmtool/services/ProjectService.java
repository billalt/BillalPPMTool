package io.billal.ppmtool.services;


import io.billal.ppmtool.domain.Project;
import io.billal.ppmtool.repositories.ProjecrtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjecrtRepository projecrtRepository;


    public Project saveOrUpdateProject(Project project){
        return projecrtRepository.save(project);
    }
}
