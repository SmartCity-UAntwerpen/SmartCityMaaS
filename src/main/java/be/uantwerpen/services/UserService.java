package be.uantwerpen.services;

import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public Iterable<User> findAll() {
        return this.userRepository.findAll();
    }

    public void save(final User user){

        this.userRepository.save(user);
    }

    public User findOne(Long id) {
        return this.userRepository.findOne(id);
    }

    public void delete(Long id) {
        this.userRepository.delete(id);
    }

    public void saveSomeAttributes(User user) {
        User tempUser = user.getId()==null?null:findOne(user.getId());
        if (tempUser != null){
            tempUser.setRoles(user.getRoles());
            tempUser.setLastName(user.getLastName());
            tempUser.setFirstName(user.getFirstName());
            userRepository.save(tempUser);
        }
        else{
            userRepository.save(user);
        }
    }

}
