package be.uantwerpen.repositories;

import be.uantwerpen.model.JobList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository for JobList. This class was originally called Orders. However, after multiple errors, i discovered that
 * order is a reserved word for sql functions, which ended up with the compiler giving a index out of bound exception
 * for the orders (where no index was being used). Hence it was refactored to JobList, however, references to orders
 * will still often be found
 * NV 2018
 */
@Repository
public interface JobListRepository extends CrudRepository<JobList, Long>
{
    List<JobList> findAll();

}
