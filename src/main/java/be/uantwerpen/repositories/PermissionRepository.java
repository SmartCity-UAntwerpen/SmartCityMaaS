package be.uantwerpen.repositories;

import be.uantwerpen.model.Permission;
import be.uantwerpen.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
    @Query(value = "select p from User u left join u.roles r left join r.permissions p where u=:usr")
    Iterable<Permission> findAllForUser(@Param("usr") User user);
}
