package io.billal.ppmtool.repositories;


import io.billal.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjecrtRepository extends CrudRepository<Project , Long> {

    @Override
    Iterable<Project> findAllById(Iterable<Long> iterable);
}
