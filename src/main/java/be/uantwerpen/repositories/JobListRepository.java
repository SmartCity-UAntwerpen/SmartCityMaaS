package be.uantwerpen.repositories;

import be.uantwerpen.model.JobList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Revil on 29/05/2017.
 */
@Repository
public interface JobListRepository extends CrudRepository<JobList, Long>
{
    List<JobList> findAll();

}
