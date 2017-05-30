package be.uantwerpen.repositories;

import be.uantwerpen.model.JobList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Revil on 29/05/2017.
 */
@Repository
public interface OrderRepository extends CrudRepository<JobList, Long>
{


}
