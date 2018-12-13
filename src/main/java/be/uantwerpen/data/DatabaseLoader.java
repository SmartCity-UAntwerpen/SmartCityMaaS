package be.uantwerpen.data;

import be.uantwerpen.repositories.DeliveryRepository;
import be.uantwerpen.repositories.PermissionRepository;
import be.uantwerpen.repositories.RoleRepository;
import be.uantwerpen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Databaseloader is used to store information in the database.
 */
@Service
@Profile("default")
public class DatabaseLoader {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DatabaseLoader(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, DeliveryRepository deliveryRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @PostConstruct
    private void initDatabase() {

    }
}

