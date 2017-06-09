package be.uantwerpen.repositories;

import be.uantwerpen.model.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Job
 * @version 1 17 may 2017
 * @author Oliver Nyssen
 */
@Repository
public interface JobRepository extends CrudRepository <Job, Long>{
    List<Job> findAll();
}
