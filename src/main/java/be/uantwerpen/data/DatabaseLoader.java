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
        /*Permission p1 = new Permission("logon");
        permissionRepository.save(p1);
        Permission p2 = new Permission("secret-message");
        permissionRepository.save(p2);
        Permission p3 = new Permission("admin");
        permissionRepository.save(p3);
        Role administrator = new Role("Administrator");
        Role User = new Role("user");
        List<Permission> permissions =  new ArrayList<>();
        permissions.add(p1);
        User.setPermissions(permissions);
        roleRepository.save(User);
        permissions =  new ArrayList<>();
        permissions.add(p1);
        permissions.add(p2);
        permissions.add(p3);
        administrator.setPermissions(permissions);
        roleRepository.save(administrator);
        User u1 = new User("Edwin","Walsh", "admin", "admin");
        List<Role> roles = new ArrayList<>();
        roles.add(administrator);
        u1.setRoles(roles);
        userRepository.save(u1);
        User u2 = new User("Siegfried","Mercelis", "siggy", "test");
        roles = new ArrayList<>();
        roles.add(User);
        u2.setRoles(roles);
        userRepository.save(u2);*/

        /*Delivery delivery = new Delivery("Lorance", "Malfait","Seg 9", "Seg 10",2);
        deliveryRepository.save(delivery);
        */
    }
}

