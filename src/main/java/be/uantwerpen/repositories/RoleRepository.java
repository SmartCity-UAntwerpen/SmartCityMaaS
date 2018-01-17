package be.uantwerpen.repositories;

import be.uantwerpen.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * NV 2018
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
