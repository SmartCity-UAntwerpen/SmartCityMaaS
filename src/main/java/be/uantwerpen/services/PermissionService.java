package be.uantwerpen.services;

import be.uantwerpen.model.Permission;
import be.uantwerpen.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Frédéric Melaerts on 26/04/2017.
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public Iterable<Permission> findAll() {
        return this.permissionRepository.findAll();
    }
}