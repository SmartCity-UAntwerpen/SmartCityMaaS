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
 */
@Service
@Profile("default")
public class DatabaseLoader {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final SegmentRepository segmentRepository;
    private final JobRepository jobRepository;

    @Autowired
    public DatabaseLoader(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, SegmentRepository segmentRepository, JobRepository jobRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.segmentRepository = segmentRepository;
        this.jobRepository = jobRepository;
    }

    @PostConstruct
    private void initDatabase() {
        Permission p1 = new Permission("logon");
        permissionRepository.save(p1);
        Permission p2 = new Permission("secret-message");
        permissionRepository.save(p2);
        Role administrator = new Role("Administrator");
        Role tester = new Role("Tester");
        List<Permission> permissions =  new ArrayList<Permission>();
        permissions.add(p1);
        tester.setPermissions(permissions);
        roleRepository.save(tester);
        permissions =  new ArrayList<Permission>();
        permissions.add(p1);
        permissions.add(p2);
        administrator.setPermissions(permissions);
        roleRepository.save(administrator);
        User u1 = new User("Edwin","Walsh");
        List<Role> roles = new ArrayList<>();
        roles.add(administrator);
        u1.setRoles(roles);
        userRepository.save(u1);
        User u2 = new User("Siegfried","Mercelis");
        roles = new ArrayList<>();
        roles.add(tester);
        u2.setRoles(roles);
        userRepository.save(u2);

        Delivery delivery = new Delivery("Lorance", "Malfait","Seg 9", "Seg 10",2);
        deliveryRepository.save(delivery);

        Segment segment1 = new Segment("Seg 1");
        Segment segment2 = new Segment("Seg 2");
        Segment segment3 = new Segment("Seg 3");
        Segment segment4 = new Segment("Seg 4");
        Segment segment5 = new Segment("Seg 5");
        Segment segment6 = new Segment("Seg 6");
        Segment segment7 = new Segment("Seg 7");
        Segment segment8 = new Segment("Seg 8");
        Segment segment9 = new Segment("Seg 9");
        Segment segment10 = new Segment("Seg 10");
        Segment segment11 = new Segment("Seg 11");
        segmentRepository.save(segment1);
        segmentRepository.save(segment2);
        segmentRepository.save(segment3);
        segmentRepository.save(segment4);
        segmentRepository.save(segment5);
        segmentRepository.save(segment6);
        segmentRepository.save(segment7);
        segmentRepository.save(segment8);
        segmentRepository.save(segment9);
        segmentRepository.save(segment10);
        segmentRepository.save(segment11);
    }
}

