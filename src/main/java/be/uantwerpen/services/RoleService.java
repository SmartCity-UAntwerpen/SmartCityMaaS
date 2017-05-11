package be.uantwerpen.services;

import be.uantwerpen.model.Role;
import be.uantwerpen.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Iterable<Role> findAll() {
        return this.roleRepository.findAll();
    }

}
