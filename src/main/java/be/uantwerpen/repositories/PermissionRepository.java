package be.uantwerpen.repositories;

import be.uantwerpen.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
}