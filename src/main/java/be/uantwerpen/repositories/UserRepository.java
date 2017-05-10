package be.uantwerpen.repositories;

import be.uantwerpen.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserName(String userName);
    List<User> findAll();

}

