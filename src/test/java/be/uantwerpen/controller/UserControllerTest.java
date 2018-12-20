package be.uantwerpen.controller;

import be.uantwerpen.services.PassengerService;
import be.uantwerpen.services.RoleService;
import be.uantwerpen.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by Frédéric Melaerts on 10/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PassengerService passengerService;
    @Mock
    public BackendRestTemplate backendRestTemplate;
    @Mock
    private RestTemplate restTemplate;

    private MockMvc mvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testShowUsers() throws Exception {
        mvc.perform(get("/users")).andExpect(view().name("users-list"));
    }

    @Test
    public void testViewCreateUser() throws Exception {
        mvc.perform(get("/users/put")).andExpect(view().name("users-manage"));
    }

    @Test
    public void testViewEditUser() throws Exception {
        mvc.perform(get("/users/1")).andExpect(view().name("users-manage"));
    }

    @Test
    public void testAddUser() throws Exception {
        mvc.perform(post("/users/")).andExpect(view().name("redirect:/login"));
    }

    @Test
    public void testEditUserError() throws Exception {
        mvc.perform(post("/users/1")).andExpect(view().name("users-manage"));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(post("/users/1/delete")).andExpect(view().name("redirect:/users"));
    }

}
