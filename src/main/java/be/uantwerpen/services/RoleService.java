package be.uantwerpen.services;

import be.uantwerpen.model.Role;
import be.uantwerpen.repositories.RoleRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 *  NV 2018
 */
@Service
public class RoleService {
    private static final Logger logger = LogManager.getLogger(RoleService.class);
    @Autowired
    private RoleRepository roleRepository;

    public Iterable<Role> findAll() {
        return this.roleRepository.findAll();
    }

    public Role findRole(String role) {
        Iterator<Role> iterator = roleRepository.findAll().iterator();
        while(iterator.hasNext())
        {
            Role current = iterator.next();
            if(current.getName().equals(role))
            {
                return current;
            }
        }
        return null;
    }

}
