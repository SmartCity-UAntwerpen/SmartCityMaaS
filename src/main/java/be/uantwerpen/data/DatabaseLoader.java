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
 * Created by Frédéric Melaerts on 26/04/2017.
 * Databaseloader is used to store information in the database.
 */
@Service
@Profile("default")
public class DatabaseLoader {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final JobRepository jobRepository;
    private final JobListRepository jobListRepository;

    @Autowired
    public DatabaseLoader(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, JobRepository jobRepository, JobListRepository jobListRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.jobRepository = jobRepository;
        this.jobListRepository = jobListRepository;
    }

    @PostConstruct
    private void initDatabase() {

    }
}

