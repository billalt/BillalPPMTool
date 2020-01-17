package io.billal.ppmtool.repositories;

import io.billal.ppmtool.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog , Long> {

    Backlog findByProjectIdentifier(String identifier);
}
