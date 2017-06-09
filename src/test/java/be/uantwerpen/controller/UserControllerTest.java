package be.uantwerpen.controller;

import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.User;
import be.uantwerpen.services.*;
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
 * Created by Frédéric Melaerts on 8/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;


    @Mock
    public BackendRestemplate backendRestemplate;
    @Mock
    public Astar astarService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PassengerService passengerService;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;

    @Mock
    private JobService jobService;
    @Mock
    private JobListService jobListService;

    private MockMvc mvc;

    @Before
    public void setup()
    {
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
        mvc.perform(get("/users/")).andExpect(view().name("users-list"));
    }

    @Test
    public void testEditUser() throws Exception {
        mvc.perform(post("/users/0")).andExpect(view().name("users-manage"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mvc.perform(get("/users/1/delete")).andExpect(view().name("redirect:/users"));
    }

    @Test
    public void testViewDeliveries() throws Exception {
        mvc.perform(get("/deliveries")).andExpect(view().name("delivery-list"));
    }


    @Test
    public void testViewCreateDelivery() throws Exception {
        mvc.perform(get("/deliveries/put")).andExpect(view().name("delivery-manage-user"));
    }

    @Test
    public void testDeleteDelivery() throws Exception {
        mvc.perform(get("/deliveries/1/delete")).andExpect(view().name("delivery-list"));
    }

    @Test
    public void testAddDelive() throws Exception {
        mvc.perform(post("/deliveries/1")).andExpect(view().name("delivery-navigate-user"));
    }

    @Test
    public void testGetSimulation() throws Exception {
        mvc.perform(get("/visualization")).andExpect(view().name("visualization_map"));
    }

}
