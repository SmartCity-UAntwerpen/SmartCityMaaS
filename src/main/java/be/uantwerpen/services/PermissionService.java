package be.uantwerpen.services;

import be.uantwerpen.model.Permission;
import be.uantwerpen.model.User;
import be.uantwerpen.repositories.PermissionRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 *  NV 2018
 */
@Service
public class PermissionService {
    private static final Logger logger = LogManager.getLogger(PermissionService.class);
    @Autowired
    private PermissionRepository permissionRepository;

    public Iterable<Permission> findAll() {
        return this.permissionRepository.findAll();
    }
    public Iterable<Permission> findAllForUser(User user) { return permissionRepository.findAllForUser(user);}
}