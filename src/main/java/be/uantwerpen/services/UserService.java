package be.uantwerpen.services;

import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  NV 2018
 */
@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    public Iterable<User> findAll() {
        return this.userRepository.findAll();
    }

    public boolean save(final User user){

        for(User u : findAll())
        {
            if(!checkUserName(u.getUserName()))
            {
                u.setFirstName(user.getFirstName());
                u.setLastName(user.getLastName());
                u.setUserName(user.getUserName());
                u.setPassword(user.getPassword());
                u.setRoles(user.getRoles());

                if(userRepository.save(u) != null)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkUserName(String username) {
        List<User> users = userRepository.findAll();

        for(User userIt : users)
        {
            if(userIt.getUserName().equals(username))
            {
                return true;
            }
        }

        return false;
    }

    public User findOne(Long id) {
        return this.userRepository.findOne(id);
    }

    public User findByUserName(String userName) {return userRepository.findByUserName(userName);}

    public void delete(Long id) {
        this.userRepository.delete(id);
    }

    public User getPrincipalUser()
    {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUserName(user.getUsername());
    }

    public boolean saveSomeAttributes(User user) {
        User tempUser = user.getId()==null?null:findOne(user.getId());
        if (tempUser != null && !checkUserName(user.getUserName())){
            tempUser.setRoles(user.getRoles());
            tempUser.setLastName(user.getLastName());
            tempUser.setFirstName(user.getFirstName());
            userRepository.save(tempUser);
            return false;
        }
        else {
            userRepository.save(user);
            return true;
        }
    }

}
