package be.uantwerpen.repositories;

import be.uantwerpen.model.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Revil on 17/05/2017.
 */
@Repository
public interface JobRepository extends CrudRepository <Job, Long>{
    List<Job> findAll();
}
