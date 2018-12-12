package be.uantwerpen.data;

import be.uantwerpen.model.*;
import be.uantwerpen.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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
    private final JobListRepository jobListRepository;

    @Autowired
    public DatabaseLoader(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, JobListRepository jobListRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.jobListRepository = jobListRepository;
    }

    @PostConstruct
    private void initDatabase() {

    }
}

