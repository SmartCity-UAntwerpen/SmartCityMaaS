package be.uantwerpen.services;

import be.uantwerpen.model.Permission;
import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserRepository userRepository;

    private List<User> userList;
    private List<Permission> permissionListTester;
    private List<Permission> permissionListAdmin;

    @Before
    public void init() {
        Permission p1 = new Permission("logon");
        Permission p2 = new Permission("secret-message");

        Role administrator = new Role("Administrator");
        Role tester = new Role("Tester");

        permissionListTester = new ArrayList<Permission>();
        permissionListTester.add(p1);
        tester.setPermissions(permissionListTester);

        permissionListAdmin = new ArrayList<Permission>();
        permissionListAdmin.add(p1);
        permissionListAdmin.add(p2);
        administrator.setPermissions(permissionListAdmin);

        User u1 = new User("Frédéric", "Melaerts");
        u1.setUserName("Held");
        u1.setPassword("GUI");
        u1.setRoles(Arrays.asList(administrator));

        userList = new ArrayList<User>();
        userList.add(u1);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addNewUserTest() {
        User u2 = new User("Dries", "Blontrock");
        u2.setUserName("Pruster");
        u2.setPassword("DoingMyBest");

        when(userRepository.findAll()).thenReturn(userList);
        when(userRepository.save(u2)).thenReturn(u2);

        assertTrue(userService.saveSomeAttributes(u2));
    }

    @Test
    public void addExistingUserTest() {
        when(userRepository.findAll()).thenReturn(userList);
        when(userRepository.save(userList.get(0))).thenReturn(userList.get(0));

        assertTrue(userService.saveSomeAttributes(userList.get(0)));
    }

    @Test
    public void checkExistingNameTest() {
        when(userRepository.findAll()).thenReturn(userList);

        assertTrue(userService.checkUserName("Held"));
    }

    @Test
    public void addUserWithDuplicatedUsernameTest() {
        User u3 = new User("Oliver", "Nyssen");
        u3.setUserName("Held");
        u3.setPassword("twister");

        when(userRepository.findAll()).thenReturn(userList);
        when(userRepository.save(u3)).thenReturn(u3);

        assertTrue(!userService.save(u3));
    }

    @Test
    public void usernameNotExistsTest() {
        when(userRepository.findAll()).thenReturn(userList);

        String nonExistingUsername = new String("roadBlock");

        assertTrue(!userService.checkUserName(nonExistingUsername));
    }
}
